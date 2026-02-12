package com.developerstring.jetco.ui.components.stepper.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single step within a stepper.
 *
 * @property title A short title describing the step.
 * @property description Optional secondary text providing additional details.
 * @property icon Optional custom icon for the step (fallbacks to default icons if null).
 * @property status Current status of the step (idle, active, completed, or error).
 */
@Stable
data class StepperNode(
    val title: String? = null,
    val description: String? = null,
    val icon: ImageVector? = null,
    val painter: Painter? = null,
    val status: StepperStatus = StepperStatus.IDLE
)