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

package com.developerstring.jetco.ui.charts.linegraph

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.charts.linegraph.components.LineGraphPopup
import com.developerstring.jetco.ui.charts.linegraph.components.LineGraphYAxisGridLines
import com.developerstring.jetco.ui.charts.linegraph.components.LineGraphYAxisScale
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphAnimationConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphDefaults
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphGridLineStyle
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphLineConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphLiveUpdateConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphPointConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphPopUpConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphXAxisConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphYAxisConfig
import com.developerstring.jetco.ui.charts.linegraph.model.LineGraphSeries
import kotlin.math.abs
import kotlin.math.round

/**
 * A multi-series animated line graph composable for Jetpack Compose.
 *
 * Draws multiple data series on the same Cartesian plane, each with
 * its own color. Area fill gradients are automatically derived from
 * the series color. Supports smooth cubic Bézier curves, data-point
 * dots, grid lines, axes, tooltips, and an optional legend.
 *
 * ## Quick start
 * ```kotlin
 * MultiLineGraph(
 *     labels = listOf("Jan", "Feb", "Mar", "Apr"),
 *     seriesList = listOf(
 *         LineGraphSeries("Revenue", listOf(100f, 200f, 150f, 300f), Color.Blue),
 *         LineGraphSeries("Expense", listOf(80f,  120f, 90f,  200f), Color.Red),
 *     ),
 * )
 * ```
 *
 * @param modifier Modifier applied to the outermost container.
 * @param labels Ordered X-axis labels. Every series must have
 *               the same number of values as [labels].
 * @param seriesList List of [LineGraphSeries] to render.
 * @param chartHeight Height of the drawing area (excludes axes).
 * @param lineConfig Shared line styling (width, cap, curve mode).
 *                   The **colour is overridden** per series.
 * @param pointConfig Shared dot styling. The **border colour is
 *                    overridden** per series.
 * @param enableAreaFill Whether a gradient area fill is drawn below
 *                       each line.
 * @param yAxisConfig Y-axis label configuration.
 * @param xAxisConfig X-axis label configuration.
 * @param gridLineStyle Horizontal grid lines.
 * @param popUpConfig Tooltip configuration.
 * @param animationConfig Entrance animation timing.
 * @param maxValue Optional Y-axis ceiling. Defaults to the global
 *                 maximum across all series.
 * @param enableGridLines Whether to draw grid lines.
 * @param enableLegend Whether to render a colour-coded legend below
 *                     the chart.
 * @param maxTextLengthXAxis Labels longer than this are truncated.
 * @param horizontalDrawPadding Horizontal space between the Y-axis
 *                              and the first / last data point.
 *                              Defaults to `0.dp` (line starts right
 *                              at the axis).
 * @param scrollEnabled When `true` the chart area becomes horizontally
 *                      scrollable — useful for large data sets.
 * @param minPointSpacing Minimum horizontal spacing between data points
 *                        when [scrollEnabled] is `true`.
 * @param autoShrinkXAxisLabels When `true`, X-axis label font size is
 *                              automatically reduced to fit when many
 *                              labels are present.
 * @param autoShrinkYAxisLabels When `true`, Y-axis label font size is
 *                              automatically reduced when there are many
 *                              scale steps.
 * @param customPointMarker Optional custom composable for rendering data point markers.
 *                          Receives series name, label, value, and offset position.
 *                          When `null`, default circle markers are drawn.
 * @param customPopup Optional custom composable for the tooltip popup.
 *                    Receives series name, label, value, and dismiss callback.
 *                    When `null`, default popup is shown.
 * @param onPointClicked Callback receiving (seriesName, label, value)
 *                       when a data point is tapped.
 *
 * @see LineGraphDefaults
 * @see LineGraphSeries
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MultiLineGraph(
    modifier: Modifier = Modifier,
    labels: List<String>,
    seriesList: List<LineGraphSeries>,
    chartHeight: Dp = 200.dp,
    lineConfig: LineGraphLineConfig = LineGraphDefaults.lineConfig(),
    pointConfig: LineGraphPointConfig = LineGraphDefaults.pointConfig(),
    enableAreaFill: Boolean = true,
    yAxisConfig: LineGraphYAxisConfig = LineGraphDefaults.yAxisConfig(),
    xAxisConfig: LineGraphXAxisConfig = LineGraphDefaults.xAxisConfig(),
    gridLineStyle: LineGraphGridLineStyle = LineGraphDefaults.gridLineStyle(),
    popUpConfig: LineGraphPopUpConfig = LineGraphDefaults.popUpConfig(),
    animationConfig: LineGraphAnimationConfig = LineGraphDefaults.animationConfig(),
    liveUpdateConfig: LineGraphLiveUpdateConfig = LineGraphDefaults.liveUpdateConfig(),
    maxValue: Float? = null,
    enableGridLines: Boolean = true,
    enableLegend: Boolean = true,
    maxTextLengthXAxis: Int = 6,
    horizontalDrawPadding: Dp = 0.dp,
    scrollEnabled: Boolean = false,
    minPointSpacing: Dp = 48.dp,
    autoShrinkXAxisLabels: Boolean = false,
    autoShrinkYAxisLabels: Boolean = false,
    customPointMarker: (@Composable (seriesName: String, label: String, value: Float, offset: Offset, color: Color) -> Unit)? = null,
    customPopup: (@Composable (seriesName: String, label: String, value: Float, onDismiss: () -> Unit) -> Unit)? = null,
    onPointClicked: ((seriesName: String, label: String, value: Float) -> Unit)? = null,
) {
    if (seriesList.isEmpty() || labels.isEmpty()) return

    // ── data preparation ─────────────────────────────────────────────
    val globalMax = maxValue
        ?: seriesList.maxOfOrNull { it.data.maxOrNull() ?: 0f }
        ?: 0f

    // Normalize every series independently using the shared globalMax
    data class NormalizedPoint(val label: String, val value: Float, val normalizedY: Float)

    val normalizedSeries: List<List<NormalizedPoint>> = seriesList.map { series ->
        series.data.mapIndexed { index, value ->
            NormalizedPoint(
                label = labels.getOrElse(index) { "" },
                value = value,
                normalizedY = if (globalMax != 0f) value / globalMax else 0f
            )
        }
    }

    // ── animation ────────────────────────────────────────────────────
    var animationPlayed by remember { mutableStateOf(!animationConfig.enabled) }

    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationConfig.durationMillis,
            delayMillis = animationConfig.delayMillis,
            easing = LinearOutSlowInEasing
        ),
        label = "multiLineGraphAnimation"
    )

    if (animationConfig.enabled) {
        LaunchedEffect(Unit) { animationPlayed = true }
    }

    // ── live-update: animated normalizedY per series ──────────────────
    val animatedNormalizedSeries: List<List<Float>> = normalizedSeries.map { series ->
        series.map { pt ->
            if (liveUpdateConfig.enabled) {
                animateFloatAsState(
                    targetValue = pt.normalizedY,
                    animationSpec = tween(
                        durationMillis = liveUpdateConfig.pathTransitionDurationMillis,
                        easing = LinearOutSlowInEasing
                    ),
                    label = "liveMultiY"
                ).value
            } else {
                pt.normalizedY
            }
        }
    }

    // ── live-update: blink animation ─────────────────────────────────
    val blinkRadius = if (liveUpdateConfig.enabled && liveUpdateConfig.blinkEnabled) {
        val infiniteTransition = rememberInfiniteTransition(label = "multiBlinkTransition")
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
            label = "multiBlinkRadius"
        ).value
    } else {
        0f
    }

    val blinkAlpha = if (liveUpdateConfig.enabled && liveUpdateConfig.blinkEnabled) {
        val infiniteTransition = rememberInfiniteTransition(label = "multiBlinkAlphaTransition")
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
            label = "multiBlinkAlpha"
        ).value
    } else {
        0f
    }

    // ── axis maths ───────────────────────────────────────────────────
    val yAxisScaleStep = if (yAxisConfig.axisScaleCount > 0) {
        globalMax / yAxisConfig.axisScaleCount
    } else globalMax

    val yAxisStepHeight = if (yAxisConfig.axisScaleCount > 0) {
        chartHeight / yAxisConfig.axisScaleCount
    } else chartHeight

    // ── tooltip state ────────────────────────────────────────────────
    var selectedSeriesIndex by remember { mutableIntStateOf(-1) }
    var selectedPointIndex by remember { mutableIntStateOf(-1) }

    val density = LocalDensity.current

    // horizontal scroll state
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
                val scrollableContentWidth =
                    if (scrollEnabled && labels.size > 1) {
                        minPointSpacing * (labels.size - 1) +
                                horizontalDrawPadding * 2
                    } else null

                val scrollContentModifier =
                    if (scrollEnabled &&
                        scrollableContentWidth != null
                    ) {
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
                        if (enableGridLines) {
                            LineGraphYAxisGridLines(
                                gridLineStyle = gridLineStyle,
                                yAxisStepHeight = yAxisStepHeight,
                            )
                        }

                        Canvas(
                            modifier = Modifier
                                .padding(top = yAxisStepHeight)
                                .fillMaxWidth()
                                .height(chartHeight)
                                .pointerInput(normalizedSeries) {
                                    detectTapGestures { tapOffset ->
                                        val canvasWidth =
                                            size.width.toFloat()
                                        val canvasHeight =
                                            size.height.toFloat()
                                        val pointCount = labels.size
                                        if (pointCount < 2)
                                            return@detectTapGestures

                                        val horizontalPadding =
                                            with(density) {
                                                horizontalDrawPadding.toPx()
                                            }
                                        val drawableWidth =
                                            canvasWidth -
                                                    2 * horizontalPadding
                                        val stepX =
                                            drawableWidth /
                                                    (pointCount - 1)
                                                        .coerceAtLeast(1)
                                        val touchRadius =
                                            with(density) { 24.dp.toPx() }

                                        var bestSeries = -1
                                        var bestPoint = -1
                                        var bestDist = Float.MAX_VALUE

                                        normalizedSeries.forEachIndexed {
                                                sIdx, series ->
                                            series.forEachIndexed {
                                                    pIdx, pt ->
                                                val px =
                                                    horizontalPadding +
                                                            pIdx * stepX
                                                val py =
                                                    canvasHeight -
                                                            pt.normalizedY *
                                                            canvasHeight
                                                val dist =
                                                    kotlin.math.sqrt(
                                                        (tapOffset.x - px) *
                                                                (tapOffset.x - px) +
                                                                (tapOffset.y - py) *
                                                                (tapOffset.y - py)
                                                    )
                                                if (dist < bestDist) {
                                                    bestDist = dist
                                                    bestSeries = sIdx
                                                    bestPoint = pIdx
                                                }
                                            }
                                        }

                                        if (bestDist <= touchRadius &&
                                            bestSeries >= 0
                                        ) {
                                            selectedSeriesIndex = bestSeries
                                            selectedPointIndex = bestPoint
                                            val pt = normalizedSeries[bestSeries][bestPoint]
                                            onPointClicked?.invoke(
                                                seriesList[bestSeries].name,
                                                pt.label,
                                                pt.value
                                            )
                                        } else {
                                            selectedSeriesIndex = -1
                                            selectedPointIndex = -1
                                        }
                                    }
                                }
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val pointCount = labels.size

                            if (pointCount < 2) return@Canvas

                            val horizontalPadding =
                                horizontalDrawPadding.toPx()
                            val drawableWidth =
                                canvasWidth - 2 * horizontalPadding
                            val stepX =
                                drawableWidth / (pointCount - 1)
                                    .coerceAtLeast(1)

                            normalizedSeries.forEachIndexed {
                                    seriesIndex, series ->
                                val seriesColor =
                                    seriesList[seriesIndex].color

                                val points =
                                    series.mapIndexed { index, pt ->
                                        val animY = animatedNormalizedSeries[seriesIndex][index]
                                        Offset(
                                            x = horizontalPadding +
                                                    index * stepX,
                                            y = canvasHeight -
                                                    animY *
                                                    canvasHeight
                                        )
                                    }

                                val linePath = Path()
                                val areaPath = Path()

                                buildMultiLinePaths(
                                    points = points,
                                    linePath = linePath,
                                    areaPath = areaPath,
                                    canvasBottom = canvasHeight,
                                    smoothCurve = lineConfig.smoothCurve,
                                    curvature = lineConfig.curvature,
                                    animationProgress = animationProgress
                                )

                                // Area fill
                                if (enableAreaFill) {
                                    drawPath(
                                        path = areaPath,
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                seriesColor.copy(
                                                    alpha = 0.3f
                                                ),
                                                Color.Transparent
                                            )
                                        ),
                                        alpha = animationProgress
                                    )
                                }

                                // Line
                                drawPath(
                                    path = linePath,
                                    color = seriesColor,
                                    style = Stroke(
                                        width =
                                            lineConfig.lineWidth.toPx(),
                                        cap = lineConfig.strokeCap
                                    ),
                                    alpha = animationProgress
                                )

                                // Dots
                                if (pointConfig.enabled && customPointMarker == null) {
                                    val visibleCount =
                                        (points.size *
                                                animationProgress)
                                            .toInt()
                                            .coerceIn(0, points.size)

                                    for (i in 0 until visibleCount) {
                                        val pt = points[i]
                                        drawCircle(
                                            color = seriesColor,
                                            radius = (pointConfig.radius +
                                                    pointConfig.borderWidth)
                                                .toPx(),
                                            center = pt
                                        )
                                        drawCircle(
                                            color = pointConfig.color,
                                            radius =
                                                pointConfig.radius.toPx(),
                                            center = pt
                                        )
                                    }
                                }

                                // Selection highlight
                                if (selectedSeriesIndex == seriesIndex &&
                                    selectedPointIndex in points.indices
                                ) {
                                    val pt = points[selectedPointIndex]
                                    drawCircle(
                                        color = seriesColor.copy(
                                            alpha = 0.3f
                                        ),
                                        radius = (pointConfig.radius +
                                                pointConfig.borderWidth +
                                                4.dp).toPx(),
                                        center = pt
                                    )
                                }

                                // ── live-update: blinking last point ─
                                if (liveUpdateConfig.enabled &&
                                    liveUpdateConfig.blinkEnabled &&
                                    points.isNotEmpty()
                                ) {
                                    val lastPt = points.last()
                                    val pulseColor = (liveUpdateConfig.blinkColor
                                        ?: seriesColor).copy(alpha = blinkAlpha)
                                    drawCircle(
                                        color = pulseColor,
                                        radius = blinkRadius,
                                        center = lastPt
                                    )
                                }
                            }
                        }

                        // Custom point markers
                        if (customPointMarker != null && pointConfig.enabled) {
                            
                            normalizedSeries.forEachIndexed { seriesIndex, series ->
                                val visibleCount =
                                    (series.size * animationProgress).toInt()
                                        .coerceIn(0, series.size)

                                for (i in 0 until visibleCount) {
                                    val pt = series[i]
                                    val pointCount = labels.size
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
                                        chartHeight.toPx() - pt.normalizedY * chartHeight.toPx()
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
                                            seriesList[seriesIndex].name,
                                            pt.label,
                                            pt.value,
                                            Offset(xPos, yPos),
                                            seriesList[seriesIndex].color
                                        )
                                    }
                                }
                            }
                        }

                        // Tooltip
                        if (
                            selectedSeriesIndex in seriesList.indices &&
                            selectedPointIndex in
                            normalizedSeries[selectedSeriesIndex].indices &&
                            popUpConfig.enabled
                        ) {
                            val pt = normalizedSeries[selectedSeriesIndex][selectedPointIndex]

                            // Calculate popup offset to align with the selected point
                            val pCount = labels.size
                            val hPaddingPx = with(density) { horizontalDrawPadding.toPx() }
                            val cWidthPx = with(density) { containerWidth.toPx() }
                            val dWidth = cWidthPx - 2 * hPaddingPx
                            val sX = dWidth / (pCount - 1).coerceAtLeast(1)
                            val pointX = hPaddingPx + selectedPointIndex * sX

                            // Data point Y in canvas coords, then absolute Y
                            // from BoxWithConstraints top
                            val chartHeightPx = with(density) { chartHeight.toPx() }
                            val yAxisStepPx = with(density) { yAxisStepHeight.toPx() }
                            val pointYInCanvas = chartHeightPx - pt.normalizedY * chartHeightPx

                            if (customPopup != null) {
                                // Use layout-based positioning (same coordinate
                                // system as customPointMarker) so the popup
                                // tracks the data point reliably.
                                Box(
                                    modifier = Modifier
                                        .layout { measurable, constraints ->
                                            val placeable = measurable.measure(constraints)
                                            layout(placeable.width, placeable.height) {
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
                                        seriesList[selectedSeriesIndex].name,
                                        pt.label,
                                        pt.value
                                    ) {
                                        selectedSeriesIndex = -1
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
                                    text = "${
                                        seriesList[selectedSeriesIndex].name
                                    }: ${formatDecimal(pt.value)}",
                                    offset = IntOffset(popupOffsetX, popupOffsetY),
                                    onDismissRequest = {
                                        selectedSeriesIndex = -1
                                        selectedPointIndex = -1
                                    }
                                )
                            }
                        }
                    }

                    // ── X-axis line ──────────────────────────────
                    if (xAxisConfig.isAxisLineEnabled) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(xAxisConfig.axisLineWidth)
                                .clip(xAxisConfig.axisLineShape)
                                .background(xAxisConfig.axisLineColor)
                        )
                    }

                    // ── X-axis labels ────────────────────────────
                    if (xAxisConfig.isAxisScaleEnabled) {
                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        val labelTextStyle =
                            if (autoShrinkXAxisLabels &&
                                labels.size > 8
                            ) {
                                val scaleFactor =
                                    (8f / labels.size)
                                        .coerceIn(0.5f, 1f)
                                val baseFontSize =
                                    xAxisConfig.textStyle.fontSize
                                val newFontSize =
                                    if (baseFontSize.isSp) {
                                        (baseFontSize.value *
                                                scaleFactor).sp
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
                            labels.forEachIndexed { index, label ->
                                val displayText =
                                    if (label.length >
                                        maxTextLengthXAxis
                                    ) {
                                        "${
                                            label.take(
                                                if (maxTextLengthXAxis > 5)
                                                    (maxTextLengthXAxis - 2)
                                                else maxTextLengthXAxis
                                            )
                                        }.."
                                    } else label

                                // Position label using same calculation as line points
                                val pointCount = labels.size
                                val fraction = if (pointCount > 1) {
                                    index.toFloat() / (pointCount - 1)
                                } else 0.5f

                                Text(
                                    text = displayText,
                                    style = labelTextStyle,
                                    modifier = Modifier
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

        // Legend
        if (enableLegend) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                seriesList.forEach { series ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(series.color)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = series.name,
                            style = xAxisConfig.textStyle
                        )
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
    val rounded = round(value * 100) / 100
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString().removeSuffix("0").removeSuffix(".")
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  Private helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * Builds line and area paths for a single series in the multi-line
 * graph. The algorithm mirrors [buildLineAndAreaPaths] in the
 * single-series [LineGraph].
 */
