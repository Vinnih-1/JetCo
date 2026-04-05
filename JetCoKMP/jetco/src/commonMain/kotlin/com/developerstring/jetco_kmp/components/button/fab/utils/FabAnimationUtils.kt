package com.developerstring.jetco_kmp.components.button.fab.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.button.fab.model.FabMainConfig
import com.developerstring.jetco_kmp.components.button.fab.transition.FabButtonTransition
import com.developerstring.jetco_kmp.components.button.fab.transition.FabItemTransition
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal suspend fun animateFabItem(
    expanded: Boolean,
    transition: FabItemTransition,
    alpha: Animatable<Float, *>,
    scale: Animatable<Float, *>,
    rotation: Animatable<Float, *>,
    offsetX: Animatable<Dp, *>? = null,
    offsetY: Animatable<Dp, *>? = null,
    targetOffsetX: Dp = 0.dp,
    targetOffsetY: Dp = 0.dp,
) {
    coroutineScope {
        offsetX?.let {
            launch {
                it.animateOrSnap(
                    targetValue = if (expanded) targetOffsetX else 0.dp,
                    spec = transition.offsetSpec,
                    predicate = { expanded }
                )
            }
        }
        offsetY?.let {
            launch {
                it.animateOrSnap(
                    targetValue = if (expanded) targetOffsetY else 0.dp,
                    spec = transition.offsetSpec,
                    predicate = { expanded }
                )
            }
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

    if (!expanded) {
        offsetX?.snapTo(0.dp)
        offsetY?.snapTo(0.dp)
        alpha.snapTo(0f)
        scale.snapTo(0f)
        rotation.snapTo(0f)
    }
}

internal suspend fun animateFabButton(
    btnTransition: FabButtonTransition,
    fabOffsetX: Animatable<Dp, *>,
    fabOffsetY: Animatable<Dp, *>,
    fabScale: Animatable<Float, *>,
    fabRotation: Animatable<Float, *>,
    fabColor: Animatable<Color, AnimationVector4D>? = null,
) {
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
        fabColor?.let { colorAnim ->
            launch {
                btnTransition.color?.let { colorAnim.animateTo(it.target, it.spec) }
            }
        }
    }
    btnTransition.then?.let { next ->
        animateFabButton(next, fabOffsetX, fabOffsetY, fabScale, fabRotation, fabColor)
    }
}

internal fun FabButtonTransition.resolvedFinalOffset(): Pair<Dp, Dp> {
    var offsetX = 0.dp
    var offsetY = 0.dp
    var current: FabButtonTransition? = this
    while (current != null) {
        current.offset?.let {
            offsetX = it.offsetX
            offsetY = it.offsetY
        }
        current = current.then
    }
    return offsetX to offsetY
}

internal fun fabItemStaggerDelay(
    expanded: Boolean,
    index: Int,
    total: Int,
    totalCount: Int,
    config: FabMainConfig,
): Long {
    val stepMs = 300 / totalCount.coerceAtLeast(1)
    val order = if (expanded) config.animation.itemEnterOrder else config.animation.itemExitOrder
    return order.delayFor(
        index = index,
        total = (total).coerceAtLeast(0),
        stepMs = stepMs
    )
}
