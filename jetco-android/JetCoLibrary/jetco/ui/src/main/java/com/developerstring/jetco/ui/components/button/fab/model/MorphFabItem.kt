package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.developerstring.jetco.ui.components.button.fab.MorphFloatingActionButton

/**
 * A wrapper for a custom composable to be used as an item in [MorphFloatingActionButton].
 *
 * Use this class when you want to provide a fully custom UI for an item inside
 * the morphed card grid instead of the default [FabItem].
 *
 * @property content The composable content to be rendered for this item in the grid.
 */
@Stable
class MorphFabItem(
    val content: @Composable () -> Unit
)
