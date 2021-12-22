package sp.ax.ui.entity

import android.util.TypedValue

enum class TypeDimension {
    PIXEL,
    DEVICE_INDEPENDENT,
    SCALED_PIXEL;

    companion object {
        fun TypeDimension.toInt(): Int {
            return when (this) {
                PIXEL -> TypedValue.COMPLEX_UNIT_PX
                DEVICE_INDEPENDENT -> TypedValue.COMPLEX_UNIT_DIP
                SCALED_PIXEL -> TypedValue.COMPLEX_UNIT_SP
            }
        }
    }
}
