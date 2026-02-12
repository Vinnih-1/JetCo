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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Factory functions with sensible defaults for the [com.developerstring.jetco.ui.charts.candlestickchart.CandlestickChart].
 *
 * Example:
 * ```
 * CandlestickChart(
 *     data = entries,
 *     candleConfig = CandlestickDefaults.candleConfig(bullishColor = Color.Green),
 * )
 * ```
 */
object CandlestickDefaults {

    // ── shared constants ─────────────────────────────────────────────────

    private val textStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
    )

    /** Standard bullish (up) green. */
    val defaultBullishColor = Color(0xFF26A69A)

    /** Standard bearish (down) red. */
    val defaultBearishColor = Color(0xFFEF5350)

    /** Neutral grey. */
    val defaultNeutralColor = Color(0xFF9E9E9E)

    // ── candle ───────────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickCandleConfig].
     */
    fun candleConfig(
        bullishColor: Color = defaultBullishColor,
        bearishColor: Color = defaultBearishColor,
        neutralColor: Color = defaultNeutralColor,
        bodyWidthDp: Dp = 8.dp,
        wickWidthDp: Dp = 1.5.dp,
        wickCap: StrokeCap = StrokeCap.Round,
        hollowBullish: Boolean = false,
    ) = CandlestickCandleConfig(
        bullishColor = bullishColor,
        bearishColor = bearishColor,
        neutralColor = neutralColor,
        bodyWidthDp = bodyWidthDp,
        wickWidthDp = wickWidthDp,
        wickCap = wickCap,
        hollowBullish = hollowBullish,
    )

    // ── crosshair ────────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickCrosshairConfig].
     */
    fun crosshairConfig(
        enabled: Boolean = true,
        lineColor: Color = Color.Gray,
        lineWidthDp: Dp = 1.dp,
        dashLengthDp: Dp = 6.dp,
        gapLengthDp: Dp = 4.dp,
    ) = CandlestickCrosshairConfig(
        enabled = enabled,
        lineColor = lineColor,
        lineWidthDp = lineWidthDp,
        dashLengthDp = dashLengthDp,
        gapLengthDp = gapLengthDp,
    )

    // ── marker / tooltip ─────────────────────────────────────────────────

    /**
     * Creates a [CandlestickMarkerConfig].
     */
    fun markerConfig(
        enabled: Boolean = true,
        background: Color = Color(0xFF333333),
        shape: Shape = RoundedCornerShape(8.dp),
        textStyle: TextStyle = CandlestickDefaults.textStyle.copy(
            color = Color.White,
            fontSize = 12.sp
        ),
    ) = CandlestickMarkerConfig(
        enabled = enabled,
        background = background,
        shape = shape,
        textStyle = textStyle,
    )

    // ── volume ───────────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickVolumeConfig].
     */
    fun volumeConfig(
        enabled: Boolean = true,
        heightRatio: Float = 0.2f,
        bullishColor: Color = defaultBullishColor.copy(alpha = 0.4f),
        bearishColor: Color = defaultBearishColor.copy(alpha = 0.4f),
        neutralColor: Color = defaultNeutralColor.copy(alpha = 0.4f),
        barWidthDp: Dp? = null,
    ) = CandlestickVolumeConfig(
        enabled = enabled,
        heightRatio = heightRatio,
        bullishColor = bullishColor,
        bearishColor = bearishColor,
        neutralColor = neutralColor,
        barWidthDp = barWidthDp,
    )

    // ── x-axis ───────────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickXAxisConfig].
     */
    fun xAxisConfig(
        isAxisScaleEnabled: Boolean = true,
        isAxisLineEnabled: Boolean = true,
        axisLineWidth: Dp = 2.dp,
        axisLineShape: Shape = RoundedCornerShape(3.dp),
        axisLineColor: Color = Color.LightGray,
        textStyle: TextStyle = CandlestickDefaults.textStyle,
    ) = CandlestickXAxisConfig(
        isAxisScaleEnabled = isAxisScaleEnabled,
        isAxisLineEnabled = isAxisLineEnabled,
        axisLineWidth = axisLineWidth,
        axisLineShape = axisLineShape,
        axisLineColor = axisLineColor,
        textStyle = textStyle,
    )

    // ── y-axis ───────────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickYAxisConfig].
     */
    fun yAxisConfig(
        isAxisScaleEnabled: Boolean = true,
        isAxisLineEnabled: Boolean = true,
        axisLineWidth: Dp = 2.dp,
        axisLineShape: Shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
        axisLineColor: Color = Color.LightGray,
        axisScaleCount: Int = 4,
        textStyle: TextStyle = CandlestickDefaults.textStyle,
        textPrefix: String = "",
        textPostfix: String = "",
    ) = CandlestickYAxisConfig(
        isAxisScaleEnabled = isAxisScaleEnabled,
        isAxisLineEnabled = isAxisLineEnabled,
        axisLineWidth = axisLineWidth,
        axisLineShape = axisLineShape,
        axisLineColor = axisLineColor,
        axisScaleCount = axisScaleCount,
        textStyle = textStyle,
        textPrefix = textPrefix,
        textPostfix = textPostfix,
    )

    // ── grid lines ───────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickGridLineStyle].
     */
    fun gridLineStyle(
        color: Color = Color.LightGray,
        strokeWidth: Dp = 1.dp,
        dashLength: Dp = 8.dp,
        gapLength: Dp = 8.dp,
        totalGridLines: Int = 4,
        dashCap: StrokeCap = StrokeCap.Square,
    ) = CandlestickGridLineStyle(
        color = color,
        strokeWidth = strokeWidth,
        dashLength = dashLength,
        gapLength = gapLength,
        totalGridLines = totalGridLines,
        dashCap = dashCap,
    )

    // ── animation ────────────────────────────────────────────────────────

    /**
     * Creates a [CandlestickAnimationConfig].
     */
    fun animationConfig(
        enabled: Boolean = true,
        durationMillis: Int = 800,
        delayMillis: Int = 0,
    ) = CandlestickAnimationConfig(
        enabled = enabled,
        durationMillis = durationMillis,
        delayMillis = delayMillis,
    )
}
