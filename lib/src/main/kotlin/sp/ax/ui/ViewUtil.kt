package sp.ax.ui

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
