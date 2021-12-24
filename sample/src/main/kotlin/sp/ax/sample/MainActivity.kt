package sp.ax.sample

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import sp.ax.ui.entity.Gravity
import sp.ax.ui.entity.Orientation
import sp.ax.ui.entity.TypeDimension
import sp.ax.ui.view.group.layoutParams
import sp.ax.ui.view.group.linearLayout
import sp.ax.ui.view.group.view
import sp.ax.ui.widget.textView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = linearLayout(this, orientation = Orientation.VERTICAL) {
            val height = 64
            val layoutParams = ViewGroup::class.layoutParams(
                width = ViewGroup.LayoutParams.MATCH_PARENT,
                height = height
            )
            Gravity.values().forEach {
                view(width = ViewGroup.LayoutParams.MATCH_PARENT, height = 1, background = ColorDrawable(Color.BLACK))
                val view = textView(
                    context = context,
                    layoutParams = layoutParams,
                    gravity = it,
                    text = it.name,
                    textSizeUnit = TypeDimension.PIXEL,
                    textSize = height / 2f
                )
                addView(view)
            }
            view(width = ViewGroup.LayoutParams.MATCH_PARENT, height = 1, background = ColorDrawable(Color.BLACK))
        }
        setContentView(root)
    }
}
