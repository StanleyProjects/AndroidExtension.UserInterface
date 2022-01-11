package sp.ax.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import sp.ax.ui.entity.Gravity
import sp.ax.ui.entity.Insets
import sp.ax.ui.entity.TypeDimension
import sp.ax.ui.entity.TypefaceStyle
import sp.ax.ui.entity.Visibility
import sp.ax.ui.view.ViewDefault

/**
 * Builds an instance of [EditText] by [context] and parameters or default. Sample:
 * ```
 * fun foo(context: Context): EditText {
 *     return editText(
 *         context = context,
 *         layoutParams = ViewGroup::class.layoutParams(width = 512, height = 64),
 *         textWatchers = setOf(onTextChanged { println(it) })
 *     )
 * }
 * ```
 * @author Stanley Wintergreen
 * @since 0.0.9
 */
fun editText(
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
    text: CharSequence = "",
    textSizeUnit: TypeDimension = TextViewDefault.textSizeUnit,
    textSize: Float = TextViewDefault.textSize,
    textColor: Int = TextViewDefault.textColor,
    typeface: Typeface? = TextViewDefault.typeface,
    typefaceStyle: TypefaceStyle = TextViewDefault.typefaceStyle,
    isAllCaps: Boolean = TextViewDefault.isAllCaps,
    textWatchers: Set<TextWatcher> = TextViewDefault.textWatchers,
    block: EditText.() -> Unit = {}
): EditText {
    val result = EditText(context)
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
        textColor = textColor,
        typeface = typeface,
        typefaceStyle = typefaceStyle,
        isAllCaps = isAllCaps,
        textWatchers = textWatchers
    )
    result.block()
    return result
}
