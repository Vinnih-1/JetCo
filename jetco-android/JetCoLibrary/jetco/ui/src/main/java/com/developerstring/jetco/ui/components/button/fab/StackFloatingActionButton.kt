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
import kotlinx.coroutines.coroutineScope
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
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    val direction = config.itemArrangement.stack.direction
    val density = LocalDensity.current
    var fabWidthDp by remember { mutableStateOf(config.buttonStyle.size) }
    var fabHeightDp by remember { mutableStateOf(config.buttonStyle.size) }
    val spacedBy = config.itemArrangement.stack.spacedBy

    // The Box anchor changes depending on which direction items spread
    val alignment = when (direction) {
        FabMainConfig.Orientation.Stack.Direction.TOP -> Alignment.BottomEnd
        FabMainConfig.Orientation.Stack.Direction.START -> Alignment.CenterEnd
        FabMainConfig.Orientation.Stack.Direction.END -> Alignment.CenterStart
    }

    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        val fabOffsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
        val fabOffsetY = remember { Animatable(0.dp, Dp.VectorConverter) }
        // Sub-items — stacked in the direction defined by Orientation.Stack
        items.forEachIndexed { index, item ->

            val spacing = when (direction) {
                FabMainConfig.Orientation.Stack.Direction.TOP ->
                    fabHeightDp + spacedBy + index * (item.buttonStyle.size + spacedBy)
                else ->
                    fabWidthDp + spacedBy + index * (item.buttonStyle.size + spacedBy)
            }

            // Each orientation moves items along a different axis
            val targetOffsetX = when (direction) {
                FabMainConfig.Orientation.Stack.Direction.START -> -spacing
                FabMainConfig.Orientation.Stack.Direction.END -> spacing
                else -> 0.dp
            }

            val targetOffsetY = when (direction) {
                FabMainConfig.Orientation.Stack.Direction.TOP -> -spacing
                else -> 0.dp
            }

            val rotation = remember { Animatable(0f) }
            val scale = remember { Animatable(0f) }
            val alpha = remember { Animatable(0f) }
            val offsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
            val offsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

            LaunchedEffect(expanded) {
                val stepMs = 300 / (items.size + 1)
                val order = if (expanded) config.animation.enterOrder else config.animation.exitOrder
                val transition = if (expanded) config.animation.enterTransition else config.animation.exitTransition
                val staggerDelay = order.delayFor(index = index, total = items.size, stepMs = stepMs)

                delay(staggerDelay)

                coroutineScope {
                    launch {
                        offsetX.animateOrSnap(
                            targetValue = if (expanded) targetOffsetX else 0.dp,
                            spec = transition.offsetSpec,
                            predicate = { expanded }
                        )
                    }
                    launch {
                        offsetY.animateOrSnap(
                            targetValue = if (expanded) targetOffsetY else 0.dp,
                            spec = transition.offsetSpec,
                            predicate = { expanded }
                        )
                    }
                    launch {
                        alpha.animateOrSnap(
                            targetValue = if (expanded) 1f else 0f,
                            spec = transition.alphaSpec,
                            predicate = { expanded }
                        )
                    }
                    launch {
                        scale.animateOrSnap(
                            targetValue = if (expanded) 1f else 0f,
                            spec = transition.scaleSpec,
                            predicate = { expanded }
                        )
                    }
                    launch {
                        rotation.animateOrSnap(
                            targetValue = transition.rotate?.target,
                            spec = transition.rotate?.spec,
                            predicate = { expanded }
                        )
                    }
                }

                if (!expanded) { // Reset to initial position
                    offsetX.snapTo(0.dp)
                    offsetY.snapTo(0.dp)
                    alpha.snapTo(0f)
                    scale.snapTo(0f)
                }
            }

            SubFabItem(
                item = item,
                modifier = Modifier
                    .offset(x = offsetX.value + fabOffsetX.value, y = offsetY.value + fabOffsetY.value)
                    .padding(
                        end = if (direction == FabMainConfig.Orientation.Stack.Direction.TOP) {
                            (fabWidthDp - item.buttonStyle.size) / 2
                        } else 0.dp
                    ).graphicsLayer {
                        this.alpha = alpha.value
                        this.scaleX = scale.value
                        this.scaleY = scale.value
                        this.rotationZ = rotation.value
                    },
                onClick = { item.onClick() }
            )
        }

        val fabScale = remember { Animatable(1f) }
        val fabRotation = remember { Animatable(0f) }

        LaunchedEffect(expanded) {
            val btnTransition = if (expanded) config.animation.buttonEnterTransition
            else config.animation.buttonExitTransition

            coroutineScope {
                launch {
                    fabOffsetX.animateOrSnap(btnTransition.offset?.offsetX, btnTransition.offset?.spec)
                }
                launch {
                    fabOffsetY.animateOrSnap(btnTransition.offset?.offsetY, btnTransition.offset?.spec)
                }
                launch {
                    fabScale.animateOrSnap(btnTransition.scale?.target, btnTransition.scale?.spec)
                }
                launch {
                    fabRotation.animateOrSnap(btnTransition.rotation?.target, btnTransition.rotation?.spec)
                }
            }
        }

        // Main FAB
        Box(
            modifier = Modifier
                .offset(x = fabOffsetX.value, y = fabOffsetY.value)
                .graphicsLayer {
                    scaleX = fabScale.value
                    scaleY = fabScale.value
                    rotationZ = fabRotation.value
                }.onSizeChanged { size ->
                    fabWidthDp = with(density) { size.width.toDp() }
                    fabHeightDp = with(density) { size.height.toDp() }
                }
        ) {
            content()
        }
    }
}
