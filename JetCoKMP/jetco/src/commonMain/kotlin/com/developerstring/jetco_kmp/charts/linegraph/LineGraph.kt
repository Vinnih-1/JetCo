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

package com.developerstring.jetco_kmp.charts.linegraph

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco_kmp.charts.linegraph.components.LineGraphPopup
import com.developerstring.jetco_kmp.charts.linegraph.components.LineGraphYAxisGridLines
import com.developerstring.jetco_kmp.charts.linegraph.components.LineGraphYAxisScale
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphAnimationConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphAreaFillConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphDefaults
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphGridLineStyle
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphLineConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphLiveUpdateConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphPointConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphPopUpConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphXAxisConfig
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphYAxisConfig
import com.developerstring.jetco_kmp.charts.linegraph.model.LineGraphEntry
import com.developerstring.jetco_kmp.charts.linegraph.model.mapToLineGraphEntries
import kotlin.math.abs
import kotlin.math.round

/**
 * A fully customisable, animated line graph composable for Jetpack Compose.
 *
 * The graph draws a continuous line connecting data points, with optional
 * area fill, data-point dots, grid lines, axes, tooltips and entrance
 * animation. The line can be rendered as straight segments or smooth
 * cubic Bézier curves (inspired by Vico's `CubicPointConnector`).
 *
 * ## Quick start
 * ```kotlin
 * LineGraph(
 *     chartData = mapOf("Jan" to 10f, "Feb" to 25f, "Mar" to 18f, "Apr" to 30f),
 * )
 * ```
 *
 * ## Full customisation
 * ```kotlin
 * LineGraph(
 *     modifier        = Modifier.fillMaxWidth().padding(16.dp),
 *     chartData       = mapOf("Q1" to 120f, "Q2" to 340f, "Q3" to 260f, "Q4" to 410f),
 *     chartHeight     = 250.dp,
 *     lineConfig      = LineGraphDefaults.lineConfig(
 *                            lineColor   = Color(0xFFEF5350),
 *                            smoothCurve = true,
 *                            curvature   = 0.4f
 *                        ),
 *     areaFillConfig  = LineGraphDefaults.areaFillConfig(
 *                            enabled   = true,
 *                            lineColor = Color(0xFFEF5350)
 *                        ),
 *     pointConfig     = LineGraphDefaults.pointConfig(borderColor = Color(0xFFEF5350)),
 *     yAxisConfig     = LineGraphDefaults.yAxisConfig(textPrefix = "$"),
 *     animationConfig = LineGraphDefaults.animationConfig(durationMillis = 1500),
 *     onPointClicked  = { label, value -> Log.d("Chart", "$label → $value") }
 * )
 * ```
 *
 * @param modifier Modifier applied to the outermost container.
 * @param chartData A **map** of label → value pairs that define the graph.
 *                  Insertion order is preserved and used as the X-axis order.
 * @param chartHeight The height of the drawing area (excluding axis labels).
 * @param lineConfig Visual configuration for the line stroke.
 *                   Default: [LineGraphDefaults.lineConfig].
 * @param areaFillConfig Controls the gradient fill below the line.
 *                       Default: [LineGraphDefaults.areaFillConfig].
 * @param pointConfig Controls the data-point dots.
 *                    Default: [LineGraphDefaults.pointConfig].
 * @param yAxisConfig Y-axis labels and axis line.
 *                    Default: [LineGraphDefaults.yAxisConfig].
 * @param xAxisConfig X-axis labels and axis line.
 *                    Default: [LineGraphDefaults.xAxisConfig].
 * @param gridLineStyle Horizontal dashed grid lines.
 *                      Default: [LineGraphDefaults.gridLineStyle].
 * @param popUpConfig Tooltip shown when a data point is tapped.
 *                    Default: [LineGraphDefaults.popUpConfig].
 * @param animationConfig Entrance animation timing.
 *                        Default: [LineGraphDefaults.animationConfig].
 * @param maxValue Optional ceiling for the Y-axis. When `null` the
 *                 maximum value in [chartData] is used.
 * @param enableGridLines Whether to draw horizontal grid lines.
 * @param maxTextLengthXAxis X-axis labels longer than this are truncated.
 * @param enableTextRotate Whether X-axis labels are rotated.
 * @param textRotateAngle Rotation angle in degrees (only when
 *                        [enableTextRotate] is `true`).
 * @param horizontalDrawPadding Horizontal space between the Y-axis
 *                              and the first / last data point.
 *                              Defaults to `0.dp` (line starts right
 *                              at the axis). Increase for visual
 *                              breathing room.
 * @param scrollEnabled When `true` the chart area becomes horizontally
 *                      scrollable — useful for large data sets.
 * @param minPointSpacing Minimum horizontal spacing between data points
 *                        when [scrollEnabled] is `true`. The chart will
 *                        expand beyond the viewport to honour this value.
 * @param autoShrinkXAxisLabels When `true`, X-axis label font size is
 *                              automatically reduced to fit within the
 *                              available width when there are many labels.
 * @param autoShrinkYAxisLabels When `true`, Y-axis label font size is
 *                              automatically reduced when there are many
 *                              scale steps.
 * @param liveUpdateConfig Configuration for real-time data updates with
 *                        blinking last-point indicator and smooth path
 *                        transitions. Default: [LineGraphDefaults.liveUpdateConfig].
 * @param customPointMarker Optional custom composable for rendering data point markers.
 *                          Receives label, value, and offset position.
 *                          When `null`, default circle markers are drawn.
 * @param customPopup Optional custom composable for the tooltip popup.
 *                    Receives label, value, and dismiss callback.
 *                    When `null`, default popup is shown.
 * @param onPointClicked Callback invoked when a data point is tapped,
 *                       receiving the label and raw value.
 *
 * @see LineGraphDefaults
 */
