package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data model representing a single sub-action item displayed by a FAB variant.
 *
 * Each [FabItem] carries an action callback, an optional icon, an optional title,
 * and independent style configurations for both the button and the title label.
 * The title is only rendered when the FAB variant explicitly enables it.
 *
 * For [StackFloatingActionButton], each item can also carry a [direction] that controls
 * which side of the main FAB it spreads toward.
 *
 * @param onClick Action invoked when the sub-item is clicked.
 * @param icon Optional icon displayed inside the sub-item button.
 * @param buttonStyle Visual style of the sub-item button. See [ButtonStyle].
 */
@Stable
data class FabItem(
    val onClick: () -> Unit,
    val icon: ImageVector? = null,
    val buttonStyle: ButtonStyle = ButtonStyle()
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
}
