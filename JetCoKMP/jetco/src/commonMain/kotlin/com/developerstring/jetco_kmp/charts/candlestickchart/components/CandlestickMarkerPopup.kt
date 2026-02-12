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

package com.developerstring.jetco_kmp.charts.candlestickchart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.developerstring.jetco_kmp.charts.candlestickchart.config.CandlestickMarkerConfig
import com.developerstring.jetco_kmp.charts.candlestickchart.model.CandlestickEntry
import kotlin.math.abs
import kotlin.math.round

/**
 * Default OHLC marker tooltip for the candlestick chart.
 *
 * Shows Open, High, Low, Close (and volume when > 0) in a
 * compact popup aligned at the centre of the chart.
 */
@Composable
internal fun CandlestickMarkerPopup(
    markerConfig: CandlestickMarkerConfig,
    entry: CandlestickEntry,
    offset: IntOffset = IntOffset.Zero,
    onDismissRequest: () -> Unit,
) {
    Popup(
        alignment = Alignment.TopCenter,
        offset = offset,
        onDismissRequest = onDismissRequest,
    ) {
        Box(
            modifier = Modifier
                .clip(markerConfig.shape)
                .background(markerConfig.background)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = entry.label,
                    style = markerConfig.textStyle.copy(
                        color = markerConfig.textStyle.color.copy(alpha = 0.7f)
                    ),
                )
                Text(
                    text = "O ${formatMarkerValue(entry.open)}  " +
                            "C ${formatMarkerValue(entry.close)}",
                    style = markerConfig.textStyle,
                )
                Text(
                    text = "L ${formatMarkerValue(entry.low)}  " +
                            "H ${formatMarkerValue(entry.high)}",
                    style = markerConfig.textStyle,
                )
                if (entry.volume > 0f) {
                    Text(
                        text = "Vol ${formatMarkerVolume(entry.volume)}",
                        style = markerConfig.textStyle.copy(
                            color = markerConfig.textStyle.color.copy(alpha = 0.7f)
                        ),
                    )
                }
            }
        }
    }
}

private fun formatMarkerValue(value: Float): String {
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

private fun formatMarkerVolume(value: Float): String {
    return when {
        value >= 1_000_000_000 -> "${formatMarkerValue(value / 1_000_000_000)}B"
        value >= 1_000_000 -> "${formatMarkerValue(value / 1_000_000)}M"
        value >= 1_000 -> "${formatMarkerValue(value / 1_000)}K"
        else -> formatMarkerValue(value)
    }
}
