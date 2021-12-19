package sp.ax.ui.entity

/**
 * The type holds four integer offsets which describe changes to the four edges of a rectangle.
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
interface Insets {
    /**
     * Holds integer offset which describe the `left` edge of a rectangle.
     */
    val left: Int
    /**
     * Holds integer offset which describe the `top` edge of a rectangle.
     */
    val top: Int
    /**
     * Holds integer offset which describe the `right` edge of a rectangle.
     */
    val right: Int
    /**
     * Holds integer offset which describe the `bottom` edge of a rectangle.
     */
    val bottom: Int
}

private class InsetsImpl(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int
) : Insets {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Insets -> {
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
 * @return An instance of [Insets] by [left], [top], [right] and [bottom] values or default zeros.
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun insets(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
): Insets {
    return InsetsImpl(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
}
