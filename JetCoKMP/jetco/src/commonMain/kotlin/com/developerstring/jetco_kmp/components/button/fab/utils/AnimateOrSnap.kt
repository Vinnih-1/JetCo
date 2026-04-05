package com.developerstring.jetco_kmp.components.button.fab.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector

internal suspend fun <T, V : AnimationVector> Animatable<T, V>.animateOrSnap(
    targetValue: T?,
    spec: AnimationSpec<T>?,
    predicate: () -> Boolean = { true }
) {
    if (targetValue == null) return

    if (spec != null) {
        animateTo(targetValue, spec)
    } else if (predicate.invoke()) {
        snapTo(targetValue)
    }
}