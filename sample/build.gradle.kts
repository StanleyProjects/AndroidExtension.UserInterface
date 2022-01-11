repositories {
    mavenCentral()
    google()
}

plugins {
    apply(
        P.Android.application,
        P.Android.kotlin
    )
}

android {
    compileSdk = Version.Android.compileSdk
    buildToolsVersion = Version.Android.buildTools

    defaultConfig {
        minSdk = Version.Android.minSdk
        targetSdk = Version.Android.targetSdk
        applicationId = "sp.ax.sample"
        versionCode = 10
        versionName = "0.0.$versionCode"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }

    sourceSets.all {
        java.srcDir("src/$name/kotlin")
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    implementation(project(":lib"))
}
