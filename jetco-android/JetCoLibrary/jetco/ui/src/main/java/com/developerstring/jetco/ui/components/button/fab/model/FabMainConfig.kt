package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class FabMainConfig(
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val itemArrangement: ItemArrangement = ItemArrangement(),
    val animation: Animation = Animation()
) {
    sealed interface Orientation {
        enum class Radial(val start: Double, val end: Double) : Orientation {
            END(90.0, 180.0),
            START(90.0, 0.0),
            CENTER(0.0, 180.0)
        }

        enum class Stack(val spacedBy: Dp = 40.dp) : Orientation {
            TOP,
            START,
            END
        }

        data class Morph(
            val columns: Int = 2,
            val spacedBy: Dp = 12.dp,
            val headerSpace: Dp = 20.dp,
            val width: Dp = 250.dp,
            val cardShape: Shape = RoundedCornerShape(24.dp)
        ) : Orientation
    }

    @Stable
    open class Animation(
        val durationMillis: Int = 300,
        val easing: Easing = FastOutSlowInEasing,
        val animationSpec: AnimationSpec<Float> = tween(durationMillis, easing = easing)
    )

    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val horizontalSpace: Dp = 12.dp,
        val size: Dp = 72.dp,
        val iconRotation: Float = 45f,
        val padding: PaddingValues = PaddingValues()
    )

    @Stable
    data class ItemArrangement(
        val radius: Dp = 80.dp,
        val radial: Orientation.Radial = Orientation.Radial.END,
        val stack: Orientation.Stack = Orientation.Stack.TOP,
        val morph: Orientation.Morph = Orientation.Morph()
    )
}
