package sp.ax.ui.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sp.ax.ui.entity.Gravity.Companion.toInt
import android.view.Gravity as AndroidGravity

class GravityTest {
    @Test
    fun toIntTest() {
        Gravity.values().map {
            it to when (it) {
                Gravity.TOP -> AndroidGravity.TOP or AndroidGravity.CENTER_HORIZONTAL
                Gravity.TOP_LEFT -> AndroidGravity.TOP or AndroidGravity.LEFT
                Gravity.TOP_RIGHT -> AndroidGravity.TOP or AndroidGravity.RIGHT
                Gravity.BOTTOM -> AndroidGravity.BOTTOM or AndroidGravity.CENTER_HORIZONTAL
                Gravity.BOTTOM_LEFT -> AndroidGravity.BOTTOM or AndroidGravity.LEFT
                Gravity.BOTTOM_RIGHT -> AndroidGravity.BOTTOM or AndroidGravity.RIGHT
                Gravity.LEFT -> AndroidGravity.LEFT or AndroidGravity.CENTER_VERTICAL
                Gravity.RIGHT -> AndroidGravity.RIGHT or AndroidGravity.CENTER_VERTICAL
                Gravity.CENTER -> AndroidGravity.CENTER
            }
        }.forEach { (gravity, expected) ->
            val actual = gravity.toInt()
            assertEquals("An expected android integer for Gravity.${gravity.name} is $expected, but actual is $actual!", expected, actual)
        }
    }
}
