package sp.ax.ui.entity

import android.util.TypedValue
import org.junit.Assert.assertEquals
import org.junit.Test
import sp.ax.ui.entity.TypeDimension.Companion.toInt

class TypeDimensionTest {
    @Test
    fun toIntTest() {
        TypeDimension.values().map {
            it to when (it) {
                TypeDimension.PIXEL -> TypedValue.COMPLEX_UNIT_PX
                TypeDimension.DEVICE_INDEPENDENT -> TypedValue.COMPLEX_UNIT_DIP
                TypeDimension.SCALED_PIXEL -> TypedValue.COMPLEX_UNIT_SP
            }
        }.forEach { (dimension, expected) ->
            val actual = dimension.toInt()
            assertEquals("An expected android integer for TypeDimension.${dimension.name} is $expected, but actual is $actual!", expected, actual)
        }
    }
}
