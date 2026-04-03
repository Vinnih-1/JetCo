package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.developerstring.jetco.ui.components.button.fab.StackFloatingActionButton

/**
 * A wrapper for a custom composable with a specific direction to be used as an item in [StackFloatingActionButton].
 *
 * Use this class when you need to specify a different expansion direction for each item
 * or when you want to provide a fully custom UI instead of the default [FabItem].
 *
 * @property direction The direction in which this item will expand from the main FAB (TOP, START, END).
 * @property content The composable content to be rendered for this item.
 */
@Stable
class StackFabItem(
    val direction: StackDirection = StackDirection.TOP,
    val content: @Composable () -> Unit
)
