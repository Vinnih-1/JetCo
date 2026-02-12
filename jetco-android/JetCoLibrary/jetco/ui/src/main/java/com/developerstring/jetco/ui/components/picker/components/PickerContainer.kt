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

package com.developerstring.jetco.ui.components.picker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.developerstring.jetco.ui.components.picker.config.PickerContainerConfig
import com.developerstring.jetco.ui.components.picker.config.PickerDefaults
import com.developerstring.jetco.ui.components.picker.model.PickerDisplayMode

/**
 * Wraps picker [content] inside the appropriate container based on [displayMode].
 *
 * - [PickerDisplayMode.DIALOG] → Material 3 AlertDialog
 * - [PickerDisplayMode.BOTTOM_SHEET] → Material 3 ModalBottomSheet
 * - [PickerDisplayMode.INLINE] → Directly renders content (no wrapper)
 *
 * @param visible Whether the picker container is currently shown.
 * @param displayMode How the picker is presented.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet; null uses default.
 * @param onDismiss Callback when the container is dismissed.
 * @param content The picker content composable to embed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PickerContainer(
    visible: Boolean,
    displayMode: PickerDisplayMode,
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (!visible) return

    when (displayMode) {
        PickerDisplayMode.DIALOG -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
                containerColor = containerConfig.containerColor,
                shape = containerConfig.shape,
                tonalElevation = containerConfig.tonalElevation,
                confirmButton = {},
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        content()
                    }
                }
            )
        }

        PickerDisplayMode.BOTTOM_SHEET -> {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )

            ModalBottomSheet(
                onDismissRequest = onDismiss,
                sheetState = sheetState,
                containerColor = containerConfig.containerColor,
                shape = containerConfig.shape,
                tonalElevation = containerConfig.tonalElevation,
                dragHandle = dragHandle
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    content()
                }
            }
        }

        PickerDisplayMode.INLINE -> {
            Surface(
                color = containerConfig.containerColor,
                shape = containerConfig.shape,
                tonalElevation = containerConfig.tonalElevation,
                shadowElevation = containerConfig.shadowElevation
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}
