package sp.ax.ui.view

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.entity.padding

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class ViewUtilTest {
    companion object {
        private fun assertPadding(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            assertEquals("Left padding is not $left!", left, view.paddingLeft)
            assertEquals("Top padding is not $top!", top, view.paddingTop)
            assertEquals("Right padding is not $right!", right, view.paddingRight)
            assertEquals("Bottom padding is not $bottom!", bottom, view.paddingBottom)
        }
    }

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun getPaddingTest() {
        val view = View(context)
        assertPadding(view, left = 0, top = 0, right = 0, bottom = 0)
        val paddingLeft = 1
        val paddingTop = 2
        val paddingRight = 3
        val paddingBottom = 4
        view.setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            paddingBottom
        )
        assertPadding(view, left = paddingLeft, top = paddingTop, right = paddingRight, bottom = paddingBottom)
        val padding = view.getPadding()
        assertPadding(view, left = padding.left, top = padding.top, right = padding.right, bottom = padding.bottom)
    }

    @Test
    fun setPaddingTest() {
        val view = View(context)
        assertPadding(view, left = 0, top = 0, right = 0, bottom = 0)
        val value = AtomicInteger(1)
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        view.setPadding(
            padding(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        )
        assertPadding(view, left = left, top = top, right = right, bottom = bottom)
    }

    @Test
    fun updatePaddingTest() {
        val view = View(context)
        assertPadding(view, left = 0, top = 0, right = 0, bottom = 0)
        val value = AtomicInteger(1)
        value.also {
            val left = it.getAndIncrement()
            val top = it.getAndIncrement()
            val right = it.getAndIncrement()
            val bottom = it.getAndIncrement()
            view.updatePadding(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
            assertPadding(view, left = left, top = top, right = right, bottom = bottom)
        }
        value.also {
            val left = view.paddingLeft
            val top = it.getAndIncrement()
            val right = view.paddingRight
            val bottom = it.getAndIncrement()
            view.updatePadding(
                top = top,
                bottom = bottom
            )
            assertPadding(view, left = left, top = top, right = right, bottom = bottom)
        }
        value.also {
            val left = it.getAndIncrement()
            val top = view.paddingTop
            val right = it.getAndIncrement()
            val bottom = view.paddingBottom
            view.updatePadding(
                left = left,
                right = right
            )
            assertPadding(view, left = left, top = top, right = right, bottom = bottom)
        }
    }
}
