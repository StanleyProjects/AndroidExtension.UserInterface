package sp.ax.ui.entity

import android.util.TypedValue

/**
 * Represents the values of the [android.util.TypedValue] class as enumerations.
 * @author Stanley Wintergreen
 * @since 0.0.7
 */
enum class TypeDimension {
    PIXEL,
    DEVICE_INDEPENDENT,
    SCALED_PIXEL;

    companion object {
        /**
         * Converts instance of [TypeDimension] to integer value by [android.util.TypedValue].
         * @author Stanley Wintergreen
         * @since 0.0.7
         */
        fun TypeDimension.toInt(): Int {
            return when (this) {
                PIXEL -> TypedValue.COMPLEX_UNIT_PX
                DEVICE_INDEPENDENT -> TypedValue.COMPLEX_UNIT_DIP
                SCALED_PIXEL -> TypedValue.COMPLEX_UNIT_SP
            }
        }
    }
}
