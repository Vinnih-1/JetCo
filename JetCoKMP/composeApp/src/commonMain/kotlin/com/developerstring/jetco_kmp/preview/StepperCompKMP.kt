package com.developerstring.jetco_kmp.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco_kmp.components.stepper.CompactHorizontalStepper
import com.developerstring.jetco_kmp.components.stepper.HorizontalStepper
import com.developerstring.jetco_kmp.components.stepper.VerticalStepper
import com.developerstring.jetco_kmp.components.stepper.model.StepperActionIcons
import com.developerstring.jetco_kmp.components.stepper.model.StepperConfig
import com.developerstring.jetco_kmp.components.stepper.model.StepperNode
import com.developerstring.jetco_kmp.components.stepper.model.StepperStatus
import jetcokmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun StepperCompPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
    ) {
        val steps = listOf(
            StepperNode(
                "Order Placed",
                "Your order is confirmed",
                Icons.Default.CheckCircle,
            ),
            StepperNode("Processing", "Preparing your order", Icons.Default.Build),
            StepperNode("Shipped", "On the way to you", Icons.Default.Done),
            StepperNode("Delivered", "Successfully delivered", Icons.Default.Home)
        )

        VerticalStepper(
            steps = steps,
            onStepClick = { index -> /* Handle navigation */ },
        )

        HorizontalStepper(
            steps,

            )
    }

}

@Composable
fun StepperPreview() {

    val sampleSteps = listOf(
        StepperNode(
            title = "Order Placed",
            description = "Your order has been successfully placed",
            icon = Icons.Rounded.ShoppingCart,
            status = StepperStatus.COMPLETE,
            painter = painterResource(Res.drawable.img)
        ),
        StepperNode(
            title = "Processing",
            description = "We're preparing your order",
            icon = Icons.Rounded.DateRange,
            status = StepperStatus.COMPLETE
        ),
        StepperNode(
            title = "Shipped\nIt may take a while to reach you",
            description = "Your order is on the way! \nWould love to have some patience! \nThank you.",
            icon = Icons.Rounded.AccountCircle,
            status = StepperStatus.ERROR
        ),
        StepperNode(
            title = "Shipped once again!",
            description = "Your order is on the way! Sorry for the issue in previous shipping attempt.",
            icon = Icons.Rounded.AccountCircle,
            status = StepperStatus.ACTIVE
        ),
        StepperNode(
            title = "Delivered",
            description = "Order delivered successfully",
            icon = Icons.Rounded.ShoppingCart,
            status = StepperStatus.IDLE
        )
    )

    MaterialTheme {

        Column(
            modifier = Modifier
                .padding(top = 50.dp, bottom = 60.dp)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Vertical Stepper",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            VerticalStepper(
                steps = sampleSteps,
                modifier = Modifier.weight(1f),
                style = StepperConfig(
                    textConfig = StepperConfig.TextStyleConfig(
                        titleTextStyle = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        descriptionTextStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        ),
                    ),
                    connector = StepperConfig.ConnectorStyle(
                        spacing = 12.dp,
                    ),
                    animation = StepperConfig.AnimationConfig(
                        enabled = true,
                        durationMillis = 2000
                    ),
                    node = StepperConfig.NodeStyle(
//                        completedColor = Color(0xFF02D902),
//                        inactiveColor = Color(0xFFE1E1E1),
//                        idleIconColor = Color.Gray
                    ),
                    imageConfig = StepperConfig.ImageConfig(
                        maxWidth = 120.dp,
                        maxHeight = 70.dp,
                        contentScale = ContentScale.Crop,
                    )
                ),
            ) { index ->
                // Handle step click, e.g., navigate to step details
                println("Clicked on step: ${sampleSteps[index].title}")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Horizontal Stepper",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            HorizontalStepper(
                steps = sampleSteps,
                style = StepperConfig(
                    textConfig = StepperConfig.TextStyleConfig(
                        titleTextStyle = TextStyle(
                            textAlign = TextAlign.Center,
                        )
                    ),
//                    titleTextStyle = TextStyle(
//                        textAlign = TextAlign.Center
//                    ),
//                    activeColor = Color(0xFF7FC4E5)
                ),
                stepperActionIcons = StepperActionIcons(
                    completed = Icons.Filled.FavoriteBorder,
                    error = Icons.Filled.Info,
                    active = Icons.Rounded.Notifications
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Compact Stepper",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            CompactHorizontalStepper(
                steps = sampleSteps,
                currentStep = 3,
                stepperActionIcons = StepperActionIcons(
                    completed = Icons.Filled.FavoriteBorder,
                    error = Icons.Filled.Info,
                    active = Icons.Rounded.Notifications
                ),

            )
        }
    }
}