package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.components.SubFabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A Floating Action Button that morphs into an expanded card grid when activated.
 *
 * When collapsed, it displays a standard circular FAB. When expanded, it animates into
 * a rounded card containing a title header with a close button, and a configurable grid
 * of sub-action items. Sub-items fade in one by one with a staggered entrance animation.
 *
 * ## Example Usage:
 * ```kotlin
 * MorphFloatingActionButton(
 *     expanded = isExpanded,
 *     items = listOf(
 *         FabSubItem(
 *             onClick = { }
 *         )
 *     )
 * )
 * ```
 *
 * @param expanded Whether the FAB is currently expanded into card form.
 * @param items List of [FabSubItem] sub-actions to display in the card grid.
 * @param modifier Modifier applied to the root [Box] container.
 * @param onClick Click handler for both the main FAB button and the card close button.
 * @param title Optional composable rendered as the card header title.
 * @param config Visual and layout configuration. See [FabMainConfig].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MorphFloatingActionButton(
    expanded: Boolean,
    items: List<FabSubItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    title: (@Composable () -> Unit)? = null,
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(
            expanded = false,
            onClick = onClick,
            config = config
        )
    }
) {
    val morph = config.itemArrangement.morph

    Box(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
            .background(
                color = if (expanded) config.buttonStyle.color else Color.Transparent,
                shape = if (expanded) morph.cardShape else config.buttonStyle.shape
            )
    ) {
        if (expanded) {
            Column(
                modifier = Modifier
                    .width(morph.width)
                    .padding(16.dp)
            ) {
                // Card header: title on the left, close button on the right
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.align(Alignment.CenterStart)) {
                        title?.invoke()
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(morph.headerSpace))

                FlowRow(
                    maxItemsInEachRow = morph.columns,
                    horizontalArrangement = Arrangement.spacedBy(morph.spacedBy, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(morph.spacedBy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEachIndexed { index, item ->
                        val alpha = remember { Animatable(0f) }

                        LaunchedEffect(Unit) {
                            val stepMs = 300 / (items.size + 1)
                            val order = if (expanded) config.animation.enterOrder else config.animation.exitOrder
                            val staggerDelay = order.delayFor(
                                index = index,
                                total = items.size,
                                stepMs = stepMs
                            )
                            val transition = if (expanded) config.animation.enterTransition else config.animation.exitTransition

                            delay(staggerDelay)

                            val targetAlpha = if (expanded) 1f else 0f

                            if (transition.alphaSpec != null) {
                                launch { alpha.animateTo(targetAlpha, transition.alphaSpec) }
                            } else {
                                alpha.snapTo(targetAlpha)
                            }
                        }

                        SubFabItem(
                            item = item,
                            onClick = { item.onClick() },
                            modifier = Modifier.graphicsLayer { this.alpha = alpha.value }
                        )
                    }
                }
            }
        } else {
            Box(contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}
