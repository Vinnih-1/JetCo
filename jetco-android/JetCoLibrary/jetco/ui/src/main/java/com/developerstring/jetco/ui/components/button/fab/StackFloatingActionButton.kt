package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.components.SubFabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A Floating Action Button that expands sub-items linearly in a stack — above, to the left,
 * or to the right of the main button.
 *
 * Sub-items are pushed outward using animated offsets driven by tween animations.
 *
 * ## Example Usage:
 * ```kotlin
 * StackFloatingActionButton(
 *     expanded = isExpanded,
 *     items = listOf(
 *         FabSubItem(
 *             onClick = { }
 *         )
 *     )
 * )
 * ```
 *
 * @param expanded Whether the FAB is currently expanded, showing sub-items.
 * @param items List of [FabSubItem] sub-actions to display when expanded.
 * @param modifier Modifier applied to the root [Box] container.
 * @param onClick Click handler for the main FAB button.
 * @param config Visual and layout configuration. See [FabMainConfig].
 */
@Composable
fun StackFloatingActionButton(
    expanded: Boolean,
    items: List<FabSubItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(
            expanded = expanded,
            onClick = onClick,
            config = config
        )
    }
) {
    val stack = config.itemArrangement.stack
    val density = LocalDensity.current
    var fabWidthDp by remember { mutableStateOf(config.buttonStyle.size) }
    var fabHeightDp by remember { mutableStateOf(config.buttonStyle.size) }
    val spacedBy = stack.spacedBy

    // The Box anchor changes depending on which direction items spread
    val alignment = when (stack) {
        FabMainConfig.Orientation.Stack.TOP -> Alignment.BottomEnd
        FabMainConfig.Orientation.Stack.START -> Alignment.CenterEnd
        FabMainConfig.Orientation.Stack.END -> Alignment.CenterStart
    }

    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        // Sub-items — stacked in the direction defined by Orientation.Stack
        items.forEachIndexed { index, item ->


            val spacing = when (stack) {
                FabMainConfig.Orientation.Stack.TOP ->
                    fabHeightDp + spacedBy + index * (item.buttonStyle.size + spacedBy)
                else ->
                    fabWidthDp + spacedBy + index * (item.buttonStyle.size + spacedBy)
            }

            // Each orientation moves items along a different axis
            val targetOffsetX = when (stack) {
                FabMainConfig.Orientation.Stack.START -> -spacing
                FabMainConfig.Orientation.Stack.END -> spacing
                else -> 0.dp
            }

            val targetOffsetY = when (stack) {
                FabMainConfig.Orientation.Stack.TOP -> -spacing
                else -> 0.dp
            }

            val alpha = remember { Animatable(0f) }
            val offsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
            val offsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

            LaunchedEffect(expanded) {
                val stepMs = 300 / (items.size + 1)
                val order = if (expanded) config.animation.enterOrder else config.animation.exitOrder
                val transition = if (expanded) config.animation.enterTransition else config.animation.exitTransition
                val staggerDelay = order.delayFor(index = index, total = items.size, stepMs = stepMs)

                delay(staggerDelay)

                val targetX = if (expanded) targetOffsetX else 0.dp
                val targetY = if (expanded) targetOffsetY else 0.dp
                val targetAlpha = if (expanded) 1f else 0f

                if (transition.offsetSpec != null) {
                    launch { offsetX.animateTo(targetX, transition.offsetSpec) }
                    launch { offsetY.animateTo(targetY, transition.offsetSpec) }
                } else if (expanded) {
                    offsetX.snapTo(targetX)
                    offsetY.snapTo(targetY)
                }

                if (transition.alphaSpec != null) {
                    launch { alpha.animateTo(targetAlpha, transition.alphaSpec) }
                } else {
                    alpha.snapTo(targetAlpha)
                }
            }

            SubFabItem(
                item = item,
                modifier = Modifier
                    .offset(x = offsetX.value, y = offsetY.value)
                    .padding(
                        end = if (stack == FabMainConfig.Orientation.Stack.TOP) {
                            (fabWidthDp - item.buttonStyle.size) / 2
                        } else 0.dp
                    ).graphicsLayer { this.alpha = alpha.value },
                onClick = { item.onClick() }
            )
        }

        // Main FAB
        Box(
            modifier = Modifier.onSizeChanged { size ->
                fabWidthDp = with(density) { size.width.toDp() }
                fabHeightDp = with(density) { size.height.toDp() }
            }
        ) {
            content()
        }
    }
}
