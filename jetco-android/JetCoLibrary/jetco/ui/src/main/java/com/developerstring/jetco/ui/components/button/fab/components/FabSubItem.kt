package com.developerstring.jetco.ui.components.button.fab.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem

@Composable
internal fun SubFabItem(
    item: FabSubItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(item.style.size)
            .clip(item.style.shape)
            .background(item.style.color)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        item.icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = item.title,
                tint = Color.White,
                modifier = Modifier.size(item.style.size * 0.55f)
            )
        }
    }
}