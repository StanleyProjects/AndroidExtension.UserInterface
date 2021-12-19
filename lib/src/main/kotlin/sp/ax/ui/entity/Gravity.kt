package sp.ax.ui.entity

import android.view.Gravity as AndroidGravity

/**
 * Represents the values of the [android.view.Gravity] class as enumerations.
 * @author Stanley Wintergreen
 * @since 0.0.6
 */
enum class Gravity {
    LEFT, TOP, RIGHT, BOTTOM,
    TOP_LEFT,
    CENTER, CENTER_VERTICAL, CENTER_HORIZONTAL;

    companion object {
        /**
         * Converts instance of [Gravity] to integer value by [android.view.Gravity].
         * @author Stanley Wintergreen
         * @since 0.0.6
         */
        fun Gravity.toInt(): Int {
            return when (this) {
                TOP -> AndroidGravity.TOP
                TOP_LEFT -> AndroidGravity.TOP or AndroidGravity.LEFT
                BOTTOM -> AndroidGravity.BOTTOM
                LEFT -> AndroidGravity.LEFT
                RIGHT -> AndroidGravity.RIGHT
                CENTER -> AndroidGravity.CENTER
                CENTER_VERTICAL -> AndroidGravity.CENTER_VERTICAL
                CENTER_HORIZONTAL -> AndroidGravity.CENTER_HORIZONTAL
            }
        }
    }
}
