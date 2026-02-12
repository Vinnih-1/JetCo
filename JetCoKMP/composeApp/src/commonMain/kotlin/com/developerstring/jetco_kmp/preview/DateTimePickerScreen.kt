package com.developerstring.jetco_kmp.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco_kmp.components.picker.WheelDatePicker
import com.developerstring.jetco_kmp.components.picker.WheelDatePickerView
import com.developerstring.jetco_kmp.components.picker.WheelDateRangePickerView
import com.developerstring.jetco_kmp.components.picker.WheelDateTimePickerView
import com.developerstring.jetco_kmp.components.picker.WheelMonthPickerView
import com.developerstring.jetco_kmp.components.picker.WheelMonthYearPickerView
import com.developerstring.jetco_kmp.components.picker.WheelTimePickerView
import com.developerstring.jetco_kmp.components.picker.WheelYearPickerView
import com.developerstring.jetco_kmp.components.picker.config.PickerDefaults
import com.developerstring.jetco_kmp.components.picker.model.DateOrder
import com.developerstring.jetco_kmp.components.picker.model.PickerDate
import com.developerstring.jetco_kmp.components.picker.model.PickerDisplayMode
import com.developerstring.jetco_kmp.components.picker.model.TimeFormat

@Composable
fun DateTimePickerScreen() {
    // State for bottom sheet pickers
    var showDateBottomSheet by remember { mutableStateOf(false) }
    var showTimeBottomSheet by remember { mutableStateOf(false) }
    var showDateTimeBottomSheet by remember { mutableStateOf(false) }
    var showDateRangeBottomSheet by remember { mutableStateOf(false) }
    var showMonthYearBottomSheet by remember { mutableStateOf(false) }
    var showYearBottomSheet by remember { mutableStateOf(false) }
    var showMonthBottomSheet by remember { mutableStateOf(false) }

    // State for dialog pickers
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var showDateTimeDialog by remember { mutableStateOf(false) }
    var showDateRangeDialog by remember { mutableStateOf(false) }

    // State for highly customized variants
    var showCustomDateDialog by remember { mutableStateOf(false) }
    var showCustomTimeBottomSheet by remember { mutableStateOf(false) }
    var showCustomDateRangeDialog by remember { mutableStateOf(false) }

    // Selected values
    var selectedDate by remember { mutableStateOf("Not selected") }
    var selectedTime by remember { mutableStateOf("Not selected") }
    var selectedDateTime by remember { mutableStateOf("Not selected") }
    var selectedDateRange by remember { mutableStateOf("Not selected") }
    var selectedMonthYear by remember { mutableStateOf("Not selected") }
    var selectedYear by remember { mutableStateOf("Not selected") }
    var selectedMonth by remember { mutableStateOf("Not selected") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Date & Time Pickers",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // ━━━━━━━━━━━━━━━━━━━ BOTTOM SHEET VARIANTS ━━━━━━━━━━━━━━━━━━━
        SectionCard(
            title = "Bottom Sheet Pickers",
            description = "Default style with Material 3 BottomSheet"
        ) {
            PickerButton(
                text = "Date Picker",
                value = selectedDate,
                onClick = { showDateBottomSheet = true }
            )

            PickerButton(
                text = "Time Picker (24h)",
                value = selectedTime,
                onClick = { showTimeBottomSheet = true }
            )

            PickerButton(
                text = "Date & Time Picker",
                value = selectedDateTime,
                onClick = { showDateTimeBottomSheet = true }
            )

            PickerButton(
                text = "Date Range Picker",
                value = selectedDateRange,
                onClick = { showDateRangeBottomSheet = true }
            )

            PickerButton(
                text = "Month & Year Picker",
                value = selectedMonthYear,
                onClick = { showMonthYearBottomSheet = true }
            )

            PickerButton(
                text = "Year Picker",
                value = selectedYear,
                onClick = { showYearBottomSheet = true }
            )

            PickerButton(
                text = "Month Picker",
                value = selectedMonth,
                onClick = { showMonthBottomSheet = true }
            )
        }

        // ━━━━━━━━━━━━━━━━━━━ DIALOG VARIANTS ━━━━━━━━━━━━━━━━━━━
        SectionCard(
            title = "Dialog Pickers",
            description = "Clean & compact dialog presentation"
        ) {
            PickerButton(
                text = "Date Picker Dialog",
                value = selectedDate,
                onClick = { showDateDialog = true }
            )

            PickerButton(
                text = "Time Picker Dialog (12h)",
                value = selectedTime,
                onClick = { showTimeDialog = true }
            )

            PickerButton(
                text = "DateTime Dialog",
                value = selectedDateTime,
                onClick = { showDateTimeDialog = true }
            )

            PickerButton(
                text = "Date Range Dialog",
                value = selectedDateRange,
                onClick = { showDateRangeDialog = true }
            )
        }

        // ━━━━━━━━━━━━━━━━━━━ CUSTOMIZED VARIANTS ━━━━━━━━━━━━━━━━━━━
        SectionCard(
            title = "Highly Customized Variants",
            description = "Custom colors, fonts, intervals & formats"
        ) {
            PickerButton(
                text = "Custom Date (MDY Order)",
                value = selectedDate,
                onClick = { showCustomDateDialog = true }
            )

            PickerButton(
                text = "Custom Time (5-min intervals)",
                value = selectedTime,
                onClick = { showCustomTimeBottomSheet = true }
            )

            PickerButton(
                text = "Custom Range (Short Months)",
                value = selectedDateRange,
                onClick = { showCustomDateRangeDialog = true }
            )
        }

        // ━━━━━━━━━━━━━━━━━━━ INLINE VARIANT ━━━━━━━━━━━━━━━━━━━
        SectionCard(
            title = "Inline Picker",
            description = "Embedded directly in the UI"
        ) {
            WheelDatePicker(
                startDate = PickerDate.now(),
                wheelConfig = PickerDefaults.wheelPickerConfig(height = 180.dp),
                headerConfig = PickerDefaults.headerConfig(
                    title = "Select Date",
                    enabled = true
                ),
                onDoneClick = { date ->
                    selectedDate = date.toFormattedString()
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // ━━━━━━━━━━━━━━━━━━━ BOTTOM SHEET PICKERS ━━━━━━━━━━━━━━━━━━━

    WheelDatePickerView(
        visible = showDateBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        startDate = PickerDate.now(),
        onDoneClick = { date ->
            selectedDate = date.toFormattedString()
        },
        onDismiss = { showDateBottomSheet = false }
    )

    WheelTimePickerView(
        visible = showTimeBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        timeConfig = PickerDefaults.timePickerConfig(
            timeFormat = TimeFormat.HOUR_24
        ),
        onDoneClick = { time ->
            selectedTime = time.toFormattedString(TimeFormat.HOUR_24)
        },
        onDismiss = { showTimeBottomSheet = false }
    )

    WheelDateTimePickerView(
        visible = showDateTimeBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        onDoneClick = { dateTime ->
            selectedDateTime = "${dateTime.date.toFormattedString()} ${dateTime.time.toFormattedString()}"
        },
        onDismiss = { showDateTimeBottomSheet = false }
    )

    WheelDateRangePickerView(
        visible = showDateRangeBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        onDoneClick = { from, to ->
            selectedDateRange = "${from.toFormattedString()} to ${to.toFormattedString()}"
        },
        onDismiss = { showDateRangeBottomSheet = false }
    )

    WheelMonthYearPickerView(
        visible = showMonthYearBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        onDoneClick = { month, year ->
            val monthNames = listOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            selectedMonthYear = "${monthNames[month - 1]} $year"
        },
        onDismiss = { showMonthYearBottomSheet = false }
    )

    WheelYearPickerView(
        visible = showYearBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        yearsRange = IntRange(2000, 2050),
        onDoneClick = { year ->
            selectedYear = year.toString()
        },
        onDismiss = { showYearBottomSheet = false }
    )

    WheelMonthPickerView(
        visible = showMonthBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        dateConfig = PickerDefaults.datePickerConfig(showShortMonths = true),
        onDoneClick = { month ->
            val monthNames = listOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            selectedMonth = monthNames[month - 1]
        },
        onDismiss = { showMonthBottomSheet = false }
    )

    // ━━━━━━━━━━━━━━━━━━━ DIALOG PICKERS ━━━━━━━━━━━━━━━━━━━

    WheelDatePickerView(
        visible = showDateDialog,
        displayMode = PickerDisplayMode.DIALOG,
        headerConfig = PickerDefaults.headerConfig(
            title = "Pick a Date",
            cancelLabel = "Cancel"
        ),
        onDoneClick = { date ->
            selectedDate = date.format("dd MMMM yyyy")
        },
        onDismiss = { showDateDialog = false }
    )

    WheelTimePickerView(
        visible = showTimeDialog,
        displayMode = PickerDisplayMode.DIALOG,
        timeConfig = PickerDefaults.timePickerConfig(
            timeFormat = TimeFormat.HOUR_12,
            showSeconds = false
        ),
        headerConfig = PickerDefaults.headerConfig(
            title = "Pick Time",
            cancelLabel = "Cancel"
        ),
        onDoneClick = { time ->
            selectedTime = time.toFormattedString(TimeFormat.HOUR_12)
        },
        onDismiss = { showTimeDialog = false }
    )

    WheelDateTimePickerView(
        visible = showDateTimeDialog,
        displayMode = PickerDisplayMode.DIALOG,
        headerConfig = PickerDefaults.headerConfig(
            title = "Select Date & Time",
            cancelLabel = "Cancel"
        ),
        onDoneClick = { dateTime ->
            selectedDateTime = "${dateTime.date.format("MMM dd, yyyy")} at ${dateTime.time.toFormattedString()}"
        },
        onDismiss = { showDateTimeDialog = false }
    )

    WheelDateRangePickerView(
        visible = showDateRangeDialog,
        displayMode = PickerDisplayMode.DIALOG,
        headerConfig = PickerDefaults.headerConfig(
            title = "Select Date Range",
            cancelLabel = "Cancel"
        ),
        onDoneClick = { from, to ->
            selectedDateRange = "${from.format("MMM dd")} - ${to.format("MMM dd, yyyy")}"
        },
        onDismiss = { showDateRangeDialog = false }
    )

    // ━━━━━━━━━━━━━━━━━━━ HIGHLY CUSTOMIZED VARIANTS ━━━━━━━━━━━━━━━━━━━

    WheelDatePickerView(
        visible = showCustomDateDialog,
        displayMode = PickerDisplayMode.DIALOG,
        wheelConfig = PickerDefaults.wheelPickerConfig(
            height = 220.dp,
            rowCount = 5,
            selectedTextStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066CC)
            ),
            defaultTextStyle = TextStyle(
                fontSize = 16.sp,
                color = Color(0xFF999999)
            ),
            selectorColor = Color(0x1A0066CC),
            selectorShape = RoundedCornerShape(16.dp),
            fadeEdges = true
        ),
        headerConfig = PickerDefaults.headerConfig(
            title = "Custom Date Picker",
            doneLabel = "Select",
            doneLabelColor = Color(0xFF0066CC),
            cancelLabel = "Dismiss",
            titleStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            ),
            doneLabelStyle = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        ),
        dateConfig = PickerDefaults.datePickerConfig(
            dateOrder = DateOrder.MONTH_DAY_YEAR,
            showShortMonths = true,
            yearsRange = IntRange(1990, 2030)
        ),
        containerConfig = PickerDefaults.containerConfig(
            containerColor = Color(0xFFFAFAFA),
            shape = RoundedCornerShape(24.dp)
        ),
        onDoneClick = { date ->
            selectedDate = date.format("MMM dd, yyyy")
        },
        onDismiss = { showCustomDateDialog = false }
    )

    WheelTimePickerView(
        visible = showCustomTimeBottomSheet,
        displayMode = PickerDisplayMode.BOTTOM_SHEET,
        wheelConfig = PickerDefaults.wheelPickerConfig(
            height = 200.dp,
            selectedTextStyle = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00AA00)
            ),
            selectorColor = Color(0x2200AA00),
            selectorShape = RoundedCornerShape(12.dp)
        ),
        headerConfig = PickerDefaults.headerConfig(
            title = "Pick Your Time",
            doneLabel = "Confirm",
            doneLabelColor = Color(0xFF00AA00),
            titleStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        ),
        timeConfig = PickerDefaults.timePickerConfig(
            timeFormat = TimeFormat.HOUR_24,
            showSeconds = true,
            minuteInterval = 5,
            secondInterval = 15
        ),
        onDoneClick = { time ->
            selectedTime = "${time.hour}:${time.minute.toString().padStart(2, '0')}:${time.second.toString().padStart(2, '0')}"
        },
        onDismiss = { showCustomTimeBottomSheet = false }
    )

    WheelDateRangePickerView(
        visible = showCustomDateRangeDialog,
        displayMode = PickerDisplayMode.DIALOG,
        wheelConfig = PickerDefaults.wheelPickerConfig(
            height = 180.dp,
            selectedTextStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF6B35)
            ),
            selectorColor = Color(0x1AFF6B35)
        ),
        headerConfig = PickerDefaults.headerConfig(
            title = "Select Trip Dates",
            doneLabel = "Book",
            doneLabelColor = Color(0xFFFF6B35),
            cancelLabel = "Back",
            titleStyle = TextStyle(
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        ),
        dateConfig = PickerDefaults.datePickerConfig(
            showShortMonths = true,
            dateOrder = DateOrder.DAY_MONTH_YEAR
        ),
        rangeConfig = PickerDefaults.dateRangePickerConfig(
            centerText = "→",
            selectedBoxColor = Color(0xFFFFE5DC),
            selectedBoxBorderColor = Color(0xFFFF6B35),
            selectedTextColor = Color(0xFFFF6B35),
            boxShape = RoundedCornerShape(12.dp),
            dateTextFormat = "dd MMM yyyy"
        ),
        containerConfig = PickerDefaults.containerConfig(
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        ),
        onDoneClick = { from, to ->
            selectedDateRange = "${from.format("dd MMM")} → ${to.format("dd MMM yyyy")}"
        },
        onDismiss = { showCustomDateRangeDialog = false }
    )
}

@Composable
private fun SectionCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            content()
        }
    }
}

@Composable
private fun PickerButton(
    text: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF519DE9)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = text)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = if (value == "Not selected") Color(0xFF999999) else Color(0xFF1A1A1A),
                fontWeight = if (value == "Not selected") FontWeight.Normal else FontWeight.Medium
            )
        }
    }
}