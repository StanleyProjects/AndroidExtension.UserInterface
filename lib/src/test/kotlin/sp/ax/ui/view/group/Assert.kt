package sp.ax.ui.view.group

import android.view.ViewGroup
import android.widget.LinearLayout
import org.junit.Assert
import sp.ax.ui.assertType
import kotlin.reflect.KClass

fun ViewGroup.LayoutParams.assert(width: Int, height: Int) {
    Assert.assertEquals(width, this.width)
    Assert.assertEquals(height, this.height)
}

fun ViewGroup.MarginLayoutParams.assert(
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

fun ViewGroup.MarginLayoutParams.assertMargin(
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
        Assert.assertEquals("Margin $key is not $expected!", expected, actual)
    }
}

fun KClass<ViewGroup.MarginLayoutParams>.assertEquals(
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

fun LinearLayout.LayoutParams.assert(
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
    Assert.assertEquals("Weight is not expected!", weight, this.weight)
}

fun KClass<LinearLayout.LayoutParams>.assertEquals(
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

fun ViewGroup.LayoutParams.assertEquals(expected: ViewGroup.LayoutParams) {
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
