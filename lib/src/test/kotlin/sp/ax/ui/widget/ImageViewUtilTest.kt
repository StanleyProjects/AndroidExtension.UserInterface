package sp.ax.ui.widget

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.assertDefault

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class ImageViewUtilTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun imageViewDefaultTest() {
        imageView(context).assertDefault(layoutParams = ViewDefault.layoutParams)
    }
}
