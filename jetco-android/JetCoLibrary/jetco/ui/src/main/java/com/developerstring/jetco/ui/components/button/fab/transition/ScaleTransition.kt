package com.developerstring.jetco.ui.components.button.fab.transition

import androidx.compose.animation.core.AnimationSpec

data class ScaleTransition(
    val target: Float,
    val spec: AnimationSpec<Float>
)