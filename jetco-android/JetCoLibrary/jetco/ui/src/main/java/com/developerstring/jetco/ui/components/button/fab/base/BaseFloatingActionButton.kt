package com.developerstring.jetco.ui.components.button.fab.base

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig

@Composable
internal fun BaseFloatingActionButton(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit) = {},
    config: FabMainConfig = FabMainConfig()
) {
    // Rotate the main icon smoothly when toggling open/close
    val mainIconRotation by animateFloatAsState(
        targetValue = if (expanded) config.buttonStyle.iconRotation else 0f,
        animationSpec = config.animation.animationSpec,
        label = "mainIconRotation"
    )

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
        Row(
            horizontalArrangement = Arrangement.spacedBy(config.buttonStyle.horizontalSpace),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(config.buttonStyle.padding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.rotate(mainIconRotation)
            ) {
                if (icon != null) {
                    icon.invoke()
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Base FAB icon",
                        tint = Color.White,
                        modifier = Modifier.size(config.buttonStyle.size * 0.55f)
                    )
                }
            }
            text?.invoke()
        }
    }
}