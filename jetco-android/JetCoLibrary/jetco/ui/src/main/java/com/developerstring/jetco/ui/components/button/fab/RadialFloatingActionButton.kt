package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFabItem
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.model.FabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.RadialFabItem
import com.developerstring.jetco.ui.components.button.fab.utils.LocalFabButtonColor
import com.developerstring.jetco.ui.components.button.fab.utils.animateFabButton
import com.developerstring.jetco.ui.components.button.fab.utils.animateFabItem
import com.developerstring.jetco.ui.components.button.fab.utils.fabItemStaggerDelay
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * A floating action button that expands items in a radial (arc) pattern.
 *
 * This component provides a main FAB that, when expanded, reveals a set of items
 * arranged along a configurable arc. It supports both simple [FabItem]s (icons with actions)
 * and custom [RadialFabItem]s for full composable control.
 *
 * @param expanded Whether the FAB is currently showing its items.
 * @param items List of [FabItem] to be displayed as buttons.
 * @param modifier Modifier applied to the root container of the FAB.
 * @param onClick Callback triggered when the main FAB button is clicked.
 * @param config Configuration for styling and animations. See [FabMainConfig].
 * @param content Optional custom composable for the main FAB button. Defaults to [DefaultFloatingActionButton].
 *
 * @see FabMainConfig for detailed arrangement and animation options.
 * @see FabItem for item data model.
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
    val items = items.map { item ->
        RadialFabItem {
            DefaultFabItem(item = item, onClick = { item.onClick() })
        }
    }

    RadialFloatingActionButtonBase(
        expanded = expanded,
        items = items,
        modifier = modifier,
        onClick = onClick,
        config = config,
        content = content
    )
}

/**
 * A floating action button that expands custom [RadialFabItem]s in a radial (arc) pattern.
 *
 * Use this overload when you need to provide fully custom composables for each item
 * instead of the standard icon/button pair.
 *
 * @param expanded Whether the FAB is currently showing its items.
 * @param items List of [RadialFabItem] containing custom composables.
 * @param modifier Modifier applied to the root container of the FAB.
 * @param onClick Callback triggered when the main FAB button is clicked.
 * @param config Configuration for styling and animations.
 * @param content Optional custom composable for the main FAB button.
 */
@JvmName("RadialFloatingActionButtonCustom")
@Composable
fun RadialFloatingActionButton(
    expanded: Boolean,
    items: List<RadialFabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    RadialFloatingActionButtonBase(
        expanded = expanded,
        items = items,
        modifier = modifier,
        onClick = onClick,
        config = config,
        content = content
    )
}

@Composable
internal fun RadialFloatingActionButtonBase(
    expanded: Boolean,
    items: List<RadialFabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        val fabOffsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
        val fabOffsetY = remember { Animatable(0.dp, Dp.VectorConverter) }
        val fabScale = remember { Animatable(1f) }
        val fabRotation = remember { Animatable(0f) }
        val fabColor = remember { Animatable(config.buttonStyle.color, Color.VectorConverter(config.buttonStyle.color.colorSpace)) }

        items.forEachIndexed { index, radialItem ->
            val startAngle = config.itemArrangement.radial.arc.start
            val endAngle = config.itemArrangement.radial.arc.end

            val angleDeg = if (items.size == 1) {
                (startAngle + endAngle) / 2.0
            } else {
                startAngle + (endAngle - startAngle) * (index.toDouble() / (items.size - 1))
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
                val transition = if (expanded) config.animation.itemEnterTransition else config.animation.itemExitTransition
                val staggerDelay = fabItemStaggerDelay(
                    expanded = expanded,
                    index = index,
                    total = items.size - 1,
                    totalCount = (items.size - 1).coerceAtLeast(1),
                    config = config
                )
                val enterDelay = if (expanded) config.animation.itemEnterDelay else 0L
                delay(enterDelay + staggerDelay)

                animateFabItem(
                    expanded = expanded,
                    transition = transition,
                    alpha = alpha,
                    scale = scale,
                    rotation = rotation,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    targetOffsetX = if (expanded) targetOffsetX else fabOffsetX.value,
                    targetOffsetY = if (expanded) targetOffsetY else -fabOffsetY.value,
                )
            }

            Box(
                modifier = Modifier
                    .offset(x = offsetX.value + fabOffsetX.value - 8.dp, y = -offsetY.value + fabOffsetY.value)
                    .graphicsLayer {
                        this.alpha = alpha.value
                        this.scaleX = scale.value
                        this.scaleY = scale.value
                        this.rotationZ = rotation.value
                    }
            ) {
                radialItem.content()
            }
        }

        LaunchedEffect(expanded) {
            val btnTransition = if (expanded) config.animation.buttonEnterTransition
            else config.animation.buttonExitTransition

            animateFabButton(
                btnTransition = btnTransition,
                fabOffsetX = fabOffsetX,
                fabOffsetY = fabOffsetY,
                fabScale = fabScale,
                fabRotation = fabRotation,
                fabColor = fabColor,
            )
        }

        CompositionLocalProvider(LocalFabButtonColor provides fabColor.value) {
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
}
