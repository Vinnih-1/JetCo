package com.developerstring.jetco.ui.components.search.animated_searchbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Configuration class for text input customization in [AnimatedSearchBar].
 *
 * @param textStyle Base text style for the input text.
 * @param fontFamily Custom font family for input text.
 * @param fontWeight Custom font weight for input text.
 * @param letterSpacing Letter spacing for input text.
 * @param focusedTextColor Text color when the search bar is focused.
 * @param unfocusedTextColor Text color when the search bar is not focused.
 * @param placeholderTextStyle Text style for the placeholder (size, line height, etc.).
 * @param placeholderFontFamily Custom font family for placeholder text.
 * @param placeholderFontWeight Custom font weight for placeholder text.
 * @param placeholderTextColor Color for the placeholder text. (Text string is in [AnimatedSearchBarConfig])
 * @param cursorColor Color of the text cursor.
 *
 * Example:
 * ```kotlin
 * AnimatedSearchBarTextConfig(
 *     fontFamily = FontFamily.SansSerif,
 *     fontWeight = FontWeight.Medium,
 *     letterSpacing = 0.5.sp,
 *     focusedTextColor = Color.Black,
 *     placeholderTextColor = Color.Black,
 *     cursorColor = Color.Blue
 * )
 * ```
 *
 *  @see AnimatedSearchBarConfig for appearance configuration options
 *  @see AnimatedSearchBarAnimationConfig for animation configuration options
 */

data class AnimatedSearchBarTextConfig(
    val textStyle: TextStyle = TextStyle(
        fontSize = 16.sp, lineHeight = 18.sp
    ),
    val fontFamily: FontFamily? = null,
    val fontWeight: FontWeight? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val focusedTextColor: Color = Color.Black,
    val unfocusedTextColor: Color = Color.DarkGray,
    val placeholderTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp, lineHeight = 18.sp
    ),
    val placeholderFontFamily: FontFamily? = null,
    val placeholderFontWeight: FontWeight? = null,
    val placeholderTextColor: Color = Color.Black,
    val cursorColor: Color = Color.DarkGray
)