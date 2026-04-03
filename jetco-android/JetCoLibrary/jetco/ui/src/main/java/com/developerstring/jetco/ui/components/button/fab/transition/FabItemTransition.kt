package com.developerstring.jetco.ui.components.button.fab.transition

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp

/**
 * Describes how an item (FAB) animates when it enters or exits the screen.
 *
 * Transitions can be combined using the `+` operator.
 * Each factory function returns a new [FabItemTransition] that only defines the specific
 * animation spec for its property.
 *
 * ## Example:
 * ```kotlin
 * val myItemTransition = FabItemTransition.Slide() + FabItemTransition.Fade()
 * ```
 *
 * @property offsetSpec Animation for the item's movement (displacement).
 * @property alphaSpec Animation for the item's transparency.
 * @property scaleSpec Animation for the item's size (X and Y).
 * @property rotate Configuration for the item's rotation.
 */
class FabItemTransition(
    val offsetSpec: AnimationSpec<Dp>? = null,
    val alphaSpec: AnimationSpec<Float>? = null,
    val scaleSpec: AnimationSpec<Float>? = null,
    val rotate: RotateTransition? = null
) {
    operator fun plus(other: FabItemTransition): FabItemTransition = FabItemTransition(
        offsetSpec = other.offsetSpec ?: this.offsetSpec,
        alphaSpec = other.alphaSpec ?: this.alphaSpec,
        scaleSpec = other.scaleSpec ?: this.scaleSpec,
        rotate = other.rotate ?: this.rotate
    )

    companion object {

        fun Slide(
            stiffness: Float = Spring.StiffnessMedium,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        ): FabItemTransition = FabItemTransition(
            offsetSpec = spring(dampingRatio = dampingRatio, stiffness = stiffness)
        )

        fun Fade(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabItemTransition = FabItemTransition(
            alphaSpec = tween(durationMillis, easing = easing),
        )

        fun SlideAndFade(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing,
            stiffness: Float = Spring.StiffnessMedium,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        ): FabItemTransition = FabItemTransition(
            offsetSpec = spring(dampingRatio = dampingRatio, stiffness = stiffness),
            alphaSpec = tween(durationMillis, easing = easing),
        )

        fun Scale(
            stiffness: Float = Spring.StiffnessMedium,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        ): FabItemTransition = FabItemTransition(
            scaleSpec = spring(dampingRatio = dampingRatio, stiffness = stiffness)
        )

        fun Rotate(
            target: Float,
            stiffness: Float = Spring.StiffnessMedium,
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        ): FabItemTransition = FabItemTransition(
            rotate = RotateTransition(
                target = target,
                spec = spring(dampingRatio = dampingRatio, stiffness = stiffness)
            )
        )
    }
}
