package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Main configuration class for all Floating Action Button variants in JetCo.
 *
 * @param buttonStyle Visual appearance of the main FAB button. See [ButtonStyle].
 * @param itemArrangement Layout and orientation of sub-items. See [ItemArrangement].
 * @param animation Animation timing and spec used across all transitions. See [Animation].
 */
@Stable
data class FabMainConfig(
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val itemArrangement: ItemArrangement = ItemArrangement(),
    val animation: Animation = Animation()
) {

    /**
     * Sealed interface representing the available layout orientations for FAB sub-items.
     *
     * Use [Radial] for arc-based spreading, [Stack] for linear stacking,
     * and [Morph] for the card expansion layout.
     */
    sealed interface Orientation {

        /**
         * Radial orientation that spreads sub-items in an arc around the main FAB.
         *
         * @property start Start angle of the arc in degrees (standard math convention).
         * @property end End angle of the arc in degrees.
         */
        enum class Radial(val start: Double, val end: Double) : Orientation {
            /** Spreads items from 90° to 180° (upward and to the left). */
            END(90.0, 180.0),
            /** Spreads items from 90° to 0° (upward and to the right). */
            START(90.0, 0.0),
            /** Spreads items from 0° to 180° (full upper arc). */
            CENTER(0.0, 180.0)
        }

        /**
         * Stack orientation that spreads sub-items linearly in one direction.
         *
         * @property spacedBy Gap between each sub-item. Default is 40.dp.
         */
        enum class Stack(val spacedBy: Dp = 40.dp) : Orientation {
            /** Spreads items upward above the main FAB. */
            TOP,
            /** Spreads items to the left of the main FAB. */
            START,
            /** Spreads items to the right of the main FAB. */
            END
        }

        /**
         * Morph orientation that expands the main FAB into a card grid.
         *
         * @param columns Number of sub-item columns in the card grid. Default is 2.
         * @param spacedBy Gap between sub-items inside the grid. Default is 12.dp.
         * @param headerSpace Space between the card header and the item grid. Default is 20.dp.
         * @param width Total width of the expanded card. Default is 250.dp.
         * @param cardShape Shape of the expanded card. Default is [RoundedCornerShape] with 24.dp.
         */
        data class Morph(
            val columns: Int = 2,
            val spacedBy: Dp = 12.dp,
            val headerSpace: Dp = 20.dp,
            val width: Dp = 250.dp,
            val cardShape: Shape = RoundedCornerShape(24.dp)
        ) : Orientation
    }

    /**
     * Animation configuration shared across all FAB variants.
     *
     * @param durationMillis Duration of each animation in milliseconds. Default is 300.
     * @param easing Easing curve applied to tween-based animations. Default is [FastOutSlowInEasing].
     * @param animationSpec Full [AnimationSpec] used for float animations such as alpha and rotation.
     */
    @Stable
    open class Animation(
        val durationMillis: Int = 300,
        val easing: Easing = FastOutSlowInEasing,
        val animationSpec: AnimationSpec<Float> = tween(durationMillis, easing = easing)
    )

    /**
     * Visual style configuration for the main FAB button.
     *
     * @param color Background color of the main FAB. Default is Material blue.
     * @param shape Shape of the main FAB button. Default is [CircleShape].
     * @param horizontalSpace Horizontal gap between icon and text when both are present. Default is 12.dp.
     * @param size Diameter (and height) of the main FAB. Width expands via [defaultMinSize] when text is added. Default is 72.dp.
     * @param iconRotation Target rotation angle of the icon when the FAB is expanded. Default is 45f.
     * @param padding Internal padding applied inside the FAB button row. Default is no padding.
     */
    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val horizontalSpace: Dp = 12.dp,
        val size: Dp = 72.dp,
        val iconRotation: Float = 45f,
        val padding: PaddingValues = PaddingValues()
    )

    /**
     * Layout and orientation configuration for FAB sub-items.
     *
     * @param radius Distance from the main FAB center to each sub-item in radial layout. Default is 80.dp.
     * @param radial Radial arc orientation. Default is [Orientation.Radial.END].
     * @param stack Stack direction orientation. Default is [Orientation.Stack.TOP].
     * @param morph Morph card configuration. Default is [Orientation.Morph].
     */
    @Stable
    data class ItemArrangement(
        val radius: Dp = 80.dp,
        val radial: Orientation.Radial = Orientation.Radial.END,
        val stack: Orientation.Stack = Orientation.Stack.TOP,
        val morph: Orientation.Morph = Orientation.Morph()
    )
}
