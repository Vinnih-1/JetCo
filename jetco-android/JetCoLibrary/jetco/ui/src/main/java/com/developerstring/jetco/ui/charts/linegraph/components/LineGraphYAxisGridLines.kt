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

package com.developerstring.jetco.ui.charts.linegraph.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphGridLineStyle

/**
 * Renders a stack of horizontal dashed grid lines that span the
 * chart width.
 *
 * The composable positions [LineGraphGridLine]s at equal vertical
 * intervals determined by [yAxisStepHeight].
 *
 * @param gridLineStyle Visual configuration for each grid line.
 * @param yAxisStepHeight Vertical distance between consecutive grid lines.
 *
 * @see LineGraphGridLine
 * @see LineGraphGridLineStyle
 */
@Composable
internal fun LineGraphYAxisGridLines(
    gridLineStyle: LineGraphGridLineStyle,
    yAxisStepHeight: Dp
) {
    Column(
        modifier = Modifier
            .padding(top = yAxisStepHeight)
            .fillMaxWidth(),
    ) {
        repeat(gridLineStyle.totalGridLines + 1) { index ->
            if (index != 0) {
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight),
                    verticalAlignment = Alignment.Top
                ) {
                    LineGraphGridLine(
                        modifier = Modifier.fillMaxWidth(),
                        gridLineStyle = gridLineStyle,
                    )
                }
            }
        }
    }
}
