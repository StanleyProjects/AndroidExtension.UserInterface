package sp.ax.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import sp.ax.ui.entity.Gravity
import sp.ax.ui.entity.Gravity.Companion.toInt
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.TypeDimension
import sp.ax.ui.entity.TypeDimension.Companion.toInt
import sp.ax.ui.entity.Visibility
import sp.ax.ui.view.ViewDefault
import sp.ax.ui.view.configure

internal fun TextView.configure(
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
    gravity: Gravity,
    text: CharSequence,
    textSizeUnit: TypeDimension,
    textSize: Float,
    textColor: Int
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
    this.gravity = gravity.toInt()
    this.text = text
    setTextSize(textSizeUnit.toInt(), textSize)
    setTextColor(textColor)
}

internal object TextViewDefault {
    val gravity: Gravity = Gravity.TOP_LEFT
    val textSizeUnit: TypeDimension = TypeDimension.SCALED_PIXEL
    const val textSize: Float = 12f
    val textColor: Int = Color.parseColor("#000000")
}

fun textView(
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
    gravity: Gravity = TextViewDefault.gravity,
    text: CharSequence,
    textSizeUnit: TypeDimension = TextViewDefault.textSizeUnit,
    textSize: Float = TextViewDefault.textSize,
    textColor: Int = TextViewDefault.textColor,
    block: TextView.() -> Unit = {}
): TextView {
    val result = TextView(context)
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
        gravity = gravity,
        text = text,
        textSizeUnit = textSizeUnit,
        textSize = textSize,
        textColor = textColor
    )
    result.block()
    return result
}
