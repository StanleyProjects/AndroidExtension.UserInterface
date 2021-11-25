package sp.ax.ui.entity

/**
 * A type to represent paddings of some view.
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
interface Padding {
    /**
     * Represent the value from [android.view.View.getPaddingLeft].
     */
    val left: Int
    /**
     * Represent the value from [android.view.View.getPaddingTop].
     */
    val top: Int
    /**
     * Represent the value from [android.view.View.getPaddingRight].
     */
    val right: Int
    /**
     * Represent the value from [android.view.View.getPaddingBottom].
     */
    val bottom: Int
}

private class PaddingImpl(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int
) : Padding {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Padding -> {
                left == other.left &&
                    top == other.top &&
                    right == other.right &&
                    bottom == other.bottom
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return listOf(left, top, right, bottom).fold(17) { acc, it -> 31 * acc + it }
    }
}

/**
 * @return An instance of [Padding] by [left], [top], [right] and [bottom] values or default zeros.
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
fun padding(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
): Padding {
    return PaddingImpl(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
}
