package com.developerstring.jetco.ui.components.button.fab.scope

import androidx.compose.runtime.Composable

class MorphCardScope(
    internal val itemsContent: @Composable () -> Unit
) {
    @Composable
    fun MorphItems() = itemsContent()
}