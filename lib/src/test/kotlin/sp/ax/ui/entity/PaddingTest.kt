package sp.ax.ui.entity

import org.junit.Assert.assertEquals
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
            assertEquals("Left padding is not $left!", left, this.left)
            assertEquals("Top padding is not $top!", top, this.top)
            assertEquals("Right padding is not $right!", right, this.right)
            assertEquals("Bottom padding is not $bottom!", bottom, this.bottom)
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
}
