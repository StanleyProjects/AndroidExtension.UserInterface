package sp.ax.ui.entity

import android.view.View

enum class Visibility {
    VISIBLE,
    INVISIBLE,
    GONE,
}

fun Visibility.asViewValue(): Int {
    return when (this) {
        Visibility.VISIBLE -> View.VISIBLE
        Visibility.INVISIBLE -> View.INVISIBLE
        Visibility.GONE -> View.GONE
    }
}
