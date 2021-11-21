package sp.ax.ui.view.group

import android.view.ViewGroup
import kotlin.reflect.KClass

fun KClass<ViewGroup>.layoutParams(width: Int, height: Int): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(width, height)
}

fun KClass<ViewGroup>.layoutParams(size: Int): ViewGroup.LayoutParams {
    return layoutParams(width = size, height = size)
}

fun KClass<ViewGroup.LayoutParams>.wrapped(): ViewGroup.LayoutParams {
    return ViewGroup::class.layoutParams(size = ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun KClass<ViewGroup.LayoutParams>.matched(): ViewGroup.LayoutParams {
    return ViewGroup::class.layoutParams(size = ViewGroup.LayoutParams.MATCH_PARENT)
}
