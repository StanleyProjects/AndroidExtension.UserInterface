package sp.ax.ui.entity

interface Padding {
    val left: Int
    val top: Int
    val right: Int
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
