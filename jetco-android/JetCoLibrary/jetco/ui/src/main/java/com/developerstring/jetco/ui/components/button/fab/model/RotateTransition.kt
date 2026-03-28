package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.animation.core.AnimationSpec

data class RotateTransition(
    val target: Float,
    val spec: AnimationSpec<Float>
)