package sp.ax.ui

import org.junit.Assert.assertFalse
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

fun Any?.exhaustive() {
    // ignored
}

fun <T : KClass<*>> T.onFields(block: (Field) -> Unit) {
    val fields = java.declaredFields
    assertFalse(fields.isEmpty())
    for (field in fields) {
        if (field.name.startsWith("$") && Modifier.isStatic(field.modifiers) && Modifier.isPrivate(field.modifiers)) {
            continue
        }
        if (field.name == "INSTANCE" && Modifier.isStatic(field.modifiers) && Modifier.isPublic(field.modifiers)) {
            continue
        }
        block(field)
    }
}
