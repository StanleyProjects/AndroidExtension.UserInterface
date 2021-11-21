package sp.ax.ui.view

import android.content.Context
import android.os.Build
import android.view.View
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
import sp.ax.ui.entity.asViewValue
import sp.ax.ui.entity.padding
import sp.ax.ui.onFields
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
        val padding = view.getPadding()
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
            padding(
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
        val view = view(context)
        ViewDefault::class.onFields { field ->
            when (field.name) {
                "id" -> {
                    assertEquals("\"" + field.name + "\" is not default!", ViewDefault.id, view.id)
                }
                "layoutParams" -> {
                    assertEquals("\"" + field.name + "\" is not default!", ViewDefault.layoutParams, view.layoutParams)
                }
                "background" -> {
                    assertEquals("\"" + field.name + "\" is not default!", ViewDefault.background, view.background)
                }
                "visibility" -> {
                    assertEquals(
                        "\"" + field.name + "\" is not default!",
                        ViewDefault.visibility.asViewValue(),
                        view.visibility
                    )
                }
                "padding" -> {
                    assertEquals("\"" + field.name + "\" is not default!", ViewDefault.padding, view.getPadding())
                }
                "keepScreenOn" -> {
                    assertEquals("\"" + field.name + "\" is not default!", ViewDefault.keepScreenOn, view.keepScreenOn)
                }
                "onClick" -> {
                    assertFalse("\"" + field.name + "\" is not default!", view.hasOnClickListeners())
                }
                "onLongClick" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        assertFalse("\"" + field.name + "\" is not default!", view.hasOnLongClickListeners())
                    }
                }
                "isClickable" -> {
                    assertFalse("\"" + field.name + "\" is not default!", view.isClickable)
                }
                "isLongClickable" -> {
                    assertFalse("\"" + field.name + "\" is not default!", view.isLongClickable)
                }
            }
        }
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
        val init = 11
        val value = AtomicInteger(init)
        val first = 21
        val second = 31
        assertNotEquals(first, value.get())
        assertNotEquals(second, value.get())
        assertNotEquals(first, second)
        val onClick: () -> Unit = {
            value.set(first)
        }
        assertNotEquals(UNSPECIFIED_ON_CLICK, onClick)
        val onLongClick: () -> Boolean = {
            value.set(second)
            true
        }
        assertNotEquals(UNSPECIFIED_ON_LONG_CLICK, onLongClick)
        val view = view(
            context = context,
            onClick = onClick,
            onLongClick = onLongClick
        )
        assertTrue(view.hasOnClickListeners())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            assertTrue(view.hasOnLongClickListeners())
        }
        assertEquals(init, value.get())
        view.performClick()
        assertEquals(first, value.get())
        view.performLongClick()
        assertEquals(second, value.get())
        view.performClick()
        assertEquals(first, value.get())
        view.performLongClick()
        assertEquals(second, value.get())
    }

    @Test
    fun viewDefaultClickTest() {
        assertThrows(Throwable::class.java) {
            ViewDefault.onClick()
        }
        assertThrows(Throwable::class.java) {
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
