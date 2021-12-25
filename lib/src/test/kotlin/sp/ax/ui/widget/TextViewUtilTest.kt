package sp.ax.ui.widget

import android.content.Context
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.entity.Gravity
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.entity.TypeDimension
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
    }

    @Test
    fun textViewParametersTest() {
        val text = "foo"
        val gravity = Gravity.BOTTOM_RIGHT
        val textSize = 12.34f
        val textSizeUnit = TypeDimension.PIXEL
        val textColor = Color.GREEN
        val view = textView(
            context = context,
            gravity = gravity,
            text = text,
            textSize = textSize,
            textSizeUnit = textSizeUnit,
            textColor = textColor
        )
        assertEquals("Value by \"text\" is not \"$text\"!", text, view.text)
        assertEquals("Value by \"gravity\" is not \"$gravity\"!", gravity.toInt(), view.gravity)
        assertEquals("Value by \"textSize\" is not \"$textSize\"!", textSize, view.textSize)
        // todo textSizeUnit api 30
        assertEquals("Value by \"textColor\" is not \"$textColor\"!", textColor, view.currentTextColor)
    }
}
