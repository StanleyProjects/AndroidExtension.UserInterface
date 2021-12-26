package sp.ax.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.entity.Gravity
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.entity.TypeDimension
import sp.ax.ui.entity.TypefaceStyle
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.ViewUtilTest.Companion.assertDefault

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class TextViewUtilTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun textViewDefaultTest() {
        val text = "foo"
        val view = textView(context, text = text)
        view.assertDefault(layoutParams = ViewDefault.layoutParams)
        assertEquals("\"gravity\" is not default!", TextViewDefault.gravity.toInt(), view.gravity)
//        assertEquals("\"textSizeUnit\" is not default!", TextViewDefault.textSizeUnit.toInt(), view.textSizeUnit) // todo api 30
        assertEquals("\"textSize\" is not default!", TextViewDefault.textSize, view.textSize)
        assertEquals("\"currentTextColor\" is not default!", TextViewDefault.textColor, view.currentTextColor)
        assertEquals("\"typeface\" is not default!", TextViewDefault.typeface, view.typeface)
        // todo TextViewDefault.typefaceStyle
//        assertEquals("\"isAllCaps\" is not default!", TextViewDefault.isAllCaps, view.isAllCaps) // todo api 28
        // todo TextViewDefault.textWatchers
    }

    @Test
    fun textViewParametersTest() {
        val text = "foo"
        val gravity = Gravity.BOTTOM_RIGHT
        val textSize = 12.34f
        val textSizeUnit = TypeDimension.PIXEL
        val textColor = Color.GREEN
        val isAllCaps = true
        val typeface = Typeface.SERIF
        val typefaceStyle = TypefaceStyle.NORMAL
        val view = textView(
            context = context,
            gravity = gravity,
            text = text,
            textSize = textSize,
            textSizeUnit = textSizeUnit,
            textColor = textColor,
            isAllCaps = isAllCaps,
            typeface = typeface,
            typefaceStyle = typefaceStyle
        )
        assertEquals("Value by \"text\" is not \"$text\"!", text, view.text)
        assertEquals("Value by \"gravity\" is not \"$gravity\"!", gravity.toInt(), view.gravity)
        assertEquals("Value by \"textSize\" is not \"$textSize\"!", textSize, view.textSize)
        // todo textSizeUnit api 30
        assertEquals("Value by \"textColor\" is not \"$textColor\"!", textColor, view.currentTextColor)
//        assertEquals("Value by \"isAllCaps\" is not \"$isAllCaps\"!", isAllCaps, view.isAllCaps) // todo api 28
        assertEquals("Value by \"typeface\" is not \"$typeface\"!", typeface, view.typeface)
    }

    @Test
    fun textViewTextWatchersTest() {
        var editable = ""
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
        val view = textView(
            context = context,
            text = "1",
            textWatchers = setOf(textWatcher)
        )
        "2".also { edited ->
            assertNotEquals(view.text, edited)
            assertNotEquals(view.text, editable)
            assertNotEquals(edited, editable)
            view.text = edited
            assertEquals(
                "TextChangedListener should change the global variable!",
                edited,
                editable
            )
        }
        "3".also { edited ->
            assertNotEquals(view.text, edited)
            assertNotEquals(edited, editable)
            view.removeTextChangedListener(textWatcher)
            view.text = edited
            assertNotEquals(
                "TextChangedListener removed, so the listener should not change the global variable!",
                edited,
                editable
            )
        }
    }
}
