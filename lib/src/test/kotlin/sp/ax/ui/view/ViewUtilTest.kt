package sp.ax.ui.view

import android.content.Context
import android.os.Build
import android.view.View
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.insets
import java.util.concurrent.atomic.AtomicInteger

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class ViewUtilTest {
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
