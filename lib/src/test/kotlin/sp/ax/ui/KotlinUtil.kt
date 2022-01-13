package sp.ax.ui

@Suppress("unchecked_cast")
inline fun <reified T : Any> List<*>.cast(): List<T> {
    check(all { it is T })
    return this as List<T>
}
