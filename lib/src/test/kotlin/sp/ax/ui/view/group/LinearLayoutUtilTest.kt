package sp.ax.ui.view.group

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.entity.Orientation
import sp.ax.ui.entity.Visibility
import sp.ax.ui.entity.insets
import sp.ax.ui.fail
import sp.ax.ui.getPrivateInt
import sp.ax.ui.view.ViewUtilTest.Companion.assert
import sp.ax.ui.view.ViewUtilTest.Companion.assertDefault
import sp.ax.ui.view.ViewUtilTest.Companion.assertSetOnClick
import sp.ax.ui.view.group.LinearLayoutUtilTest.Companion.assert
import sp.ax.ui.view.group.ViewGroupUtilTest.Companion.assert
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

internal fun KClass<LinearLayout.LayoutParams>.assertEquals(
    actual: LinearLayout.LayoutParams,
    expected: LinearLayout.LayoutParams
) {
    actual.assert(
        width = expected.width,
        height = expected.height,
        left = expected.leftMargin,
        top = expected.topMargin,
        right = expected.rightMargin,
        bottom = expected.bottomMargin,
        weight = expected.weight
    )
}

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class LinearLayoutUtilTest {
    companion object {
        internal fun LinearLayout.LayoutParams.assert(
            width: Int,
            height: Int,
            weight: Float,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ) {
            assert(
                width = width,
                height = height,
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
            assertEquals("Weight is not expected!", weight, this.weight)
        }

        private fun getGravity(view: LinearLayout): Int {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return view.getPrivateInt("mGravity")
            }
            return view.gravity
        }

        private fun getOrientation(view: LinearLayout): Orientation {
            return when (view.orientation) {
                LinearLayout.VERTICAL -> Orientation.VERTICAL
                LinearLayout.HORIZONTAL -> Orientation.HORIZONTAL
                else -> fail("Expected only LinearLayout.VERTICAL or LinearLayout.HORIZONTAL!")
            }
        }
    }

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun setOrientationTest() {
        val view = LinearLayout(context)
        assertEquals(view.orientation, LinearLayout.HORIZONTAL)
        view.setOrientation(Orientation.VERTICAL)
        assertEquals(view.orientation, LinearLayout.VERTICAL)
        view.setOrientation(Orientation.HORIZONTAL)
        assertEquals(view.orientation, LinearLayout.HORIZONTAL)
    }

    @Test
    fun layoutParamsTest() {
        val value = AtomicInteger(1)
        val width = value.getAndIncrement()
        val height = value.getAndIncrement()
        val weight = value.getAndIncrement().toFloat()
        val left = value.getAndIncrement()
        val top = value.getAndIncrement()
        val right = value.getAndIncrement()
        val bottom = value.getAndIncrement()
        val layoutParams: LinearLayout.LayoutParams = LinearLayout::class.layoutParams(
            width = width,
            height = height,
            weight = weight,
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
            weight = weight,
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }

    @Test
    fun linearLayoutDefaultTest() {
        val view = linearLayout(context)
        view.assertDefault(layoutParams = LinearLayoutDefault.layoutParams)
        assertEquals("\"orientation\" is not default!", LinearLayoutDefault.orientation, getOrientation(view))
        assertEquals("\"gravity\" is not default!", LinearLayoutDefault.gravity.toInt(), getGravity(view))
    }

    @Test
    fun viewDefaultTest() {
        val root = LinearLayout(context)
        val view = root.view()
        view.assertDefault(
            layoutParams = LinearLayout.LayoutParams::class.wrapped(
                weight = LinearLayoutDefault.LayoutParams.weight,
                margin = LinearLayoutDefault.LayoutParams.margin
            )
        )
        assertEquals("Child count is not 1!", 1, root.childCount)
    }

    @Test
    fun viewAddedTest() {
        val root = LinearLayout(context)
        val value = AtomicInteger(1)
        val width = value.getAndIncrement()
        val height = value.getAndIncrement()
        val weight: Float = value.getAndIncrement().toFloat()
        val margin = insets(
            left = value.getAndIncrement(),
            top = value.getAndIncrement(),
            right = value.getAndIncrement(),
            bottom = value.getAndIncrement()
        )
        val id = value.getAndIncrement()
        val background = ColorDrawable(value.getAndIncrement())
        val visibility: Int = View.VISIBLE
        val padding = insets(
            left = value.getAndIncrement(),
            top = value.getAndIncrement(),
            right = value.getAndIncrement(),
            bottom = value.getAndIncrement()
        )
        val isClickable: Boolean = true
        val isLongClickable: Boolean = true
        val keepScreenOn: Boolean = true
        val view = root.view(
            id = id,
            width = width,
            height = height,
            weight = weight,
            margin = margin,
            background = background,
            visibility = Visibility.fromInt(visibility),
            padding = padding,
            isClickable = isClickable,
            isLongClickable = isLongClickable,
            keepScreenOn = keepScreenOn
        )
        val layoutParams: ViewGroup.LayoutParams = LinearLayout::class.layoutParams(
            width = width,
            height = height,
            weight = weight,
            margin = margin
        )
        view.assert(
            id = id,
            layoutParams = layoutParams,
            background = background,
            visibility = visibility,
            paddingLeft = padding.left,
            paddingTop = padding.top,
            paddingRight = padding.right,
            paddingBottom = padding.bottom,
            isClickable = isClickable,
            isLongClickable = isLongClickable,
            keepScreenOn = keepScreenOn
        )
        assertEquals("Child count is not 1!", 1, root.childCount)
        val child: View = root.getChildAt(0)
        child.assert(expected = view)
    }

    @Test
    fun viewNotAddedTest() {
        val root = LinearLayout(context)
        root.view(needToAdd = false)
        assertEquals("Child count is not 0!", 0, root.childCount)
    }

    @Test
    fun linearLayoutSetOnClickTest() {
        assertSetOnClick { onClick: () -> Unit, onLongClick: () -> Boolean ->
            linearLayout(
                context = context,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }

    @Test
    fun viewSetOnClickTest() {
        val root = LinearLayout(context)
        assertSetOnClick { onClick: () -> Unit, onLongClick: () -> Boolean ->
            root.view(
                context = context,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }
}
