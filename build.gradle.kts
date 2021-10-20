buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies(classpath = setOf(D.kotlinGradlePlugin, D.Android.toolsBuildGradle))
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
