package sp.ax.ui.view

import android.view.View
import sp.ax.ui.entity.Padding
import sp.ax.ui.entity.padding

fun View.getPadding(): Padding {
    return padding(
        left = paddingLeft,
        top = paddingTop,
        right = paddingRight,
        bottom = paddingBottom
    )
}

fun View.setPadding(padding: Padding) {
    setPadding(
        padding.left,
        padding.top,
        padding.right,
        padding.bottom
    )
}

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
