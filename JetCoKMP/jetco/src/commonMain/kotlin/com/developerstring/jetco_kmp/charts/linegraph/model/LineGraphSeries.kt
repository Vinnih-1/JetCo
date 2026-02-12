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

package com.developerstring.jetco_kmp.charts.linegraph.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

/**
 * Describes a single data series to be drawn in the line graph.
 *
 * Each series is rendered as a separate line on the same chart
 * canvas, allowing side-by-side comparison of multiple data sets.
 *
 * @property name Human-readable name for the series (used in legends).
 * @property data Ordered list of Y-axis values. The X-axis position
 *                is determined by the index and the shared label list
 *                passed to the chart.
 * @property color The color used for the line, dots, and area fill
 *                 of this series.
 *
 * Example:
 * ```
 * val series = listOf(
 *     LineGraphSeries("Revenue", listOf(100f, 200f, 150f, 300f), Color.Blue),
 *     LineGraphSeries("Expense", listOf(80f,  120f, 90f,  200f), Color.Red),
 * )
 * ```
 */
@Stable
data class LineGraphSeries(
    val name: String,
    val data: List<Float>,
    val color: Color
)
