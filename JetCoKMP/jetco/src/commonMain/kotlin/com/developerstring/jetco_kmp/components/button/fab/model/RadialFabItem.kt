package com.developerstring.jetco_kmp.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.developerstring.jetco_kmp.components.button.fab.RadialFloatingActionButton

/**
 * A wrapper for a custom composable to be used as an item in [RadialFloatingActionButton].
 *
 * @property content The composable content to be rendered for this radial item.
 */
@Stable
class RadialFabItem(
    val content: @Composable () -> Unit
)
