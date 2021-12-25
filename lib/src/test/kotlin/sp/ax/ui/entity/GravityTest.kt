package sp.ax.ui.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.exhaustive
import android.view.Gravity as AndroidGravity

class GravityTest {
    @Test
    fun toIntTest() {
        Gravity.values().forEach {
            when (it) {
                Gravity.LEFT -> assertEquals(AndroidGravity.LEFT, it.toInt())
                Gravity.TOP -> assertEquals(AndroidGravity.TOP, it.toInt())
                Gravity.RIGHT -> assertEquals(AndroidGravity.RIGHT, it.toInt())
                Gravity.BOTTOM -> assertEquals(AndroidGravity.BOTTOM, it.toInt())
                Gravity.TOP_LEFT -> assertEquals(AndroidGravity.TOP or AndroidGravity.LEFT, it.toInt())
                Gravity.CENTER -> assertEquals(AndroidGravity.CENTER, it.toInt())
                Gravity.TOP_RIGHT -> TODO()
                Gravity.BOTTOM_LEFT -> TODO()
                Gravity.BOTTOM_RIGHT -> TODO()
            }.exhaustive()
        }
    }
}
