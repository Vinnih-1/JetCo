package com.developerstring.jetco_kmp.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.developerstring.jetco_kmp.components.button.fab.StackFloatingActionButton

/**
 * A wrapper for a custom composable with a specific direction to be used as an item
 * in [StackFloatingActionButton].
 *
 * @property direction The direction in which this item will expand from the main FAB (TOP, START, END).
 * @property content The composable content to be rendered for this item.
 */
@Stable
class StackFabItem(
    val direction: StackDirection = StackDirection.TOP,
    val content: @Composable () -> Unit
)
