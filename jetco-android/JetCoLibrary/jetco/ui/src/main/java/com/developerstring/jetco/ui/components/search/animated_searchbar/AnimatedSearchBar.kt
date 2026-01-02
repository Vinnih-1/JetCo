package com.developerstring.jetco.ui.components.search.animated_searchbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * A modern animated search bar component with smooth expansion,
 * bounce effects, loading indicators, and full user control through a configuration
 * system and controller API.
 *
 * This composable provides a minimal collapsed circular search button that expands
 * into a full search bar with text input, clear action, and optional loading state.
 * The expansion uses customizable spring animations, scale pop effects, and animated
 * width transitions. Developers can trigger collapse externally using the
 * [AnimatedSearchBarController].
 *
 * ## Features:
 *  - Smooth expand/collapse animations using spring physics
 *  - Icon bounce & rotation animation on expansion
 *  - Configurable width, height, colors, radii, padding, and corner styling
 *  - Optional loading indicator that replaces the search icon when searching
 *  - Clear button with optional collapse behavior
 *  - IME "Search" action callback support
 *  - External collapse control through [AnimatedSearchBarController]
 *  - Fully customizable through [AnimatedSearchBarConfig] and
 *    [AnimatedSearchBarAnimationConfig]
 *
 *   ## Example Usage:
 * ```kotlin
 * val query by remember { mutableStateOf("") }
 * val controller = rememberAnimatedSearchBarController()
 *
 * AnimatedSearchBar(
 *     value = query,
 *     onValueChange = { query = it },
 *     controller = controller,
 *     onSearch = { text ->
 *         // trigger real search
 *     },
 *     config = AnimatedSearchBarConfig(
 *         collapsedWidth = 56.dp,
 *         expandedWidth = 300.dp,
 *         height = 48.dp,
 *         barBackgroundColor = Color.White,
 *         barCornerRadius = 32.dp
 *     ),
 *     animationConfig = AnimatedSearchBarAnimationConfig(
 *         widthSpringStiffness = Spring.StiffnessLow,
 *         widthSpringDamping = Spring.DampingRatioMediumBouncy
 *     )
 * )
 * ```
 * ## Collapsing search bar programmatically
 * You can collapse the bar manually using the controller:
 *
 * ```kotlin
 * controller.collapse()
 * ```
 * This is useful when the search results list captures a click event.
 *
 * @param value Current text inside the search input.
 * @param onValueChange Callback invoked when the text input changes.
 * @param modifier Modifier applied to the entire search bar layout.
 * @param config Appearance configuration. See [AnimatedSearchBarConfig].
 * @param animationConfig Animation configuration. See [AnimatedSearchBarAnimationConfig].
 * @param isLoading Displays a circular loading indicator instead of the search icon when true.
 * @param onSearch Callback triggered when the user presses IME Search.
 * @param controller Controls external behavior such as collapsing the search bar.
 * @param onExpand Callback invoked when the bar expands.
 * @param onCollapse Callback invoked when the bar collapses.
 *
 * @see AnimatedSearchBarConfig for appearance configuration options
 * @see AnimatedSearchBarAnimationConfig for animation configuration options
 * @see [AnimatedSearchBarController] for external collapse control
 *
 */

