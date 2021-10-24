buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies(
        classpath = setOf(
            D.Kotlin.gradlePlugin,
            D.Android.toolsBuildGradle
        )
    )
}

task<Delete>("clean") {
    delete = setOf(buildDir, file("buildSrc/build"))
}

task("getVersionName") {
    doLast {
        println(Version.name)
    }
}

task("getGroupId") {
    doLast {
        println(Maven.groupId)
    }
}

task("getArtifactId") {
    doLast {
        println(Maven.artifactId)
    }
}

task("verifyReadme") {
    doLast {
        val file = File(rootDir, "README.md")
        val text = file.requireFilledText()
        val projectCommon = MarkdownUtil.table(
            heads = listOf("Android project common", "version"),
            dividers = listOf("-", "-:"),
            rows = listOf(
                listOf("build gradle", "`${Version.Android.toolsBuildGradle}`"),
                listOf("compile sdk", "`${Version.Android.compileSdk}`"),
                listOf("build tools", "`${Version.Android.buildTools}`"),
                listOf("min sdk", "`${Version.Android.minSdk}`"),
                listOf("target sdk", "`${Version.Android.targetSdk}`")
            )
        )
        setOf(projectCommon).forEach {
            check(text.contains(it)) { "File by path ${file.absolutePath} must contains \"$it\"!" }
        }
        val lines = text.split(SystemUtil.newLine)
        val versionBadge = MarkdownUtil.image(
            text = "version",
            url = BadgeUtil.url(
                label = "version",
                message = Version.name,
                color = "2962ff"
            )
        )
        setOf(versionBadge).forEach {
            check(lines.contains(it)) { "File by path ${file.absolutePath} must contains \"$it\" line!" }
        }
    }
}

val kotlinLint: Configuration by configurations.creating

dependencies {
    kotlinLint(D.Kotlin.lint.notation()) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}
repositories.mavenCentral() // com.pinterest.ktlint

task<JavaExec>("verifyCodeStyle") {
    classpath = kotlinLint
    main = "com.pinterest.ktlint.Main"
    args(
        "build.gradle.kts",
        "settings.gradle.kts",
        "buildSrc/src/main/kotlin/**/*.kt",
        "buildSrc/build.gradle.kts",
        "lib/src/main/kotlin/**/*.kt",
        "lib/src/test/kotlin/**/*.kt",
        "lib/build.gradle.kts",
        "sample/src/main/kotlin/**/*.kt",
        "sample/build.gradle.kts",
        "--reporter=html,output=${File(buildDir, "reports/analysis/style/html/report.html").absolutePath}"
    )
}
