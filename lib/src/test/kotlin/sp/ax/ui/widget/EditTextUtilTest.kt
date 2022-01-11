package sp.ax.ui.widget

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.ViewUtilTest.Companion.assertDefault
import sp.ax.ui.view.ViewUtilTest.Companion.assertSetOnClick
import sp.ax.ui.widget.TextViewUtilTest.Companion.assertTextWatcher

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class EditTextUtilTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun editTextDefaultTest() {
        val view = editText(context)
        view.assertDefault(layoutParams = ViewDefault.layoutParams)
        assertEquals("\"gravity\" is not default!", TextViewDefault.gravity.toInt(), view.gravity)
//        assertEquals("\"textSizeUnit\" is not default!", TextViewDefault.textSizeUnit.toInt(), view.textSizeUnit) // todo api 30
        assertEquals("\"textSize\" is not default!", TextViewDefault.textSize, view.textSize)
        assertEquals("\"currentTextColor\" is not default!", TextViewDefault.textColor, view.currentTextColor)
        assertEquals("\"typeface\" is not default!", TextViewDefault.typeface, view.typeface)
        // todo TextViewDefault.typefaceStyle
//        assertEquals("\"isAllCaps\" is not default!", TextViewDefault.isAllCaps, view.isAllCaps) // todo api 28
        // todo TextViewDefault.textWatchers
        assertTrue("\"text\" is not default!", view.text.isEmpty())
    }

    @Test
    fun textViewTextWatchersTest() {
        assertTextWatcher { initial, textWatcher ->
            editText(
                context = context,
                text = initial,
                textWatchers = setOf(textWatcher)
            )
        }
    }

    @Test
    fun editTextSetOnClickTest() {
        assertSetOnClick { onClick: () -> Unit, onLongClick: () -> Boolean ->
            editText(
                context = context,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }
}
