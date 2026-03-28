package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.unit.Dp

data class OffsetTransition(
    val offsetX: Dp,
    val offsetY: Dp,
    val spec: AnimationSpec<Dp>
)