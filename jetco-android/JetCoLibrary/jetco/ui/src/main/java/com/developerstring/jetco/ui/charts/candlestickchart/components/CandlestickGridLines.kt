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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickGridLineStyle

/**
 * Draws a single horizontal dashed grid line for the candlestick chart.
 */
@Composable
internal fun CandlestickGridLine(
    modifier: Modifier = Modifier,
    gridLineStyle: CandlestickGridLineStyle,
) {
    Canvas(modifier = modifier.padding(top = gridLineStyle.strokeWidth / 2)) {
        val dashLengthPx = gridLineStyle.dashLength.toPx()
        val gapLengthPx = gridLineStyle.gapLength.toPx()
        val strokeWidthPx = gridLineStyle.strokeWidth.toPx()
        val pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashLengthPx, gapLengthPx), 0f
        )
        drawLine(
            color = gridLineStyle.color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidthPx,
            pathEffect = pathEffect,
            cap = gridLineStyle.dashCap,
        )
    }
}

/**
 * Stacks [CandlestickGridLine] composables vertically inside a [Column],
 * matching the layout approach used by [com.developerstring.jetco.ui.charts.linegraph.components.LineGraphYAxisGridLines].
 *
 * @param gridLineStyle Visual configuration for each grid line.
 * @param yAxisStepHeight Vertical distance between consecutive grid lines.
 * @param totalGridLines Override for the number of grid lines. Defaults
 *                       to [CandlestickGridLineStyle.totalGridLines].
 */
@Composable
internal fun CandlestickYAxisGridLines(
    gridLineStyle: CandlestickGridLineStyle,
    yAxisStepHeight: Dp,
    totalGridLines: Int = gridLineStyle.totalGridLines,
) {
    Column(
        modifier = Modifier
            .padding(top = yAxisStepHeight)
            .fillMaxWidth(),
    ) {
        repeat(totalGridLines + 1) { index ->
            if (index != 0) {
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight),
                    verticalAlignment = Alignment.Top
                ) {
                    CandlestickGridLine(
                        modifier = Modifier.fillMaxWidth(),
                        gridLineStyle = gridLineStyle,
                    )
                }
            }
        }
    }
}
