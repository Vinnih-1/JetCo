package com.developerstring.jetco.ui.components.search.animated_searchbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration class for the visual appearance and behaviour of [AnimatedSearchBar].
 *
 * Use this to tune sizes, colors, radii, spacing and other visual defaults.
 *
 * @param collapsedWidth Width of the bar when collapsed into a circular icon.
 *                       Default is 64.dp.
 *
 * @param expandedWidth Target width when the search bar is fully expanded.
 *                      Default is 320.dp.
 *                      Note: The parent layout may still constrain this width.
 *
 * @param height Total height of the search bar, including icon and text field.
 *               Default is 48.dp.
 *
 * @param searchBarBackgroundColor Background color of the expanded search bar container.
 *
 * @param searchBarBorderWidth Width of the border around the expanded bar.
 *                       Default is 2.dp.
 *
 * @param searchBarBorderColor Border color for the expanded bar.
 *                       Only visible when `barBorderWidth` > 0.dp.
 *
 * @param searchBarCornerRadius Corner radius of the expanded search bar container.
 *                        Default is 35.dp for a rounded-pill shape.
 *
 * @param iconBackgroundColor Background color of the circular icon container in collapsed mode.
 *
 * @param iconTint Tint color for the search icon inside the collapsed button.
 *
 * @param clearIconTint Tint color for the clear (✕) icon shown when expanded.
 *
 * @param placeholderTextString Text displayed when the input field is empty. Default is "Search".
 *
 * Example:
 * ```kotlin
 * AnimatedSearchBarConfig(
 *     collapsedWidth = 56.dp,
 *     expandedWidth = 320.dp,
 *     height = 48.dp,
 *     barBackgroundColor = Color.White,
 *     barCornerRadius = 28.dp
 * )
 * ```
 *
 * @see AnimatedSearchBar for usage
 * @see AnimatedSearchBarTextConfig for text configuration options
 * @see AnimatedSearchBarAnimationConfig for animation configuration options
 */

data class AnimatedSearchBarConfig (
    val height: Dp = 48.dp,
    val expandedWidth: Dp = 320.dp,
    val collapsedWidth: Dp = 64.dp,

    val searchBarBackgroundColor: Color = Color.White,
    val searchBarCornerRadius: Dp = 35.dp,
    val searchBarBorderColor: Color = Color(0xFFE0E0E0),
    val searchBarBorderWidth: Dp = 2.dp,

    val iconBackgroundColor: Color = Color(0xFF558B2F),
    val iconTint: Color = Color.White,
    val clearIconTint: Color = Color.Black,
    val placeholderTextString: String = "Search"
)
