package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.StackFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.MorphFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.RadialFloatingActionButton

/**
 * Data model representing a standard item in a Floating Action Button (FAB) menu.
 *
 * This is the default item type used by [RadialFloatingActionButton], [StackFloatingActionButton],
 * and [MorphFloatingActionButton]. It consists of an icon, a click action, and visual styling.
 *
 * @property onClick Callback triggered when this specific item is clicked.
 * @property icon to be displayed inside the item button.
 * @property buttonStyle Visual configuration for this item, including color, shape, and size.
 */
@Stable
data class FabItem(
    val onClick: () -> Unit,
    val icon: ImageVector? = null,
    val buttonStyle: ButtonStyle = ButtonStyle()
) {

    /**
     * Visual style configuration for an individual [FabItem].
     *
     * @property color Background color of the item button.
     * @property shape Shape of the item button. Default is [CircleShape].
     * @property size Diameter/Height of the item button. Default is 52.dp.
     */
    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val size: Dp = 52.dp
    )
}
