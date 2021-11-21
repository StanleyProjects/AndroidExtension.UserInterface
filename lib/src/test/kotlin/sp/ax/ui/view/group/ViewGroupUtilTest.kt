package sp.ax.ui.view.group

import android.view.ViewGroup
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.atomic.AtomicInteger

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class ViewGroupUtilTest {
    companion object {
        private fun ViewGroup.LayoutParams.assert(width: Int, height: Int) {
            assertEquals(width, this.width)
            assertEquals(height, this.height)
        }
    }

    @Test
    fun layoutParamsWidthHeightTest() {
        val value = AtomicInteger(1)
        value.also {
            val width = value.getAndIncrement()
            val height = value.getAndIncrement()
            val layoutParams = ViewGroup::class.layoutParams(width = width, height = height)
            layoutParams.assert(width = width, height = height)
        }
    }

    @Test
    fun layoutParamsSizeTest() {
        val value = AtomicInteger(1)
        value.also {
            val size = value.getAndIncrement()
            val layoutParams = ViewGroup::class.layoutParams(size = size)
            layoutParams.assert(width = size, height = size)
        }
    }

    @Test
    fun layoutParamsWrappedTest() {
        val layoutParams = ViewGroup.LayoutParams::class.wrapped()
        layoutParams.assert(
            width = ViewGroup.LayoutParams.WRAP_CONTENT,
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    @Test
    fun layoutParamsMatchedTest() {
        val layoutParams = ViewGroup.LayoutParams::class.matched()
        layoutParams.assert(
            width = ViewGroup.LayoutParams.MATCH_PARENT,
            height = ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
