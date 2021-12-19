package sp.ax.sample

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import sp.ax.ui.entity.insets
import sp.ax.ui.view.group.layoutParams
import sp.ax.ui.view.view

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = view(
            layoutParams = ViewGroup::class.layoutParams(
                width = 256,
                height = 128,
                margin = insets(left = 16, right = 16)
            ),
            context = this
        )
    }
}
