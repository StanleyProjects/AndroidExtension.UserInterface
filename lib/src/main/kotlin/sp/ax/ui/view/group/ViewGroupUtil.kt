package sp.ax.ui.view.group

import android.view.ViewGroup
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.insets
import kotlin.reflect.KClass

/**
 * A kotlin function variant of [ViewGroup.LayoutParams]. To use named arguments. Sample:
 * ```
 * fun foo(): ViewGroup.LayoutParams {
 *     return ViewGroup::class.layoutParams(height = 5, width = 6)
 * }
 * ```
 * @return An instance of [ViewGroup.LayoutParams] using its constructor.
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
fun KClass<ViewGroup>.layoutParams(width: Int, height: Int): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(width, height)
}

/**
 * A variant of [layoutParams] with one value [size] to both width and height.
 * @return An instance of [ViewGroup.LayoutParams]
 * such that its [ViewGroup.LayoutParams.width] == [size]
 * and [ViewGroup.LayoutParams.height] == [size].
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
fun KClass<ViewGroup>.layoutParams(size: Int): ViewGroup.LayoutParams {
    return layoutParams(width = size, height = size)
}

/**
 * A variant of [layoutParams] with one value [ViewGroup.LayoutParams.WRAP_CONTENT]
 * to both width and height.
 * @return An instance of [ViewGroup.LayoutParams]
 * such that its [ViewGroup.LayoutParams.width] == [ViewGroup.LayoutParams.WRAP_CONTENT]
 * and [ViewGroup.LayoutParams.height] == [ViewGroup.LayoutParams.WRAP_CONTENT].
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
fun KClass<ViewGroup.LayoutParams>.wrapped(): ViewGroup.LayoutParams {
    return ViewGroup::class.layoutParams(size = ViewGroup.LayoutParams.WRAP_CONTENT)
}

/**
 * A variant of [layoutParams] with one value [ViewGroup.LayoutParams.MATCH_PARENT]
 * to both width and height.
 * @return An instance of [ViewGroup.LayoutParams]
 * such that its [ViewGroup.LayoutParams.width] == [ViewGroup.LayoutParams.MATCH_PARENT]
 * and [ViewGroup.LayoutParams.height] == [ViewGroup.LayoutParams.MATCH_PARENT].
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
fun KClass<ViewGroup.LayoutParams>.matched(): ViewGroup.LayoutParams {
    return ViewGroup::class.layoutParams(size = ViewGroup.LayoutParams.MATCH_PARENT)
}

/**
 * Calls [ViewGroup.MarginLayoutParams.setMargins] with values of [margin].
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun ViewGroup.MarginLayoutParams.setMargin(margin: Insets) {
    setMargins(
        margin.left,
        margin.top,
        margin.right,
        margin.bottom
    )
}

/**
 * A kotlin function variant of [ViewGroup.MarginLayoutParams]. To use named arguments. Sample:
 * ```
 * fun foo(): ViewGroup.MarginLayoutParams {
 *     return ViewGroup::class.layoutParams(height = 5, width = 6, margin(right = 7, left = 8))
 * }
 * ```
 * @return An instance of [ViewGroup.MarginLayoutParams] using its constructor with [ViewGroup.MarginLayoutParams.setMargins].
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun KClass<ViewGroup>.layoutParams(width: Int, height: Int, margin: Insets): ViewGroup.MarginLayoutParams {
    val result = ViewGroup.MarginLayoutParams(width, height)
    result.setMargin(margin)
    return result
}

/**
 * @return An instance of [Insets] by [ViewGroup.MarginLayoutParams.leftMargin], [ViewGroup.MarginLayoutParams.topMargin], [ViewGroup.MarginLayoutParams.rightMargin] and [ViewGroup.MarginLayoutParams.bottomMargin] values.
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun ViewGroup.MarginLayoutParams.getMargin(): Insets {
    return insets(
        left = leftMargin,
        top = topMargin,
        right = rightMargin,
        bottom = bottomMargin
    )
}
