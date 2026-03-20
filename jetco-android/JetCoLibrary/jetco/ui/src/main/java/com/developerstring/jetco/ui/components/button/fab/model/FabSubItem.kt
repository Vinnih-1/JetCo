package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class FabSubItem(
    val onClick: () -> Unit,
    val title: String? = null,
    val icon: ImageVector? = null,
    val style: ButtonStyle = ButtonStyle(),
) {
    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val size: Dp = 52.dp
    )
}

