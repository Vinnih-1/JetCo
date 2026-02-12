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

package com.developerstring.jetco_kmp.charts.linegraph.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphGridLineStyle

/**
 * Draws a single horizontal dashed grid line.
 *
 * This composable is designed to be placed inside a repeating layout
 * so that multiple grid lines can be stacked vertically across the
 * chart area.
 *
 * @param modifier Modifier applied to the underlying [Canvas].
 * @param gridLineStyle Visual configuration for the grid line (color,
 *                      stroke width, dash pattern, cap).
 *
 * @see LineGraphGridLineStyle
 */
@Composable
internal fun LineGraphGridLine(
    modifier: Modifier = Modifier,
    gridLineStyle: LineGraphGridLineStyle,
) {
    Canvas(modifier = modifier.padding(top = gridLineStyle.strokeWidth / 2)) {

        val dashLengthPx = gridLineStyle.dashLength.toPx()
        val gapLengthPx = gridLineStyle.gapLength.toPx()
        val strokeWidthPx = gridLineStyle.strokeWidth.toPx()

        // Skia's MakeDash requires all intervals > 0; passing 0 returns
        // nullptr which crashes on Kotlin/Native ("Can't wrap nullptr").
        val pathEffect = if (dashLengthPx > 0f && gapLengthPx > 0f) {
            PathEffect.dashPathEffect(
                floatArrayOf(dashLengthPx, gapLengthPx), 0f
            )
        } else null

        drawLine(
            color = gridLineStyle.color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidthPx,
            pathEffect = pathEffect,
            cap = gridLineStyle.dashCap
        )
    }
}
