package sp.ax.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import sp.ax.ui.getPrivateList
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.assertSetOnClick
import java.util.concurrent.atomic.AtomicInteger

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class TextViewUtilTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun textViewDefaultTest() {
        val text = "foo"
        assertDefault(
            view = textView(context, text = text),
            layoutParams = ViewDefault.layoutParams,
            text = text
        )
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
        assertTextWatcher { initial, textWatcher ->
            textView(
                context = context,
                text = initial,
                textWatchers = setOf(textWatcher)
            )
        }
    }

    @Test
    fun addOnTextChangedTest() {
        val value = AtomicInteger(0)
        var editable = value.getAndIncrement().toString()
        val view = textView(context, text = value.getAndIncrement().toString())
        view.addOnTextChanged {
            editable = it.toString()
        }
        value.getAndIncrement().toString().also { edited ->
            assertNotEquals(
                "Text before and after setting should not equal but both are \"${view.text}\"!",
                view.text.toString(),
                edited
            )
            assertNotEquals(
                "Text before and after catching should not equal but both are \"${view.text}\"!",
                view.text.toString(),
                editable
            )
            assertNotEquals(edited, editable)
            view.text = edited
            assertEquals(
                "TextChangedListener should change the global variable!",
                edited,
                editable
            )
        }
        value.getAndIncrement().toString().also { edited ->
            assertNotEquals(view.text.toString(), edited)
            assertEquals(view.text.toString(), editable)
            val listeners: List<TextWatcher> = view.getPrivateList("mListeners")
            view.removeTextChangedListener(listeners.single())
            view.text = edited
            assertNotEquals(
                "TextChangedListener removed, so the listener should not change the global variable!",
                edited,
                editable
            )
        }
    }

    @Test
    fun textViewSetOnClickTest() {
        assertSetOnClick { onClick: () -> Unit, onLongClick: () -> Boolean ->
            textView(
                context = context,
                onClick = onClick,
                onLongClick = onLongClick,
                text = ""
            )
        }
    }
}
