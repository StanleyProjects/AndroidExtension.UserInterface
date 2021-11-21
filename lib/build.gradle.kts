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

fun getOutputFileName(
    prefix: String,
    versionName: String,
    fileExtension: String
): String {
    return "$prefix-$versionName.$fileExtension"
}

fun setSnapshot(variant: com.android.build.gradle.api.LibraryVariant) {
    val output = variant.outputs.single()
    check(output is com.android.build.gradle.internal.api.LibraryVariantOutputImpl)
    val versionName = Version.name + "-SNAPSHOT"
    output.outputFileName = getOutputFileName(
        prefix = Maven.artifactId,
        versionName = versionName,
        fileExtension = "aar"
    )
    tasks.getByName("assemble${variant.name.capitalize()}") {
        doLast {
            checkFileExists(output.outputFile)
            val resultFile = File(buildDir, "libs/" + output.outputFileName)
            if (resultFile.exists()) resultFile.delete()
            output.outputFile.copyTo(resultFile)
        }
    }
    val mainSourceSets = android.sourceSets["main"]!!
    task<Jar>("assemble${variant.name.capitalize()}Source") {
        archiveBaseName.set(Maven.artifactId)
        archiveVersion.set(versionName)
        archiveClassifier.set("sources")
        from(mainSourceSets.java.srcDirs)
    }
    task("assemble${variant.name.capitalize()}Pom") {
        doLast {
            val parent = File(buildDir, "libs")
            if (!parent.exists()) parent.mkdirs()
            val outputFileName = getOutputFileName(
                prefix = Maven.artifactId,
                versionName = versionName,
                fileExtension = "pom"
            )
            val file = File(parent, outputFileName)
            if (file.exists()) file.delete()
            file.createNewFile()
            checkFileExists(file)
            val text = MavenUtil.pom(
                modelVersion = "4.0.0",
                groupId = Maven.groupId,
                artifactId = Maven.artifactId,
                version = versionName,
                packaging = "aar"
            )
            file.writeText(text)
        }
    }
}

android {
    compileSdk = Version.Android.compileSdk
    buildToolsVersion = Version.Android.buildTools
    testOptions.unitTests.isIncludeAndroidResources = true // sdk >= 29

    defaultConfig {
        minSdk = Version.Android.minSdk
        targetSdk = Version.Android.targetSdk
        buildConfigField("int", "MIN_SDK", "$minSdk")
        buildConfigField("int", "TARGET_SDK", "$targetSdk")
    }

    buildTypes {
        create(BuildType.snapshot) {
            isTestCoverageEnabled = true
        }
    }

    testCoverage {
        jacocoVersion = Version.jacoco
    }

    sourceSets.getByName("main") {
        java.srcDir("src/$name/kotlin")
    }

    libraryVariants.all {
        setCoverage(this)
        val capitalize = name.capitalize()
        val taskCompileKotlin = tasks.getByName<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compile${capitalize}Kotlin")
        taskCompileKotlin.kotlinOptions {
            jvmTarget = Version.jvmTarget
            freeCompilerArgs = freeCompilerArgs + setOf("-module-name", Maven.groupId + ":" + Maven.artifactId)
        }
        when (buildType.name) {
            BuildType.snapshot -> {
                setSnapshot(this)
            }
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.robolectric:robolectric:4.7.2")
}

jacoco {
    toolVersion = Version.jacoco
}
