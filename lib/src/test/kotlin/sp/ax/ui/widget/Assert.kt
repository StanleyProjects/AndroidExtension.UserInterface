package sp.ax.ui.widget

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.TextView
import org.junit.Assert
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.view.assertDefault
import java.util.concurrent.atomic.AtomicInteger

internal fun assertTextWatcher(supplier: (initial: String, TextWatcher) -> TextView) {
    val value = AtomicInteger(0)
    var editable = value.getAndIncrement().toString()
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            // ignored
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // ignored
        }

        override fun afterTextChanged(s: Editable?) {
            editable = s!!.toString()
        }
    }
    val view: TextView = supplier(value.getAndIncrement().toString(), textWatcher)
    value.getAndIncrement().toString().also { edited ->
        Assert.assertNotEquals(
            "Text before and after setting should not equal but both are \"${view.text}\"!",
            view.text.toString(),
            edited
        )
        Assert.assertNotEquals(
            "Text before and after catching should not equal but both are \"${view.text}\"!",
            view.text.toString(),
            editable
        )
        Assert.assertNotEquals(edited, editable)
        view.text = edited
        Assert.assertEquals(
            "TextChangedListener should change the global variable!",
            edited,
            editable
        )
    }
    value.getAndIncrement().toString().also { edited ->
        Assert.assertNotEquals(view.text.toString(), edited)
        Assert.assertNotEquals(edited, editable)
        view.removeTextChangedListener(textWatcher)
        view.text = edited
        Assert.assertNotEquals(
            "TextChangedListener removed, so the listener should not change the global variable!",
            edited,
            editable
        )
    }
}

internal fun assertDefault(view: TextView, layoutParams: ViewGroup.LayoutParams, text: String) {
    view.assertDefault(layoutParams = layoutParams)
    Assert.assertEquals("\"gravity\" is not default!", TextViewDefault.gravity.toInt(), view.gravity)
//        assertEquals("\"textSizeUnit\" is not default!", TextViewDefault.textSizeUnit.toInt(), view.textSizeUnit) // todo api 30
    Assert.assertEquals("\"textSize\" is not default!", TextViewDefault.textSize, view.textSize)
    Assert.assertEquals("\"currentTextColor\" is not default!", TextViewDefault.textColor, view.currentTextColor)
    Assert.assertEquals("\"typeface\" is not default!", TextViewDefault.typeface, view.typeface)
    // todo TextViewDefault.typefaceStyle
//        assertEquals("\"isAllCaps\" is not default!", TextViewDefault.isAllCaps, view.isAllCaps) // todo api 28
    // todo TextViewDefault.textWatchers
    Assert.assertEquals("\"text\" is not default!", text, view.text.toString())
}
