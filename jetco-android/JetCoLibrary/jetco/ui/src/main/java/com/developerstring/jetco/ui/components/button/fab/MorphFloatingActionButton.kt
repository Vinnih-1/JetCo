package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFabItem
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.base.DefaultMorphCard
import com.developerstring.jetco.ui.components.button.fab.model.FabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.MorphFabItem
import com.developerstring.jetco.ui.components.button.fab.scope.MorphCardScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A Floating Action Button that morphs into an expanded card grid when activated.
 *
 * When collapsed, it displays a standard circular FAB. When expanded, it animates into
 * a rounded card containing a title header with a close button, and a configurable grid
 * of sub-action items. Sub-items fade in one by one with a staggered entrance animation.
 *
 * ## Example Usage:
 * ```kotlin
 * MorphFloatingActionButton(
 *     expanded = isExpanded,
 *     items = listOf(
 *         FabSubItem(
 *             onClick = { }
 *         )
 *     )
 * )
 * ```
 *
 * @param expanded Whether the FAB is currently expanded into card form.
 * @param items List of [FabItem] sub-actions to display in the card grid.
 * @param modifier Modifier applied to the root [Box] container.
 * @param onClick Click handler for both the main FAB button and the card close button.
 * @param config Visual and layout configuration. See [FabMainConfig].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MorphFloatingActionButton(
    expanded: Boolean,
    items: List<FabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    },
    card: @Composable MorphCardScope.() -> Unit = {
        DefaultMorphCard(config = config, onClose = onClick, scope = this)
    }
) {
    val items = items.map { item ->
        MorphFabItem {
            DefaultFabItem(item = item, onClick = { item.onClick() })
        }
    }

    MorphFloatingActionButtonBase(
        expanded = expanded,
        items = items,
        modifier = modifier,
        onClick = onClick,
        config = config,
        content = content,
        card = card
    )
}

@JvmName("MorphFloatingActionButtonCustom")
@Composable
fun MorphFloatingActionButton(
    expanded: Boolean,
    items: List<MorphFabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    },
    card: @Composable MorphCardScope.() -> Unit = {
        DefaultMorphCard(config = config, onClose = onClick, scope = this)
    }
) {
    MorphFloatingActionButtonBase(
        expanded = expanded,
        items = items,
        modifier = modifier,
        onClick = onClick,
        config = config,
        content = content,
        card = card
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MorphFloatingActionButtonBase(
    expanded: Boolean,
    items: List<MorphFabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    },
    card: @Composable MorphCardScope.() -> Unit = {
        DefaultMorphCard(config = config, onClose = onClick, scope = this)
    }
) {
    val morph = config.itemArrangement.morph
    val itemsContent: @Composable () -> Unit = {
        Spacer(modifier = Modifier.size(morph.headerSpace))

        FlowRow(
            maxItemsInEachRow = morph.columns,
            horizontalArrangement = Arrangement.spacedBy(morph.spacedBy, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(morph.spacedBy),
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, morphItem ->
                // key = index ensures remember is stable per item slot
                val alpha = remember(index) { Animatable(0f) }
                val scale = remember(index) { Animatable(0f) }
                val rotation = remember { Animatable(0f) }

                LaunchedEffect(index) {
                    val transition = config.animation.enterTransition
                    val stepMs = 300 / (items.size - 1)
                    val staggerDelay = config.animation.enterOrder.delayFor(
                        index = index,
                        total = items.size - 1,
                        stepMs = stepMs
                    )

                    delay(staggerDelay)

                    coroutineScope {
                        launch {
                            alpha.animateOrSnap(
                                targetValue = if (expanded) 1f else 0f,
                                spec = transition.alphaSpec,
                                predicate = { expanded }
                            )
                        }
                        launch {
                            scale.animateOrSnap(
                                targetValue = if (expanded) 1f else 0f,
                                spec = transition.scaleSpec,
                                predicate = { expanded }
                            )
                        }
                        launch {
                            rotation.animateOrSnap(
                                targetValue = transition.rotate?.target,
                                spec = transition.rotate?.spec,
                                predicate = { expanded }
                            )
                        }
                    }

                    if (!expanded) { // Reset to initial position
                        alpha.snapTo(0f)
                        scale.snapTo(0f)
                        rotation.snapTo(0f)
                    }
                }

                Box(
                    modifier = Modifier.graphicsLayer {
                        this.alpha = alpha.value
                        this.scaleX = scale.value
                        this.scaleY = scale.value
                        this.rotationZ = rotation.value
                    }
                ) {
                    morphItem.content()
                }
            }
        }
    }
    val scope = MorphCardScope(
        itemsContent = itemsContent
    )

    AnimatedContent(
        targetState = expanded,
        label = "MorphFloatingActionButton",
        modifier = modifier
    ) {
        if (it) {
            scope.card()
        } else {
            Box {
                content()
            }
        }
    }
}
