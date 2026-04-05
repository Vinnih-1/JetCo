package com.developerstring.jetco_kmp.components.button.fab.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.button.fab.transition.FabButtonTransition
import com.developerstring.jetco_kmp.components.button.fab.transition.FabItemTransition

/**
 * Main configuration class for the Floating Action Button (FAB) family.
 *
 * @property buttonStyle Visual styling for the main FAB (color, shape, size, etc.).
 * @property itemArrangement Configuration for how items are positioned when expanded.
 * @property animation Animation specs for entry/exit transitions and stagger orders.
 */
@Stable
data class FabMainConfig(
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val itemArrangement: ItemArrangement = ItemArrangement(),
    val animation: Animation = Animation()
) {

    sealed interface Orientation {
        data class Radial(
            val arc: Arc = Arc.END,
            val radius: Dp = 80.dp
        ) : Orientation {
            enum class Arc(val start: Double, val end: Double) {
                END(90.0, 180.0),
                START(90.0, 0.0),
                CENTER(0.0, 180.0)
            }
        }

        data class Stack(
            val spacedBy: Dp = 40.dp,
            val spacingPadding: Dp = 24.dp
        ) : Orientation

        data class Morph(
            val columns: Int = 2,
            val spacedBy: Dp = 12.dp,
            val headerSpace: Dp = 20.dp,
            val width: Dp = 250.dp,
            val cardShape: Shape = RoundedCornerShape(24.dp)
        ) : Orientation
    }

    /**
     * Animation configuration for the FAB family.
     *
     * @property itemEnterOrder The order in which items appear (FIFO, FILO, ALL).
     * @property itemExitOrder The order in which items disappear.
     * @property itemEnterTransition Combined transitions for sub-item entry.
     * @property itemExitTransition Combined transitions for sub-item exit.
     * @property buttonEnterTransition Transitions applied to the main FAB on expansion.
     * @property buttonExitTransition Transitions applied to the main FAB on collapse.
     * @property itemEnterDelay Additional delay in milliseconds before items start their
     * enter animation.
     */
    @Stable
    open class Animation(
        val itemEnterOrder: StaggerOrder = StaggerOrder.FIFO,
        val itemExitOrder: StaggerOrder = StaggerOrder.FILO,
        val itemEnterTransition: FabItemTransition = FabItemTransition.Slide() + FabItemTransition.Fade(),
        val itemExitTransition: FabItemTransition = FabItemTransition.Slide() + FabItemTransition.Fade(),
        val buttonEnterTransition: FabButtonTransition = FabButtonTransition.Rotate(45f),
        val buttonExitTransition: FabButtonTransition = FabButtonTransition.Rotate(0f),
        val itemEnterDelay: Long = 300L,
    )

    @Stable
    enum class StaggerOrder {
        FIFO,
        FILO,
        ALL;

        internal fun delayFor(index: Int, total: Int, stepMs: Int): Long = when (this) {
            FIFO -> (index * stepMs).toLong()
            FILO -> ((total - 1 - index) * stepMs).toLong()
            ALL  -> 0L
        }
    }

    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val size: Dp = 72.dp,
        val iconRotation: Float = 45f,
        val padding: PaddingValues = PaddingValues()
    )

    @Stable
    data class ItemArrangement(
        val radial: Orientation.Radial = Orientation.Radial(),
        val stack: Orientation.Stack = Orientation.Stack(),
        val morph: Orientation.Morph = Orientation.Morph()
    )
}
