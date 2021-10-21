import sp.kx.gradle.entity.dependency
import sp.kx.gradle.entity.plugin

object Version {
    const val jacoco = "0.8.7"
    const val jupiter = "5.6.2"
    const val kotlin = "1.5.21"

    object Android {
        const val toolsBuildGradle = "4.2.2"
        const val compileSdk = 30
        const val buildTools = "30.0.3"
        const val minSdk = 16
        const val targetSdk = 30
    }
}

private object Group {
    const val android = "com.android"
    const val jetbrains = "org.jetbrains"
    const val jupiter = "org.junit.jupiter"
    const val kotlin = "$jetbrains.kotlin"
}

object D {
    val kotlinGradlePlugin = dependency(
        group = Group.kotlin,
        name = "kotlin-gradle-plugin",
        version = Version.kotlin
    )

    object Android {
        val toolsBuildGradle = dependency(
            group = Group.android + ".tools.build",
            name = "gradle",
            version = Version.Android.toolsBuildGradle
        )
    }

    object Jupiter {
        val api = dependency(
            group = Group.jupiter,
            name = "junit-jupiter-api",
            version = Version.jupiter
        )
        val engine = dependency(
            group = Group.jupiter,
            name = "junit-jupiter-engine",
            version = Version.jupiter
        )
    }
}

object P {
    object Android {
        val application = plugin(name = Group.android + ".application")
        val kotlin = plugin(
            name = "kotlin-android",
            version = Version.kotlin
        )
        val library = plugin(name = Group.android + ".library")
    }
}
