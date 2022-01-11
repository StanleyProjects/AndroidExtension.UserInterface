package sp.ax.sample

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import sp.ax.ui.entity.Orientation
import sp.ax.ui.view.group.layoutParams
import sp.ax.ui.view.group.linearLayout
import sp.ax.ui.widget.addOnTextChanged
import sp.ax.ui.widget.editText

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = linearLayout(this, orientation = Orientation.VERTICAL) {
            val height = 64
            val layoutParams = ViewGroup::class.layoutParams(
                width = ViewGroup.LayoutParams.MATCH_PARENT,
                height = height
            )
            addView(
                editText(context, layoutParams = layoutParams) {
                    addOnTextChanged { println(it) }
                }
            )
        }
        setContentView(root)
    }
}
