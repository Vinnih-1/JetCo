package com.developerstring.jetco_kmp.components.stepper

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.stepper.model.StepperActionIcons
import com.developerstring.jetco_kmp.components.stepper.model.StepperConfig
import com.developerstring.jetco_kmp.components.stepper.model.StepperNode
import com.developerstring.jetco_kmp.components.stepper.model.StepperStatus

/**
 * A compact horizontal stepper component for Jetpack Compose.
 *
 * The [CompactHorizontalStepper] arranges steps horizontally in a more space-efficient layout
 * compared to [HorizontalStepper]. It is designed for cases where screen width is limited,
 * or when you want a cleaner, minimal representation of step progress.
 *
 * ### Features
 * - Displays step nodes with optional icons based on [StepperActionIcons].
 * - Provides a **compact layout** with smaller labels and reduced spacing.
 * - Supports **custom styling** with [StepperConfig] (colors, spacing, shapes, typography).
 * - Allows **step interactions** through [onStepClick] callback.
 *
 * ### Example Usage
 * ```kotlin
 * val steps = listOf(
 *     StepperNode("Login", status = StepperStatus.COMPLETE),
 *     StepperNode("Details", status = StepperStatus.ACTIVE),
 *     StepperNode("Summary", status = StepperStatus.IDLE),
 *     StepperNode("Finish", status = StepperStatus.IDLE),
 * )
 *
 * CompactHorizontalStepper(
 *     steps = steps,
 *     currentStep = 2,
 *     style = StepperConfig(),
 *     onStepClick = { index -> println("Step $index clicked") }
 * )
 * ```
 *
 * @param steps List of [StepperNode] representing the steps in the compact horizontal flow.
 * @param currentStep The index of the current active step (0-based).
 * @param modifier Modifier to be applied to the stepper layout.
 * @param style Styling options for customizing the stepper’s appearance via [StepperConfig].
 * @param stepperActionIcons Icons for step states like active, completed, and error.
 * @param onStepClick Optional callback invoked with the index when a step is clicked.
 */
@Composable
fun CompactHorizontalStepper(
    steps: List<StepperNode>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    style: StepperConfig = StepperConfig(),
    stepperActionIcons: StepperActionIcons = StepperActionIcons(),
    onStepClick: ((Int) -> Unit)? = null
) {

    var animationTriggered by rememberSaveable {
        mutableStateOf(!style.animation.enabled)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationTriggered) 1f else 0f,
        animationSpec = tween(style.animation.durationMillis),
        label = "node_animation"
    )

    LaunchedEffect(Unit) {
        // Trigger animation on composition
        animationTriggered = true
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isActive = index == currentStep
            val isCompleted = index < currentStep
            val isError = step.status == StepperStatus.ERROR

            val nodeColor = when {
                isError -> style.node.errorColor
                isCompleted -> style.node.completedColor
                isActive -> style.node.activeColor
                else -> style.node.inactiveColor
            }

            // Node
            Card(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                colors = CardDefaults.cardColors(containerColor = nodeColor),
                onClick = { onStepClick?.invoke(index) }
            ) {
                val prefix = "${index}_step_"
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val (iconVector, iconTint, iconDescription) = when {
                        isError -> Triple(
                            stepperActionIcons.error,
                            style.node.actionIconColor,
                            prefix + "error"
                        )
                        isCompleted || index == currentStep - 1 -> Triple(
                            stepperActionIcons.completed,
                            style.node.actionIconColor,
                            prefix + "completed"
                        )
                        isActive && step.icon != null -> Triple(
                            step.icon,
                            style.node.actionIconColor,
                            prefix + "active"
                        )
                        step.icon != null -> Triple(
                            step.icon,
                            style.node.idleIconColor,
                            prefix + "idle"
                        )
                        else -> Triple(
                            stepperActionIcons.active,
                            style.node.idleIconColor,
                            prefix + "idle"
                        )
                    }

                    Icon(
                        imageVector = iconVector,
                        contentDescription = iconDescription,
                        tint = iconTint,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Connector
            if (index < steps.lastIndex) {
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    val pathEffect = if (!isCompleted) style.connector.pathEffect else null
                    val strokeColor = if (isCompleted) style.node.completedColor else style.node.inactiveColor
                    drawLine(
                        color = strokeColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width * animatedProgress, size.height / 2),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = pathEffect
                    )
                }
            }
        }
    }
}