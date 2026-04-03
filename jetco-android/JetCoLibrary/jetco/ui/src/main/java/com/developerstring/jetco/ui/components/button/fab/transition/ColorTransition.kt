package com.developerstring.jetco.ui.components.button.fab.transition

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color

data class ColorTransition(
    val target: Color,
    val spec: AnimationSpec<Color> = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)