@Composable
fun AnimatedSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    config: AnimatedSearchBarConfig = AnimatedSearchBarConfig(),
    animationConfig: AnimatedSearchBarAnimationConfig = AnimatedSearchBarAnimationConfig(),
    textConfig: AnimatedSearchBarTextConfig = AnimatedSearchBarTextConfig(),
    isLoading: Boolean = false,
    onSearch: (String) -> Unit = {},
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {},
    controller: AnimatedSearchBarController = rememberAnimatedSearchBarController()
) {
    var isExpanded by remember { mutableStateOf(false) }

    val iconScale = remember { Animatable(1f) }

    val scope = rememberCoroutineScope()

    var isFocused by remember { mutableStateOf(false) }

    fun triggerSearchIconBounceAnimation() {
        scope.launch {
            iconScale.animateTo(
                1.5f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            iconScale.animateTo(
                1f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }


    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 360f else 0f, animationSpec = tween(
            durationMillis = 500, easing = LinearOutSlowInEasing
        )
    )

    val searchBarWidth by animateDpAsState(
        targetValue = if (isExpanded) config.expandedWidth else config.collapsedWidth,
        animationSpec = spring(
            dampingRatio = animationConfig.widthSpringDamping,
            stiffness = animationConfig.widthSpringStiffness
        )
    )

    val searchBarAlpha by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f, animationSpec = tween(durationMillis = 500)
    )

    val searchBarScale by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.8f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
        )
    )

    val focusManager = LocalFocusManager.current

    val currentTextColor = if (isFocused) {
        textConfig.focusedTextColor
    } else {
        textConfig.unfocusedTextColor
    }

    LaunchedEffect(Unit) {
        controller.collapseRequest = {
            isExpanded = false
            onCollapse()
            triggerSearchIconBounceAnimation()
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier
            .width(searchBarWidth)
            .height(config.height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .scale(searchBarScale)
                .alpha(searchBarAlpha)
                .background(
                    color = config.searchBarBackgroundColor,
                    shape = RoundedCornerShape(config.searchBarCornerRadius)
                )
                .border(
                    width = config.searchBarBorderWidth,
                    color = config.searchBarBorderColor,
                    shape = RoundedCornerShape(config.searchBarCornerRadius)
                )
        )


        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(config.height)
                    .padding(6.dp)
                    .scale(iconScale.value)
                    .rotate(iconRotation)
                    .background(
                        color = config.iconBackgroundColor, shape = CircleShape
                    )
                    .clip(RoundedCornerShape(64.dp))
                    .clickable {
                        if (!isExpanded) {
                            isExpanded = true
                            onExpand()
                        }
                        triggerSearchIconBounceAnimation()
                    }, contentAlignment = Alignment.Center
            ) {

                val iconSize = config.height / 2

                if (isExpanded && isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = config.iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = config.iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
            AnimatedVisibility(
                visible = isExpanded, enter = fadeIn(tween(200)), exit = fadeOut(tween(150))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = { onValueChange(it) },
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged{ isFocused = it.isFocused },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                onSearch(value)
                                focusManager.clearFocus()
                            }),
                        textStyle = textConfig.textStyle.copy(
                            color = currentTextColor,
                            fontFamily = textConfig.fontFamily ?: textConfig.textStyle.fontFamily,
                            fontWeight = textConfig.fontWeight ?: textConfig.textStyle.fontWeight,
                            letterSpacing = textConfig.letterSpacing,
                        ),
                        cursorBrush = SolidColor(textConfig.cursorColor),
                        decorationBox = { innerTextField ->
                            if (value.isEmpty()) {
                                Box(
                                    modifier = Modifier, contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        config.placeholderTextString,
                                        style = textConfig.placeholderTextStyle.copy(
                                            color = textConfig.placeholderTextColor,
                                            fontFamily = textConfig.placeholderFontFamily ?: textConfig.placeholderTextStyle.fontFamily,
                                            fontWeight = textConfig.placeholderFontWeight ?: textConfig.placeholderTextStyle.fontWeight,
                                            letterSpacing = textConfig.letterSpacing
                                        ),
                                        modifier = Modifier.padding(start = 2.dp)
                                    )
                                }
                            }
                            innerTextField()
                        })

                    IconButton(
                        onClick = {
                            if (value.isNotEmpty()) {
                                onValueChange("")
                                focusManager.clearFocus()
                            } else {
                                onValueChange("")
                                isExpanded = false
                                onCollapse()
                                triggerSearchIconBounceAnimation()
                            }
                        }, modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search",
                            tint = config.clearIconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

