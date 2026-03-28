package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFabItem
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * A Floating Action Button that expands sub-items radially in an arc around the main button.
 *
 * When expanded, sub-items fan out in a configurable arc using spring physics with a staggered
 * delay, creating a natural and lively feel. When collapsed, items snap back with a clean tween.
 * The arc direction and radius are fully configurable via [FabMainConfig.ItemArrangement].
 *
 * ## Example Usage:
 * ```kotlin
 * RadialFloatingActionButton(
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
 * @param items List of [FabItem] sub-actions to display when expanded.
 * @param modifier Modifier applied to the root [Box] container.
 * @param onClick Click handler for the main FAB button.
 * @param config Visual and layout configuration. See [FabMainConfig].
 */
@Composable
fun RadialFloatingActionButton(
    expanded: Boolean,
    items: List<FabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    RadialFloatingActionButtonBase(
        expanded = expanded,
        itemCount = items.size,
        modifier = modifier,
        onClick = onClick,
        config = config,
        content = content,
        itemContent = { index ->
            val item = items[index]

            DefaultFabItem(
                item = item,
                modifier = Modifier.padding(end = (config.buttonStyle.size - item.buttonStyle.size) / 2),
                onClick = { item.onClick() }
            )
        }
    )
}

@JvmName("RadialFloatingActionButtonCustom")
@Composable
fun RadialFloatingActionButton(
    expanded: Boolean,
    items: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    RadialFloatingActionButtonBase(
        expanded = expanded,
        itemCount = items.size,
        modifier = modifier,
        onClick = onClick,
        config = config,
        content = content,
        itemContent = { items[it]() }
    )
}

@Composable
internal fun RadialFloatingActionButtonBase(
    expanded: Boolean,
    itemCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    },
    itemContent: @Composable (index: Int) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        val fabOffsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
        val fabOffsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

        // Sub-items — laid out behind the main FAB
        repeat(itemCount) { index ->
            val startAngle = config.itemArrangement.radial.arc.start
            val endAngle = config.itemArrangement.radial.arc.end

            val angleDeg = if (itemCount == 1) {
                (startAngle + endAngle) / 2.0  // single item lands at the midpoint of the arc
            } else {
                startAngle + (endAngle - startAngle) * (index.toDouble() / (itemCount - 1))
            }
            val angleRad = Math.toRadians(angleDeg)

            val targetOffsetX = (config.itemArrangement.radial.radius.value * cos(angleRad)).dp
            val targetOffsetY = (config.itemArrangement.radial.radius.value * sin(angleRad)).dp

            val rotation = remember { Animatable(0f) }
            val scale = remember { Animatable(0f) }
            val alpha = remember { Animatable(0f) }
            val offsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
            val offsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

            LaunchedEffect(expanded) {
                val stepMs = 300 / itemCount
                val order = if (expanded) config.animation.enterOrder else config.animation.exitOrder
                val transition = if (expanded) config.animation.enterTransition else config.animation.exitTransition
                val staggerDelay = order.delayFor(index = index, total = (itemCount - 1), stepMs = stepMs)

                delay(staggerDelay)

                coroutineScope {
                    launch {
                        offsetX.animateOrSnap(
                            targetValue = if (expanded) targetOffsetX else fabOffsetX.value,
                            spec = transition.offsetSpec,
                            predicate = { expanded }
                        )
                    }
                    launch {
                        offsetY.animateOrSnap(
                            targetValue = if (expanded) targetOffsetY else -fabOffsetY.value,
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
                    rotation.snapTo(0f)
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = offsetX.value + fabOffsetX.value, y = -offsetY.value + fabOffsetY.value)
                    .graphicsLayer {
                        this.alpha = alpha.value
                        this.scaleX = scale.value
                        this.scaleY = scale.value
                        this.rotationZ = rotation.value
                    }
            ) {
                itemContent(index)
            }
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

        // Main FAB button
        Box(
            modifier = Modifier
                .offset(x = fabOffsetX.value, y = fabOffsetY.value)
                .graphicsLayer {
                    scaleX = fabScale.value
                    scaleY = fabScale.value
                    rotationZ = fabRotation.value
                }
        ) {
            content()
        }
    }
}
