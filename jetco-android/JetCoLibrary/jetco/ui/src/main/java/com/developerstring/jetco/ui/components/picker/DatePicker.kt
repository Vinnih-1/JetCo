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

package com.developerstring.jetco.ui.components.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.picker.components.PickerContainer
import com.developerstring.jetco.ui.components.picker.components.PickerHeader
import com.developerstring.jetco.ui.components.picker.components.PickerWheel
import com.developerstring.jetco.ui.components.picker.config.DatePickerConfig
import com.developerstring.jetco.ui.components.picker.config.PickerContainerConfig
import com.developerstring.jetco.ui.components.picker.config.PickerDefaults
import com.developerstring.jetco.ui.components.picker.config.PickerHeaderConfig
import com.developerstring.jetco.ui.components.picker.config.WheelPickerConfig
import com.developerstring.jetco.ui.components.picker.model.DateOrder
import com.developerstring.jetco.ui.components.picker.model.PickerDate
import com.developerstring.jetco.ui.components.picker.model.PickerDisplayMode

// ━━━━━━━━━━━━━━━━━━━━━  Private Helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

private val FULL_MONTH_NAMES = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private val SHORT_MONTH_NAMES = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

/**
 * Returns the number of days in the given [month] (1-based) of [year].
 */
private fun daysInMonth(year: Int, month: Int): Int {
    return when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
}

/**
 * Builds the list of month display strings based on config.
 */
private fun buildMonthItems(config: DatePickerConfig): List<String> {
    return config.customMonthNames
        ?: if (config.showMonthAsNumber) {
            (1..12).map { it.toString().padStart(2, '0') }
        } else if (config.showShortMonths) {
            SHORT_MONTH_NAMES
        } else {
            FULL_MONTH_NAMES
        }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelDatePicker (inline content)  ━━━━━━━━━━━

/**
 * An inline wheel-based date picker.
 *
 * Displays three wheel columns for day, month, and year. The column
 * order is configurable via [dateConfig.dateOrder][DateOrder].
 *
 * ### Example
 * ```kotlin
 * WheelDatePicker(
 *     startDate = PickerDate.now(),
 *     headerConfig = PickerDefaults.headerConfig(title = "Select Date"),
 *     onDoneClick = { date -> /* use date */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param startDate Initially selected date.
 * @param wheelConfig Configuration for the wheel scrolling behaviour.
 * @param headerConfig Configuration for the header section.
 * @param dateConfig Configuration for date-specific options.
 * @param onDoneClick Callback when "Done" is pressed with the selected date.
 * @param onDateChange Callback each time the date changes via scrolling.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelDatePicker(
    modifier: Modifier = Modifier,
    startDate: PickerDate = PickerDate.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Date"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    onDoneClick: (PickerDate) -> Unit = {},
    onDateChange: (PickerDate) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val years = (dateConfig.yearsRange.first..dateConfig.yearsRange.last).toList()
    val yearItems = years.map { it.toString() }
    val monthItems = buildMonthItems(dateConfig)

    var selectedYear by remember { mutableIntStateOf(startDate.year) }
    var selectedMonth by remember { mutableIntStateOf(startDate.month) }
    var selectedDay by remember { mutableIntStateOf(startDate.dayOfMonth) }

    val maxDays = daysInMonth(selectedYear, selectedMonth)
    val dayItems = (1..maxDays).map { it.toString().padStart(2, '0') }

    // Clamp day to valid range when month/year changes
    val clampedDay = selectedDay.coerceIn(1, maxDays)
    if (clampedDay != selectedDay) {
        selectedDay = clampedDay
    }

    val currentDate = PickerDate(selectedYear, selectedMonth, selectedDay)

    val yearInitialIndex = years.indexOf(selectedYear).coerceAtLeast(0)
    val monthInitialIndex = (selectedMonth - 1).coerceIn(0, 11)
    val dayInitialIndex = (selectedDay - 1).coerceIn(0, dayItems.lastIndex)

    Column(modifier = modifier.fillMaxWidth()) {
        PickerHeader(
            config = headerConfig,
            onDone = { onDoneClick(currentDate) },
            onCancel = onCancel
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val dayWheel: @Composable () -> Unit = {
                PickerWheel(
                    items = dayItems,
                    initialIndex = dayInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    selectedDay = index + 1
                    val date = PickerDate(selectedYear, selectedMonth, selectedDay)
                    onDateChange(date)
                }
            }

            val monthWheel: @Composable () -> Unit = {
                PickerWheel(
                    items = monthItems,
                    initialIndex = monthInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1.2f)
                ) { index ->
                    selectedMonth = index + 1
                    // Clamp day
                    val newMax = daysInMonth(selectedYear, selectedMonth)
                    if (selectedDay > newMax) selectedDay = newMax
                    val date = PickerDate(selectedYear, selectedMonth, selectedDay)
                    onDateChange(date)
                }
            }

            val yearWheel: @Composable () -> Unit = {
                PickerWheel(
                    items = yearItems,
                    initialIndex = yearInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    selectedYear = years[index]
                    // Clamp day for leap-year changes
                    val newMax = daysInMonth(selectedYear, selectedMonth)
                    if (selectedDay > newMax) selectedDay = newMax
                    val date = PickerDate(selectedYear, selectedMonth, selectedDay)
                    onDateChange(date)
                }
            }

            when (dateConfig.dateOrder) {
                DateOrder.DAY_MONTH_YEAR -> {
                    dayWheel()
                    monthWheel()
                    yearWheel()
                }
                DateOrder.MONTH_DAY_YEAR -> {
                    monthWheel()
                    dayWheel()
                    yearWheel()
                }
                DateOrder.YEAR_MONTH_DAY -> {
                    yearWheel()
                    monthWheel()
                    dayWheel()
                }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelDatePickerView (with container)  ━━━━━━━

/**
 * A date picker displayed inside a dialog, bottom sheet, or inline.
 *
 * This is the primary entry point when you want a ready-to-use date
 * picker with a presentation container.
 *
 * ### Example — Bottom Sheet
 * ```kotlin
 * WheelDatePickerView(
 *     visible = showPicker,
 *     displayMode = PickerDisplayMode.BOTTOM_SHEET,
 *     startDate = PickerDate.now(),
 *     onDoneClick = { date -> /* use date */ },
 *     onDismiss = { showPicker = false },
 * )
 * ```
 *
 * ### Example — Dialog
 * ```kotlin
 * WheelDatePickerView(
 *     visible = showPicker,
 *     displayMode = PickerDisplayMode.DIALOG,
 *     headerConfig = PickerDefaults.headerConfig(
 *         title = "Pick a Date",
 *         cancelLabel = "Cancel"
 *     ),
 *     dateConfig = PickerDefaults.datePickerConfig(
 *         dateOrder = DateOrder.MONTH_DAY_YEAR,
 *         showShortMonths = true
 *     ),
 *     onDoneClick = { date -> /* use date */ },
 *     onDismiss = { showPicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param startDate Initially selected date.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with selected date.
 * @param onDateChange Callback on date change.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelDatePickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    startDate: PickerDate = PickerDate.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Date"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (PickerDate) -> Unit = {},
    onDateChange: (PickerDate) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelDatePicker(
            modifier = modifier,
            startDate = startDate,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            dateConfig = dateConfig,
            onDoneClick = { date ->
                onDoneClick(date)
                onDismiss()
            },
            onDateChange = onDateChange,
            onCancel = onDismiss
        )
    }
}
