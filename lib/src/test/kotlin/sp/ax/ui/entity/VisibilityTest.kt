package sp.ax.ui.entity

import android.view.View
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import sp.ax.ui.entity.Visibility.Companion.toInt
import sp.ax.ui.exhaustive

class VisibilityTest {
    @Test
    fun toIntTest() {
        Visibility.values().forEach {
            when (it) {
                Visibility.VISIBLE -> {
                    assertEquals(View.VISIBLE, it.toInt())
                }
                Visibility.INVISIBLE -> {
                    assertEquals(View.INVISIBLE, it.toInt())
                }
                Visibility.GONE -> {
                    assertEquals(View.GONE, it.toInt())
                }
            }.exhaustive()
        }
    }

    @Test
    fun fromIntTest() {
        Visibility.values().forEach {
            when (it) {
                Visibility.VISIBLE -> {
                    assertEquals(it, Visibility.fromInt(View.VISIBLE))
                }
                Visibility.INVISIBLE -> {
                    assertEquals(it, Visibility.fromInt(View.INVISIBLE))
                }
                Visibility.GONE -> {
                    assertEquals(it, Visibility.fromInt(View.GONE))
                }
            }.exhaustive()
        }
        assertThrows(IllegalStateException::class.java) {
            val value = -1
            setOf(View.VISIBLE, View.INVISIBLE, View.GONE).forEach {
                assertNotEquals(it, value)
            }
            Visibility.fromInt(value)
        }
    }
}
