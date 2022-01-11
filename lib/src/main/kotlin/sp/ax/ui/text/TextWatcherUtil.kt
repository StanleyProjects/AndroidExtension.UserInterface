package sp.ax.ui.text

import android.text.Editable
import android.text.TextWatcher

/**
 * Builds a [TextWatcher] instance with an overridden [TextWatcher.onTextChanged] method. Sample:
 * ```
 * fun foo(view: EditText) {
 *     view.addTextChangedListener(onTextChanged { println(it) })
 * }
 * ```
 * @author Stanley Wintergreen
 * @since 0.0.9
 */
fun onTextChanged(
    block: (CharSequence) -> Unit
): TextWatcher {
    return object : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            // ignored
        }

        override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {
            // ignored
        }

        override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
            block(sequence)
        }
    }
}
