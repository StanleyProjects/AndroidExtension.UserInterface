package sp.ax.ui.view

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import org.junit.Assert
import sp.ax.ui.entity.Visibility.Companion.toInt
import sp.ax.ui.view.group.assertEquals

fun View.assertPadding(
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
        Assert.assertEquals("Padding $key is not $expected!", expected, actual)
    }
}

fun View.assert(
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
    Assert.assertEquals("Id is not expected!", id, this.id)
    Assert.assertEquals("Background is not expected!", background, this.background)
    Assert.assertEquals("Visibility is not expected!", visibility, this.visibility)
    assertPadding(left = paddingLeft, right = paddingRight, top = paddingTop, bottom = paddingBottom)
    Assert.assertEquals("Property \"isClickable\" is not expected!", isClickable, this.isClickable)
    Assert.assertEquals("Property \"isLongClickable\" is not expected!", isLongClickable, this.isLongClickable)
    Assert.assertEquals("Property \"keepScreenOn\" is not expected!", keepScreenOn, this.keepScreenOn)
}

fun View.assertEquals(expected: View) {
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

fun View.assertDefault(layoutParams: ViewGroup.LayoutParams) {
    mapOf(
        "id" to (ViewDefault.id to id),
        "visibility" to (ViewDefault.visibility.toInt() to visibility),
        "padding" to (ViewDefault.padding to getPadding()),
        "background" to (ViewDefault.background to background),
        "keepScreenOn" to (ViewDefault.keepScreenOn to keepScreenOn)
    ).forEach { (name, values) ->
        val (expected, actual) = values
        Assert.assertEquals("\"$name\" is not default!", expected, actual)
    }
    mapOf(
        "onClick" to hasOnClickListeners(),
        "isClickable" to isClickable,
        "isLongClickable" to isLongClickable,
    ).forEach { (name, condition) ->
        Assert.assertFalse("\"$name\" is not default!", condition)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Assert.assertFalse("\"onLongClick\" is not default!", hasOnLongClickListeners())
    }
    layoutParams.assertEquals(expected = layoutParams)
}

fun assertSetOnClick(supplier: (onClick: () -> Unit, onLongClick: () -> Boolean) -> View) {
    val init = 11
    var value = init
    val first = 21
    val second = 31
    Assert.assertNotEquals(first, value)
    Assert.assertNotEquals(second, value)
    Assert.assertNotEquals(first, second)
    val onClick: () -> Unit = {
        value = first
    }
    Assert.assertNotEquals(UNSPECIFIED_ON_CLICK, onClick)
    val onLongClick: () -> Boolean = {
        value = second
        true
    }
    Assert.assertNotEquals(UNSPECIFIED_ON_LONG_CLICK, onLongClick)
    val view = supplier(onClick, onLongClick)
    Assert.assertTrue(view.hasOnClickListeners())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Assert.assertTrue(view.hasOnLongClickListeners())
    }
    Assert.assertEquals(init, value)
    view.performClick()
    Assert.assertEquals(first, value)
    view.performLongClick()
    Assert.assertEquals(second, value)
    view.performClick()
    Assert.assertEquals(first, value)
    view.performLongClick()
    Assert.assertEquals(second, value)
}
