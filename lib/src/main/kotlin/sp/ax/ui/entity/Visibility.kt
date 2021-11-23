package sp.ax.ui.entity

import android.view.View

/**
 * A type to represent visibility state of some view.
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
enum class Visibility {
    /**
     * Represent the value from [android.view.View.VISIBLE].
     */
    VISIBLE,
    /**
     * Represent the value from [android.view.View.INVISIBLE].
     */
    INVISIBLE,
    /**
     * Represent the value from [android.view.View.GONE].
     */
    GONE;

    companion object {
        /**
         * Tries to convert [value] of [Int] to instance of [Visibility].
         * @param value Expected one of [android.view.View.VISIBLE], [android.view.View.INVISIBLE] or [android.view.View.GONE].
         * @return One of [VISIBLE], [INVISIBLE] or [GONE].
         * @throws IllegalStateException If value is not one of [android.view.View.VISIBLE], [android.view.View.INVISIBLE] or [android.view.View.GONE].
         * @author Stanley Wintergreen
         * @since 0.0.4
         */
        fun fromInt(value: Int): Visibility {
            return when (value) {
                View.VISIBLE -> VISIBLE
                View.INVISIBLE -> INVISIBLE
                View.GONE -> GONE
                else -> error("Visibility $value is not supported!")
            }
        }

        /**
         * Converts instance of [Visibility] to one of [android.view.View.VISIBLE], [android.view.View.INVISIBLE] or [android.view.View.GONE].
         * @return One of [android.view.View.VISIBLE], [android.view.View.INVISIBLE] or [android.view.View.GONE].
         * @author Stanley Wintergreen
         * @since 0.0.4
         */
        fun Visibility.toInt(): Int {
            return when (this) {
                VISIBLE -> View.VISIBLE
                INVISIBLE -> View.INVISIBLE
                GONE -> View.GONE
            }
        }
    }
}
