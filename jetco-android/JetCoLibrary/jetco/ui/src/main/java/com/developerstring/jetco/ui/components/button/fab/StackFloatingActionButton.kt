package com.developerstring.jetco.ui.components.button.fab

import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFabItem
import com.developerstring.jetco.ui.components.button.fab.base.DefaultFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.model.FabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.StackDirection
import com.developerstring.jetco.ui.components.button.fab.model.StackExpandOffset
import com.developerstring.jetco.ui.components.button.fab.model.StackFabItem
import com.developerstring.jetco.ui.components.button.fab.utils.LocalFabButtonColor
import com.developerstring.jetco.ui.components.button.fab.utils.animateFabButton
import com.developerstring.jetco.ui.components.button.fab.utils.animateFabItem
import com.developerstring.jetco.ui.components.button.fab.utils.fabItemStaggerDelay
import com.developerstring.jetco.ui.components.button.fab.utils.resolvedFinalOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A floating action button that expands items in a linear stack (Vertical or Horizontal).
 *
 * This component allows for expanding items in multiple directions (TOP, START, END)
 * simultaneously. It also provides an [onExpandChange] callback that reports the total
 * displacement of the main button, which can be used to "push" other UI elements
 * (like screen content) out of the way.
 *
 * @param expanded Whether the FAB is currently showing its items.
 * @param items List of [FabItem] to be displayed as buttons. Defaults to [StackDirection.TOP].
 * @param modifier Modifier applied to the root container.
 * @param onClick Callback triggered when the main FAB button is clicked.
 * @param config Configuration for styling and animations. See [FabMainConfig].
 * @param onExpandChange Callback that provides [StackExpandOffset].
 * @param content Optional custom composable for the main FAB button.
 *
 * @see StackExpandOffset for handling screen content displacement.
 * @see StackFabItem for custom composable items with specific directions.
 */
@Composable
fun StackFloatingActionButton(
    expanded: Boolean,
    items: List<FabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    onExpandChange: (StackExpandOffset) -> Unit = {},
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    val stackItems = items.map { item ->
        StackFabItem {
            DefaultFabItem(item = item, onClick = { item.onClick() })
        }
    }

    StackFloatingActionButtonBase(
        expanded = expanded,
        items = stackItems,
        modifier = modifier,
        onClick = onClick,
        config = config,
        onExpandChange = onExpandChange,
        content = content
    )
}

/**
 * A floating action button that expands custom [StackFabItem]s in a linear stack.
 *
 * Use this overload to specify different directions for each item or to use custom composables.
 *
 * @param expanded Whether the FAB is currently showing its items.
 * @param items List of [StackFabItem] with direction and custom content.
 * @param modifier Modifier applied to the root container.
 * @param onClick Callback triggered when the main FAB button is clicked.
 * @param config Configuration for styling and animations.
 * @param onExpandChange Callback that provides [StackExpandOffset] data.
 * @param content Optional custom composable for the main FAB button.
 */
@JvmName("StackFloatingActionButtonCustom")
@Composable
fun StackFloatingActionButton(
    expanded: Boolean,
    items: List<StackFabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    onExpandChange: (StackExpandOffset) -> Unit = {},
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    StackFloatingActionButtonBase(
        expanded = expanded,
        items = items,
        modifier = modifier,
        onClick = onClick,
        config = config,
        onExpandChange = onExpandChange,
        content = content
    )
}

