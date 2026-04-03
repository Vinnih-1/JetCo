package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.developerstring.jetco.ui.components.button.fab.RadialFloatingActionButton

/**
 * A wrapper for a custom composable to be used as an item in [RadialFloatingActionButton].
 *
 * Use this class when you want to provide a fully custom UI for a radial menu item
 * instead of the default icon-based [FabItem].
 *
 * @property content The composable content to be rendered for this radial item.
 */
@Stable
class RadialFabItem(
    val content: @Composable () -> Unit
)
