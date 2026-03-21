package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.BaseFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.components.SubFabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MorphFloatingActionButton(
    expanded: Boolean,
    items: List<FabSubItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    config: FabMainConfig = FabMainConfig()
) {
    val morph = config.itemArrangement.morph
    val staggerStep = config.animation.durationMillis / (items.size + 1)

    Box(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = config.animation.durationMillis,
                    easing = config.animation.easing
                )
            )
            .clip(if (expanded) morph.cardShape else config.buttonStyle.shape)
            .background(config.buttonStyle.color)
    ) {
        if (expanded) {
            Column(
                modifier = Modifier
                    .width(morph.width)
                    .padding(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.align(Alignment.CenterStart)) {
                        title?.invoke()
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(morph.headerSpace))

                FlowRow(
                    maxItemsInEachRow = morph.columns,
                    horizontalArrangement = Arrangement.spacedBy(morph.spacedBy, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(morph.spacedBy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEachIndexed { index, item ->
                        val alpha = remember { Animatable(0f) }

                        LaunchedEffect(Unit) {
                            delay((index * staggerStep).toLong())
                            alpha.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = (config.animation.durationMillis + ((index + 1) * 100)),
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }

                        SubFabItem(
                            item = item,
                            onClick = { item.onClick() },
                            modifier = Modifier.graphicsLayer { this.alpha = alpha.value }
                        )
                    }
                }
            }
        } else {
            BaseFloatingActionButton(
                expanded = false,
                text = text,
                icon = icon,
                onClick = onClick,
                config = config
            )
        }
    }
}
