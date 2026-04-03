package com.developerstring.jetco.ui.components.button.fab.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.transition.FabButtonTransition
import com.developerstring.jetco.ui.components.button.fab.transition.FabItemTransition

/**
 * Main configuration class for the Floating Action Button (FAB) family.
 *
 * This class centralizes all visual and behavioral settings, including button styling,
 * item arrangement patterns (Radial, Stack, Morph), and animation specifications
 * for both the main button and its items.
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

    /**
     * Defines the layout orientation and specific parameters for FAB expansion.
     */
    sealed interface Orientation {
        /**
         * Radial orientation that expands items in an arc.
         *
         * @property arc The direction and span of the arc (END, START, CENTER).
         * @property radius The distance from the main FAB to the sub-items.
         */
        data class Radial(
            val arc: Arc = Arc.END,
            val radius: Dp = 80.dp
        ) : Orientation {
            enum class Arc(val start: Double, val end: Double) {
                /** Spreads items from 90° to 180° (upward and to the left). */
                END(90.0, 180.0),
                /** Spreads items from 90° to 0° (upward and to the right). */
                START(90.0, 0.0),
                /** Spreads items from 0° to 180° (full upper arc). */
                CENTER(0.0, 180.0)
            }
        }

        /**
         * Stack orientation that expands items linearly.
         *
         * @property spacedBy Gap between consecutive items in the stack.
         * @property spacingPadding Extra padding used to report total expansion size in [StackExpandOffset].
         */
        data class Stack(
            val spacedBy: Dp = 40.dp,
            val spacingPadding: Dp = 24.dp
        ) : Orientation

        /**
         * Morph orientation that expands the main FAB into a card grid.
         *
         * @property columns Number of item columns in the card grid.
         * @property spacedBy Gap between items inside the grid.
         * @property headerSpace Space between the card header and the item grid.
         * @property width Total width of the expanded card.
         * @property cardShape Shape of the expanded card.
         */
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
     * enter animation. Use this to sequence items after the main FAB button animation —
     * for example, if you use a spring with high bounce on the button, set this to roughly
     * match the visual duration of that spring so items only appear once the button
     * looks settled. `0L` means items start immediately when expanded becomes true.
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

    /**
     * Controls the sequence of staggered animations for sub-items.
     */
    @Stable
    enum class StaggerOrder {
        /** First In, First Out: items animate in the order they appear in the list. */
        FIFO,
        /** First In, Last Out: items animate in reverse order. */
        FILO,
        /** All items animate simultaneously. */
        ALL;

        internal fun delayFor(index: Int, total: Int, stepMs: Int): Long = when (this) {
            FIFO -> (index * stepMs).toLong()
            FILO -> ((total - 1 - index) * stepMs).toLong()
            ALL  -> 0L
        }
    }

    /**
     * Visual style configuration for the main FAB button.
     *
     * @property color Background color of the main FAB.
     * @property shape Shape of the main FAB button.
     * @property size Diameter/Height of the main FAB.
     * @property iconRotation Target rotation of the icon when expanded (legacy support).
     * @property padding Internal padding for the button content.
     */
    @Stable
    data class ButtonStyle(
        val color: Color = Color(0xFF1976D2),
        val shape: Shape = CircleShape,
        val size: Dp = 72.dp,
        val iconRotation: Float = 45f,
        val padding: PaddingValues = PaddingValues()
    )

    /**
     * Container for all arrangement-specific settings.
     */
    @Stable
    data class ItemArrangement(
        val radial: Orientation.Radial = Orientation.Radial(),
        val stack: Orientation.Stack = Orientation.Stack(),
        val morph: Orientation.Morph = Orientation.Morph()
    )
}
