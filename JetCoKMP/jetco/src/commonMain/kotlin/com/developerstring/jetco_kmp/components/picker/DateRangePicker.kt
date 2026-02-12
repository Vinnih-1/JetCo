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

package com.developerstring.jetco_kmp.components.picker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.picker.components.PickerContainer
import com.developerstring.jetco_kmp.components.picker.components.PickerHeader
import com.developerstring.jetco_kmp.components.picker.components.PickerWheel
import com.developerstring.jetco_kmp.components.picker.config.DatePickerConfig
import com.developerstring.jetco_kmp.components.picker.config.DateRangePickerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerContainerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerDefaults
import com.developerstring.jetco_kmp.components.picker.config.PickerHeaderConfig
import com.developerstring.jetco_kmp.components.picker.config.WheelPickerConfig
import com.developerstring.jetco_kmp.components.picker.model.DateOrder
import com.developerstring.jetco_kmp.components.picker.model.PickerDate
import com.developerstring.jetco_kmp.components.picker.model.PickerDisplayMode

// ━━━━━━━━━━━━━━━━━━━━━  Private Helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

private val FULL_MONTHS = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private val SHORT_MONTHS = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

private fun daysInMonth(year: Int, month: Int): Int {
    return when (month) {
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
}

private fun buildMonthItems(config: DatePickerConfig): List<String> {
    return config.customMonthNames
        ?: if (config.showMonthAsNumber) {
            (1..12).map { it.toString().padStart(2, '0') }
        } else if (config.showShortMonths) {
            SHORT_MONTHS
        } else {
            FULL_MONTHS
        }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelDateRangePicker (inline)  ━━━━━━━━━━━━━━

/**
 * An inline wheel-based date range picker.
 *
 * Displays two date selection boxes ("From" and "To") at the top.
 * Tapping a box activates the corresponding wheel for editing.
 * A single set of day / month / year wheels is shared between both.
 *
 * ### Example
 * ```kotlin
 * WheelDateRangePicker(
 *     initialFromDate = PickerDate.now(),
 *     initialToDate = PickerDate(2026, 3, 15),
 *     onDoneClick = { fromDate, toDate -> /* use dates */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param initialFromDate Initially selected "from" date.
 * @param initialToDate Initially selected "to" date.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param rangeConfig Date range visual configuration.
 * @param onDoneClick Callback when "Done" is pressed.
 * @param onFromDateChange Callback when from-date changes.
 * @param onToDateChange Callback when to-date changes.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelDateRangePicker(
    modifier: Modifier = Modifier,
    initialFromDate: PickerDate = PickerDate.now(),
    initialToDate: PickerDate = PickerDate.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Date Range"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    rangeConfig: DateRangePickerConfig = PickerDefaults.dateRangePickerConfig(),
    onDoneClick: (fromDate: PickerDate, toDate: PickerDate) -> Unit = { _, _ -> },
    onFromDateChange: (PickerDate) -> Unit = {},
    onToDateChange: (PickerDate) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    // 0 = editing "from", 1 = editing "to"
    var editingIndex by remember { mutableIntStateOf(0) }

    var fromYear by remember { mutableIntStateOf(initialFromDate.year) }
    var fromMonth by remember { mutableIntStateOf(initialFromDate.month) }
    var fromDay by remember { mutableIntStateOf(initialFromDate.dayOfMonth) }

    var toYear by remember { mutableIntStateOf(initialToDate.year) }
    var toMonth by remember { mutableIntStateOf(initialToDate.month) }
    var toDay by remember { mutableIntStateOf(initialToDate.dayOfMonth) }

    // Current editing state references
    val activeYear = if (editingIndex == 0) fromYear else toYear
    val activeMonth = if (editingIndex == 0) fromMonth else toMonth
    val activeDay = if (editingIndex == 0) fromDay else toDay

    val years = (dateConfig.yearsRange.first..dateConfig.yearsRange.last).toList()
    val yearItems = years.map { it.toString() }
    val monthItems = buildMonthItems(dateConfig)
    val maxDays = daysInMonth(activeYear, activeMonth)
    val dayItems = (1..maxDays).map { it.toString().padStart(2, '0') }

    val fromDate = PickerDate(fromYear, fromMonth, fromDay.coerceIn(1, daysInMonth(fromYear, fromMonth)))
    val toDate = PickerDate(toYear, toMonth, toDay.coerceIn(1, daysInMonth(toYear, toMonth)))

    val yearInitialIndex = years.indexOf(activeYear).coerceAtLeast(0)
    val monthInitialIndex = (activeMonth - 1).coerceIn(0, 11)
    val dayInitialIndex = (activeDay - 1).coerceIn(0, dayItems.lastIndex)

    Column(modifier = modifier.fillMaxWidth()) {
        PickerHeader(
            config = headerConfig,
            onDone = { onDoneClick(fromDate, toDate) },
            onCancel = onCancel
        )

        // ── Date range boxes ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // From box
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { editingIndex = 0 },
                shape = rangeConfig.boxShape,
                color = if (editingIndex == 0) rangeConfig.selectedBoxColor else rangeConfig.boxColor,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (editingIndex == 0) rangeConfig.selectedBoxBorderColor else rangeConfig.boxBorderColor
                )
            ) {
                Text(
                    text = fromDate.format(rangeConfig.dateTextFormat),
                    style = rangeConfig.boxTextStyle,
                    color = if (editingIndex == 0) rangeConfig.selectedTextColor else rangeConfig.boxTextStyle.color,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                )
            }

            // Center text
            Text(
                text = rangeConfig.centerText,
                style = rangeConfig.boxTextStyle,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // To box
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { editingIndex = 1 },
                shape = rangeConfig.boxShape,
                color = if (editingIndex == 1) rangeConfig.selectedBoxColor else rangeConfig.boxColor,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (editingIndex == 1) rangeConfig.selectedBoxBorderColor else rangeConfig.boxBorderColor
                )
            ) {
                Text(
                    text = toDate.format(rangeConfig.dateTextFormat),
                    style = rangeConfig.boxTextStyle,
                    color = if (editingIndex == 1) rangeConfig.selectedTextColor else rangeConfig.boxTextStyle.color,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Wheel section ──
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
                    val day = index + 1
                    if (editingIndex == 0) {
                        fromDay = day
                        onFromDateChange(PickerDate(fromYear, fromMonth, fromDay))
                    } else {
                        toDay = day
                        onToDateChange(PickerDate(toYear, toMonth, toDay))
                    }
                }
            }

            val monthWheel: @Composable () -> Unit = {
                PickerWheel(
                    items = monthItems,
                    initialIndex = monthInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1.2f)
                ) { index ->
                    val month = index + 1
                    if (editingIndex == 0) {
                        fromMonth = month
                        val newMax = daysInMonth(fromYear, fromMonth)
                        if (fromDay > newMax) fromDay = newMax
                        onFromDateChange(PickerDate(fromYear, fromMonth, fromDay))
                    } else {
                        toMonth = month
                        val newMax = daysInMonth(toYear, toMonth)
                        if (toDay > newMax) toDay = newMax
                        onToDateChange(PickerDate(toYear, toMonth, toDay))
                    }
                }
            }

            val yearWheel: @Composable () -> Unit = {
                PickerWheel(
                    items = yearItems,
                    initialIndex = yearInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    val year = years[index]
                    if (editingIndex == 0) {
                        fromYear = year
                        val newMax = daysInMonth(fromYear, fromMonth)
                        if (fromDay > newMax) fromDay = newMax
                        onFromDateChange(PickerDate(fromYear, fromMonth, fromDay))
                    } else {
                        toYear = year
                        val newMax = daysInMonth(toYear, toMonth)
                        if (toDay > newMax) toDay = newMax
                        onToDateChange(PickerDate(toYear, toMonth, toDay))
                    }
                }
            }

            when (dateConfig.dateOrder) {
                DateOrder.DAY_MONTH_YEAR -> { dayWheel(); monthWheel(); yearWheel() }
                DateOrder.MONTH_DAY_YEAR -> { monthWheel(); dayWheel(); yearWheel() }
                DateOrder.YEAR_MONTH_DAY -> { yearWheel(); monthWheel(); dayWheel() }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelDateRangePickerView (with container)  ━━

/**
 * A date range picker displayed inside a dialog, bottom sheet, or inline.
 *
 * ### Example — Bottom Sheet
 * ```kotlin
 * WheelDateRangePickerView(
 *     visible = showRangePicker,
 *     displayMode = PickerDisplayMode.BOTTOM_SHEET,
 *     initialFromDate = PickerDate.now(),
 *     initialToDate = PickerDate(2026, 12, 31),
 *     onDoneClick = { from, to -> /* use dates */ },
 *     onDismiss = { showRangePicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param initialFromDate Initially selected "from" date.
 * @param initialToDate Initially selected "to" date.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param rangeConfig Date range visual configuration.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with from and to dates.
 * @param onFromDateChange Callback when from-date changes.
 * @param onToDateChange Callback when to-date changes.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelDateRangePickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    initialFromDate: PickerDate = PickerDate.now(),
    initialToDate: PickerDate = PickerDate.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Date Range"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    rangeConfig: DateRangePickerConfig = PickerDefaults.dateRangePickerConfig(),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (fromDate: PickerDate, toDate: PickerDate) -> Unit = { _, _ -> },
    onFromDateChange: (PickerDate) -> Unit = {},
    onToDateChange: (PickerDate) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelDateRangePicker(
            modifier = modifier,
            initialFromDate = initialFromDate,
            initialToDate = initialToDate,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            dateConfig = dateConfig,
            rangeConfig = rangeConfig,
            onDoneClick = { from, to ->
                onDoneClick(from, to)
                onDismiss()
            },
            onFromDateChange = onFromDateChange,
            onToDateChange = onToDateChange,
            onCancel = onDismiss
        )
    }
}
