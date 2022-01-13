package sp.ax.ui.widget

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sp.ax.ui.BuildConfig
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.assertSetOnClick

@Config(manifest = Config.NONE, minSdk = BuildConfig.MIN_SDK, maxSdk = BuildConfig.TARGET_SDK)
@RunWith(RobolectricTestRunner::class)
class EditTextUtilTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun editTextDefaultTest() {
        assertDefault(
            view = editText(context),
            layoutParams = ViewDefault.layoutParams,
            text = ""
        )
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
