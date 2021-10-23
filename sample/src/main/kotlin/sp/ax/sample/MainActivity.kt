package sp.ax.sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import sp.ax.ui.getPadding

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View(this)
        view.getPadding()
    }
}
