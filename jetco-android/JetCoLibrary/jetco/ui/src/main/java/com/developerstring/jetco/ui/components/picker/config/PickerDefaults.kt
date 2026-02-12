/*
 * Copyright 2026 by Developer Chunk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.developerstring.jetco.ui.components.picker.config

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.components.picker.model.DateOrder
import com.developerstring.jetco.ui.components.picker.model.TimeFormat

/**
 * Provides default configurations for all picker components.
 *
 * Every factory method mirrors the properties of the corresponding
 * config `data class` and supplies sensible defaults.
 *
 * ### Example
 * ```kotlin
 * DatePicker(
 *     wheelConfig = PickerDefaults.wheelPickerConfig(rowCount = 5),
 *     headerConfig = PickerDefaults.headerConfig(title = "Select Date"),
 * )
 * ```
 */
object PickerDefaults {

    // ── Shared private defaults ──────────────────────────────────────

    private val defaultSelectedTextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1A1A1A)
    )

    private val defaultTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF888888)
    )

    private val defaultTitleStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1A1A1A)
    )

    private val defaultDoneLabelStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )

    private val defaultCancelLabelStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    private val defaultAccentColor = Color(0xFF519DE9)

    // ── Wheel Picker ─────────────────────────────────────────────────

    /**
     * Creates a [WheelPickerConfig] with the given parameters.
     *
     * @param height Total height of the wheel picker area.
     * @param rowCount Number of visible rows (should be odd).
     * @param selectedTextStyle Text style for the selected item.
     * @param defaultTextStyle Text style for non-selected items.
     * @param selectorColor Background color of the selector highlight.
     * @param selectorShape Shape of the selector highlight.
     * @param selectorHeight Height of the selector highlight row.
     * @param fadeEdges Whether to fade edges.
     * @param fadeEdgeColor Fade edge gradient color.
     * @param hapticFeedback Whether to trigger haptic on change.
     * @return A configured [WheelPickerConfig].
     */
    fun wheelPickerConfig(
        height: Dp = 144.dp,
        rowCount: Int = 3,
        selectedTextStyle: TextStyle = defaultSelectedTextStyle,
        defaultTextStyle: TextStyle = PickerDefaults.defaultTextStyle,
        selectorColor: Color = Color(0x1A519DE9),
        selectorShape: Shape = RoundedCornerShape(12.dp),
        selectorHeight: Dp = 48.dp,
        fadeEdges: Boolean = true,
        fadeEdgeColor: Color = Color.White,
        hapticFeedback: Boolean = true
    ): WheelPickerConfig = WheelPickerConfig(
        height = height,
        rowCount = rowCount,
        selectedTextStyle = selectedTextStyle,
        defaultTextStyle = defaultTextStyle,
        selectorColor = selectorColor,
        selectorShape = selectorShape,
        selectorHeight = selectorHeight,
        fadeEdges = fadeEdges,
        fadeEdgeColor = fadeEdgeColor,
        hapticFeedback = hapticFeedback
    )

    // ── Header ───────────────────────────────────────────────────────

    /**
     * Creates a [PickerHeaderConfig] with the given parameters.
     *
     * @param enabled Whether to show the header.
     * @param title Title text.
     * @param doneLabel Done button label.
     * @param titleStyle Title text style.
     * @param doneLabelStyle Done button text style.
     * @param doneLabelColor Done label color.
     * @param cancelLabel Cancel button label; null hides the button.
     * @param cancelLabelStyle Cancel button text style.
     * @param cancelLabelColor Cancel label color.
     * @return A configured [PickerHeaderConfig].
     */
    fun headerConfig(
        enabled: Boolean = true,
        title: String = "Select",
        doneLabel: String = "Done",
        titleStyle: TextStyle = defaultTitleStyle,
        doneLabelStyle: TextStyle = defaultDoneLabelStyle,
        doneLabelColor: Color = defaultAccentColor,
        cancelLabel: String? = null,
        cancelLabelStyle: TextStyle = defaultCancelLabelStyle,
        cancelLabelColor: Color = Color(0xFF888888)
    ): PickerHeaderConfig = PickerHeaderConfig(
        enabled = enabled,
        title = title,
        doneLabel = doneLabel,
        titleStyle = titleStyle,
        doneLabelStyle = doneLabelStyle,
        doneLabelColor = doneLabelColor,
        cancelLabel = cancelLabel,
        cancelLabelStyle = cancelLabelStyle,
        cancelLabelColor = cancelLabelColor
    )

    // ── Date Picker ──────────────────────────────────────────────────

    /**
     * Creates a [DatePickerConfig] with the given parameters.
     *
     * @param yearsRange Allowed year range.
     * @param dateOrder Column ordering of day, month, year.
     * @param showShortMonths Whether to abbreviate month names.
     * @param showMonthAsNumber Whether to show months as numbers.
     * @param customMonthNames Optional custom month names.
     * @return A configured [DatePickerConfig].
     */
    fun datePickerConfig(
        yearsRange: IntRange = IntRange(1922, 2122),
        dateOrder: DateOrder = DateOrder.DAY_MONTH_YEAR,
        showShortMonths: Boolean = false,
        showMonthAsNumber: Boolean = false,
        customMonthNames: List<String>? = null
    ): DatePickerConfig = DatePickerConfig(
        yearsRange = yearsRange,
        dateOrder = dateOrder,
        showShortMonths = showShortMonths,
        showMonthAsNumber = showMonthAsNumber,
        customMonthNames = customMonthNames
    )

    // ── Time Picker ──────────────────────────────────────────────────

    /**
     * Creates a [TimePickerConfig] with the given parameters.
     *
     * @param timeFormat 12-hour or 24-hour display.
     * @param showSeconds Whether to include seconds.
     * @param minuteInterval Minute step granularity.
     * @param secondInterval Second step granularity.
     * @return A configured [TimePickerConfig].
     */
    fun timePickerConfig(
        timeFormat: TimeFormat = TimeFormat.HOUR_24,
        showSeconds: Boolean = false,
        minuteInterval: Int = 1,
        secondInterval: Int = 1
    ): TimePickerConfig = TimePickerConfig(
        timeFormat = timeFormat,
        showSeconds = showSeconds,
        minuteInterval = minuteInterval,
        secondInterval = secondInterval
    )

    // ── Date Range Picker ────────────────────────────────────────────

    /**
     * Creates a [DateRangePickerConfig] with the given parameters.
     *
     * @param centerText Text between from/to boxes.
     * @param boxColor Box background color.
     * @param selectedBoxColor Selected box background color.
     * @param boxBorderColor Box border color.
     * @param selectedBoxBorderColor Selected box border color.
     * @param selectedTextColor Selected text color.
     * @param boxShape Box corner shape.
     * @param boxTextStyle Text style for box content.
     * @param dateTextFormat Date display format.
     * @return A configured [DateRangePickerConfig].
     */
    fun dateRangePickerConfig(
        centerText: String = "to",
        boxColor: Color = Color.White,
        selectedBoxColor: Color = Color(0xFFE9F2FE),
        boxBorderColor: Color = Color(0xFFD0D0D0),
        selectedBoxBorderColor: Color = Color(0xFF99BDFB),
        selectedTextColor: Color = Color(0xFF5288F9),
        boxShape: Shape = RoundedCornerShape(8.dp),
        boxTextStyle: TextStyle = TextStyle(
            color = Color(0xFF333333),
            fontWeight = FontWeight.W600,
            fontSize = 14.sp
        ),
        dateTextFormat: String = "yyyy-MM-dd"
    ): DateRangePickerConfig = DateRangePickerConfig(
        centerText = centerText,
        boxColor = boxColor,
        selectedBoxColor = selectedBoxColor,
        boxBorderColor = boxBorderColor,
        selectedBoxBorderColor = selectedBoxBorderColor,
        selectedTextColor = selectedTextColor,
        boxShape = boxShape,
        boxTextStyle = boxTextStyle,
        dateTextFormat = dateTextFormat
    )

    // ── Container ────────────────────────────────────────────────────

    /**
     * Creates a [PickerContainerConfig] with the given parameters.
     *
     * @param containerColor Background color.
     * @param shape Container shape.
     * @param tonalElevation M3 tonal elevation.
     * @param shadowElevation Shadow elevation.
     * @return A configured [PickerContainerConfig].
     */
    fun containerConfig(
        containerColor: Color = Color.White,
        shape: Shape = RoundedCornerShape(16.dp),
        tonalElevation: Dp = 6.dp,
        shadowElevation: Dp = 0.dp
    ): PickerContainerConfig = PickerContainerConfig(
        containerColor = containerColor,
        shape = shape,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation
    )
}
