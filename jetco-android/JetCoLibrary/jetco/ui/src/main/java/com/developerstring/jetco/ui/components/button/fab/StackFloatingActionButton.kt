package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.developerstring.jetco.ui.components.button.fab.base.BaseFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.components.SubFabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig

@Composable
fun StackFloatingActionButton(
    expanded: Boolean,
    items: List<FabSubItem>,
    modifier: Modifier = Modifier,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig()
) {
    // Rotate the main icon smoothly when toggling open/close
    val mainIconRotation by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = config.animation.animationSpec,
        label = "mainIconRotation"
    )

    val stack = config.itemArrangement.stack

    // The Box anchor changes depending on which direction items spread
    val alignment = when (stack) {
        FabMainConfig.Orientation.Stack.TOP -> Alignment.BottomEnd
        FabMainConfig.Orientation.Stack.START -> Alignment.CenterEnd
        FabMainConfig.Orientation.Stack.END -> Alignment.CenterStart
    }

    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        // Sub-items — stacked in the direction defined by Orientation.Stack
        items.forEachIndexed { index, item ->
            val animatedAlpha by animateFloatAsState(
                targetValue = if (expanded) 1f else 0f,
                animationSpec = config.animation.animationSpec,
                label = "alpha_$index"
            )

            val spacing = (index + 1) * (item.style.size + stack.spacedBy)

            // Each orientation moves items along a different axis
            val targetOffsetX = when (stack) {
                FabMainConfig.Orientation.Stack.START -> if (expanded) -spacing else 0.dp
                FabMainConfig.Orientation.Stack.END -> if (expanded) spacing else 0.dp
                else -> 0.dp
            }

            val targetOffsetY = when (stack) {
                FabMainConfig.Orientation.Stack.TOP -> if (expanded) -spacing else 0.dp
                else -> 0.dp
            }

            val animatedOffsetX by animateDpAsState(
                targetValue = targetOffsetX,
                animationSpec = tween(
                    durationMillis = config.animation.durationMillis,
                    easing = config.animation.easing
                ),
                label = "offsetX_$index"
            )

            val animatedOffsetY by animateDpAsState(
                targetValue = targetOffsetY,
                animationSpec = tween(
                    durationMillis = config.animation.durationMillis,
                    easing = config.animation.easing
                ),
                label = "offsetY_$index"
            )

            SubFabItem(
                item = item,
                modifier = Modifier
                    .offset(x = animatedOffsetX, y = animatedOffsetY)
                    .padding(end = (config.buttonStyle.size - item.style.size) / 2)
                    .graphicsLayer { alpha = animatedAlpha },
                onClick = { item.onClick() }
            )
        }

        // Main FAB button
        BaseFloatingActionButton(
            text = text,
            icon = icon,
            onClick = onClick,
            config = config,
            modifier = Modifier.rotate(mainIconRotation)
        )
    }
}
