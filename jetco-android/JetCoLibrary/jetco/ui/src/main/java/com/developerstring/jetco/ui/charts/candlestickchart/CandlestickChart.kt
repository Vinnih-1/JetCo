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

package com.developerstring.jetco.ui.charts.candlestickchart

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.charts.candlestickchart.components.CandlestickMarkerPopup
import com.developerstring.jetco.ui.charts.candlestickchart.components.CandlestickYAxisGridLines
import com.developerstring.jetco.ui.charts.candlestickchart.components.CandlestickYAxisScale
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickAnimationConfig
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickCandleConfig
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickCrosshairConfig
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickDefaults
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickGridLineStyle
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickMarkerConfig
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickVolumeConfig
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickXAxisConfig
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickYAxisConfig
import com.developerstring.jetco.ui.charts.candlestickchart.model.CandleChange
import com.developerstring.jetco.ui.charts.candlestickchart.model.CandlestickEntry
import kotlin.math.abs

/**
 * A fully customisable candlestick (OHLC) chart composable for
 * Jetpack Compose, inspired by the Vico library.
 *
 * Renders each data point as a candle with a body (open→close) and
 * wicks (high/low). Supports:
 *
 * - **Bullish / Bearish / Neutral** colour coding
 * - **Hollow** bullish candles
 * - **Volume bars** below the candles
 * - **Crosshair** indicator on selection
 * - **OHLC marker tooltip** (default or custom)
 * - **Grid lines** and **axis labels**
 * - **Entrance animation** (fade-in + vertical stretch)
 * - **Horizontal scrolling** for large data sets
 *
 * ## Quick start
 * ```kotlin
 * CandlestickChart(
 *     data = listOf(
 *         CandlestickEntry("Mon", 100f, 110f, 95f, 105f),
 *         CandlestickEntry("Tue", 105f, 115f, 100f,  98f),
 *     ),
 * )
 * ```
 *
 * ## Full customisation
 * ```kotlin
 * CandlestickChart(
 *     modifier       = Modifier.fillMaxWidth().padding(16.dp),
 *     data           = entries,
 *     chartHeight    = 300.dp,
 *     candleConfig   = CandlestickDefaults.candleConfig(hollowBullish = true),
 *     volumeConfig   = CandlestickDefaults.volumeConfig(heightRatio = 0.25f),
 *     crosshairConfig= CandlestickDefaults.crosshairConfig(),
 *     markerConfig   = CandlestickDefaults.markerConfig(),
 *     yAxisConfig    = CandlestickDefaults.yAxisConfig(textPrefix = "$"),
 *     onCandleClicked = { entry -> Log.d("Chart", entry.toString()) },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outermost container.
 * @param data Ordered list of [CandlestickEntry] values. The insertion
 *             order determines the X-axis position.
 * @param chartHeight Height of the candle area (excluding volume and axes).
 * @param candleConfig Visual configuration for candle bodies and wicks.
 * @param volumeConfig Volume-bar overlay configuration.
 * @param crosshairConfig Crosshair indicator configuration.
 * @param markerConfig OHLC marker tooltip configuration.
 * @param yAxisConfig Y-axis labels and line.
 * @param xAxisConfig X-axis labels and line.
 * @param gridLineStyle Horizontal grid lines.
 * @param animationConfig Entrance animation timing.
 * @param minValue Optional Y-axis floor. When `null` the global low is used.
 * @param maxValue Optional Y-axis ceiling. When `null` the global high is used.
 * @param enableGridLines Whether to draw grid lines.
 * @param maxTextLengthXAxis Labels longer than this are truncated.
 * @param horizontalDrawPadding Space between the Y-axis and the first / last candle.
 * @param scrollEnabled Enables horizontal scrolling for large data sets.
 * @param minCandleSpacing Minimum horizontal gap between candle centres
 *                         when [scrollEnabled] is `true`.
 * @param autoShrinkXAxisLabels Auto-reduce font size for many X labels.
 * @param autoShrinkYAxisLabels Auto-reduce font size for many Y labels.
 * @param customMarker Optional custom composable for the marker tooltip.
 * @param onCandleClicked Callback invoked when a candle is tapped.
 *
 * @see CandlestickDefaults
 * @see CandlestickEntry
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CandlestickChart(
    modifier: Modifier = Modifier,
    data: List<CandlestickEntry>,
    chartHeight: Dp = 250.dp,
    candleConfig: CandlestickCandleConfig = CandlestickDefaults.candleConfig(),
    volumeConfig: CandlestickVolumeConfig = CandlestickDefaults.volumeConfig(),
    crosshairConfig: CandlestickCrosshairConfig = CandlestickDefaults.crosshairConfig(),
    markerConfig: CandlestickMarkerConfig = CandlestickDefaults.markerConfig(),
    yAxisConfig: CandlestickYAxisConfig = CandlestickDefaults.yAxisConfig(),
    xAxisConfig: CandlestickXAxisConfig = CandlestickDefaults.xAxisConfig(),
    gridLineStyle: CandlestickGridLineStyle = CandlestickDefaults.gridLineStyle(),
    animationConfig: CandlestickAnimationConfig = CandlestickDefaults.animationConfig(),
    minValue: Float? = null,
    maxValue: Float? = null,
    enableGridLines: Boolean = true,
    maxTextLengthXAxis: Int = 6,
    horizontalDrawPadding: Dp = 16.dp,
    scrollEnabled: Boolean = false,
    minCandleSpacing: Dp = 16.dp,
    autoShrinkXAxisLabels: Boolean = false,
    autoShrinkYAxisLabels: Boolean = false,
    customMarker: (@Composable (entry: CandlestickEntry, onDismiss: () -> Unit) -> Unit)? = null,
    onCandleClicked: ((CandlestickEntry) -> Unit)? = null,
) {
    if (data.isEmpty()) return

    // ── price range ──────────────────────────────────────────────────
    val globalLow = minValue ?: data.minOf { it.low }
    val globalHigh = maxValue ?: data.maxOf { it.high }
    // Add 5% padding to the price range to avoid candles touching edges
    val priceRange = (globalHigh - globalLow).coerceAtLeast(0.01f)
    val paddedLow = globalLow - priceRange * 0.05f
    val paddedHigh = globalHigh + priceRange * 0.05f
    val paddedRange = paddedHigh - paddedLow

    // ── volume range ─────────────────────────────────────────────────
    val maxVolume = if (volumeConfig.enabled) {
        data.maxOf { it.volume }.coerceAtLeast(1f)
    } else 1f

    // ── animation ────────────────────────────────────────────────────
    var animationPlayed by remember { mutableStateOf(!animationConfig.enabled) }

    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationConfig.durationMillis,
            delayMillis = animationConfig.delayMillis,
            easing = LinearOutSlowInEasing
        ),
        label = "candlestickChartAnimation"
    )

    if (animationConfig.enabled) {
        LaunchedEffect(Unit) { animationPlayed = true }
    }

    // ── axis maths ───────────────────────────────────────────────────
    val yAxisStepHeight = if (yAxisConfig.axisScaleCount > 0) {
        chartHeight / yAxisConfig.axisScaleCount
    } else chartHeight

    // ── selection state ──────────────────────────────────────────────
    var selectedIndex by remember { mutableIntStateOf(-1) }

    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    // Total chart height: price area (= chartHeight) + volume area below
    val volumeHeight = if (volumeConfig.enabled) {
        chartHeight * volumeConfig.heightRatio
    } else 0.dp
    val totalCanvasHeight = chartHeight + volumeHeight

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
                CandlestickYAxisScale(
                    yAxisConfig = effectiveYAxisConfig,
                    yAxisStepHeight = yAxisStepHeight,
                    minValue = paddedLow,
                    maxValue = paddedHigh,
                    chartHeight = chartHeight,
                )
            }

            // Chart content
            Column(modifier = Modifier.weight(1f)) {
                val scrollableContentWidth = if (scrollEnabled && data.size > 1) {
                    (candleConfig.bodyWidthDp + minCandleSpacing) * data.size +
                            horizontalDrawPadding * 2
                } else null

                val scrollContentModifier = if (scrollEnabled && scrollableContentWidth != null) {
                    Modifier
                        .horizontalScroll(scrollState)
                        .width(scrollableContentWidth)
                } else {
                    Modifier.fillMaxWidth()
                }

                Column(modifier = scrollContentModifier) {

                    BoxWithConstraints {
                        // Grid lines
                        if (enableGridLines) {
                            CandlestickYAxisGridLines(
                                gridLineStyle = gridLineStyle,
                                yAxisStepHeight = yAxisStepHeight,
                                totalGridLines = yAxisConfig.axisScaleCount,
                            )
                        }

                        // ── Canvas: candles + wicks + volume + crosshair ──
                        Canvas(
                            modifier = Modifier
                                .padding(top = yAxisStepHeight)
                                .fillMaxWidth()
                                .height(totalCanvasHeight)
                                .pointerInput(data) {
                                    detectTapGestures { tapOffset ->
                                        val canvasWidth = size.width.toFloat()
                                        val pointCount = data.size
                                        if (pointCount == 0) return@detectTapGestures

                                        val horizontalPadding =
                                            with(density) { horizontalDrawPadding.toPx() }
                                        val drawableWidth = canvasWidth - 2 * horizontalPadding
                                        val stepX = if (pointCount > 1)
                                            drawableWidth / (pointCount - 1)
                                        else drawableWidth

                                        val bodyWidthPx = with(density) {
                                            candleConfig.bodyWidthDp.toPx()
                                        }
                                        val touchSlop = bodyWidthPx * 2f

                                        var closestIndex = -1
                                        var closestDist = Float.MAX_VALUE

                                        data.forEachIndexed { index, _ ->
                                            val cx = horizontalPadding + index * stepX
                                            val dist = abs(tapOffset.x - cx)
                                            if (dist < closestDist) {
                                                closestDist = dist
                                                closestIndex = index
                                            }
                                        }

                                        if (closestDist <= touchSlop && closestIndex >= 0) {
                                            selectedIndex = closestIndex
                                            onCandleClicked?.invoke(data[closestIndex])
                                        } else {
                                            selectedIndex = -1
                                        }
                                    }
                                }
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val pointCount = data.size

                            if (pointCount == 0) return@Canvas

                            val horizontalPadding = horizontalDrawPadding.toPx()
                            val drawableWidth = canvasWidth - 2 * horizontalPadding
                            val stepX = if (pointCount > 1)
                                drawableWidth / (pointCount - 1)
                            else drawableWidth

                            val bodyWidthPx = candleConfig.bodyWidthDp.toPx()
                            val wickWidthPx = candleConfig.wickWidthDp.toPx()

                            // Height available for price candles (= chartHeight)
                            val chartHeightPx = with(density) { chartHeight.toPx() }
                            val priceAreaHeight = chartHeightPx

                            val volumeAreaHeight = canvasHeight - priceAreaHeight

                            val volumeAreaTop = priceAreaHeight

                            // ── Draw crosshair behind candles ────────
                            if (crosshairConfig.enabled && selectedIndex in data.indices) {
                                val cx = horizontalPadding + selectedIndex * stepX
                                val entry = data[selectedIndex]
                                val midY = priceAreaHeight -
                                        ((entry.close - paddedLow) / paddedRange) *
                                        priceAreaHeight

                                val dashEffect = if (crosshairConfig.dashLengthDp > 0.dp) {
                                    PathEffect.dashPathEffect(
                                        floatArrayOf(
                                            crosshairConfig.dashLengthDp.toPx(),
                                            crosshairConfig.gapLengthDp.toPx()
                                        ), 0f
                                    )
                                } else null

                                // Vertical crosshair
                                drawLine(
                                    color = crosshairConfig.lineColor,
                                    start = Offset(cx, 0f),
                                    end = Offset(cx, priceAreaHeight),
                                    strokeWidth = crosshairConfig.lineWidthDp.toPx(),
                                    pathEffect = dashEffect,
                                )
                                // Horizontal crosshair at close price
                                drawLine(
                                    color = crosshairConfig.lineColor,
                                    start = Offset(0f, midY),
                                    end = Offset(canvasWidth, midY),
                                    strokeWidth = crosshairConfig.lineWidthDp.toPx(),
                                    pathEffect = dashEffect,
                                )
                            }

                            // ── Draw candles ─────────────────────────
                            val visibleCount =
                                (pointCount * animationProgress).toInt()
                                    .coerceIn(0, pointCount)

                            for (i in 0 until visibleCount) {
                                val entry = data[i]
                                val cx = horizontalPadding + i * stepX

                                val candleColor = when (entry.change) {
                                    CandleChange.Bullish -> candleConfig.bullishColor
                                    CandleChange.Bearish -> candleConfig.bearishColor
                                    CandleChange.Neutral -> candleConfig.neutralColor
                                }

                                // Map prices to Y coordinates (inverted: high price = low Y)
                                val highY = priceAreaHeight -
                                        ((entry.high - paddedLow) / paddedRange) * priceAreaHeight
                                val lowY = priceAreaHeight -
                                        ((entry.low - paddedLow) / paddedRange) * priceAreaHeight
                                val openY = priceAreaHeight -
                                        ((entry.open - paddedLow) / paddedRange) * priceAreaHeight
                                val closeY = priceAreaHeight -
                                        ((entry.close - paddedLow) / paddedRange) * priceAreaHeight

                                val bodyTop = minOf(openY, closeY)
                                val bodyBottom = maxOf(openY, closeY)
                                // Ensure min body height of 1px
                                val minBodyHeight = 1f
                                val bodyHeight = (bodyBottom - bodyTop)
                                    .coerceAtLeast(minBodyHeight)

                                // ── Top wick ─────────────────────────
                                drawLine(
                                    color = candleColor,
                                    start = Offset(cx, highY),
                                    end = Offset(cx, bodyTop),
                                    strokeWidth = wickWidthPx,
                                    cap = candleConfig.wickCap,
                                )

                                // ── Bottom wick ──────────────────────
                                drawLine(
                                    color = candleColor,
                                    start = Offset(cx, bodyBottom),
                                    end = Offset(cx, lowY),
                                    strokeWidth = wickWidthPx,
                                    cap = candleConfig.wickCap,
                                )

                                // ── Candle body ──────────────────────
                                if (candleConfig.hollowBullish &&
                                    entry.change == CandleChange.Bullish
                                ) {
                                    // Hollow body (outline only)
                                    drawRect(
                                        color = candleColor,
                                        topLeft = Offset(
                                            cx - bodyWidthPx / 2,
                                            bodyTop
                                        ),
                                        size = Size(bodyWidthPx, bodyHeight),
                                        style = Stroke(width = wickWidthPx),
                                    )
                                } else {
                                    // Filled body
                                    drawRect(
                                        color = candleColor,
                                        topLeft = Offset(
                                            cx - bodyWidthPx / 2,
                                            bodyTop
                                        ),
                                        size = Size(bodyWidthPx, bodyHeight),
                                    )
                                }

                                // ── Selection highlight ──────────────
                                if (i == selectedIndex) {
                                    drawRect(
                                        color = candleColor.copy(alpha = 0.15f),
                                        topLeft = Offset(
                                            cx - bodyWidthPx,
                                            highY
                                        ),
                                        size = Size(
                                            bodyWidthPx * 2,
                                            lowY - highY
                                        ),
                                    )
                                }
                            }

                            // ── Draw volume bars ─────────────────────
                            if (volumeConfig.enabled) {
                                for (i in 0 until visibleCount) {
                                    val entry = data[i]
                                    val cx = horizontalPadding + i * stepX

                                    val volColor = when (entry.change) {
                                        CandleChange.Bullish -> volumeConfig.bullishColor
                                        CandleChange.Bearish -> volumeConfig.bearishColor
                                        CandleChange.Neutral -> volumeConfig.neutralColor
                                    }

                                    val barWidth =
                                        (volumeConfig.barWidthDp ?: candleConfig.bodyWidthDp)
                                            .toPx()
                                    val barHeight = (entry.volume / maxVolume) *
                                            volumeAreaHeight * 0.9f // 90% fill

                                    drawRect(
                                        color = volColor,
                                        topLeft = Offset(
                                            cx - barWidth / 2,
                                            volumeAreaTop + volumeAreaHeight - barHeight
                                        ),
                                        size = Size(barWidth, barHeight),
                                    )
                                }
                            }
                        }

                        // ── Marker tooltip ────────────────────────────
                        if (selectedIndex in data.indices && markerConfig.enabled) {
                            val entry = data[selectedIndex]

                            // Calculate popup offset to position near the selected candle
                            val pCount = data.size
                            val hPaddingPx = with(density) { horizontalDrawPadding.toPx() }
                            val cWidthPx = with(density) { maxWidth.toPx() }
                            val dWidth = cWidthPx - 2 * hPaddingPx
                            val sX = if (pCount > 1) dWidth / (pCount - 1) else dWidth
                            val candleX = hPaddingPx + selectedIndex * sX

                            // Candle high-Y in canvas coords, then absolute Y from BoxWithConstraints top
                            val chartHeightPx = with(density) { chartHeight.toPx() }
                            val yAxisStepPx = with(density) { yAxisStepHeight.toPx() }
                            val highY = chartHeightPx -
                                    ((entry.high - paddedLow) / paddedRange) * chartHeightPx

                            val rawPopupOffsetX = (candleX - cWidthPx / 2f).toInt()
                            val estimatedHalfWidthPx = with(density) { 80.dp.toPx() }
                            val maxAbsOffsetX = ((cWidthPx / 2f) - estimatedHalfWidthPx)
                                .coerceAtLeast(0f).toInt()
                            val popupOffsetX = rawPopupOffsetX.coerceIn(-maxAbsOffsetX, maxAbsOffsetX)

                            val pointYFromTop = yAxisStepPx + highY
                            val popupMargin = with(density) { 48.dp.toPx() }
                            val popupOffsetY = (pointYFromTop - popupMargin).toInt().coerceAtLeast(0)

                            if (customMarker != null) {
                                customMarker(entry) { selectedIndex = -1 }
                            } else {
                                CandlestickMarkerPopup(
                                    markerConfig = markerConfig,
                                    entry = entry,
                                    offset = IntOffset(popupOffsetX, popupOffsetY),
                                    onDismissRequest = { selectedIndex = -1 },
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
                        Spacer(modifier = Modifier.height(4.dp))

                        val labelTextStyle = if (autoShrinkXAxisLabels && data.size > 8) {
                            val scaleFactor = (8f / data.size).coerceIn(0.5f, 1f)
                            val baseFontSize = xAxisConfig.textStyle.fontSize
                            val newFontSize = if (baseFontSize.isSp) {
                                (baseFontSize.value * scaleFactor).sp
                            } else baseFontSize
                            xAxisConfig.textStyle.copy(fontSize = newFontSize)
                        } else {
                            xAxisConfig.textStyle
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            data.forEachIndexed { index, entry ->
                                val displayText = if (entry.label.length > maxTextLengthXAxis) {
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

                                val fraction = if (data.size > 1) {
                                    index.toFloat() / (data.size - 1)
                                } else 0.5f

                                Text(
                                    text = displayText,
                                    style = labelTextStyle,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .layout { measurable, constraints ->
                                            val placeable = measurable.measure(constraints)
                                            layout(placeable.width, placeable.height) {
                                                val horizontalPaddingPx =
                                                    horizontalDrawPadding.toPx()
                                                val availableWidth =
                                                    constraints.maxWidth.toFloat()
                                                val drawableWidth =
                                                    availableWidth - 2 * horizontalPaddingPx
                                                val xPos = horizontalPaddingPx +
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
