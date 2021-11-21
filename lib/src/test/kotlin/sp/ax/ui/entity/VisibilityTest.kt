package sp.ax.ui.entity

import android.view.View
import org.junit.Assert.assertEquals
import org.junit.Test
import sp.ax.ui.exhaustive

class VisibilityTest {
    @Test
    fun asViewValueTest() {
        Visibility.values().forEach {
            when (it) {
                Visibility.VISIBLE -> {
                    assertEquals(View.VISIBLE, it.asViewValue())
                }
                Visibility.INVISIBLE -> {
                    assertEquals(View.INVISIBLE, it.asViewValue())
                }
                Visibility.GONE -> {
                    assertEquals(View.GONE, it.asViewValue())
                }
            }.exhaustive()
        }
    }
}
