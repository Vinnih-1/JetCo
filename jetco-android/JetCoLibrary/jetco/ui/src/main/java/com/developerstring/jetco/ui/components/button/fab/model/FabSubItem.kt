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

@Stable
data class FabSubItem(
    val onClick: () -> Unit,
    val title: String? = null,
    val icon: ImageVector? = null,
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val titleStyle: TitleStyle = TitleStyle()
) {
    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val size: Dp = 52.dp
    )

    @Stable
    data class TitleStyle(
        val color: Color = Color.White,
        val size: Dp = 12.dp,
        val weight: FontWeight = FontWeight.Light,
        val maxLines: Int = 1,
        val style: TextStyle = TextStyle.Default
    )
}

