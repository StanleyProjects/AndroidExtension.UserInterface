plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "2.1.4"
}

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(
        group = "com.github.kepocnhh",
        name = "KotlinExtension.GradleUtil",
        version = "0.0.2-SNAPSHOT"
    )
}
