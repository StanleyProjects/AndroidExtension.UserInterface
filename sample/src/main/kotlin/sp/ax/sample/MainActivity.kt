package sp.ax.sample

import android.app.Activity
import android.os.Bundle
import sp.ax.ui.view.group.linearLayout
import sp.ax.ui.view.group.view

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = linearLayout(this) {
            view(
                width = 256,
                height = 128,
                onClick = {
                    // todo
                }
            )
        }
        setContentView(root)
    }
}
