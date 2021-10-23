repositories {
    mavenCentral()
    google()
}

plugins {
    apply(
        P.Android.library,
        P.Android.kotlin
    )
    id("org.gradle.jacoco")
}

fun setCoverage(variant: com.android.build.gradle.api.LibraryVariant) {
    if (!variant.buildType.isTestCoverageEnabled) return
    val capitalize = variant.name.capitalize()
    val taskTest = tasks.getByName<Test>("test${capitalize}UnitTest")
    val taskCoverage = task<JacocoReport>("test${capitalize}CoverageReport") {
        dependsOn(taskTest)
        sourceDirectories.from(File("$projectDir/src/main/kotlin"))
        classDirectories.from(File("$buildDir/tmp/kotlin-classes/" + variant.name))
        executionData(taskTest)
        reports {
            csv.isEnabled = false
            html.isEnabled = true
            xml.isEnabled = false
        }
    }
    task<JacocoCoverageVerification>("test${capitalize}CoverageVerification") {
        dependsOn(taskCoverage)
        classDirectories.from(taskCoverage.classDirectories)
        executionData(taskCoverage.executionData)
        violationRules {
            rule {
                limit {
                    minimum = BigDecimal(0.9)
                }
            }
        }
    }
}

android {
    compileSdk = Version.Android.compileSdk
    buildToolsVersion = Version.Android.buildTools

    defaultConfig {
        minSdk = Version.Android.minSdk
        targetSdk = Version.Android.targetSdk
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
            testCoverage {
                jacocoVersion = Version.jacoco
            }
        }
    }

    sourceSets.getByName("main") {
        java.srcDir("src/$name/kotlin")
    }

    libraryVariants.all {
        setCoverage(this)
    }
}

dependencies {
    testImplementation("junit:junit:4.13")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.robolectric:robolectric:4.6")
}

jacoco {
    toolVersion = Version.jacoco
}
