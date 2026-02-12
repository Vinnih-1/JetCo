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

package com.developerstring.jetco_kmp.charts.linegraph.config

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
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
 * Default factory functions and constants for configuring a [LineGraph][com.developerstring.jetco_kmp.charts.linegraph.LineGraph].
 *
 * Each function mirrors one of the configuration data classes and
 * provides sensible defaults, making it easy for consumers to
 * customise only the properties they care about.
 *
 * **Example:**
 * ```
 * LineGraph(
 *     chartData   = mapOf("Jan" to 10f, "Feb" to 20f),
 *     lineConfig  = LineGraphDefaults.lineConfig(lineColor = Color.Red),
 *     yAxisConfig = LineGraphDefaults.yAxisConfig(axisScaleCount = 6),
 * )
 * ```
 */
object LineGraphDefaults {

    // ── shared text style ────────────────────────────────────────────────
    /**
     * Default [TextStyle] used for axis labels and tooltips.
     */
    private val textStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
    )

    /** Default primary line color. */
    private val defaultLineColor = Color(0xFF519DE9)

    // ── line ─────────────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphLineConfig] with customisable defaults.
     *
     * @param lineColor Stroke color of the line.
     * @param lineWidth Stroke thickness.
     * @param strokeCap Cap style for each segment.
     * @param smoothCurve Whether to connect points with cubic Bézier curves.
     * @param curvature Bézier curvature strength in `(0, 1]`.
     * @return Configured [LineGraphLineConfig].
     */
    fun lineConfig(
        lineColor: Color = defaultLineColor,
        lineWidth: Dp = 2.dp,
        strokeCap: StrokeCap = StrokeCap.Round,
        smoothCurve: Boolean = true,
        curvature: Float = 0.5f
    ) = LineGraphLineConfig(
        lineColor = lineColor,
        lineWidth = lineWidth,
        strokeCap = strokeCap,
        smoothCurve = smoothCurve,
        curvature = curvature
    )

    // ── area fill ────────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphAreaFillConfig].
     *
     * When [enabled] the area below the line receives a gradient fill that
     * fades from the [lineColor] at 40 % opacity down to transparent.
     *
     * @param enabled Whether the area fill is rendered.
     * @param lineColor Used to derive the default gradient brush.
     * @param brush Custom brush; overrides the default gradient.
     * @return Configured [LineGraphAreaFillConfig].
     */
    fun areaFillConfig(
        enabled: Boolean = true,
        lineColor: Color = defaultLineColor,
        brush: Brush = Brush.verticalGradient(
            listOf(
                lineColor.copy(alpha = 0.4f),
                Color.Transparent
            )
        )
    ) = LineGraphAreaFillConfig(
        enabled = enabled,
        brush = brush
    )

    // ── data-point dots ──────────────────────────────────────────────────

    /**
     * Creates a [LineGraphPointConfig].
     *
     * @param enabled Whether dots are drawn at each data point.
     * @param radius Dot radius.
     * @param color Dot fill color.
     * @param borderColor Dot border color.
     * @param borderWidth Dot border thickness.
     * @return Configured [LineGraphPointConfig].
     */
    fun pointConfig(
        enabled: Boolean = true,
        radius: Dp = 4.dp,
        color: Color = Color.White,
        borderColor: Color = defaultLineColor,
        borderWidth: Dp = 2.dp
    ) = LineGraphPointConfig(
        enabled = enabled,
        radius = radius,
        color = color,
        borderColor = borderColor,
        borderWidth = borderWidth
    )

    // ── X-axis ───────────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphXAxisConfig].
     *
     * @param isAxisScaleEnabled Whether labels are shown on the X-axis.
     * @param isAxisLineEnabled Whether the horizontal axis line is drawn.
     * @param axisLineWidth Thickness of the axis line.
     * @param axisLineShape Shape of the axis line.
     * @param axisLineColor Color of the axis line.
     * @param textStyle Style for the axis labels.
     * @return Configured [LineGraphXAxisConfig].
     */
    fun xAxisConfig(
        isAxisScaleEnabled: Boolean = true,
        isAxisLineEnabled: Boolean = true,
        axisLineWidth: Dp = 2.dp,
        axisLineShape: Shape = RoundedCornerShape(3.dp),
        axisLineColor: Color = Color.LightGray,
        textStyle: TextStyle = LineGraphDefaults.textStyle,
    ) = LineGraphXAxisConfig(
        isAxisScaleEnabled = isAxisScaleEnabled,
        isAxisLineEnabled = isAxisLineEnabled,
        axisLineWidth = axisLineWidth,
        axisLineShape = axisLineShape,
        axisLineColor = axisLineColor,
        textStyle = textStyle,
    )

    // ── Y-axis ───────────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphYAxisConfig].
     *
     * @param isAxisScaleEnabled Whether scale labels are displayed.
     * @param isAxisLineEnabled Whether the vertical axis line is shown.
     * @param axisLineWidth Thickness of the axis line.
     * @param axisLineShape Shape of the axis line.
     * @param axisLineColor Color of the axis line.
     * @param axisScaleCount Number of intervals on the Y-axis.
     * @param textStyle Style for the scale labels.
     * @param textPrefix Prefix before each label value.
     * @param textPostfix Postfix after each label value.
     * @return Configured [LineGraphYAxisConfig].
     */
    fun yAxisConfig(
        isAxisScaleEnabled: Boolean = true,
        isAxisLineEnabled: Boolean = true,
        axisLineWidth: Dp = 2.dp,
        axisLineShape: Shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
        axisLineColor: Color = Color.LightGray,
        axisScaleCount: Int = 4,
        textStyle: TextStyle = LineGraphDefaults.textStyle,
        textPrefix: String = "",
        textPostfix: String = ""
    ) = LineGraphYAxisConfig(
        isAxisScaleEnabled = isAxisScaleEnabled,
        isAxisLineEnabled = isAxisLineEnabled,
        axisLineWidth = axisLineWidth,
        axisLineShape = axisLineShape,
        axisLineColor = axisLineColor,
        axisScaleCount = axisScaleCount,
        textStyle = textStyle,
        textPrefix = textPrefix,
        textPostfix = textPostfix
    )

    // ── grid lines ───────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphGridLineStyle].
     *
     * @param color Grid line color.
     * @param strokeWidth Grid line thickness.
     * @param dashLength Dash segment length.
     * @param gapLength Gap between dash segments.
     * @param totalGridLines Number of horizontal grid lines.
     * @param dashCap Cap style for each dash.
     * @return Configured [LineGraphGridLineStyle].
     */
    fun gridLineStyle(
        color: Color = Color.LightGray,
        strokeWidth: Dp = 1.dp,
        dashLength: Dp = 8.dp,
        gapLength: Dp = 8.dp,
        totalGridLines: Int = 4,
        dashCap: StrokeCap = StrokeCap.Square
    ) = LineGraphGridLineStyle(
        color = color,
        strokeWidth = strokeWidth,
        dashLength = dashLength,
        gapLength = gapLength,
        totalGridLines = totalGridLines,
        dashCap = dashCap
    )

    // ── pop-up / tooltip ─────────────────────────────────────────────────

    /**
     * Creates a [LineGraphPopUpConfig].
     *
     * @param enabled Whether tooltips are enabled.
     * @param background Tooltip background color.
     * @param shape Tooltip container shape.
     * @param textStyle Text style for the tooltip content.
     * @return Configured [LineGraphPopUpConfig].
     */
    fun popUpConfig(
        enabled: Boolean = true,
        background: Color = Color(0xFFCCC2DC),
        shape: Shape = RoundedCornerShape(25),
        textStyle: TextStyle = LineGraphDefaults.textStyle
    ) = LineGraphPopUpConfig(
        enabled = enabled,
        background = background,
        shape = shape,
        textStyle = textStyle
    )

    // ── animation ────────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphAnimationConfig].
     *
     * @param enabled Whether the entrance animation is played.
     * @param durationMillis Total animation duration.
     * @param delayMillis Delay before the animation starts.
     * @return Configured [LineGraphAnimationConfig].
     */
    fun animationConfig(
        enabled: Boolean = true,
        durationMillis: Int = 1000,
        delayMillis: Int = 0
    ) = LineGraphAnimationConfig(
        enabled = enabled,
        durationMillis = durationMillis,
        delayMillis = delayMillis
    )

    // ── live update ──────────────────────────────────────────────────────

    /**
     * Creates a [LineGraphLiveUpdateConfig].
     *
     * When enabled the graph animates path transitions when
     * data changes, and optionally pulses the last data point.
     *
     * @param enabled Whether live-update mode is active.
     * @param blinkEnabled Whether the last point pulses.
     * @param blinkDurationMillis Duration of one blink cycle.
     * @param blinkMinRadius Minimum pulse radius.
     * @param blinkMaxRadius Maximum pulse radius.
     * @param blinkColor Pulse color; `null` uses the line color.
     * @param pathTransitionDurationMillis Duration of the smooth
     *        path transition animation.
     * @return Configured [LineGraphLiveUpdateConfig].
     */
    fun liveUpdateConfig(
        enabled: Boolean = false,
        blinkEnabled: Boolean = true,
        blinkDurationMillis: Int = 800,
        blinkMinRadius: Float = 4f,
        blinkMaxRadius: Float = 12f,
        blinkColor: Color? = null,
        pathTransitionDurationMillis: Int = 500,
    ) = LineGraphLiveUpdateConfig(
        enabled = enabled,
        blinkEnabled = blinkEnabled,
        blinkDurationMillis = blinkDurationMillis,
        blinkMinRadius = blinkMinRadius,
        blinkMaxRadius = blinkMaxRadius,
        blinkColor = blinkColor,
        pathTransitionDurationMillis = pathTransitionDurationMillis,
    )
}
