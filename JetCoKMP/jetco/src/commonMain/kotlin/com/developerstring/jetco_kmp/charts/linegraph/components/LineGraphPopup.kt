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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.developerstring.jetco_kmp.charts.linegraph.config.LineGraphPopUpConfig

/**
 * Displays a floating popup tooltip above a data point.
 *
 * The popup is dismissed automatically when the user touches
 * outside, or the caller can trigger [onDismissRequest].
 *
 * @param popUpConfig Visual configuration for the tooltip.
 * @param text Content text to show inside the tooltip.
 * @param onDismissRequest Callback invoked when the popup should
 *                         be closed.
 *
 * @see LineGraphPopUpConfig
 */
@Composable
internal fun LineGraphPopup(
    popUpConfig: LineGraphPopUpConfig,
    text: String,
    offset: IntOffset = IntOffset.Zero,
    onDismissRequest: () -> Unit
) {
    Popup(
        alignment = Alignment.TopCenter,
        offset = offset,
        onDismissRequest = { onDismissRequest() },
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = popUpConfig.background,
                    shape = popUpConfig.shape
                )
                .padding(8.dp)
        ) {
            Text(
                text = text,
                style = popUpConfig.textStyle
            )
        }
    }
}
