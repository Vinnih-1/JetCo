package com.developerstring.jetco_kmp.charts.barchart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.charts.barchart.config.YAxisConfig
import kotlin.math.abs
import kotlin.math.round

/**
 * A composable function that displays the Y-axis scale labels and an optional Y-axis line for a chart.
 *
 * @param yAxisConfig Configuration for the Y-axis, including text style, scale count, line appearance, and more.
 * @param yAxisStepHeight The height of each step in the Y-axis scale.
 * @param yAxisScaleStep The step value used to calculate the scale labels on the Y-axis.
 * @param barHeight The height of the Y-axis line.
 *
 * @see YAxisConfig
 */
@Composable
fun YAxisScale(
    yAxisConfig: YAxisConfig,
    yAxisStepHeight: Dp,
    yAxisScaleStep: Float,
    barHeight: Dp,
) {
    Row {
        // Column for the Y-axis scale labels
        Column(horizontalAlignment = Alignment.End) {
            repeat(yAxisConfig.axisScaleCount + 1) { index ->
                val barScale = (yAxisConfig.axisScaleCount) - index
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = yAxisConfig.textPrefix + formatScaleValue(yAxisScaleStep * barScale) + yAxisConfig.textPostfix,
                        style = yAxisConfig.textStyle
                    )
                }
            }
        }


        // Y-axis line
        if (yAxisConfig.isAxisLineEnabled) {
            Spacer(modifier = Modifier.width(10.dp)) // Adds spacing between scale labels and Y-axis line

            Box(
                modifier = Modifier
                    .padding(top = yAxisStepHeight)
                    .clip(shape = yAxisConfig.axisLineShape)
                    .width(yAxisConfig.axisLineWidth)
                    .height(barHeight)
                    .background(yAxisConfig.axisLineColor)
            )
        }
    }
}

/**
 * Formats a numeric value for display on the Y-axis scale, using abbreviated notation for large values.
 *
 * This function converts large numeric values into a more readable format with suffixes:
 * - Values below 10,000 are displayed as-is.
 * - Values in the millions are formatted with an "M" suffix.
 * - Values in the thousands are formatted with a "K" suffix.
 *
 * @param value The numeric value to be formatted.
 * @return A formatted string representing the value with appropriate suffixes for readability.
 */
fun formatScaleValue(value: Float): String {
    if (value.isNaN() || value.isInfinite()) return "0"

    if (abs(value) < 10000) {
        return formatBarDecimal(value)
    }

    val am: Float
    if (abs(value / 1_000_000) >= 1) {
        am = value / 1_000_000
        return formatBarDecimal(am) + "M"
    } else if (abs(value / 1_000) >= 1) {
        am = value / 1_000
        return formatBarDecimal(am) + "K"
    }
    return formatBarDecimal(value)
}

/**
 * NaN-safe decimal formatting using integer arithmetic.
 * Produces at most 2 decimal places, removing trailing zeros.
 */
private fun formatBarDecimal(value: Float): String {
    if (value.isNaN() || value.isInfinite()) return "0"
    val negative = value < 0f
    val absRounded = round(abs(value) * 100).toLong()
    val intPart = absRounded / 100
    val decPart = (absRounded % 100).toInt()
    val prefix = if (negative && (intPart > 0 || decPart > 0)) "-" else ""
    return when {
        decPart == 0 -> "$prefix$intPart"
        decPart % 10 == 0 -> "$prefix$intPart.${decPart / 10}"
        else -> "$prefix$intPart.${decPart.toString().padStart(2, '0')}"
    }
}