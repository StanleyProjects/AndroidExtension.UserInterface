package sp.ax.ui

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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

    @Test
    fun getPaddingTest() {
        val context: Context = ApplicationProvider.getApplicationContext()
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
}
