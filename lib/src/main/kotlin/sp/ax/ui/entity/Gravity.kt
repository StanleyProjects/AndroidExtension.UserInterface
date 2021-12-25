package sp.ax.ui.entity

import android.view.Gravity as AndroidGravity

/**
 * Represents the values of the [android.view.Gravity] class as enumerations.
 * @author Stanley Wintergreen
 * @since 0.0.7
 */
enum class Gravity {
    /**```
     * ┌--*--┐
     * |     |
     * └-----┘
     * ```*/
    TOP,
    /**```
     * *-----┐
     * |     |
     * └-----┘
     * ```*/
    TOP_LEFT,
    /**```
     * ┌-----*
     * |     |
     * └-----┘
     * ```*/
    TOP_RIGHT,
    /**```
     * ┌-----┐
     * |     |
     * └--*--┘
     * ```*/
    BOTTOM,
    /**```
     * ┌-----┐
     * |     |
     * *-----┘
     * ```*/
    BOTTOM_LEFT,
    /**```
     * ┌-----┐
     * |     |
     * └-----*
     * ```*/
    BOTTOM_RIGHT,
    /**```
     * ┌-----┐
     * *     |
     * └-----┘
     * ```*/
    LEFT,
    /**```
     * ┌-----┐
     * |     *
     * └-----┘
     * ```*/
    RIGHT,
    /**```
     * ┌-----┐
     * |  *  |
     * └-----┘
     * ```*/
    CENTER;

    companion object {
        /**
         * Converts instance of [Gravity] to integer value by [android.view.Gravity].
         * @author Stanley Wintergreen
         * @since 0.0.7
         */
        fun Gravity.toInt(): Int {
            return when (this) {
                TOP -> AndroidGravity.TOP or AndroidGravity.CENTER_HORIZONTAL
                TOP_LEFT -> AndroidGravity.TOP or AndroidGravity.LEFT
                TOP_RIGHT -> AndroidGravity.TOP or AndroidGravity.RIGHT
                BOTTOM -> AndroidGravity.BOTTOM or AndroidGravity.CENTER_HORIZONTAL
                BOTTOM_LEFT -> AndroidGravity.BOTTOM or AndroidGravity.LEFT
                BOTTOM_RIGHT -> AndroidGravity.BOTTOM or AndroidGravity.RIGHT
                LEFT -> AndroidGravity.LEFT or AndroidGravity.CENTER_VERTICAL
                RIGHT -> AndroidGravity.RIGHT or AndroidGravity.CENTER_VERTICAL
                CENTER -> AndroidGravity.CENTER
            }
        }
    }
}
