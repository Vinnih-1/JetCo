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
 * A floating action button that morphs into a card containing a grid of actions.
 *
 * This component provides a circular FAB that, when expanded, smoothly transitions
 * into a rectangular card layout. The card contains a grid of items arranged
 * in a [FlowRow], with customizable header space and item arrangement.
 *
 * @param expanded Whether the FAB is currently morphed into a card.
 * @param items List of [FabItem] to be displayed in the card grid.
 * @param modifier Modifier applied to the root container.
 * @param onClick Callback triggered when the FAB is clicked or the card's close action is invoked.
 * @param config Comprehensive configuration for styling and layout. See [FabMainConfig].
 * @param content Optional custom composable for the collapsed FAB button.
 * @param card Optional custom composable for the expanded card shell. Use [MorphCardScope.MorphItems] to place the grid.
 *
 * @see FabMainConfig for detailed arrangement and animation options.
 * @see MorphCardScope for customizing the expanded card layout.
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

/**
 * A floating action button that morphs into a card with custom [MorphFabItem]s.
 *
 * Use this overload when you need to provide fully custom composables for each item
 * in the grid instead of the standard icon/button pair.
 *
 * @param expanded Whether the FAB is currently morphed into a card.
 * @param items List of [MorphFabItem] containing custom composables.
 * @param modifier Modifier applied to the root container.
 * @param onClick Callback triggered when the FAB is clicked.
 * @param config Configuration for styling and layout.
 * @param content Optional custom composable for the collapsed FAB button.
 * @param card Optional custom composable for the expanded card shell.
 */
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
                    val stepMs = 300 / (items.size - 1).coerceAtLeast(1)
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
