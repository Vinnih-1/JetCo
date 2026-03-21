package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data model representing a single sub-action item displayed by a FAB variant.
 *
 * Each [FabSubItem] carries an action callback, an optional icon, an optional title,
 * and independent style configurations for both the button and the title label.
 * The title is only rendered when the FAB variant explicitly enables it
 *
 * @param onClick Action invoked when the sub-item is clicked.
 * @param title Optional label displayed below the icon.
 * @param icon Optional icon displayed inside the sub-item button.
 * @param buttonStyle Visual style of the sub-item button. See [ButtonStyle].
 * @param titleStyle Visual style of the title label. See [TitleStyle].
 */
@Stable
data class FabSubItem(
    val onClick: () -> Unit,
    val title: String? = null,
    val icon: ImageVector? = null,
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val titleStyle: TitleStyle = TitleStyle()
) {

    /**
     * Visual style configuration for the sub-item button.
     *
     * @param color Background color of the sub-item button. Default is Material blue.
     * @param shape Shape of the sub-item button. Default is [CircleShape].
     * @param size Diameter of the sub-item button. Default is 52.dp.
     */
    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val size: Dp = 52.dp
    )

    /**
     * Visual style configuration for the sub-item title label.
     *
     * @param color Text color of the title. Default is [Color.White].
     * @param size Font size of the title in Dp. Default is 12.dp.
     * @param weight Font weight of the title. Default is [FontWeight.Light].
     * @param maxLines Maximum number of lines before the text is ellipsized. Default is 1.
     * @param style Base [TextStyle] applied to the title. Default is [TextStyle.Default].
     */
    @Stable
    data class TitleStyle(
        val color: Color = Color.White,
        val size: Dp = 12.dp,
        val weight: FontWeight = FontWeight.Light,
        val maxLines: Int = 1,
        val style: TextStyle = TextStyle.Default
    )
}
