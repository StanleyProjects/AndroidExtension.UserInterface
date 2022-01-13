package sp.ax.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.Visibility
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.configure

internal val UNSPECIFIED_DRAWABLE: Drawable = object : Drawable() {
    override fun draw(canvas: Canvas) {
        error("not for use")
    }

    override fun setAlpha(alpha: Int) {
        error("not for use")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        error("not for use")
    }

    override fun getOpacity(): Int {
        error("not for use")
    }
}

internal object ImageViewDefault {
    val drawable: Drawable = UNSPECIFIED_DRAWABLE
    val scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER
}

internal fun ImageView.configure(
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
    drawable: Drawable,
    scaleType: ImageView.ScaleType
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
    if (drawable !== UNSPECIFIED_DRAWABLE) {
        setImageDrawable(drawable)
    }
    this.scaleType = scaleType
}

/**
 * Builds an instance of [ImageView] by [context] and parameters or default. Sample:
 * ```
 * fun foo(context: Context): ImageView {
 *     return imageView(
 *         context = context,
 *         layoutParams = ViewGroup::class.layoutParams(width = 512, height = 64),
 *         drawable = context.resources.getDrawable(R.drawable.image),
 *         scaleType = ImageView.ScaleType.CENTER_CROP
 *     )
 * }
 * ```
 * @author Stanley Wintergreen
 * @since 0.0.11
 */
fun imageView(
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
    drawable: Drawable = ImageViewDefault.drawable,
    scaleType: ImageView.ScaleType = ImageViewDefault.scaleType,
    block: ImageView.() -> Unit = {}
): ImageView {
    val result = ImageView(context)
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
        drawable = drawable,
        scaleType = scaleType
    )
    result.block()
    return result
}
