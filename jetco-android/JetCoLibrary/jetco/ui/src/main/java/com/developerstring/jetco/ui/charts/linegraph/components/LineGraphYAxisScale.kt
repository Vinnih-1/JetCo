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

package com.developerstring.jetco.ui.charts.linegraph.components

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
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphYAxisConfig
import kotlin.math.abs
import kotlin.math.round

/**
 * Renders the Y-axis scale labels and optional vertical axis line
 * for the line graph.
 *
 * Labels are evenly distributed from `0` to the chart's maximum
 * value using [yAxisScaleStep] as the increment.
 *
 * @param yAxisConfig Y-axis visual configuration.
 * @param yAxisStepHeight Height of each scale step (controls label spacing).
 * @param yAxisScaleStep Numeric increment between labels.
 * @param chartHeight Total height of the charting area.
 *
 * @see LineGraphYAxisConfig
 */
@Composable
internal fun LineGraphYAxisScale(
    yAxisConfig: LineGraphYAxisConfig,
    yAxisStepHeight: Dp,
    yAxisScaleStep: Float,
    chartHeight: Dp,
) {
    Row {
        Column(horizontalAlignment = Alignment.End) {
            repeat(yAxisConfig.axisScaleCount + 1) { index ->
                val barScale = (yAxisConfig.axisScaleCount) - index
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = yAxisConfig.textPrefix +
                                formatLineGraphScaleValue(yAxisScaleStep * barScale) +
                                yAxisConfig.textPostfix,
                        style = yAxisConfig.textStyle
                    )
                }
            }
        }

        if (yAxisConfig.isAxisLineEnabled) {
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .padding(top = yAxisStepHeight)
                    .clip(shape = yAxisConfig.axisLineShape)
                    .width(yAxisConfig.axisLineWidth)
                    .height(chartHeight)
                    .background(yAxisConfig.axisLineColor)
            )
        }
    }
}

/**
 * Formats a decimal number to at most 2 decimal places, removing trailing zeros.
 * Pure Kotlin implementation for Multiplatform compatibility.
 */
private fun formatDecimal(value: Float): String {
    val rounded = round(value * 100) / 100
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString().removeSuffix("0").removeSuffix(".")
    }
}

/**
 * Formats a numeric value for the Y-axis scale, abbreviating large
 * numbers with K (thousands) and M (millions) suffixes.
 *
 * Values below 10 000 are displayed as-is with up to two decimal places.
 *
 * @param value The raw numeric value.
 * @return A human-readable string representation.
 */
internal fun formatLineGraphScaleValue(value: Float): String {
    if (value < 10000) {
        return formatDecimal(value)
    }

    val am: Float
    if (abs(value / 1_000_000) >= 1) {
        am = value / 1_000_000
        return formatDecimal(am) + "M"
    } else if (abs(value / 1000) >= 1) {
        am = value / 1000
        return formatDecimal(am) + "K"
    } else {
        return formatDecimal(value)
    }
}
