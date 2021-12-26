package sp.ax.ui.entity

import android.graphics.Typeface

/**
 * Represents the values of the [android.graphics.Typeface] class as enumerations.
 * @author Stanley Wintergreen
 * @since 0.0.8
 */
enum class TypefaceStyle {
    NORMAL,
    BOLD,
    ITALIC,
    BOLD_ITALIC;

    companion object {
        /**
         * Converts instance of [TypefaceStyle] to integer value by [android.graphics.Typeface].
         * @author Stanley Wintergreen
         * @since 0.0.8
         */
        fun TypefaceStyle.toInt(): Int {
            return when (this) {
                NORMAL -> Typeface.NORMAL
                BOLD -> Typeface.BOLD
                ITALIC -> Typeface.ITALIC
                BOLD_ITALIC -> Typeface.BOLD_ITALIC
            }
        }
    }
}
