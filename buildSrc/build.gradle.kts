plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "2.1.7"
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

tasks.getByName<JavaCompile>("compileJava").also {
    it.targetCompatibility = "1.8"
}

tasks.getByName<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin").also {
    it.kotlinOptions.jvmTarget = "1.8"
}
