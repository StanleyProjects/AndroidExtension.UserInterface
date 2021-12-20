package sp.ax.ui

import org.junit.Assert
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

fun Any?.exhaustive() {
    // ignored
}

fun <T : KClass<*>> T.onFields(block: (Field) -> Unit) {
    val fields = java.declaredFields
    Assert.assertFalse(fields.isEmpty())
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

fun fail(): Nothing {
    Assert.fail()
    error("Impossible!")
}

fun fail(message: String): Nothing {
    if (message.isEmpty()) fail()
    Assert.fail(message)
    error("Impossible!")
}

fun Throwable.fail(): Nothing {
    fail(toString())
}

fun <T : Any> T?.assertNotNull(): T {
    Assert.assertNotNull(this)
    if (this == null) error("Impossible!")
    return this
}

fun <T : Any> T?.assertNotNull(message: String): T {
    if (message.isEmpty()) return assertNotNull()
    Assert.assertNotNull(message, this)
    if (this == null) error("Impossible!")
    return this
}

inline fun <reified A : Any, reified E : A> A.assertType(): E {
    if (this is E) return this
    fail("Expected type is ${E::class}, but actual is ${A::class}!")
}
