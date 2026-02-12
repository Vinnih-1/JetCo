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

package com.developerstring.jetco.ui.charts.candlestickchart.config

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Candle body and wick appearance.
 *
 * @property bullishColor Color for candles where close > open.
 * @property bearishColor Color for candles where close < open.
 * @property neutralColor Color for candles where close == open.
 * @property bodyWidthDp Width of the candle body in dp.
 * @property wickWidthDp Width of the wick line in dp.
 * @property wickCap [StrokeCap] applied to wick ends.
 * @property hollowBullish When `true`, bullish candles are rendered
 *                         with a hollow (outlined) body instead of filled.
 */
@Keep
@Stable
data class CandlestickCandleConfig(
    val bullishColor: Color,
    val bearishColor: Color,
    val neutralColor: Color,
    val bodyWidthDp: Dp,
    val wickWidthDp: Dp,
    val wickCap: StrokeCap,
    val hollowBullish: Boolean,
)

/**
 * Crosshair indicator shown when a candle is selected.
 *
 * @property enabled Whether the crosshair is drawn.
 * @property lineColor Color of the crosshair lines.
 * @property lineWidthDp Width of the crosshair lines.
 * @property dashLengthDp Dash segment length (0 = solid).
 * @property gapLengthDp Gap between dash segments.
 */
@Keep
@Stable
data class CandlestickCrosshairConfig(
    val enabled: Boolean,
    val lineColor: Color,
    val lineWidthDp: Dp,
    val dashLengthDp: Dp,
    val gapLengthDp: Dp,
)

/**
 * Marker tooltip shown when a candle is tapped/selected.
 *
 * @property enabled Whether the marker is shown.
 * @property background Background color of the tooltip.
 * @property shape Shape of the tooltip container.
 * @property textStyle Style used for OHLC labels inside the tooltip.
 */
@Keep
@Stable
data class CandlestickMarkerConfig(
    val enabled: Boolean,
    val background: Color,
    val shape: Shape,
    val textStyle: TextStyle,
)

/**
 * Volume-bar overlay rendered below the candles.
 *
 * @property enabled Whether volume bars are drawn.
 * @property heightRatio Fraction of the chart height occupied by volume bars
 *                       (e.g., `0.2f` = 20 %).
 * @property bullishColor Volume bar color for bullish candles.
 * @property bearishColor Volume bar color for bearish candles.
 * @property neutralColor Volume bar color for neutral candles.
 * @property barWidthDp Width of each volume bar; when `null` the
 *                      candle body width is used.
 */
@Keep
@Stable
data class CandlestickVolumeConfig(
    val enabled: Boolean,
    val heightRatio: Float,
    val bullishColor: Color,
    val bearishColor: Color,
    val neutralColor: Color,
    val barWidthDp: Dp?,
)

/**
 * X-axis configuration for the candlestick chart.
 *
 * @property isAxisScaleEnabled Whether X-axis labels are displayed.
 * @property isAxisLineEnabled Whether the horizontal line is drawn.
 * @property axisLineWidth Thickness of the axis line.
 * @property axisLineShape Shape applied to the line.
 * @property axisLineColor Color of the axis line.
 * @property textStyle Style for labels.
 */
@Keep
@Stable
data class CandlestickXAxisConfig(
    val isAxisScaleEnabled: Boolean,
    val isAxisLineEnabled: Boolean,
    val axisLineWidth: Dp,
    val axisLineShape: Shape,
    val axisLineColor: Color,
    val textStyle: TextStyle,
)

/**
 * Y-axis configuration for the candlestick chart.
 *
 * @property isAxisScaleEnabled Whether Y-axis labels are displayed.
 * @property isAxisLineEnabled Whether the vertical line is drawn.
 * @property axisLineWidth Thickness of the axis line.
 * @property axisLineShape Shape applied to the line.
 * @property axisLineColor Color of the axis line.
 * @property axisScaleCount Number of divisions on the Y-axis.
 * @property textStyle Style for labels.
 * @property textPrefix Prefix added before label values (e.g., "$").
 * @property textPostfix Postfix added after label values.
 */
@Keep
@Stable
data class CandlestickYAxisConfig(
    val isAxisScaleEnabled: Boolean,
    val isAxisLineEnabled: Boolean,
    val axisLineWidth: Dp,
    val axisLineShape: Shape,
    val axisLineColor: Color,
    val axisScaleCount: Int,
    val textStyle: TextStyle,
    val textPrefix: String,
    val textPostfix: String,
)

/**
 * Horizontal grid-line styling.
 *
 * @property color Grid-line color.
 * @property strokeWidth Line thickness.
 * @property dashLength Dash segment length.
 * @property gapLength Gap between dashes.
 * @property totalGridLines Number of horizontal lines.
 * @property dashCap Cap style applied to each dash.
 */
@Keep
@Stable
data class CandlestickGridLineStyle(
    val color: Color,
    val strokeWidth: Dp,
    val dashLength: Dp = 0.dp,
    val gapLength: Dp = 0.dp,
    val totalGridLines: Int = 4,
    val dashCap: StrokeCap = StrokeCap.Square,
)

/**
 * Entrance animation timing for the candlestick chart.
 *
 * @property enabled Whether fade-in / pop-in animation is played.
 * @property durationMillis Total animation duration.
 * @property delayMillis Delay before animation starts.
 */
@Keep
@Stable
data class CandlestickAnimationConfig(
    val enabled: Boolean,
    val durationMillis: Int,
    val delayMillis: Int = 0,
)