@Composable
internal fun StackFloatingActionButtonBase(
    expanded: Boolean,
    items: List<StackFabItem>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    config: FabMainConfig = FabMainConfig(),
    onExpandChange: (StackExpandOffset) -> Unit = {},
    content: (@Composable () -> Unit) = {
        DefaultFloatingActionButton(onClick = onClick, config = config)
    }
) {
    val density = LocalDensity.current
    var fabWidthDp by remember { mutableStateOf(config.buttonStyle.size) }
    var fabHeightDp by remember { mutableStateOf(config.buttonStyle.size) }
    val spacedBy = config.itemArrangement.stack.spacedBy
    val spacingPadding = config.itemArrangement.stack.spacingPadding

    val itemWidths = remember(items.size) {
        mutableStateListOf<Dp>().also { list ->
            repeat(items.size) { list.add(config.buttonStyle.size) }
        }
    }
    val itemHeights = remember(items.size) {
        mutableStateListOf<Dp>().also { list ->
            repeat(items.size) { list.add(config.buttonStyle.size) }
        }
    }

    val topIndices = remember(items) { items.indices.filter { items[it].direction == StackDirection.TOP } }
    val startIndices = remember(items) { items.indices.filter { items[it].direction == StackDirection.START } }
    val endIndices = remember(items) { items.indices.filter { items[it].direction == StackDirection.END } }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        val fabOffsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
        val fabOffsetY = remember { Animatable(0.dp, Dp.VectorConverter) }
        val fabScale = remember { Animatable(1f) }
        val fabRotation = remember { Animatable(0f) }
        val fabColor = remember { Animatable(config.buttonStyle.color, Color.VectorConverter(config.buttonStyle.color.colorSpace)) }

        items.forEachIndexed { index, stackItem ->
            val direction = stackItem.direction

            val groupIndices = when (direction) {
                StackDirection.TOP -> topIndices
                StackDirection.START -> startIndices
                StackDirection.END -> endIndices
            }
            val positionInGroup = groupIndices.indexOf(index)

            val spacing = when (direction) {
                StackDirection.TOP -> {
                    val heightBefore = (0 until positionInGroup).fold(0.dp) { acc, pos ->
                        acc + itemHeights[groupIndices[pos]] + spacedBy
                    }
                    fabHeightDp + spacedBy + heightBefore
                }
                StackDirection.START,
                StackDirection.END -> {
                    val widthBefore = (0 until positionInGroup).fold(0.dp) { acc, pos ->
                        acc + itemWidths[groupIndices[pos]] + spacedBy
                    }
                    fabWidthDp + spacedBy + widthBefore
                }
            }

            val targetOffsetX = when (direction) {
                StackDirection.START -> -spacing
                StackDirection.END -> spacing
                StackDirection.TOP -> -(fabWidthDp - itemWidths[index]) / 2
            }

            val targetOffsetY = when (direction) {
                StackDirection.TOP -> -spacing
                StackDirection.START,
                StackDirection.END -> -(fabHeightDp - itemHeights[index]) / 2
            }

            val rotation = remember { Animatable(0f) }
            val scale = remember { Animatable(0f) }
            val alpha = remember { Animatable(0f) }
            val offsetX = remember { Animatable(0.dp, Dp.VectorConverter) }
            val offsetY = remember { Animatable(0.dp, Dp.VectorConverter) }

            val groupSize = groupIndices.size

            LaunchedEffect(expanded, targetOffsetX, targetOffsetY) {
                val transition = if (expanded) config.animation.itemEnterTransition
                else config.animation.itemExitTransition

                val staggerDelay = fabItemStaggerDelay(
                    expanded = expanded,
                    index = positionInGroup,
                    total = (groupSize - 1).coerceAtLeast(0),
                    totalCount = groupSize.coerceAtLeast(1),
                    config = config
                )
                val enterDelay = if (expanded) config.animation.itemEnterDelay else 0L
                delay(enterDelay + staggerDelay)

                animateFabItem(
                    expanded = expanded,
                    transition = transition,
                    alpha = alpha,
                    scale = scale,
                    rotation = rotation,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    targetOffsetX = targetOffsetX,
                    targetOffsetY = targetOffsetY,
                )
            }

            Box(
                modifier = Modifier
                    .offset(x = offsetX.value + fabOffsetX.value, y = offsetY.value + fabOffsetY.value)
                    .onSizeChanged { size ->
                        val width = with(density) { size.width.toDp() }
                        val height = with(density) { size.height.toDp() }
                        if (itemWidths[index] != width) itemWidths[index] = width
                        if (itemHeights[index] != height) itemHeights[index] = height
                    }.graphicsLayer {
                        this.alpha    = alpha.value
                        this.scaleX   = scale.value
                        this.scaleY   = scale.value
                        this.rotationZ = rotation.value
                    }
            ) {
                stackItem.content()
            }
        }

        LaunchedEffect(expanded) {
            val btnTransition = if (expanded) config.animation.buttonEnterTransition
            else config.animation.buttonExitTransition

            if (expanded) {
                val (finalOffsetX, finalOffsetY) = btnTransition.resolvedFinalOffset()
                onExpandChange(StackExpandOffset(
                    offsetY = fabHeightDp + spacingPadding - finalOffsetY,
                    offsetX = fabWidthDp + spacingPadding - finalOffsetX
                ))
            } else {
                onExpandChange(StackExpandOffset())
            }

            launch {
                animateFabButton(
                    btnTransition = btnTransition,
                    fabOffsetX = fabOffsetX,
                    fabOffsetY = fabOffsetY,
                    fabScale = fabScale,
                    fabRotation = fabRotation,
                    fabColor = fabColor,
                )
            }
        }

        CompositionLocalProvider(LocalFabButtonColor provides fabColor.value) {
            Box(
                modifier = Modifier
                    .offset(x = fabOffsetX.value, y = fabOffsetY.value)
                    .graphicsLayer {
                        scaleX     = fabScale.value
                        scaleY     = fabScale.value
                        rotationZ  = fabRotation.value
                    }.onSizeChanged { size ->
                        fabWidthDp  = with(density) { size.width.toDp() }
                        fabHeightDp = with(density) { size.height.toDp() }
                    }
            ) {
                content()
            }
        }
    }
}
