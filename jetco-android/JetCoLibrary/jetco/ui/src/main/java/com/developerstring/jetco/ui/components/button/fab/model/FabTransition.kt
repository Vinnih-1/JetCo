package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp

class FabTransition internal constructor(
    internal val offsetSpec: AnimationSpec<Dp>?,
    internal val alphaSpec: AnimationSpec<Float>?
) {
    operator fun plus(other: FabTransition): FabTransition = FabTransition(
        offsetSpec = other.offsetSpec ?: this.offsetSpec,
        alphaSpec = other.alphaSpec ?: this.alphaSpec
    )

    companion object {
        fun Spring(
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
            stiffness: Float = Spring.StiffnessMedium
        ): FabTransition = FabTransition(
            offsetSpec = spring(dampingRatio, stiffness),
            alphaSpec = null
        )

        fun Slide(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabTransition = FabTransition(
            offsetSpec = tween(durationMillis, easing = easing),
            alphaSpec = null
        )

        fun Fade(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabTransition = FabTransition(
            offsetSpec = null,
            alphaSpec = tween(durationMillis, easing = easing)
        )

        fun SlideAndFade(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabTransition = FabTransition(
            offsetSpec = tween(durationMillis, easing = easing),
            alphaSpec = tween(durationMillis, easing = easing)
        )
    }
}