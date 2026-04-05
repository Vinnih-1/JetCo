package com.developerstring.jetco_kmp.components.button.fab.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.button.fab.model.FabMainConfig
import com.developerstring.jetco_kmp.components.button.fab.scope.MorphCardScope

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun DefaultMorphCard(
    config: FabMainConfig,
    onClose: () -> Unit,
    scope: MorphCardScope
) {
    val morph = config.itemArrangement.morph

    Column(
        modifier = Modifier
            .width(morph.width)
            .background(
                color = config.buttonStyle.color,
                shape = morph.cardShape
            ).padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = "Quick Actions",
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClose
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
        scope.MorphItems()
    }
}
