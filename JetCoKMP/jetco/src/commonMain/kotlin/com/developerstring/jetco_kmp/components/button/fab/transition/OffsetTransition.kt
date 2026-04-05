package com.developerstring.jetco_kmp.components.button.fab.transition

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.unit.Dp

data class OffsetTransition(
    val offsetX: Dp,
    val offsetY: Dp,
    val spec: AnimationSpec<Dp>
)
