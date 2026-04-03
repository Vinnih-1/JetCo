package com.developerstring.jetco.ui.components.button.fab.transition

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
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
 * val enterTransition  = FabButtonTransition.ColorTo(Color.Red) + FabButtonTransition.Rotate(45f)
 * ```
 *
 * @property offset Translation animation for the main button.
 * @property scale Scale animation for the main button.
 * @property rotation Rotation animation for the main button.
 * @property color Background color animation for the main button.
 * @property spring Optional global spring override for all properties.
 * @property then Optional transition to run sequentially after this one completes.
 */
class FabButtonTransition(
    val offset: OffsetTransition? = null,
    val scale: ScaleTransition? = null,
    val rotation: RotateTransition? = null,
    val color: ColorTransition? = null,
    val then: FabButtonTransition? = null
) {
    /**
     * Combines two button transitions. Properties in [other] take precedence.
     */
    operator fun plus(other: FabButtonTransition): FabButtonTransition = FabButtonTransition(
        offset = other.offset ?: this.offset,
        scale = other.scale ?: this.scale,
        rotation = other.rotation ?: this.rotation,
        color = other.color ?: this.color,
        then = other.then ?: this.then
    )

    companion object {

        /**
         * Animates the main button position to a specific (x, y) offset.
         *
         * @param x Target horizontal offset.
         * @param y Target vertical offset.
         * @param stiffness How quickly the spring moves toward its target.
         * @param dampingRatio How much the spring oscillates.
         * @param then Optional transition to run sequentially after this one completes.
         */
        fun SlideTo(
            x: Dp = 0.dp,
            y: Dp = 0.dp,
            stiffness: Float = Spring.StiffnessMediumLow,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
            then: FabButtonTransition? = null
        ): FabButtonTransition = FabButtonTransition(
            offset = OffsetTransition(
                offsetX = x,
                offsetY = y,
                spec = spring(dampingRatio = dampingRatio, stiffness = stiffness)
            ),
            then = then
        )

        /**
         * Scales the main button to a specific target value.
         *
         * @param scale Target scale factor (e.g. `0.9f` to slightly shrink).
         * @param stiffness How quickly the spring moves toward its target.
         * @param dampingRatio How much the spring oscillates.
         * @param then Optional transition to run sequentially after this one completes.
         */
        fun Scale(
            scale: Float,
            stiffness: Float = Spring.StiffnessMediumLow,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
            then: FabButtonTransition? = null
        ): FabButtonTransition = FabButtonTransition(
            scale = ScaleTransition(
                target = scale,
                spec = spring(dampingRatio = dampingRatio, stiffness = stiffness)
            ),
            then = then
        )

        /**
         * Rotates the main button to a specific target angle.
         *
         * @param rotation Target rotation in degrees.
         * @param stiffness How quickly the spring moves toward its target.
         * @param dampingRatio How much the spring oscillates.
         * @param then Optional transition to run sequentially after this one completes.
         */
        fun Rotate(
            rotation: Float,
            stiffness: Float = Spring.StiffnessMediumLow,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
            then: FabButtonTransition? = null
        ): FabButtonTransition = FabButtonTransition(
            rotation = RotateTransition(
                target = rotation,
                spec = spring(dampingRatio = dampingRatio, stiffness = stiffness)
            ),
            then = then
        )

        /**
         * Animates the main button's background color to [color].
         *
         * @param color Target background color.
         * @param durationMillis Duration of the tween in milliseconds.
         * @param easing Easing curve for the tween.
         * @param then Optional transition to run sequentially after this one completes.
         */
        fun ColorTo(
            color: Color,
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing,
            then: FabButtonTransition? = null
        ): FabButtonTransition = FabButtonTransition(
            color = ColorTransition(
                target = color,
                spec = tween(durationMillis = durationMillis, easing = easing)
            ),
            then = then
        )
    }
}
