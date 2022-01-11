package sp.ax.ui.entity

import android.graphics.Typeface
import org.junit.Assert.assertEquals
import org.junit.Test
import sp.ax.ui.entity.TypefaceStyle.Companion.toInt
import sp.ax.ui.exhaustive

class TypefaceStyleTest {
    @Test
    fun toIntTest() {
        TypefaceStyle.values().forEach {
            when (it) {
                TypefaceStyle.NORMAL -> {
                    assertEquals(Typeface.NORMAL, it.toInt())
                }
                TypefaceStyle.BOLD -> {
                    assertEquals(Typeface.BOLD, it.toInt())
                }
                TypefaceStyle.ITALIC -> {
                    assertEquals(Typeface.ITALIC, it.toInt())
                }
                TypefaceStyle.BOLD_ITALIC -> {
                    assertEquals(Typeface.BOLD_ITALIC, it.toInt())
                }
            }.exhaustive()
        }
    }
}
