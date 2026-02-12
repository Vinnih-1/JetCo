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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.picker.components.PickerContainer
import com.developerstring.jetco_kmp.components.picker.components.PickerHeader
import com.developerstring.jetco_kmp.components.picker.components.PickerWheel
import com.developerstring.jetco_kmp.components.picker.config.DatePickerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerContainerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerDefaults
import com.developerstring.jetco_kmp.components.picker.config.PickerHeaderConfig
import com.developerstring.jetco_kmp.components.picker.config.TimePickerConfig
import com.developerstring.jetco_kmp.components.picker.config.WheelPickerConfig
import com.developerstring.jetco_kmp.components.picker.model.DateOrder
import com.developerstring.jetco_kmp.components.picker.model.PickerDate
import com.developerstring.jetco_kmp.components.picker.model.PickerDateTime
import com.developerstring.jetco_kmp.components.picker.model.PickerDisplayMode
import com.developerstring.jetco_kmp.components.picker.model.PickerTime
import com.developerstring.jetco_kmp.components.picker.model.TimeFormat

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

// ━━━━━━━━━━━━━━━━━━━━━  WheelDateTimePicker (inline)  ━━━━━━━━━━━━━━━

/**
 * An inline wheel-based date **and** time picker.
 *
 * Combines a date wheel section (day / month / year) with a time
 * wheel section (hour / minute / optional seconds / optional AM-PM)
 * in a single picker, separated by a thin divider.
 *
 * ### Example
 * ```kotlin
 * WheelDateTimePicker(
 *     startDateTime = PickerDateTime.now(),
 *     onDoneClick = { dateTime -> /* use dateTime */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param startDateTime Initially selected date and time.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param timeConfig Time-specific configuration.
 * @param dividerColor Color of the divider between date and time.
 * @param onDoneClick Callback when "Done" is pressed.
 * @param onDateTimeChange Callback each time the date or time changes.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelDateTimePicker(
    modifier: Modifier = Modifier,
    startDateTime: PickerDateTime = PickerDateTime.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Date & Time"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    timeConfig: TimePickerConfig = PickerDefaults.timePickerConfig(),
    dividerColor: Color = Color(0xFFE0E0E0),
    onDoneClick: (PickerDateTime) -> Unit = {},
    onDateTimeChange: (PickerDateTime) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val startDate = startDateTime.date
    val startTime = startDateTime.time

    val is12Hour = timeConfig.timeFormat == TimeFormat.HOUR_12
    val minuteInterval = timeConfig.minuteInterval.coerceIn(1, 30)
    val secondInterval = timeConfig.secondInterval.coerceIn(1, 30)

    // ── Date state ──
    val years = (dateConfig.yearsRange.first..dateConfig.yearsRange.last).toList()
    val yearItems = years.map { it.toString() }
    val monthItems = buildMonthItems(dateConfig)

    var selectedYear by remember { mutableIntStateOf(startDate.year) }
    var selectedMonth by remember { mutableIntStateOf(startDate.month) }
    var selectedDay by remember { mutableIntStateOf(startDate.dayOfMonth) }

    val maxDays = daysInMonth(selectedYear, selectedMonth)
    val dayItems = (1..maxDays).map { it.toString().padStart(2, '0') }
    val clampedDay = selectedDay.coerceIn(1, maxDays)
    if (clampedDay != selectedDay) selectedDay = clampedDay

    // ── Time state ──
    val hourRange = if (is12Hour) (1..12) else (0..23)
    val hourItems = hourRange.map { it.toString().padStart(2, '0') }
    val minuteValues = (0..59).filter { it % minuteInterval == 0 }
    val minuteItems = minuteValues.map { it.toString().padStart(2, '0') }
    val secondValues = (0..59).filter { it % secondInterval == 0 }
    val secondItems = secondValues.map { it.toString().padStart(2, '0') }
    val amPmItems = listOf("AM", "PM")

    var selectedHour by remember { mutableIntStateOf(startTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(startTime.minute) }
    var selectedSecond by remember { mutableIntStateOf(startTime.second) }
    var selectedAmPm by remember { mutableIntStateOf(if (startTime.hour >= 12) 1 else 0) }

    fun get24Hour(): Int {
        return if (is12Hour) {
            val h = if (selectedHour == 0) 12 else selectedHour
            when {
                selectedAmPm == 0 && h == 12 -> 0
                selectedAmPm == 1 && h == 12 -> 12
                selectedAmPm == 1 -> h + 12
                else -> h
            }
        } else selectedHour
    }

    val clampedMinute = minuteValues.minByOrNull { kotlin.math.abs(it - selectedMinute) } ?: 0
    val clampedSecond = secondValues.minByOrNull { kotlin.math.abs(it - selectedSecond) } ?: 0

    fun currentDateTime(): PickerDateTime = PickerDateTime(
        date = PickerDate(selectedYear, selectedMonth, selectedDay),
        time = PickerTime(
            hour = get24Hour(),
            minute = clampedMinute,
            second = if (timeConfig.showSeconds) clampedSecond else 0
        )
    )

    // ── Initial indices ──
    val yearInitialIndex = years.indexOf(selectedYear).coerceAtLeast(0)
    val monthInitialIndex = (selectedMonth - 1).coerceIn(0, 11)
    val dayInitialIndex = (selectedDay - 1).coerceIn(0, dayItems.lastIndex)

    val hourInitialIndex = if (is12Hour) {
        val display12 = if (startTime.hour % 12 == 0) 12 else startTime.hour % 12
        hourRange.indexOf(display12).coerceAtLeast(0)
    } else {
        hourRange.indexOf(startTime.hour).coerceAtLeast(0)
    }
    val minuteInitialIndex = minuteValues.indexOf(
        minuteValues.minByOrNull { kotlin.math.abs(it - startTime.minute) } ?: 0
    ).coerceAtLeast(0)
    val secondInitialIndex = secondValues.indexOf(
        secondValues.minByOrNull { kotlin.math.abs(it - startTime.second) } ?: 0
    ).coerceAtLeast(0)
    val amPmInitialIndex = if (startTime.hour >= 12) 1 else 0

    Column(modifier = modifier.fillMaxWidth()) {
        PickerHeader(
            config = headerConfig,
            onDone = { onDoneClick(currentDateTime()) },
            onCancel = onCancel
        )

        // ── Date section ──
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
                    onDateTimeChange(currentDateTime())
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
                    val newMax = daysInMonth(selectedYear, selectedMonth)
                    if (selectedDay > newMax) selectedDay = newMax
                    onDateTimeChange(currentDateTime())
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
                    val newMax = daysInMonth(selectedYear, selectedMonth)
                    if (selectedDay > newMax) selectedDay = newMax
                    onDateTimeChange(currentDateTime())
                }
            }

            when (dateConfig.dateOrder) {
                DateOrder.DAY_MONTH_YEAR -> { dayWheel(); monthWheel(); yearWheel() }
                DateOrder.MONTH_DAY_YEAR -> { monthWheel(); dayWheel(); yearWheel() }
                DateOrder.YEAR_MONTH_DAY -> { yearWheel(); monthWheel(); dayWheel() }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = dividerColor,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // ── Time section ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PickerWheel(
                items = hourItems,
                initialIndex = hourInitialIndex,
                config = wheelConfig,
                modifier = Modifier.weight(1f)
            ) { index ->
                selectedHour = hourRange.toList()[index]
                onDateTimeChange(currentDateTime())
            }

            PickerWheel(
                items = minuteItems,
                initialIndex = minuteInitialIndex,
                config = wheelConfig,
                modifier = Modifier.weight(1f)
            ) { index ->
                selectedMinute = minuteValues[index]
                onDateTimeChange(currentDateTime())
            }

            if (timeConfig.showSeconds) {
                PickerWheel(
                    items = secondItems,
                    initialIndex = secondInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    selectedSecond = secondValues[index]
                    onDateTimeChange(currentDateTime())
                }
            }

            if (is12Hour) {
                PickerWheel(
                    items = amPmItems,
                    initialIndex = amPmInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(0.8f)
                ) { index ->
                    selectedAmPm = index
                    onDateTimeChange(currentDateTime())
                }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelDateTimePickerView (with container)  ━━━

/**
 * A combined date & time picker displayed inside a dialog, bottom sheet,
 * or inline.
 *
 * ### Example — Dialog
 * ```kotlin
 * WheelDateTimePickerView(
 *     visible = showPicker,
 *     displayMode = PickerDisplayMode.DIALOG,
 *     startDateTime = PickerDateTime.now(),
 *     headerConfig = PickerDefaults.headerConfig(
 *         title = "Pick Date & Time",
 *         cancelLabel = "Cancel"
 *     ),
 *     timeConfig = PickerDefaults.timePickerConfig(
 *         timeFormat = TimeFormat.HOUR_12
 *     ),
 *     onDoneClick = { dateTime -> /* use dateTime */ },
 *     onDismiss = { showPicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param startDateTime Initially selected date and time.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param timeConfig Time-specific configuration.
 * @param dividerColor Divider color between date/time sections.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with selected date and time.
 * @param onDateTimeChange Callback on datetime change.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelDateTimePickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    startDateTime: PickerDateTime = PickerDateTime.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Date & Time"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    timeConfig: TimePickerConfig = PickerDefaults.timePickerConfig(),
    dividerColor: Color = Color(0xFFE0E0E0),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (PickerDateTime) -> Unit = {},
    onDateTimeChange: (PickerDateTime) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelDateTimePicker(
            modifier = modifier,
            startDateTime = startDateTime,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            dateConfig = dateConfig,
            timeConfig = timeConfig,
            dividerColor = dividerColor,
            onDoneClick = { dateTime ->
                onDoneClick(dateTime)
                onDismiss()
            },
            onDateTimeChange = onDateTimeChange,
            onCancel = onDismiss
        )
    }
}
