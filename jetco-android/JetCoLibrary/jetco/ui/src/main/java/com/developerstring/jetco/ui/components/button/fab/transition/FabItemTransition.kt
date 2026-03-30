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
 * val myItemTransition = FabItemTransition.Spring() + FabItemTransition.Fade()
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
        fun Spring(
            dampingRatio: Float = Spring.DampingRatioMediumBouncy,
            stiffness: Float = Spring.StiffnessMedium
        ): FabItemTransition = FabItemTransition(
            offsetSpec = spring(dampingRatio, stiffness)
        )

        fun Slide(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabItemTransition = FabItemTransition(
            offsetSpec = tween(durationMillis, easing = easing)
        )

        fun Fade(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabItemTransition = FabItemTransition(
            alphaSpec = tween(durationMillis, easing = easing),
        )

        fun SlideAndFade(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabItemTransition = FabItemTransition(
            offsetSpec = tween(durationMillis, easing = easing),
            alphaSpec = tween(durationMillis, easing = easing),
        )

        fun Scale(
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabItemTransition = FabItemTransition(
            scaleSpec = tween(durationMillis, easing = easing)
        )

        fun Rotate(
            target: Float,
            durationMillis: Int = 300,
            easing: Easing = FastOutSlowInEasing
        ): FabItemTransition = FabItemTransition(
            rotate = RotateTransition(
                target = target,
                spec = tween(durationMillis, easing = easing)
            )
        )
    }
}
