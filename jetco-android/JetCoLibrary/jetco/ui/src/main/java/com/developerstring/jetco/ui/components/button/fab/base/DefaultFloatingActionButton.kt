package com.developerstring.jetco.ui.components.button.fab.base

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig

@Composable
internal fun DefaultFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = {},
    config: FabMainConfig = FabMainConfig()
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = config.buttonStyle.size)
            .height(config.buttonStyle.size)
            .clip(config.buttonStyle.shape)
            .background(config.buttonStyle.color)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Base FAB icon",
                tint = Color.White,
                modifier = Modifier.size(config.buttonStyle.size * 0.55f)
            )
        }
    }
}