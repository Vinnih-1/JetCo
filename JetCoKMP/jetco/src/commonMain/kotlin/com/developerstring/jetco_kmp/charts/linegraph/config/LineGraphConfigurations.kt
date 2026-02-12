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

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Defines the appearance of the line drawn on the graph.
 *
 * Controls the visual style of the primary data line including its color,
 * thickness, stroke cap, and whether a smooth cubic Bézier curve or
 * straight segments are used to connect data points.
 *
 * @property lineColor Color of the line stroke.
 * @property lineWidth Thickness of the line in [Dp].
 * @property strokeCap The cap style applied to both ends of each line segment.
 * @property smoothCurve When `true`, adjacent points are connected with
 *                       cubic Bézier curves (inspired by Vico's
 *                       `CubicPointConnector`). When `false`, straight
 *                       line segments are used.
 * @property curvature Controls the strength of the cubic Bézier curve.
 *                     A value of `0` produces straight lines while `1` yields
 *                     the most pronounced curves. Only effective when
 *                     [smoothCurve] is `true`.
 */
@Stable
data class LineGraphLineConfig(
    val lineColor: Color,
    val lineWidth: Dp,
    val strokeCap: StrokeCap,
    val smoothCurve: Boolean,
    val curvature: Float,
)

/**
 * Configures the area fill rendered beneath the line.
 *
 * The fill is drawn from the line path down to the X-axis and is
 * typically a semi-transparent gradient that reinforces the data trend.
 *
 * @property enabled Whether the area fill is drawn.
 * @property brush The [Brush] used to paint the area. A vertical gradient
 *                 from the line color (with some alpha) to transparent
 *                 works well for most use cases.
 */
@Stable
data class LineGraphAreaFillConfig(
    val enabled: Boolean,
    val brush: Brush
)

/**
 * Configures the dots drawn at each data point on the line.
 *
 * @property enabled Whether data-point dots are visible.
 * @property radius Radius of each dot in [Dp].
 * @property color Fill color of the dot.
 * @property borderColor Stroke color drawn around the dot.
 * @property borderWidth Stroke width of the dot border in [Dp].
 */
@Stable
data class LineGraphPointConfig(
    val enabled: Boolean,
    val radius: Dp,
    val color: Color,
    val borderColor: Color,
    val borderWidth: Dp
)

/**
 * Configuration for the X-axis of the line graph.
 *
 * @property isAxisScaleEnabled Whether the X-axis labels are drawn.
 * @property isAxisLineEnabled Whether the horizontal axis line is visible.
 * @property axisLineWidth Thickness of the axis line.
 * @property axisLineShape Shape of the axis line (e.g., rounded corners).
 * @property axisLineColor Color of the axis line.
 * @property textStyle [TextStyle] applied to the X-axis labels.
 */
@Stable
data class LineGraphXAxisConfig(
    val isAxisScaleEnabled: Boolean,
    val isAxisLineEnabled: Boolean,
    val axisLineWidth: Dp,
    val axisLineShape: Shape,
    val axisLineColor: Color,
    val textStyle: TextStyle,
)

/**
 * Configuration for the Y-axis of the line graph.
 *
 * @property isAxisScaleEnabled Whether the Y-axis scale labels are displayed.
 * @property isAxisLineEnabled Whether the vertical axis line is visible.
 * @property axisLineWidth Thickness of the axis line.
 * @property axisLineShape Shape of the axis line.
 * @property axisLineColor Color of the axis line.
 * @property axisScaleCount Number of divisions on the Y-axis scale. This
 *                          determines how many horizontal grid lines and
 *                          labels are rendered.
 * @property textStyle [TextStyle] for the scale labels.
 * @property textPrefix Text prepended to each scale label (e.g., "$").
 * @property textPostfix Text appended to each scale label (e.g., "%").
 */
@Stable
data class LineGraphYAxisConfig(
    val isAxisScaleEnabled: Boolean,
    val isAxisLineEnabled: Boolean,
    val axisLineWidth: Dp,
    val axisLineShape: Shape,
    val axisLineColor: Color,
    val axisScaleCount: Int,
    val textStyle: TextStyle,
    val textPrefix: String,
    val textPostfix: String
)

/**
 * Configuration for horizontal grid lines drawn behind the line graph.
 *
 * Grid lines help the reader judge Y-axis values at a glance. They
 * support dashed styling through [dashLength] and [gapLength].
 *
 * @property color Color of the grid lines.
 * @property strokeWidth Width of each grid line.
 * @property dashLength Length of each dash in the dashed pattern.
 * @property gapLength Gap between dashes.
 * @property totalGridLines Number of horizontal grid lines.
 * @property dashCap [StrokeCap] applied to each dash segment.
 */
@Stable
data class LineGraphGridLineStyle(
    val color: Color,
    val strokeWidth: Dp,
    val dashLength: Dp = 0.dp,
    val gapLength: Dp = 0.dp,
    val totalGridLines: Int = 4,
    val dashCap: StrokeCap = StrokeCap.Square
)

/**
 * Configures the pop-up tooltip shown when interacting with a data point.
 *
 * @property enabled Whether touch/click tooltips are active.
 * @property background Background color of the tooltip.
 * @property shape Shape of the tooltip container.
 * @property textStyle [TextStyle] for the tooltip label.
 */
@Stable
data class LineGraphPopUpConfig(
    val enabled: Boolean,
    val background: Color,
    val shape: Shape,
    val textStyle: TextStyle
)

/**
 * Animation configuration for the line graph's entrance effect.
 *
 * The chart can animate both its line drawing and area fill when it
 * first appears on screen.
 *
 * @property enabled Whether animation is played on first composition.
 * @property durationMillis Total duration of the animation in milliseconds.
 * @property delayMillis Delay before the animation starts.
 */
@Stable
data class LineGraphAnimationConfig(
    val enabled: Boolean,
    val durationMillis: Int,
    val delayMillis: Int = 0
)

/**
 * Configuration for the live-update feature of the line graph.
 *
 * When enabled, the graph reacts to data changes with a smooth path
 * transition animation and a blinking indicator on the last data point,
 * making it ideal for real-time / streaming dashboards.
 *
 * @property enabled Whether the live-update mode is active.
 * @property blinkEnabled Whether the last data point pulses.
 * @property blinkDurationMillis Duration of one pulse cycle (expand → shrink).
 * @property blinkMinRadius Minimum radius of the blinking circle (in dp-like float).
 * @property blinkMaxRadius Maximum radius of the blinking circle.
 * @property blinkColor Color of the blinking pulse. When `null` the line color is used.
 * @property pathTransitionDurationMillis Duration of the smooth path transition
 *                                        when data changes.
 */
@Stable
data class LineGraphLiveUpdateConfig(
    val enabled: Boolean,
    val blinkEnabled: Boolean,
    val blinkDurationMillis: Int,
    val blinkMinRadius: Float,
    val blinkMaxRadius: Float,
    val blinkColor: Color?,
    val pathTransitionDurationMillis: Int,
)
