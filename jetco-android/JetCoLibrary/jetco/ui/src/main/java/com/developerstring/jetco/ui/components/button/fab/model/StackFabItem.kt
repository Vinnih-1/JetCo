package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
class StackFabItem(
    val direction: StackDirection = StackDirection.TOP,
    val content: @Composable () -> Unit
)
