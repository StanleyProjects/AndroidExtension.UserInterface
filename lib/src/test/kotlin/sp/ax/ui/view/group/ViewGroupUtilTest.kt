package sp.ax.ui.view.group

import android.view.ViewGroup
import android.widget.LinearLayout
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.assertType
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.insets
import sp.ax.ui.view.group.ViewGroupUtilTest.Companion.assert
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

internal fun KClass<ViewGroup.MarginLayoutParams>.assertEquals(
    actual: ViewGroup.MarginLayoutParams,
    expected: ViewGroup.MarginLayoutParams
) {
    actual.assert(
        width = expected.width,
        height = expected.height,
        left = expected.leftMargin,
        top = expected.topMargin,
        right = expected.rightMargin,
        bottom = expected.bottomMargin
    )
}

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class ViewGroupUtilTest {
    companion object {
        internal fun ViewGroup.LayoutParams.assertEquals(expected: ViewGroup.LayoutParams) {
            when (val actual = this) {
                is LinearLayout.LayoutParams -> {
                    LinearLayout.LayoutParams::class.assertEquals(
                        actual = actual,
                        expected = expected.assertType()
                    )
                }
                is ViewGroup.MarginLayoutParams -> {
                    ViewGroup.MarginLayoutParams::class.assertEquals(
                        actual = actual,
                        expected = expected.assertType()
                    )
                }
                else -> {
                    actual.assert(width = expected.width, height = expected.height)
                }
            }
        }

        internal fun ViewGroup.LayoutParams.assert(width: Int, height: Int) {
            assertEquals(width, this.width)
            assertEquals(height, this.height)
        }

        internal fun ViewGroup.MarginLayoutParams.assert(
            width: Int,
            height: Int,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            assert(width = width, height = height)
            assertMargin(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        }

        private fun ViewGroup.MarginLayoutParams.assertMargin(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            mapOf(
                "left" to (left to leftMargin),
                "top" to (top to topMargin),
                "right" to (right to rightMargin),
                "bottom" to (bottom to bottomMargin),
            ).forEach { (key, value) ->
                val (expected, actual) = value
                assertEquals("Margin $key is not $expected!", expected, actual)
            }
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

    @Test
    fun getMarginTest() {
        val value = AtomicInteger(1)
        val layoutParams: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(
            value.getAndIncrement(),
            value.getAndIncrement()
        )
        layoutParams.assertMargin(left = 0, top = 0, right = 0, bottom = 0)
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        layoutParams.setMargins(
            left,
            top,
            right,
            bottom
        )
        layoutParams.assertMargin(left = left, top = top, right = right, bottom = bottom)
        val margin: Insets = layoutParams.getMargin()
        layoutParams.assertMargin(left = margin.left, top = margin.top, right = margin.right, bottom = margin.bottom)
    }

    @Test
    fun setPaddingTest() {
        val value = AtomicInteger(1)
        val layoutParams: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(
            value.getAndIncrement(),
            value.getAndIncrement()
        )
        layoutParams.assertMargin(left = 0, top = 0, right = 0, bottom = 0)
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        layoutParams.setMargin(
            insets(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        )
        layoutParams.assertMargin(left = left, top = top, right = right, bottom = bottom)
    }

    @Test
    fun marginLayoutParamsTest() {
        val value = AtomicInteger(1)
        val width = value.getAndIncrement()
        val height = value.getAndIncrement()
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        val layoutParams: ViewGroup.MarginLayoutParams = ViewGroup::class.layoutParams(
            width = width,
            height = height,
            margin = insets(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        )
        layoutParams.assert(
            width = width,
            height = height,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }
}
