repositories {
    mavenCentral()
    google()
}

plugins {
    apply(
        P.Android.library,
        P.Android.kotlin
    )
//    id("org.gradle.jacoco")
}

android {
    compileSdkVersion(Version.Android.compileSdk)
    buildToolsVersion(Version.Android.buildTools)

    defaultConfig {
        minSdkVersion(Version.Android.minSdk)
        targetSdkVersion(Version.Android.targetSdk)
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.all {
        java.srcDir("src/$name/kotlin")
    }
}

dependencies {
    testImplementation("junit:junit:4.13")
    testImplementation("androidx.test:core:1.4.0")
//    testImplementation("androidx.test:runner:1.4.0")
//    testImplementation("androidx.test.ext:junit:1.1.3")
//    testImplementation("org.mockito:mockito-core:1.10.19")
    testImplementation("org.robolectric:robolectric:4.6")
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}

/*
jacoco {
    toolVersion = Version.jacoco
}

tasks.getByName<JacocoReport>("jacocoTestReport") {
    reports {
        xml.isEnabled = false
        html.isEnabled = true
        csv.isEnabled = false
    }
}

tasks.getByName<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                minimum = BigDecimal(0.9)
            }
        }
    }
}
*/