private fun buildMultiLinePaths(
    points: List<Offset>,
    linePath: Path,
    areaPath: Path,
    canvasBottom: Float,
    smoothCurve: Boolean,
    curvature: Float,
    animationProgress: Float,
) {
    if (points.isEmpty()) return

    val totalSegments = points.size - 1
    val progressSegments = animationProgress * totalSegments
    val fullSegments = progressSegments.toInt().coerceIn(0, totalSegments)
    val partialFraction = progressSegments - fullSegments

    linePath.moveTo(points[0].x, points[0].y)
    areaPath.moveTo(points[0].x, canvasBottom)
    areaPath.lineTo(points[0].x, points[0].y)

    for (i in 0 until fullSegments) {
        multiConnectPoints(
            path = linePath,
            areaPath = areaPath,
            from = points[i],
            to = points[i + 1],
            smoothCurve = smoothCurve,
            curvature = curvature,
            fraction = 1f
        )
    }

    if (fullSegments < totalSegments && partialFraction > 0f) {
        multiConnectPoints(
            path = linePath,
            areaPath = areaPath,
            from = points[fullSegments],
            to = points[fullSegments + 1],
            smoothCurve = smoothCurve,
            curvature = curvature,
            fraction = partialFraction
        )
    }

    val endX = if (partialFraction > 0f && fullSegments < totalSegments) {
        multiLerp(points[fullSegments].x, points[fullSegments + 1].x, partialFraction)
    } else {
        points[fullSegments.coerceIn(points.indices)].x
    }

    areaPath.lineTo(endX, canvasBottom)
    areaPath.close()
}

private fun multiConnectPoints(
    path: Path,
    areaPath: Path,
    from: Offset,
    to: Offset,
    smoothCurve: Boolean,
    curvature: Float,
    fraction: Float,
) {
    val targetX = multiLerp(from.x, to.x, fraction)
    val targetY = multiLerp(from.y, to.y, fraction)

    if (smoothCurve) {
        val dx = abs(to.x - from.x)
        val cp1x = from.x + dx * curvature
        val cp1y = from.y
        val cp2x = to.x - dx * curvature
        val cp2y = to.y

        val iCp1x = multiLerp(from.x, cp1x, fraction)
        val iCp1y = multiLerp(from.y, cp1y, fraction)
        val iCp2x = multiLerp(from.x, cp2x, fraction)
        val iCp2y = multiLerp(from.y, cp2y, fraction)

        path.cubicTo(iCp1x, iCp1y, iCp2x, iCp2y, targetX, targetY)
        areaPath.cubicTo(iCp1x, iCp1y, iCp2x, iCp2y, targetX, targetY)
    } else {
        path.lineTo(targetX, targetY)
        areaPath.lineTo(targetX, targetY)
    }
}

private fun multiLerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
