package sp.ax.ui.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.Visibility
import sp.ax.ui.entity.Visibility.Companion.toInt
import sp.ax.ui.entity.insets
import sp.ax.ui.view.group.wrapped

/**
 * @return An instance of [Insets] by [View.getPaddingLeft], [View.getPaddingTop], [View.getPaddingRight] and [View.getPaddingBottom] values.
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun View.getPadding(): Insets {
    return insets(
        left = paddingLeft,
        top = paddingTop,
        right = paddingRight,
        bottom = paddingBottom
    )
}

/**
 * Calls [View.setPadding] with values of [padding].
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun View.setPadding(padding: Insets) {
    setPadding(
        padding.left,
        padding.top,
        padding.right,
        padding.bottom
    )
}

/**
 * Calls [View.setPadding] with values of [left], [top], [right] and [bottom] or default [View.getPaddingLeft], [View.getPaddingTop], [View.getPaddingRight] and [View.getPaddingBottom] respectively.
 * @author Stanley Wintergreen
 * @since 0.0.3
 */
fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(
        left,
        top,
        right,
        bottom
    )
}

internal val UNSPECIFIED_ON_CLICK: () -> Unit = { error("not for use") }
internal val UNSPECIFIED_ON_LONG_CLICK: () -> Boolean = { error("not for use") }

internal object ViewDefault {
    const val id = View.NO_ID
    val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams::class.wrapped()
    val background: Drawable = ColorDrawable(0)
    val visibility: Visibility = Visibility.VISIBLE
    val padding: Insets = insets()
    val onClick: () -> Unit = UNSPECIFIED_ON_CLICK
    val onLongClick: () -> Boolean = UNSPECIFIED_ON_LONG_CLICK
    const val keepScreenOn: Boolean = false
}

internal fun View.configure(
    layoutParams: ViewGroup.LayoutParams,
    id: Int,
    background: Drawable,
    visibility: Visibility,
    padding: Insets,
    onClick: () -> Unit,
    onLongClick: () -> Boolean,
    isClickable: Boolean,
    isLongClickable: Boolean,
    keepScreenOn: Boolean
) {
    this.id = id
    this.layoutParams = layoutParams
    this.background = background
    this.visibility = visibility.toInt()
    setPadding(padding)
    if (onClick !== UNSPECIFIED_ON_CLICK) {
        setOnClickListener { onClick() }
    }
    this.isClickable = isClickable
    if (onLongClick !== UNSPECIFIED_ON_LONG_CLICK) {
        setOnLongClickListener { onLongClick() }
    }
    this.isLongClickable = isLongClickable
    this.keepScreenOn = keepScreenOn
}

/**
 * Builds an instance of [View] by [context] and parameters or default. Sample:
 * ```
 * fun foo(context: Context): View {
 *     return view(
 *         context = context,
 *         layoutParams = ViewGroup::class.layoutParams(size = 5),
 *         background = ColorDrawable(Color.GREEN)
 *     )
 * }
 * ```
 * @author Stanley Wintergreen
 * @since 0.0.5
 */
fun view(
    context: Context,
    layoutParams: ViewGroup.LayoutParams = ViewDefault.layoutParams,
    id: Int = ViewDefault.id,
    background: Drawable = ViewDefault.background,
    visibility: Visibility = ViewDefault.visibility,
    padding: Insets = ViewDefault.padding,
    onClick: () -> Unit = ViewDefault.onClick,
    onLongClick: () -> Boolean = ViewDefault.onLongClick,
    isClickable: Boolean = onClick !== ViewDefault.onClick,
    isLongClickable: Boolean = onLongClick !== ViewDefault.onLongClick,
    keepScreenOn: Boolean = ViewDefault.keepScreenOn,
    block: View.() -> Unit = {}
): View {
    val result = View(context)
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
        keepScreenOn = keepScreenOn
    )
    result.block()
    return result
}