@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    chartData: Map<String, Float>,
    chartHeight: Dp = 200.dp,
    lineConfig: LineGraphLineConfig = LineGraphDefaults.lineConfig(),
    areaFillConfig: LineGraphAreaFillConfig = LineGraphDefaults.areaFillConfig(),
    pointConfig: LineGraphPointConfig = LineGraphDefaults.pointConfig(),
    yAxisConfig: LineGraphYAxisConfig = LineGraphDefaults.yAxisConfig(),
    xAxisConfig: LineGraphXAxisConfig = LineGraphDefaults.xAxisConfig(),
    gridLineStyle: LineGraphGridLineStyle = LineGraphDefaults.gridLineStyle(),
    popUpConfig: LineGraphPopUpConfig = LineGraphDefaults.popUpConfig(),
    animationConfig: LineGraphAnimationConfig = LineGraphDefaults.animationConfig(),
    liveUpdateConfig: LineGraphLiveUpdateConfig = LineGraphDefaults.liveUpdateConfig(),
    maxValue: Float? = null,
    enableGridLines: Boolean = true,
    maxTextLengthXAxis: Int = 6,
    enableTextRotate: Boolean = false,
    textRotateAngle: Float = -60f,
    horizontalDrawPadding: Dp = 0.dp,
    scrollEnabled: Boolean = false,
    minPointSpacing: Dp = 48.dp,
    autoShrinkXAxisLabels: Boolean = false,
    autoShrinkYAxisLabels: Boolean = false,
    customPointMarker: (@Composable (label: String, value: Float, offset: Offset) -> Unit)? = null,
    customPopup: (@Composable (label: String, value: Float, onDismiss: () -> Unit) -> Unit)? = null,
    onPointClicked: ((Pair<String, Float>) -> Unit)? = null,
) {
    // ── data preparation ─────────────────────────────────────────────
    val maxDataValue: Float = maxValue ?: chartData.values.maxOrNull() ?: 0f
    val entries: List<LineGraphEntry> = chartData.mapToLineGraphEntries(maxDataValue)

    if (entries.isEmpty()) return

    // ── animation state ──────────────────────────────────────────────
    var animationPlayed by remember { mutableStateOf(!animationConfig.enabled) }

    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationConfig.durationMillis,
            delayMillis = animationConfig.delayMillis,
            easing = LinearOutSlowInEasing
        ),
        label = "lineGraphAnimation"
    )

    if (animationConfig.enabled) {
        LaunchedEffect(Unit) { animationPlayed = true }
    }

    // ── live-update: animated data values ────────────────────────────
    // When liveUpdateConfig is enabled, each entry's normalizedY is
    // animated so the path smoothly transitions to new positions.
    val animatedNormalizedYValues = entries.map { entry ->
        if (liveUpdateConfig.enabled) {
            animateFloatAsState(
                targetValue = entry.normalizedY,
                animationSpec = tween(
                    durationMillis = liveUpdateConfig.pathTransitionDurationMillis,
                    easing = LinearOutSlowInEasing
                ),
                label = "liveY_${entry.label}"
            ).value
        } else {
            entry.normalizedY
        }
    }

    // ── live-update: blink animation ─────────────────────────────────
    val blinkRadius = if (liveUpdateConfig.enabled && liveUpdateConfig.blinkEnabled) {
        val infiniteTransition = rememberInfiniteTransition(label = "blinkTransition")
        infiniteTransition.animateFloat(
            initialValue = liveUpdateConfig.blinkMinRadius,
            targetValue = liveUpdateConfig.blinkMaxRadius,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = liveUpdateConfig.blinkDurationMillis,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "blinkRadius"
        ).value
    } else {
        0f
    }

    val blinkAlpha = if (liveUpdateConfig.enabled && liveUpdateConfig.blinkEnabled) {
        val infiniteTransition = rememberInfiniteTransition(label = "blinkAlphaTransition")
        infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = liveUpdateConfig.blinkDurationMillis,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "blinkAlpha"
        ).value
    } else {
        0f
    }

    // ── axis maths ───────────────────────────────────────────────────
    val yAxisScaleStep = if (yAxisConfig.axisScaleCount > 0) {
        maxDataValue / yAxisConfig.axisScaleCount
    } else maxDataValue

    val yAxisStepHeight = if (yAxisConfig.axisScaleCount > 0) {
        chartHeight / yAxisConfig.axisScaleCount
    } else chartHeight

    // ── tooltip state ────────────────────────────────────────────────
    var selectedPointIndex by remember { mutableIntStateOf(-1) }

    // ── density for Dp → Px ─────────────────────────────────────────
    val density = LocalDensity.current

    // ── horizontal scroll state ─────────────────────────────────────
    val scrollState = rememberScrollState()

    // ── Y-axis auto-shrink ──────────────────────────────────────────
    val effectiveYAxisConfig = if (autoShrinkYAxisLabels && yAxisConfig.axisScaleCount > 6) {
        val scaleFactor = (6f / yAxisConfig.axisScaleCount).coerceIn(0.5f, 1f)
        val baseFontSize = yAxisConfig.textStyle.fontSize
        val newFontSize = if (baseFontSize.isSp) {
            (baseFontSize.value * scaleFactor).sp
        } else baseFontSize
        yAxisConfig.copy(textStyle = yAxisConfig.textStyle.copy(fontSize = newFontSize))
    } else {
        yAxisConfig
    }

    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.Start) {

            // Y-axis scale
            if (effectiveYAxisConfig.isAxisScaleEnabled) {
                LineGraphYAxisScale(
                    yAxisConfig = effectiveYAxisConfig,
                    yAxisStepHeight = yAxisStepHeight,
                    yAxisScaleStep = yAxisScaleStep,
                    chartHeight = chartHeight,
                )
            }

            // Chart content + axes (fills remaining width)
            Column(modifier = Modifier.weight(1f)) {
                val scrollableContentWidth = if (scrollEnabled && entries.size > 1) {
                    minPointSpacing * (entries.size - 1) + horizontalDrawPadding * 2
                } else null

                val scrollContentModifier =
                    if (scrollEnabled && scrollableContentWidth != null) {
                        Modifier
                            .horizontalScroll(scrollState)
                            .width(scrollableContentWidth)
                    } else {
                        Modifier.fillMaxWidth()
                    }

                Column(modifier = scrollContentModifier) {

                    // Chart area: grid + canvas + tooltip + custom markers
                    BoxWithConstraints {
                        val containerWidth = maxWidth
                        // grid lines
                        if (enableGridLines) {
                            LineGraphYAxisGridLines(
                                gridLineStyle = gridLineStyle,
                                yAxisStepHeight = yAxisStepHeight,
                            )
                        }

                        // ── Canvas: line + area fill + points ────────
                        Canvas(
                            modifier = Modifier
                                .padding(top = yAxisStepHeight)
                                .fillMaxWidth()
                                .height(chartHeight)
                                .pointerInput(entries) {
                                    detectTapGestures { tapOffset ->
                                        val canvasWidth = size.width.toFloat()
                                        val canvasHeight = size.height.toFloat()
                                        val pointCount = entries.size

                                        if (pointCount < 2) return@detectTapGestures

                                        val horizontalPadding =
                                            with(density) { horizontalDrawPadding.toPx() }
                                        val drawableWidth =
                                            canvasWidth - 2 * horizontalPadding
                                        val stepX =
                                            drawableWidth / (pointCount - 1)
                                                .coerceAtLeast(1)

                                        val touchRadius =
                                            with(density) { 24.dp.toPx() }

                                        var closestIndex = -1
                                        var closestDist = Float.MAX_VALUE

                                        entries.forEachIndexed { index, entry ->
                                            val px =
                                                horizontalPadding + index * stepX
                                            val py =
                                                canvasHeight -
                                                        entry.normalizedY * canvasHeight
                                            val dist = kotlin.math.sqrt(
                                                (tapOffset.x - px) *
                                                        (tapOffset.x - px) +
                                                        (tapOffset.y - py) *
                                                        (tapOffset.y - py)
                                            )
                                            if (dist < closestDist) {
                                                closestDist = dist
                                                closestIndex = index
                                            }
                                        }

                                        if (closestDist <= touchRadius &&
                                            closestIndex >= 0
                                        ) {
                                            selectedPointIndex = closestIndex
                                            onPointClicked?.invoke(
                                                entries[closestIndex].label to
                                                        entries[closestIndex].value
                                            )
                                        } else {
                                            selectedPointIndex = -1
                                        }
                                    }
                                }
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val pointCount = entries.size

                            if (pointCount < 2) return@Canvas

                            val horizontalPadding = horizontalDrawPadding.toPx()
                            val drawableWidth =
                                canvasWidth - 2 * horizontalPadding
                            val stepX =
                                drawableWidth / (pointCount - 1)
                                    .coerceAtLeast(1)

                            // ── compute pixel positions ──────────────
                            val points =
                                entries.mapIndexed { index, _ ->
                                    val normalizedY = animatedNormalizedYValues[index]
                                    Offset(
                                        x = horizontalPadding + index * stepX,
                                        y = canvasHeight -
                                                normalizedY * canvasHeight
                                    )
                                }

                            // ── build paths (line + area) ────────────
                            val linePath = Path()
                            val areaPath = Path()

                            buildLineAndAreaPaths(
                                points = points,
                                linePath = linePath,
                                areaPath = areaPath,
                                canvasBottom = canvasHeight,
                                smoothCurve = lineConfig.smoothCurve,
                                curvature = lineConfig.curvature,
                                animationProgress = animationProgress
                            )

                            // ── draw area fill ───────────────────────
                            if (areaFillConfig.enabled) {
                                drawPath(
                                    path = areaPath,
                                    brush = areaFillConfig.brush,
                                    alpha = animationProgress
                                )
                            }

                            // ── draw line ────────────────────────────
                            drawPath(
                                path = linePath,
                                color = lineConfig.lineColor,
                                style = Stroke(
                                    width = lineConfig.lineWidth.toPx(),
                                    cap = lineConfig.strokeCap
                                ),
                                alpha = animationProgress
                            )

                            // ── draw data-point dots ─────────────────
                            if (pointConfig.enabled && customPointMarker == null) {
                                val visibleCount =
                                    (points.size * animationProgress).toInt()
                                        .coerceIn(0, points.size)

                                for (i in 0 until visibleCount) {
                                    val pt = points[i]

                                    drawCircle(
                                        color = pointConfig.borderColor,
                                        radius = (pointConfig.radius +
                                                pointConfig.borderWidth).toPx(),
                                        center = pt
                                    )
                                    drawCircle(
                                        color = pointConfig.color,
                                        radius = pointConfig.radius.toPx(),
                                        center = pt
                                    )
                                }
                            }

                            // ── highlight selected point ─────────────
                            if (selectedPointIndex in points.indices) {
                                val pt = points[selectedPointIndex]
                                drawCircle(
                                    color = lineConfig.lineColor
                                        .copy(alpha = 0.3f),
                                    radius = (pointConfig.radius +
                                            pointConfig.borderWidth +
                                            4.dp).toPx(),
                                    center = pt
                                )
                            }

                            // ── live-update: blinking last point ─────
                            if (liveUpdateConfig.enabled &&
                                liveUpdateConfig.blinkEnabled &&
                                points.isNotEmpty()
                            ) {
                                val lastPt = points.last()
                                val pulseColor = (liveUpdateConfig.blinkColor
                                    ?: lineConfig.lineColor).copy(alpha = blinkAlpha)
                                drawCircle(
                                    color = pulseColor,
                                    radius = blinkRadius,
                                    center = lastPt
                                )
                            }
                        }

                        // ── Custom point markers ──────────────────────
                        if (customPointMarker != null && pointConfig.enabled) {
                            val visibleCount =
                                (entries.size * animationProgress).toInt()
                                    .coerceIn(0, entries.size)

                            for (i in 0 until visibleCount) {
                                val entry = entries[i]
                                val pointCount = entries.size
                                val horizontalPaddingPx = with(density) {
                                    horizontalDrawPadding.toPx()
                                }
                                val canvasWidthPx = with(density) {
                                    containerWidth.toPx()
                                }
                                val drawableWidth = canvasWidthPx - 2 * horizontalPaddingPx
                                val stepX = drawableWidth / (pointCount - 1).coerceAtLeast(1)

                                val xPos = horizontalPaddingPx + i * stepX
                                val yPos = with(density) {
                                    chartHeight.toPx() - entry.normalizedY * chartHeight.toPx()
                                }

                                Box(
                                    modifier = Modifier
                                        .layout { measurable, constraints ->
                                            val placeable = measurable.measure(constraints)
                                            layout(placeable.width, placeable.height) {
                                                val x = (xPos - placeable.width / 2).toInt()
                                                val y = (yPos - placeable.height / 2 +
                                                        with(density) { yAxisStepHeight.toPx() }).toInt()
                                                placeable.placeRelative(x, y)
                                            }
                                        }
                                ) {
                                    customPointMarker(
                                        entry.label,
                                        entry.value,
                                        Offset(xPos, yPos)
                                    )
                                }
                            }
                        }

                        // ── Tooltip popup ─────────────────────────────
                        if (
                            selectedPointIndex in entries.indices &&
                            popUpConfig.enabled
                        ) {
                            val entry = entries[selectedPointIndex]

                            // Calculate popup offset to align with the selected point
                            val pCount = entries.size
                            val hPaddingPx = with(density) { horizontalDrawPadding.toPx() }
                            val cWidthPx = with(density) { containerWidth.toPx() }
                            val dWidth = cWidthPx - 2 * hPaddingPx
                            val sX = dWidth / (pCount - 1).coerceAtLeast(1)
                            val pointX = hPaddingPx + selectedPointIndex * sX

                            // Data point Y in canvas coords, then absolute Y
                            // from BoxWithConstraints top
                            val chartHeightPx = with(density) { chartHeight.toPx() }
                            val yAxisStepPx = with(density) { yAxisStepHeight.toPx() }
                            val pointYInCanvas = chartHeightPx - entry.normalizedY * chartHeightPx

                            if (customPopup != null) {
                                // Use layout-based positioning (same coordinate
                                // system as customPointMarker) so the popup
                                // tracks the data point reliably.
                                Box(
                                    modifier = Modifier
                                        .layout { measurable, constraints ->
                                            val placeable = measurable.measure(constraints)
                                            layout(placeable.width, placeable.height) {
                                                // Centre horizontally on pointX,
                                                // place bottom edge above the point
                                                val x = (pointX - placeable.width / 2)
                                                    .toInt()
                                                    .coerceIn(0, (cWidthPx - placeable.width).toInt().coerceAtLeast(0))
                                                val y = (pointYInCanvas - placeable.height + yAxisStepPx)
                                                    .toInt()
                                                    .coerceAtLeast(0)
                                                placeable.placeRelative(x, y)
                                            }
                                        }
                                ) {
                                    customPopup(
                                        entry.label,
                                        entry.value
                                    ) {
                                        selectedPointIndex = -1
                                    }
                                }
                            } else {
                                val rawPopupOffsetX = (pointX - cWidthPx / 2f).toInt()
                                val estimatedHalfWidthPx = with(density) { 80.dp.toPx() }
                                val maxAbsOffsetX = ((cWidthPx / 2f) - estimatedHalfWidthPx)
                                    .coerceAtLeast(0f).toInt()
                                val popupOffsetX = rawPopupOffsetX
                                    .coerceIn(-maxAbsOffsetX, maxAbsOffsetX)
                                val pointYFromTop = yAxisStepPx + pointYInCanvas
                                val popupMargin = with(density) { 48.dp.toPx() }
                                val popupOffsetY = (pointYFromTop - popupMargin).toInt().coerceAtLeast(0)

                                LineGraphPopup(
                                    popUpConfig = popUpConfig,
                                    text = "${entry.label}: ${
                                        formatDecimal(entry.value)
                                    }",
                                    offset = IntOffset(popupOffsetX, popupOffsetY),
                                    onDismissRequest = {
                                        selectedPointIndex = -1
                                    }
                                )
                            }
                        }
                    }

                    // ── X-axis line ──────────────────────────────────
                    if (xAxisConfig.isAxisLineEnabled) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(xAxisConfig.axisLineWidth)
                                .clip(xAxisConfig.axisLineShape)
                                .background(xAxisConfig.axisLineColor)
                        )
                    }

                    // ── X-axis labels ────────────────────────────────
                    if (xAxisConfig.isAxisScaleEnabled) {
                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        val labelTextStyle =
                            if (autoShrinkXAxisLabels &&
                                entries.size > 8
                            ) {
                                val scaleFactor =
                                    (8f / entries.size).coerceIn(0.5f, 1f)
                                val baseFontSize =
                                    xAxisConfig.textStyle.fontSize
                                val newFontSize =
                                    if (baseFontSize.isSp) {
                                        (baseFontSize.value * scaleFactor).sp
                                    } else baseFontSize
                                xAxisConfig.textStyle.copy(
                                    fontSize = newFontSize
                                )
                            } else {
                                xAxisConfig.textStyle
                            }

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            entries.forEachIndexed { index, entry ->
                                val displayText =
                                    if (entry.label.length >
                                        maxTextLengthXAxis
                                    ) {
                                        "${
                                            entry.label.take(
                                                if (maxTextLengthXAxis > 5)
                                                    (maxTextLengthXAxis - 2)
                                                else maxTextLengthXAxis
                                            )
                                        }.."
                                    } else {
                                        entry.label
                                    }

                                val labelModifier =
                                    if (enableTextRotate) {
                                        Modifier
                                            .graphicsLayer(
                                                rotationZ = textRotateAngle
                                            )
                                            .clickable {
                                                onPointClicked?.invoke(
                                                    entry.label to
                                                            entry.value
                                                )
                                            }
                                            .padding(horizontal = 2.dp)
                                    } else {
                                        Modifier.clickable {
                                            onPointClicked?.invoke(
                                                entry.label to entry.value
                                            )
                                        }
                                    }

                                // Position label using same calculation as line points
                                val pointCount = entries.size
                                val fraction = if (pointCount > 1) {
                                    index.toFloat() / (pointCount - 1)
                                } else 0.5f

                                Text(
                                    text = displayText,
                                    style = labelTextStyle,
                                    modifier = labelModifier
                                        .align(Alignment.TopStart)
                                        .layout { measurable, constraints ->
                                            val placeable =
                                                measurable.measure(constraints)
                                            layout(placeable.width, placeable.height) {
                                                val horizontalPaddingPx =
                                                    horizontalDrawPadding.toPx()
                                                val availableWidth =
                                                    constraints.maxWidth.toFloat()
                                                val drawableWidth =
                                                    availableWidth - 2 * horizontalPaddingPx
                                                val xPos =
                                                    horizontalPaddingPx +
                                                            fraction * drawableWidth -
                                                            placeable.width / 2
                                                placeable.placeRelative(
                                                    x = xPos.toInt(),
                                                    y = 0
                                                )
                                            }
                                        },
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  Private helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

// ━━━━━━━━━━━━━━━━━━━━━  Formatting helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Formats a decimal number to at most 2 decimal places, removing trailing zeros.
 * Pure Kotlin implementation for Multiplatform compatibility.
 */
private fun formatDecimal(value: Float): String {
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

// ━━━━━━━━━━━━━━━━━━━━━  Private helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Constructs a line path and a closed area path from [points].
 *
 * When [smoothCurve] is `true` the points are connected via cubic
 * Bézier curves whose strength is controlled by [curvature]
 * (mirroring Vico's `CubicPointConnector` algorithm). Otherwise
 * straight segments are used.
 *
 * The [animationProgress] (`0f..1f`) controls how much of the
 * path is drawn — `0` means nothing is visible, `1` means the
 * entire line is drawn.
 */
private fun buildLineAndAreaPaths(
    points: List<Offset>,
    linePath: Path,
    areaPath: Path,
    canvasBottom: Float,
    smoothCurve: Boolean,
    curvature: Float,
    animationProgress: Float,
) {
    if (points.isEmpty()) return

    // Determine how many segments to draw based on animation progress.
    // Fractional parts are interpolated so the line grows smoothly.
    val totalSegments = points.size - 1
    val progressSegments = animationProgress * totalSegments
    val fullSegments = progressSegments.toInt().coerceIn(0, totalSegments)
    val partialFraction = progressSegments - fullSegments

    linePath.moveTo(points[0].x, points[0].y)
    areaPath.moveTo(points[0].x, canvasBottom) // start at bottom-left of first point X
    areaPath.lineTo(points[0].x, points[0].y)

    for (i in 0 until fullSegments) {
        connectPoints(
            path = linePath,
            areaPath = areaPath,
            from = points[i],
            to = points[i + 1],
            smoothCurve = smoothCurve,
            curvature = curvature,
            fraction = 1f
        )
    }

    // partial last segment (animated)
    if (fullSegments < totalSegments && partialFraction > 0f) {
        connectPoints(
            path = linePath,
            areaPath = areaPath,
            from = points[fullSegments],
            to = points[fullSegments + 1],
            smoothCurve = smoothCurve,
            curvature = curvature,
            fraction = partialFraction
        )
    }

    // Close the area path at the bottom
    // Interpolate the endpoint X position for the partial segment
    val endX = if (partialFraction > 0f && fullSegments < totalSegments) {
        lerp(points[fullSegments].x, points[fullSegments + 1].x, partialFraction)
    } else {
        points[fullSegments.coerceIn(points.indices)].x
    }

    areaPath.lineTo(endX, canvasBottom)
    areaPath.close()
}

/**
 * Connects two consecutive points in the line path, optionally
 * also extending the area path.
 *
 * For smooth curves the control-point placement mirrors Vico's
 * `CubicPointConnector`:
 *
 * ```
 * cp1 = (from.x + dx * curvature, from.y)
 * cp2 = (to.x   - dx * curvature, to.y  )
 * ```
 *
 * where `dx = |to.x - from.x|`. This produces aesthetically
 * pleasing horizontal-tension curves.
 *
 * The [fraction] parameter (`0f..1f`) allows drawing only a
 * portion of the segment, used for the entrance animation.
 */
private fun connectPoints(
    path: Path,
    areaPath: Path,
    from: Offset,
    to: Offset,
    smoothCurve: Boolean,
    curvature: Float,
    fraction: Float,
) {
    // interpolated endpoint
    val targetX = lerp(from.x, to.x, fraction)
    val targetY = lerp(from.y, to.y, fraction)

    if (smoothCurve) {
        val dx = abs(to.x - from.x)

        // Control points: horizontal-tension Bézier
        val cp1x = from.x + dx * curvature
        val cp1y = from.y
        val cp2x = to.x - dx * curvature
        val cp2y = to.y

        // Interpolate control points along fraction
        val iCp1x = lerp(from.x, cp1x, fraction)
        val iCp1y = lerp(from.y, cp1y, fraction)
        val iCp2x = lerp(from.x, cp2x, fraction)
        val iCp2y = lerp(from.y, cp2y, fraction)

        path.cubicTo(iCp1x, iCp1y, iCp2x, iCp2y, targetX, targetY)
        areaPath.cubicTo(iCp1x, iCp1y, iCp2x, iCp2y, targetX, targetY)
    } else {
        path.lineTo(targetX, targetY)
        areaPath.lineTo(targetX, targetY)
    }
}

/** Simple linear interpolation between [a] and [b] at [t]. */
private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
