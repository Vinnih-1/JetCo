package com.developerstring.jetco_kmp.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.developerstring.jetco_kmp.components.button.fab.MorphFloatingActionButton

/**
 * A wrapper for a custom composable to be used as an item in [MorphFloatingActionButton].
 *
 * @property content The composable content to be rendered for this item in the grid.
 */
@Stable
class MorphFabItem(
    val content: @Composable () -> Unit
)
