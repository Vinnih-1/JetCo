/*
 * Copyright 2026 by Developer Chunk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.developerstring.jetco.ui.components.picker.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.picker.config.WheelPickerConfig
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlin.math.abs

/**
 * A single-column wheel picker with snap-fling behaviour.
 *
 * Items are laid out in a [LazyColumn] that snaps to the nearest
 * item after a fling. The centre row is highlighted with a
 * configurable selector background, and edge rows gradually fade out.
 *
 * The visible height is calculated automatically from
 * [WheelPickerConfig.selectorHeight] × [WheelPickerConfig.rowCount]
 * to guarantee correct centering and snap alignment.
 *
 * @param items List of display strings for each option.
 * @param initialIndex The index to scroll to initially (centred).
 * @param config Visual / behavioural configuration.
 * @param modifier Modifier applied to the outer container.
 * @param onItemSelected Callback invoked with the new selected index.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PickerWheel(
    items: List<String>,
    initialIndex: Int,
    config: WheelPickerConfig,
    modifier: Modifier = Modifier,
    onItemSelected: (index: Int) -> Unit
) {
    if (items.isEmpty()) return

    val view = LocalView.current
    val onItemSelectedState by rememberUpdatedState(onItemSelected)

    val halfPadding = config.rowCount / 2
    val itemHeight = config.selectorHeight

    // Enforce height = itemHeight * rowCount for correct centering
    val totalHeight = itemHeight * config.rowCount

    // Content padding replaces fake empty items: each end gets
    // halfPadding × itemHeight so the first/last real item can centre.
    val verticalPadding = itemHeight * halfPadding

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex
    )

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Derive the currently centred real-item index from layout info.
    // derivedStateOf recomputes every frame during scroll but only
    // notifies observers when the centred index actually changes,
    // so items only recompose on selection change.
    val currentIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset +
                    (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
            layoutInfo.visibleItemsInfo
                .minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }
                ?.index?.coerceIn(0, items.lastIndex)
                ?: initialIndex.coerceIn(0, items.lastIndex)
        }
    }

    // Side-effects: haptic feedback + callback on selection change.
    // drop(1) skips the initial emit so the callback isn't fired
    // redundantly on first composition.
    LaunchedEffect(Unit) {
        snapshotFlow { currentIndex }
            .distinctUntilChanged()
            .drop(1)
            .collect { index ->
                if (config.hapticFeedback) {
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                }
                onItemSelectedState(index)
            }
    }

    Box(
        modifier = modifier.height(totalHeight),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = verticalPadding),
            modifier = Modifier
                .fillMaxWidth()
                .height(totalHeight)
                .then(
                    if (config.fadeEdges) {
                        Modifier.drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        config.fadeEdgeColor,
                                        Color.Transparent
                                    ),
                                    startY = 0f,
                                    endY = size.height * 0.3f
                                )
                            )
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        config.fadeEdgeColor
                                    ),
                                    startY = size.height * 0.7f,
                                    endY = size.height
                                )
                            )
                        }
                    } else Modifier
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = items.size,
                key = { it }
            ) { index ->
                val distance = abs(index - currentIndex)
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.6f
                    2 -> 0.3f
                    else -> 0.15f
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .graphicsLayer { this.alpha = alpha },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        style = if (index == currentIndex) {
                            config.selectedTextStyle
                        } else {
                            config.defaultTextStyle
                        },
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        // Selector highlight overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = config.selectorColor,
                    shape = config.selectorShape
                )
        )
    }
}
