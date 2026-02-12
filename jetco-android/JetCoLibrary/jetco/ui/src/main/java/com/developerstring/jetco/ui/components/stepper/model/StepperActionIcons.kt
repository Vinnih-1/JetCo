package com.developerstring.jetco.ui.components.stepper.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the set of icons used by the stepper for different states.
 *
 * @property completed Icon displayed for completed steps.
 * @property error Icon displayed for error steps.
 * @property active Default icon displayed for active steps (if no custom icon provided).
 */
@Stable
data class StepperActionIcons(
    val completed: ImageVector = Icons.Default.Check,
    val error: ImageVector = Icons.Default.Close,
    val active: ImageVector = Icons.Rounded.Check
)