import sp.kx.gradle.entity.dependency
import sp.kx.gradle.entity.plugin

object Repository {
    const val owner = "StanleyProjects"
    const val name = "AndroidExtension.UserInterface"
}

object Maven {
    const val groupId = "com.github.kepocnhh"
    const val artifactId = Repository.name
}

object Version {
    const val jacoco = "0.8.7"
    const val jvmTarget = "1.8"
//    const val jvmTarget = "12"
    const val kotlin = "1.5.21"
    const val kotlinDsl = "2.1.7"
    const val kotlinLint = "0.42.1"
    const val name = "0.0.8"

    object Android {
        const val toolsBuildGradle = "7.0.2"
        const val compileSdk = 30
        const val buildTools = "30.0.3"
        const val minSdk = 16
        const val targetSdk = 30
    }
}

object BuildType {
    const val snapshot = "snapshot"
}

private object Group {
    const val android = "com.android"
    const val jetbrains = "org.jetbrains"
    const val kotlin = "$jetbrains.kotlin"
    const val pinterest = "com.pinterest"
}

object D {
    object Kotlin {
        val gradlePlugin = dependency(
            group = Group.kotlin,
            name = "kotlin-gradle-plugin",
            version = Version.kotlin
        )

        val lint = dependency(
            group = Group.pinterest,
            name = "ktlint",
            version = Version.kotlinLint
        )

    }

    object Android {
        val toolsBuildGradle = dependency(
            group = Group.android + ".tools.build",
            name = "gradle",
            version = Version.Android.toolsBuildGradle
        )
    }
}

object P {
    val kotlinDsl = plugin(name = "org.gradle.kotlin.kotlin-dsl", version = Version.kotlinDsl)

    object Android {
        val application = plugin(name = Group.android + ".application")
        val kotlin = plugin(
            name = "kotlin-android",
            version = Version.kotlin
        )
        val library = plugin(name = Group.android + ".library")
    }
}
