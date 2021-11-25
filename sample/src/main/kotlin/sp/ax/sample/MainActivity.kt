package sp.ax.sample

import android.app.Activity
import android.os.Bundle
import sp.ax.ui.view.view

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = view(context = this)
    }
}
