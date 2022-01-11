package sp.ax.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.Visibility.Companion.toInt
import sp.ax.ui.entity.insets
import sp.ax.ui.onFields
import sp.ax.ui.view.group.ViewGroupUtilTest.Companion.assertEquals
import java.util.concurrent.atomic.AtomicInteger

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class ViewUtilTest {
    companion object {
        private fun View.assertPadding(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            mapOf(
                "left" to (left to paddingLeft),
                "top" to (top to paddingTop),
                "right" to (right to paddingRight),
                "bottom" to (bottom to paddingBottom),
            ).forEach { (key, value) ->
                val (expected, actual) = value
                assertEquals("Padding $key is not $expected!", expected, actual)
            }
        }

        internal fun View.assert(
            layoutParams: ViewGroup.LayoutParams,
            id: Int,
            background: Drawable,
            visibility: Int,
            paddingLeft: Int,
            paddingRight: Int,
            paddingTop: Int,
            paddingBottom: Int,
            isClickable: Boolean,
            isLongClickable: Boolean,
            keepScreenOn: Boolean
        ) {
            this.layoutParams.assertEquals(expected = layoutParams)
            assertEquals("Id is not expected!", id, this.id)
            assertEquals("Background is not expected!", background, this.background)
            assertEquals("Visibility is not expected!", visibility, this.visibility)
            assertPadding(left = paddingLeft, right = paddingRight, top = paddingTop, bottom = paddingBottom)
            assertEquals("Property \"isClickable\" is not expected!", isClickable, this.isClickable)
            assertEquals("Property \"isLongClickable\" is not expected!", isLongClickable, this.isLongClickable)
            assertEquals("Property \"keepScreenOn\" is not expected!", keepScreenOn, this.keepScreenOn)
        }

        internal fun View.assert(expected: View) {
            assert(
                layoutParams = expected.layoutParams,
                id = expected.id,
                background = expected.background,
                visibility = expected.visibility,
                paddingLeft = expected.paddingLeft,
                paddingRight = expected.paddingRight,
                paddingTop = expected.paddingTop,
                paddingBottom = expected.paddingBottom,
                isClickable = expected.isClickable,
                isLongClickable = expected.isLongClickable,
                keepScreenOn = expected.keepScreenOn
            )
        }

        internal fun View.assertDefault(layoutParams: ViewGroup.LayoutParams) {
            ViewDefault::class.onFields { field ->
                when (field.name) {
                    "id" -> {
                        assertEquals("\"" + field.name + "\" is not default!", ViewDefault.id, this.id)
                    }
                    "layoutParams" -> {
                        this.layoutParams.assertEquals(expected = layoutParams)
                    }
                    "background" -> {
                        assertEquals("\"" + field.name + "\" is not default!", ViewDefault.background, this.background)
                    }
                    "visibility" -> {
                        assertEquals(
                            "\"" + field.name + "\" is not default!",
                            ViewDefault.visibility.toInt(),
                            this.visibility
                        )
                    }
                    "padding" -> {
                        assertEquals("\"" + field.name + "\" is not default!", ViewDefault.padding, this.getPadding())
                    }
                    "keepScreenOn" -> {
                        assertEquals("\"" + field.name + "\" is not default!", ViewDefault.keepScreenOn, this.keepScreenOn)
                    }
                    "onClick" -> {
                        assertFalse("\"" + field.name + "\" is not default!", this.hasOnClickListeners())
                    }
                    "onLongClick" -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            assertFalse("\"" + field.name + "\" is not default!", this.hasOnLongClickListeners())
                        }
                    }
                    "isClickable" -> {
                        assertFalse("\"" + field.name + "\" is not default!", this.isClickable)
                    }
                    "isLongClickable" -> {
                        assertFalse("\"" + field.name + "\" is not default!", this.isLongClickable)
                    }
                }
            }
        }

        internal fun assertSetOnClick(supplier: (onClick: () -> Unit, onLongClick: () -> Boolean) -> View) {
            val init = 11
            var value = init
            val first = 21
            val second = 31
            assertNotEquals(first, value)
            assertNotEquals(second, value)
            assertNotEquals(first, second)
            val onClick: () -> Unit = {
                value = first
            }
            assertNotEquals(UNSPECIFIED_ON_CLICK, onClick)
            val onLongClick: () -> Boolean = {
                value = second
                true
            }
            assertNotEquals(UNSPECIFIED_ON_LONG_CLICK, onLongClick)
            val view = supplier(onClick, onLongClick)
            assertTrue(view.hasOnClickListeners())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                assertTrue(view.hasOnLongClickListeners())
            }
            assertEquals(init, value)
            view.performClick()
            assertEquals(first, value)
            view.performLongClick()
            assertEquals(second, value)
            view.performClick()
            assertEquals(first, value)
            view.performLongClick()
            assertEquals(second, value)
        }
    }

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun getPaddingTest() {
        val view = View(context)
        view.assertPadding(left = 0, top = 0, right = 0, bottom = 0)
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
        view.assertPadding(left = paddingLeft, top = paddingTop, right = paddingRight, bottom = paddingBottom)
        val padding: Insets = view.getPadding()
        view.assertPadding(left = padding.left, top = padding.top, right = padding.right, bottom = padding.bottom)
    }

    @Test
    fun setPaddingTest() {
        val view = View(context)
        view.assertPadding(left = 0, top = 0, right = 0, bottom = 0)
        val value = AtomicInteger(1)
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        view.setPadding(
            insets(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        )
        view.assertPadding(left = left, top = top, right = right, bottom = bottom)
    }

    @Test
    fun updatePaddingTest() {
        val view = View(context)
        view.assertPadding(left = 0, top = 0, right = 0, bottom = 0)
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
            view.assertPadding(left = left, top = top, right = right, bottom = bottom)
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
            view.assertPadding(left = left, top = top, right = right, bottom = bottom)
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
            view.assertPadding(left = left, top = top, right = right, bottom = bottom)
        }
    }

    @Test
    fun viewDefaultTest() {
        view(context).assertDefault(layoutParams = ViewDefault.layoutParams)
    }

    @Test
    fun viewHasClickListenersTest() {
        val view = view(context)
        assertFalse(view.hasOnClickListeners())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            assertFalse(view.hasOnLongClickListeners())
        }
    }

    @Test
    fun viewSetOnClickTest() {
        assertSetOnClick { onClick: () -> Unit, onLongClick: () -> Boolean ->
            view(
                context = context,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }

    @Test
    fun viewDefaultClickTest() {
        assertThrows(IllegalStateException::class.java) {
            ViewDefault.onClick()
        }
        assertThrows(IllegalStateException::class.java) {
            ViewDefault.onLongClick()
        }
    }

    @Test
    fun viewActionTest() {
        val init = 11
        val value = AtomicInteger(init)
        val first = 21
        assertNotEquals(first, value.get())
        assertEquals(init, value.get())
        view(context) {
            value.set(first)
        }
        assertEquals(first, value.get())
    }
}
