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

package com.developerstring.jetco_kmp.components.picker.config

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.developerstring.jetco_kmp.components.picker.model.DateOrder
import com.developerstring.jetco_kmp.components.picker.model.TimeFormat

// ━━━━━━━━━━━━━━━━━━━━━  Wheel Picker Config  ━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Configuration for the wheel-scroll picker component.
 *
 * @property height Total height of the wheel picker area.
 * @property rowCount Number of visible rows in the wheel (must be odd for centered selection).
 * @property selectedTextStyle Text style for the currently selected item.
 * @property defaultTextStyle Text style for non-selected items.
 * @property selectorColor Background color of the selector highlight.
 * @property selectorShape Shape of the selector highlight.
 * @property selectorHeight Height of the selector highlight row.
 * @property fadeEdges Whether to apply a fade/gradient effect at the top and bottom edges.
 * @property fadeEdgeColor Color used for fading edges (usually matches background).
 * @property hapticFeedback Whether to trigger haptic feedback on selection change.
 */
@Stable
data class WheelPickerConfig(
    val height: Dp,
    val rowCount: Int,
    val selectedTextStyle: TextStyle,
    val defaultTextStyle: TextStyle,
    val selectorColor: Color,
    val selectorShape: Shape,
    val selectorHeight: Dp,
    val fadeEdges: Boolean,
    val fadeEdgeColor: Color,
    val hapticFeedback: Boolean
)

// ━━━━━━━━━━━━━━━━━━━━━  Picker Header Config  ━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Configuration for the header section of a picker.
 *
 * @property enabled Whether to show the header (title + done button).
 * @property title Title text displayed at the top.
 * @property doneLabel Label text for the confirmation button.
 * @property titleStyle Text style for the title.
 * @property doneLabelStyle Text style for the done label.
 * @property doneLabelColor Color for the done label text.
 * @property cancelLabel Optional cancel label; if null, no cancel button is shown.
 * @property cancelLabelStyle Text style for the cancel label.
 * @property cancelLabelColor Color for the cancel label text.
 */
@Stable
data class PickerHeaderConfig(
    val enabled: Boolean,
    val title: String,
    val doneLabel: String,
    val titleStyle: TextStyle,
    val doneLabelStyle: TextStyle,
    val doneLabelColor: Color,
    val cancelLabel: String?,
    val cancelLabelStyle: TextStyle,
    val cancelLabelColor: Color
)

// ━━━━━━━━━━━━━━━━━━━━━  Date Picker Config  ━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Configuration for date picker components.
 *
 * @property yearsRange Allowed year range for selection.
 * @property dateOrder Order of day/month/year columns.
 * @property showShortMonths Whether to use abbreviated month names (Jan, Feb…).
 * @property showMonthAsNumber Whether to display months as numbers instead of names.
 * @property customMonthNames Optional custom list of 12 month names.
 */
@Stable
data class DatePickerConfig(
    val yearsRange: IntRange,
    val dateOrder: DateOrder,
    val showShortMonths: Boolean,
    val showMonthAsNumber: Boolean,
    val customMonthNames: List<String>?
)

// ━━━━━━━━━━━━━━━━━━━━━  Time Picker Config  ━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Configuration for time picker components.
 *
 * @property timeFormat 12-hour or 24-hour display.
 * @property showSeconds Whether to include a seconds column.
 * @property minuteInterval Granularity of minute selection (1, 5, 10, 15, 30).
 * @property secondInterval Granularity of second selection (1, 5, 10, 15, 30).
 */
@Stable
data class TimePickerConfig(
    val timeFormat: TimeFormat,
    val showSeconds: Boolean,
    val minuteInterval: Int,
    val secondInterval: Int
)

// ━━━━━━━━━━━━━━━━━━━━━  Date Range Picker Config  ━━━━━━━━━━━━━━━━━━━

/**
 * Configuration specific to the date range picker.
 *
 * @property centerText Text displayed between the from-date and to-date boxes (e.g. "to").
 * @property boxColor Background color of date boxes.
 * @property selectedBoxColor Background color of the currently selected date box.
 * @property boxBorderColor Border color of date boxes.
 * @property selectedBoxBorderColor Border color of the selected date box.
 * @property selectedTextColor Text color inside the selected date box.
 * @property boxShape Shape of the date boxes.
 * @property boxTextStyle Text style for date text inside boxes.
 * @property dateTextFormat Format pattern for displaying dates in boxes.
 */
@Stable
data class DateRangePickerConfig(
    val centerText: String,
    val boxColor: Color,
    val selectedBoxColor: Color,
    val boxBorderColor: Color,
    val selectedBoxBorderColor: Color,
    val selectedTextColor: Color,
    val boxShape: Shape,
    val boxTextStyle: TextStyle,
    val dateTextFormat: String
)

// ━━━━━━━━━━━━━━━━━━━━━  Container Config  ━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Configuration for the outer container of dialog/bottom-sheet pickers.
 *
 * @property containerColor Background color of the picker container.
 * @property shape Shape of the container.
 * @property tonalElevation Tonal elevation for Material 3 surfaces.
 * @property shadowElevation Shadow elevation for the container.
 */
@Stable
data class PickerContainerConfig(
    val containerColor: Color,
    val shape: Shape,
    val tonalElevation: Dp,
    val shadowElevation: Dp
)
