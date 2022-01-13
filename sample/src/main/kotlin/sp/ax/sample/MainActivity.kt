package sp.ax.sample

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import sp.ax.ui.entity.Orientation
import sp.ax.ui.text.onTextChanged
import sp.ax.ui.view.group.editText
import sp.ax.ui.view.group.linearLayout
import sp.ax.ui.view.group.textView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = linearLayout(this, orientation = Orientation.VERTICAL) {
            textView(width = ViewGroup.LayoutParams.MATCH_PARENT, text = "title")
            editText(width = ViewGroup.LayoutParams.MATCH_PARENT, textWatchers = setOf(onTextChanged { println(it) }))
        }
        setContentView(root)
    }
}
