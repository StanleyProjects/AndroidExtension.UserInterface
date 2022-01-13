package sp.ax.ui

import java.lang.reflect.Field
import java.lang.reflect.Modifier

inline fun <reified T : Any> getPrivateField(name: String): Field {
    val field = T::class.java.getDeclaredField(name)
    check(Modifier.isPrivate(field.modifiers))
    check(!field.isAccessible)
    field.isAccessible = true
    return field
}

inline fun <reified T : Any> T.getPrivateInt(name: String): Int {
    return getPrivateField<T>(name).getInt(this)
}

inline fun <reified T : Any, reified U : Any> T.getPrivateList(name: String): List<U> {
    val result = getPrivateField<T>(name).get(this)
    check(result is List<*>)
    return result.cast()
}
