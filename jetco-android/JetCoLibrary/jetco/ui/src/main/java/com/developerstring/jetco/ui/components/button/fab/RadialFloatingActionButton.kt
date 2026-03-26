package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.components.SubFabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem
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
 * @param items List of [FabSubItem] sub-actions to display when expanded.
 * @param modifier Modifier applied to the root [Box] container.
 * @param onClick Click handler for the main FAB button.
 * @param config Visual and layout configuration. See [FabMainConfig].
 */
@Composable
fun RadialFloatingActionButton(
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
    Box(
        modifier = modifier
    ) {
        // Sub-items — laid out behind the main FAB
        items.forEachIndexed { index, item ->
            val startAngle = config.itemArrangement.radial.arc.start
            val endAngle = config.itemArrangement.radial.arc.end

            val angleDeg = if (items.size == 1) {
                (startAngle + endAngle) / 2.0  // single item lands at the midpoint of the arc
            } else {
                startAngle + (endAngle - startAngle) * (index.toDouble() / items.lastIndex)
            }
            val angleRad = Math.toRadians(angleDeg)

            val targetOffsetX = if (expanded) (config.itemArrangement.radial.radius.value * cos(angleRad)).dp else 0.dp
            val targetOffsetY = if (expanded) (config.itemArrangement.radial.radius.value * sin(angleRad)).dp else 0.dp

            val alpha = remember { Animatable(0f) }
            val offsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
            val offsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

            LaunchedEffect(expanded) {
                val stepMs = 300 / (items.size + 1)
                val order = if (expanded) config.animation.enterOrder else config.animation.exitOrder
                val transition = if (expanded) config.animation.enterTransition else config.animation.exitTransition
                val staggerDelay = order.delayFor(index = index, total = items.size, stepMs = stepMs)

                delay(staggerDelay)

                val targetX = if (expanded) targetOffsetX else targetOffsetX
                val targetY = if (expanded) targetOffsetY else targetOffsetY
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
                    .offset(x = offsetX.value, y = -offsetY.value)
                    .padding(end = (config.buttonStyle.size - item.buttonStyle.size) / 2)
                    .graphicsLayer { this.alpha = alpha.value },
                onClick = { item.onClick() }
            )
        }

        // Main FAB button
        Box {
            content()
        }
    }
}
