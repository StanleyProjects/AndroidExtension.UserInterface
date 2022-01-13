package sp.ax.ui

import org.junit.Assert

fun Any?.exhaustive() {
    // ignored
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
    fail("Expected type is ${E::class}, but actual is ${this::class}!")
}
