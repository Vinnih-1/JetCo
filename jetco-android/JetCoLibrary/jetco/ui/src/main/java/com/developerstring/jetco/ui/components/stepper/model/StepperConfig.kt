package com.developerstring.jetco.ui.components.stepper.model

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.components.stepper.HorizontalStepper

/**
 * Defines the complete visual and behavioral styling of a stepper.
 *
 * This class groups related style properties into sub-configs for clarity:
 * - [NodeStyle] → appearance of the step node (circle, shape, colors, size)
 * - [ConnectorStyle] → style of the connecting lines between nodes
 * - [TextStyleConfig] → typography settings for title and description
 * - [AnimationConfig] → control animation behavior and duration
 * - [ImageConfig] → configuration for step images (only for [com.developerstring.jetco.ui.components.stepper.VerticalStepper])
 *
 * Example usage:
 * ```
 * StepperStyle(
 *     node = StepperStyle.NodeStyle(size = 40.dp),
 *     animation = StepperStyle.AnimationConfig(durationMillis = 1500)
 * )
 * ```
 */
@Stable
data class StepperConfig(
    val node: NodeStyle = NodeStyle(),
    val connector: ConnectorStyle = ConnectorStyle(),
    val textConfig: TextStyleConfig = TextStyleConfig(),
    val animation: AnimationConfig = AnimationConfig(),
    val imageConfig: ImageConfig = ImageConfig(),
) {
    /**
     * Defines the styling for a step node (circle or shape).
     *
     * @property activeColor Color for the active step node.
     * @property completedColor Color for a completed step node.
     * @property errorColor Color for a step node in error state.
     * @property inactiveColor Color for idle / pending step nodes.
     * @property size Size of the step node (width & height).
     * @property shape Shape of the node (e.g., circle, rounded rect).
     * @property internalSpacing Spacing between the node and the content.
     * @property horizontalStepperWidth minimum width for each [HorizontalStepper] item.
     */
    @Stable
    data class NodeStyle(
        val activeColor: Color = Color(0xFF1976D2),
        val completedColor: Color = Color(0xFF4CAF50),
        val errorColor: Color = Color(0xFFF44336),
        val inactiveColor: Color = Color(0xFFE0E0E0),
        val size: Dp = 32.dp,
        val shape: Shape = CircleShape,
        val internalSpacing: Dp = 16.dp,
        val spaceBetweenText: Dp = 6.dp,
        val horizontalStepperWidth: Dp = 120.dp,
        val actionIconColor: Color = Color.White,
        val idleIconColor: Color = Color.Gray,
    )

    /**
     * Defines the styling for connector lines between nodes.
     *
     * @property width Thickness of the connector line.
     * @property spacing Horizontal/vertical spacing between nodes.
     * @property lineLengthMin Minimum line length (prevents overlap in dense layouts).
     * @property pathEffect Optional dash effect for incomplete connectors.
     */
    @Stable
    data class ConnectorStyle(
        val width: Dp = 2.dp,
        val spacing: Dp = 8.dp,
        val lineLengthMin: Dp = 16.dp,
        val pathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    )

    /**
     * Defines the typography for titles and descriptions.
     *
     * @property maxTitleLines Maximum visible lines for title text.
     * @property maxDescriptionLines Maximum visible lines for description text.
     * @property overflow Text overflow handling (default: ellipsis).
     * @property titleTextStyle Style applied to step titles.
     * @property descriptionTextStyle Style applied to step descriptions.
     */
    @Stable
    data class TextStyleConfig(
        val maxTitleLines: Int = 2,
        val maxDescriptionLines: Int = 3,
        val overflow: TextOverflow = TextOverflow.Ellipsis,
        val titleTextStyle: TextStyle = TextStyle(fontSize = 16.sp),
        val descriptionTextStyle: TextStyle = TextStyle(fontSize = 14.sp)
    )

    /**
     * Defines animation behavior for the stepper.
     *
     * @property enabled Whether animations are enabled.
     * @property durationMillis Duration of node/connector animations in milliseconds.
     * @property animationSpec The animation specification to use (default: tween with [durationMillis]).
     */
    @Stable
    data class AnimationConfig(
        val enabled: Boolean = true,
        val durationMillis: Int = 1200,
        val animationSpec: AnimationSpec<Float> = tween(durationMillis)
    )

    /**
     * Defines image configuration for step images.
     * Image functionality is now only for [com.developerstring.jetco.ui.components.stepper.VerticalStepper].
     *
     * @property maxWidth Maximum width of the image.
     * @property maxHeight Maximum height of the image.
     * @property contentScale How the image should be scaled to fit within its bounds.
     */
    @Stable
    data class ImageConfig(
        val maxWidth: Dp? = null,
        val maxHeight: Dp? = null,
        val contentScale: ContentScale = ContentScale.Fit,
        val imageShape: Shape = RectangleShape,
        val paddingValues: PaddingValues = PaddingValues(4.dp)
    )
}