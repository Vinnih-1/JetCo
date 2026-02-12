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
import com.developerstring.jetco_kmp.components.picker.components.PickerContainer
import com.developerstring.jetco_kmp.components.picker.components.PickerHeader
import com.developerstring.jetco_kmp.components.picker.components.PickerWheel
import com.developerstring.jetco_kmp.components.picker.config.PickerContainerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerDefaults
import com.developerstring.jetco_kmp.components.picker.config.PickerHeaderConfig
import com.developerstring.jetco_kmp.components.picker.config.TimePickerConfig
import com.developerstring.jetco_kmp.components.picker.config.WheelPickerConfig
import com.developerstring.jetco_kmp.components.picker.model.PickerDisplayMode
import com.developerstring.jetco_kmp.components.picker.model.PickerTime
import com.developerstring.jetco_kmp.components.picker.model.TimeFormat

// ━━━━━━━━━━━━━━━━━━━━━  WheelTimePicker (inline content)  ━━━━━━━━━━━

/**
 * An inline wheel-based time picker.
 *
 * Displays columns for hour, minute, optional seconds, and AM/PM
 * (for 12-hour format). Minute and second intervals are configurable
 * to allow step-based selection (e.g. every 5 minutes).
 *
 * ### Example
 * ```kotlin
 * WheelTimePicker(
 *     startTime = PickerTime.now(),
 *     timeConfig = PickerDefaults.timePickerConfig(
 *         timeFormat = TimeFormat.HOUR_12,
 *         showSeconds = false
 *     ),
 *     onDoneClick = { time -> /* use time */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param startTime Initially selected time.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param timeConfig Time-specific configuration.
 * @param onDoneClick Callback when "Done" is pressed with the selected time.
 * @param onTimeChange Callback each time the time changes.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    startTime: PickerTime = PickerTime.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Time"),
    timeConfig: TimePickerConfig = PickerDefaults.timePickerConfig(),
    onDoneClick: (PickerTime) -> Unit = {},
    onTimeChange: (PickerTime) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val is12Hour = timeConfig.timeFormat == TimeFormat.HOUR_12
    val minuteInterval = timeConfig.minuteInterval.coerceIn(1, 30)
    val secondInterval = timeConfig.secondInterval.coerceIn(1, 30)

    // Build hour items
    val hourRange = if (is12Hour) (1..12) else (0..23)
    val hourItems = hourRange.map { it.toString().padStart(2, '0') }

    // Build minute items
    val minuteValues = (0..59).filter { it % minuteInterval == 0 }
    val minuteItems = minuteValues.map { it.toString().padStart(2, '0') }

    // Build second items
    val secondValues = (0..59).filter { it % secondInterval == 0 }
    val secondItems = secondValues.map { it.toString().padStart(2, '0') }

    // AM/PM items
    val amPmItems = listOf("AM", "PM")

    // State
    var selectedHour by remember { mutableIntStateOf(startTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(startTime.minute) }
    var selectedSecond by remember { mutableIntStateOf(startTime.second) }
    var selectedAmPm by remember {
        mutableIntStateOf(if (startTime.hour >= 12) 1 else 0) // 0 = AM, 1 = PM
    }

    // Convert display hour to 24-hour value
    fun get24Hour(): Int {
        return if (is12Hour) {
            val h = if (selectedHour == 0) 12 else selectedHour
            when {
                selectedAmPm == 0 && h == 12 -> 0    // 12 AM = 0
                selectedAmPm == 1 && h == 12 -> 12   // 12 PM = 12
                selectedAmPm == 1 -> h + 12           // PM
                else -> h                              // AM
            }
        } else {
            selectedHour
        }
    }

    // Nearest valid minute/second
    val clampedMinute = minuteValues.minByOrNull {
        kotlin.math.abs(it - selectedMinute)
    } ?: 0
    val clampedSecond = secondValues.minByOrNull {
        kotlin.math.abs(it - selectedSecond)
    } ?: 0

    val currentTime = PickerTime(
        hour = get24Hour(),
        minute = clampedMinute,
        second = if (timeConfig.showSeconds) clampedSecond else 0
    )

    // Initial indices
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
            onDone = { onDoneClick(currentTime) },
            onCancel = onCancel
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Hour wheel
            PickerWheel(
                items = hourItems,
                initialIndex = hourInitialIndex,
                config = wheelConfig,
                modifier = Modifier.weight(1f)
            ) { index ->
                selectedHour = hourRange.toList()[index]
                onTimeChange(
                    PickerTime(get24Hour(), clampedMinute, if (timeConfig.showSeconds) clampedSecond else 0)
                )
            }

            // Minute wheel
            PickerWheel(
                items = minuteItems,
                initialIndex = minuteInitialIndex,
                config = wheelConfig,
                modifier = Modifier.weight(1f)
            ) { index ->
                selectedMinute = minuteValues[index]
                onTimeChange(
                    PickerTime(get24Hour(), selectedMinute, if (timeConfig.showSeconds) clampedSecond else 0)
                )
            }

            // Seconds wheel (optional)
            if (timeConfig.showSeconds) {
                PickerWheel(
                    items = secondItems,
                    initialIndex = secondInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(1f)
                ) { index ->
                    selectedSecond = secondValues[index]
                    onTimeChange(
                        PickerTime(get24Hour(), clampedMinute, selectedSecond)
                    )
                }
            }

            // AM/PM wheel (12-hour only)
            if (is12Hour) {
                PickerWheel(
                    items = amPmItems,
                    initialIndex = amPmInitialIndex,
                    config = wheelConfig,
                    modifier = Modifier.weight(0.8f)
                ) { index ->
                    selectedAmPm = index
                    onTimeChange(
                        PickerTime(get24Hour(), clampedMinute, if (timeConfig.showSeconds) clampedSecond else 0)
                    )
                }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelTimePickerView (with container)  ━━━━━━━

/**
 * A time picker displayed inside a dialog, bottom sheet, or inline.
 *
 * ### Example — Bottom Sheet
 * ```kotlin
 * WheelTimePickerView(
 *     visible = showTimePicker,
 *     displayMode = PickerDisplayMode.BOTTOM_SHEET,
 *     startTime = PickerTime.now(),
 *     onDoneClick = { time -> /* use time */ },
 *     onDismiss = { showTimePicker = false },
 * )
 * ```
 *
 * ### Example — Dialog with 12-hour format
 * ```kotlin
 * WheelTimePickerView(
 *     visible = showTimePicker,
 *     displayMode = PickerDisplayMode.DIALOG,
 *     timeConfig = PickerDefaults.timePickerConfig(
 *         timeFormat = TimeFormat.HOUR_12,
 *         showSeconds = true,
 *         minuteInterval = 5
 *     ),
 *     onDoneClick = { time -> /* use time */ },
 *     onDismiss = { showTimePicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param startTime Initially selected time.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param timeConfig Time-specific configuration.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with selected time.
 * @param onTimeChange Callback on time change.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimePickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    startTime: PickerTime = PickerTime.now(),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Time"),
    timeConfig: TimePickerConfig = PickerDefaults.timePickerConfig(),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (PickerTime) -> Unit = {},
    onTimeChange: (PickerTime) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelTimePicker(
            modifier = modifier,
            startTime = startTime,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            timeConfig = timeConfig,
            onDoneClick = { time ->
                onDoneClick(time)
                onDismiss()
            },
            onTimeChange = onTimeChange,
            onCancel = onDismiss
        )
    }
}
