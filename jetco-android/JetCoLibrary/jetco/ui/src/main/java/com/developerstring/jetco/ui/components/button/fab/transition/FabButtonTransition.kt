package com.developerstring.jetco.ui.components.button.fab.transition

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class FabButtonTransition internal constructor(
    val offset: OffsetTransition? = null,
    val scale: ScaleTransition? = null,
    val rotation: RotateTransition? = null,
    val spring: SpringTransition? = null
) {
    operator fun plus(other: FabButtonTransition): FabButtonTransition = FabButtonTransition(
        offset = other.offset ?: this.offset,
        scale = other.scale ?: this.scale,
        rotation = other.rotation ?: this.rotation,
        spring = other.spring ?: this.spring
    )

    companion object {

        fun SlideTo(
            x: Dp = 0.dp,
            y: Dp = 0.dp,
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabButtonTransition = FabButtonTransition(
            offset = OffsetTransition(
                offsetX = x,
                offsetY = y,
                spec = tween(durationMillis, easing = easing)
            )
        )

        fun Scale(
            scale: Float,
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabButtonTransition = FabButtonTransition(
            scale = ScaleTransition(
                target = scale,
                spec = tween(durationMillis, easing = easing)
            )
        )

        fun Rotate(
            rotation: Float,
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabButtonTransition = FabButtonTransition(
            rotation = RotateTransition(
                target = rotation,
                spec = tween(durationMillis, easing = easing)
            )
        )

        fun Spring(
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
            stiffness: Float = Spring.StiffnessMedium
        ): FabButtonTransition = FabButtonTransition(
            spring = SpringTransition(
                spec = spring(dampingRatio, stiffness)
            )
        )
    }
}