package com.developerstring.jetco.ui.components.button.fab.transition

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Describes how the main FAB button animates when the component expands or collapses.
 *
 * Transitions can be combined using the `+` operator.
 *
 * ## Example:
 * ```kotlin
 * val buttonTransition = FabButtonTransition.Rotate(45f) + FabButtonTransition.Scale(0.8f)
 * ```
 *
 * @property offset Translation animation for the main button.
 * @property scale Scale animation for the main button.
 * @property rotation Rotation animation for the main button.
 * @property spring Optional global spring override for all properties.
 */
class FabButtonTransition internal constructor(
    val offset: OffsetTransition? = null,
    val scale: ScaleTransition? = null,
    val rotation: RotateTransition? = null,
    val spring: SpringTransition? = null
) {
    /**
     * Combines two button transitions. Properties in [other] take precedence.
     */
    operator fun plus(other: FabButtonTransition): FabButtonTransition = FabButtonTransition(
        offset = other.offset ?: this.offset,
        scale = other.scale ?: this.scale,
        rotation = other.rotation ?: this.rotation,
        spring = other.spring ?: this.spring
    )

    companion object {

        /**
         * Animates the main button position to a specific (x, y) offset.
         */
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

        /**
         * Scales the main button to a specific target value.
         */
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

        /**
         * Rotates the main button to a specific target angle.
         */
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

        /**
         * Overrides the animation spec with physics-based spring behavior.
         */
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
