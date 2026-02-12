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

package com.developerstring.jetco.ui.charts.linegraph.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

/**
 * Represents a single data point on the line graph.
 *
 * Each entry maps a labelled position on the X-axis to a numeric Y-axis value.
 * The [normalizedY] field is computed internally as a fraction of the maximum Y
 * value, enabling proportional rendering within the chart canvas.
 *
 * @property label The text label displayed on the X-axis for this data point.
 * @property value The raw Y-axis value of this data point.
 * @property normalizedY The Y value normalized to the range [0, 1] relative to the
 *                       maximum value across all entries. Used for positioning within
 *                       the chart drawing area.
 */
@Keep
@Stable
data class LineGraphEntry(
    val label: String,
    val value: Float,
    val normalizedY: Float
)

/**
 * Converts a [Map] of label-value pairs into a list of [LineGraphEntry] objects.
 *
 * The conversion normalizes every value relative to [maxValue], producing
 * a [LineGraphEntry.normalizedY] in `[0, 1]` for each entry. When [maxValue] is `0` the
 * normalized value defaults to `0` to avoid division by zero.
 *
 * **Example:**
 * ```
 * val data = mapOf("Jan" to 10f, "Feb" to 20f, "Mar" to 15f)
 * val entries = data.mapToLineGraphEntries(maxValue = 20f)
 * // entries[0] → LineGraphEntry("Jan", 10f, 0.5f)
 * // entries[1] → LineGraphEntry("Feb", 20f, 1.0f)
 * // entries[2] → LineGraphEntry("Mar", 15f, 0.75f)
 * ```
 *
 * @param maxValue The upper bound used for normalization. Typically the largest
 *                 value in the data set or a user-supplied ceiling.
 * @return An ordered list of [LineGraphEntry] instances ready for rendering.
 */
fun Map<String, Float>.mapToLineGraphEntries(maxValue: Float): List<LineGraphEntry> =
    map { (key, value) ->
        LineGraphEntry(
            label = key,
            value = value,
            normalizedY = if (maxValue != 0f) (value / maxValue) else 0f
        )
    }
