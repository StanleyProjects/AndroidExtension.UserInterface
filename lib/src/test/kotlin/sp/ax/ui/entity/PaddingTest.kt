package sp.ax.ui.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class PaddingTest {
    companion object {
        private fun Padding.assert(
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
                assertEquals("Padding $key is not $expected!", expected, actual)
            }
        }
    }

    @Test
    fun paddingTest() {
        val value = AtomicInteger()
        value.also {
            val left = it.getAndIncrement()
            val top = it.getAndIncrement()
            val right = it.getAndIncrement()
            val bottom = it.getAndIncrement()
            padding(left = left, top = top, right = right, bottom = bottom)
                .assert(left = left, top = top, right = right, bottom = bottom)
        }
        value.also {
            val left = it.getAndIncrement()
            padding(left = left).assert(left = left, top = 0, right = 0, bottom = 0)
        }
        value.also {
            val left = it.getAndIncrement()
            val right = it.getAndIncrement()
            padding(left = left, right = right)
                .assert(left = left, top = 0, right = right, bottom = 0)
        }
        value.also {
            val top = it.getAndIncrement()
            val bottom = it.getAndIncrement()
            padding(top = top, bottom = bottom)
                .assert(left = 0, top = top, right = 0, bottom = bottom)
        }
    }

    @Test
    fun paddingEqualsTest() {
        val value = AtomicInteger(1)
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        val actual = padding(left = left, top = top, right = right, bottom = bottom)
        listOf(left, top, right, bottom).forEachIndexed { index, it ->
            val values = arrayOf(left, top, right, bottom)
            padding(
                left = values[0],
                top = values[1],
                right = values[2],
                bottom = values[3]
            ).also { expected ->
                assertEquals(expected, actual)
            }
            values[index] = value.get()
            assertNotEquals(it, values[index])
            padding(
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
    fun paddingHashCodeTest() {
        assertTrue(padding().hashCode() > 0)
        assertTrue(padding(left = 1, top = 2, right = 3, bottom = 4).hashCode() > 0)
    }
}
