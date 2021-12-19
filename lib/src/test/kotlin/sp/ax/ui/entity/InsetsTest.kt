package sp.ax.ui.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class InsetsTest {
    companion object {
        private fun Insets.assert(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            mapOf(
                "left" to (left to this.left),
                "top" to (top to this.top),
                "right" to (right to this.right),
                "bottom" to (bottom to this.bottom),
            ).forEach { (key, value) ->
                val (expected, actual) = value
                assertEquals("Insets $key is not $expected!", expected, actual)
            }
        }
    }

    @Test
    fun insetsTest() {
        val value = AtomicInteger()
        value.also {
            val left = it.getAndIncrement()
            val top = it.getAndIncrement()
            val right = it.getAndIncrement()
            val bottom = it.getAndIncrement()
            insets(left = left, top = top, right = right, bottom = bottom)
                .assert(left = left, top = top, right = right, bottom = bottom)
        }
        value.also {
            val left = it.getAndIncrement()
            insets(left = left).assert(left = left, top = 0, right = 0, bottom = 0)
        }
        value.also {
            val left = it.getAndIncrement()
            val right = it.getAndIncrement()
            insets(left = left, right = right)
                .assert(left = left, top = 0, right = right, bottom = 0)
        }
        value.also {
            val top = it.getAndIncrement()
            val bottom = it.getAndIncrement()
            insets(top = top, bottom = bottom)
                .assert(left = 0, top = top, right = 0, bottom = bottom)
        }
    }

    @Test
    fun insetsEqualsTest() {
        val value = AtomicInteger(1)
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        val actual = insets(left = left, top = top, right = right, bottom = bottom)
        listOf(left, top, right, bottom).forEachIndexed { index, it ->
            val values = arrayOf(left, top, right, bottom)
            insets(
                left = values[0],
                top = values[1],
                right = values[2],
                bottom = values[3]
            ).also { expected ->
                assertEquals(expected, actual)
            }
            values[index] = value.get()
            assertNotEquals(it, values[index])
            insets(
                left = values[0],
                top = values[1],
                right = values[2],
                bottom = values[3]
            ).also { unexpected ->
                assertNotEquals(unexpected, actual)
            }
        }
        assertNotEquals(Unit, actual)
        assertFalse(actual.equals(null))
    }

    @Test
    fun insetsHashCodeTest() {
        assertTrue(insets().hashCode() > 0)
        assertTrue(insets(left = 1, top = 2, right = 3, bottom = 4).hashCode() > 0)
    }
}
