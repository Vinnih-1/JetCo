package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.BaseFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.components.SubFabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadialFloatingActionButton(
    expanded: Boolean,
    items: List<FabSubItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: (@Composable () -> Unit)? = null,
    config: FabMainConfig = FabMainConfig()
) {
    Box(
        modifier = modifier
    ) {
        // Sub-items — laid out behind the main FAB
        items.forEachIndexed { index, item ->
            val animatedAlpha by animateFloatAsState(
                targetValue = if (expanded) 1f else 0f,
                animationSpec = config.animation.animationSpec,
                label = "alpha_$index"
            )
            val startAngle = config.itemArrangement.radial.start
            val endAngle = config.itemArrangement.radial.end

            val angleDeg = if (items.size == 1) {
                (startAngle + endAngle) / 2.0  // single item lands at the midpoint of the arc
            } else {
                startAngle + (endAngle - startAngle) * (index.toDouble() / items.lastIndex)
            }
            val angleRad = Math.toRadians(angleDeg)

            val targetOffsetX = if (expanded) (config.itemArrangement.radius.value * cos(angleRad)).dp else 0.dp
            val targetOffsetY = if (expanded) (config.itemArrangement.radius.value * sin(angleRad)).dp else 0.dp

            val offsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
            val offsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

            val springSpec: AnimationSpec<Dp> = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )

            val tweenSpec: AnimationSpec<Dp> = tween(
                durationMillis = config.animation.durationMillis,
                easing = config.animation.easing
            )

            LaunchedEffect(expanded) {
                val staggerDelay = (index * 60).toLong()
                if (expanded) {
                    delay(staggerDelay)
                    launch { offsetX.animateTo(targetOffsetX, springSpec) }
                    launch { offsetY.animateTo(targetOffsetY, springSpec) }
                } else {
                    launch { offsetX.animateTo(0.dp, tweenSpec) }
                    launch { offsetY.animateTo(0.dp, tweenSpec) }
                }
            }

            SubFabItem(
                item = item,
                modifier = Modifier
                    .offset(x = offsetX.value, y = -offsetY.value)
                    .padding(end = (config.buttonStyle.size - item.style.size) / 2)
                    .graphicsLayer { alpha = animatedAlpha },
                onClick = { item.onClick() }
            )
        }

        // Main FAB button
        BaseFloatingActionButton(
            expanded = expanded,
            text = null,
            icon = icon,
            onClick = onClick,
            config = config
        )
    }
}

