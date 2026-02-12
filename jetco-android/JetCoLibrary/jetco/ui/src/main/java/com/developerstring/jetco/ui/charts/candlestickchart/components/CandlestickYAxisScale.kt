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

package com.developerstring.jetco.ui.charts.candlestickchart.components

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
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickYAxisConfig
import kotlin.math.abs
import kotlin.math.round

/**
 * Y-axis labels and optional axis line for the candlestick chart.
 *
 * The scale spans from [minValue] to [maxValue] divided into
 * [CandlestickYAxisConfig.axisScaleCount] steps.
 */
@Composable
internal fun CandlestickYAxisScale(
    yAxisConfig: CandlestickYAxisConfig,
    yAxisStepHeight: Dp,
    minValue: Float,
    maxValue: Float,
    chartHeight: Dp,
) {
    val range = maxValue - minValue
    val scaleStep = if (yAxisConfig.axisScaleCount > 0) {
        range / yAxisConfig.axisScaleCount
    } else range

    Row {
        Column(horizontalAlignment = Alignment.End) {
            repeat(yAxisConfig.axisScaleCount + 1) { index ->
                val barScale = yAxisConfig.axisScaleCount - index
                val value = minValue + scaleStep * barScale
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = yAxisConfig.textPrefix +
                                formatCandlestickScaleValue(value) +
                                yAxisConfig.textPostfix,
                        style = yAxisConfig.textStyle,
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

// ━━━━━━━━━━━━━━━━━━━━━  Formatting helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━

private fun formatDecimal(value: Float): String {
    val rounded = round(value * 100) / 100
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString().removeSuffix("0").removeSuffix(".")
    }
}

internal fun formatCandlestickScaleValue(value: Float): String {
    if (value < 10_000) return formatDecimal(value)
    val am: Float
    if (abs(value / 1_000_000) >= 1) {
        am = value / 1_000_000
        return formatDecimal(am) + "M"
    } else if (abs(value / 1_000) >= 1) {
        am = value / 1_000
        return formatDecimal(am) + "K"
    }
    return formatDecimal(value)
}
