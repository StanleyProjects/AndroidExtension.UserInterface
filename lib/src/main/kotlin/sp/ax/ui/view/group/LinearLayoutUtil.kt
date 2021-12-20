package sp.ax.ui.view.group

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import sp.ax.ui.entity.Gravity
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.Orientation
import sp.ax.ui.entity.Visibility
import sp.ax.ui.entity.insets
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.configure
import kotlin.reflect.KClass

/**
 * Calls [LinearLayout.setOrientation] with one of [LinearLayout.HORIZONTAL] and [LinearLayout.VERTICAL] depending on [orientation].
 * @author Stanley Wintergreen
 * @since 0.0.6
 */
fun LinearLayout.setOrientation(orientation: Orientation) {
    val result = when (orientation) {
        Orientation.HORIZONTAL -> LinearLayout.HORIZONTAL
        Orientation.VERTICAL -> LinearLayout.VERTICAL
    }
    setOrientation(result)
}

internal fun LinearLayout.configure(
    layoutParams: ViewGroup.LayoutParams,
    id: Int,
    background: Drawable,
    visibility: Visibility,
    padding: Insets,
    onClick: () -> Unit,
    onLongClick: () -> Boolean,
    isClickable: Boolean,
    isLongClickable: Boolean,
    keepScreenOn: Boolean,
    orientation: Orientation,
    gravity: Gravity,
    block: LinearLayout.() -> Unit = {}
) {
    configure(
        layoutParams = layoutParams,
        id = id,
        background = background,
        visibility = visibility,
        padding = padding,
        onClick = onClick,
        onLongClick = onLongClick,
        isClickable = isClickable,
        isLongClickable = isLongClickable,
        keepScreenOn = keepScreenOn
    )
    setOrientation(orientation)
    this.gravity = gravity.toInt()
    block()
}

internal object LinearLayoutDefault {
    val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams::class.matched()
    val orientation: Orientation = Orientation.HORIZONTAL
    val gravity: Gravity = Gravity.TOP_LEFT
    object LayoutParams {
        const val width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        const val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        const val weight: Float = 0f
        val margin: Insets = insets()
    }
}

/**
 * Builds an instance of [LinearLayout] by [context] and parameters or default. Sample:
 * ```
 * fun foo(context: Context): LinearLayout {
 *     return linearLayout(
 *         context = context,
 *         layoutParams = ViewGroup::class.layoutParams(size = 5),
 *         background = ColorDrawable(Color.GREEN)
 *     )
 * }
 * ```
 * @author Stanley Wintergreen
 * @since 0.0.6
 */
fun linearLayout(
    context: Context,
    layoutParams: ViewGroup.LayoutParams = LinearLayoutDefault.layoutParams,
    id: Int = ViewDefault.id,
    background: Drawable = ViewDefault.background,
    visibility: Visibility = ViewDefault.visibility,
    padding: Insets = ViewDefault.padding,
    onClick: () -> Unit = ViewDefault.onClick,
    onLongClick: () -> Boolean = ViewDefault.onLongClick,
    isClickable: Boolean = onClick !== ViewDefault.onClick,
    isLongClickable: Boolean = onLongClick !== ViewDefault.onLongClick,
    keepScreenOn: Boolean = ViewDefault.keepScreenOn,
    orientation: Orientation = LinearLayoutDefault.orientation,
    gravity: Gravity = LinearLayoutDefault.gravity,
    block: LinearLayout.() -> Unit = {}
): LinearLayout {
    val result = LinearLayout(context)
    result.configure(
        layoutParams = layoutParams,
        id = id,
        background = background,
        visibility = visibility,
        padding = padding,
        onClick = onClick,
        onLongClick = onLongClick,
        isClickable = isClickable,
        isLongClickable = isLongClickable,
        keepScreenOn = keepScreenOn,
        orientation = orientation,
        gravity = gravity
    )
    result.block()
    return result
}

/**
 * A kotlin function variant of [LinearLayout.LayoutParams]. To use named arguments. Sample:
 * ```
 * fun foo(): LinearLayout.LayoutParams {
 *     return LinearLayout::class.layoutParams(height = 5, width = 6, margin(right = 7, left = 8))
 * }
 * ```
 * @return An instance of [LinearLayout.LayoutParams] using its constructor with [ViewGroup.MarginLayoutParams.setMargin].
 * @author Stanley Wintergreen
 * @since 0.0.6
 */
fun KClass<LinearLayout>.layoutParams(
    width: Int,
    height: Int,
    weight: Float,
    margin: Insets
): LinearLayout.LayoutParams {
    val result = LinearLayout.LayoutParams(width, height, weight)
    result.setMargin(margin)
    return result
}

/**
 * Builds an instance of [View] by parameters or default and adds to [LinearLayout] receiver if [needToAdd] == `true`. Sample:
 * ```
 * fun foo(context: Context) {
 *     val bar = linearLayout(context) {
 *         view(
 *             width = 256,
 *             height = 128,
 *             onClick = { ... }
 *         )
 *     }
 * }
 * ```
 * @author Stanley Wintergreen
 * @since 0.0.6
 */
fun LinearLayout.view(
    context: Context = this.context,
    width: Int = LinearLayoutDefault.LayoutParams.width,
    height: Int = LinearLayoutDefault.LayoutParams.height,
    weight: Float = LinearLayoutDefault.LayoutParams.weight,
    margin: Insets = LinearLayoutDefault.LayoutParams.margin,
    id: Int = ViewDefault.id,
    background: Drawable = ViewDefault.background,
    visibility: Visibility = ViewDefault.visibility,
    padding: Insets = ViewDefault.padding,
    onClick: () -> Unit = ViewDefault.onClick,
    onLongClick: () -> Boolean = ViewDefault.onLongClick,
    isClickable: Boolean = onClick !== ViewDefault.onClick,
    isLongClickable: Boolean = onLongClick !== ViewDefault.onLongClick,
    keepScreenOn: Boolean = ViewDefault.keepScreenOn,
    needToAdd: Boolean = true,
    block: View.() -> Unit = {}
): View {
    val result = View(context)
    result.configure(
        layoutParams = LinearLayout::class.layoutParams(
            width = width,
            height = height,
            weight = weight,
            margin = margin
        ),
        id = id,
        background = background,
        visibility = visibility,
        padding = padding,
        onClick = onClick,
        onLongClick = onLongClick,
        isClickable = isClickable,
        isLongClickable = isLongClickable,
        keepScreenOn = keepScreenOn
    )
    if (needToAdd) {
        addView(result)
    }
    result.block()
    return result
}

/**
 * A variant of [layoutParams] with one value [ViewGroup.LayoutParams.WRAP_CONTENT]
 * to both width and height.
 * @return An instance of [LinearLayout.LayoutParams]
 * such that its [ViewGroup.LayoutParams.width] == [ViewGroup.LayoutParams.WRAP_CONTENT]
 * and [ViewGroup.LayoutParams.height] == [ViewGroup.LayoutParams.WRAP_CONTENT].
 * @author Stanley Wintergreen
 * @since 0.0.6
 */
fun KClass<LinearLayout.LayoutParams>.wrapped(
    weight: Float,
    margin: Insets
): LinearLayout.LayoutParams {
    return LinearLayout::class.layoutParams(
        width = ViewGroup.LayoutParams.WRAP_CONTENT,
        height = ViewGroup.LayoutParams.WRAP_CONTENT,
        weight = weight,
        margin = margin
    )
}
