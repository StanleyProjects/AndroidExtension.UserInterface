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

task("verifyService") {
    doLast {
        File(rootDir, "buildSrc/build.gradle.kts").also { file ->
            val text = file.requireFilledText()
            val lines = text.split(SystemUtil.newLine)
            setOf(
                "id(\"${P.kotlinDsl.name}\") version \"${P.kotlinDsl.version}\"" to "do not apply plugin \"${P.kotlinDsl.name}\"",
                "targetCompatibility = \"${Version.jvmTarget}\"" to "do not set jvm target to \"${Version.jvmTarget}\"",
                "kotlinOptions.jvmTarget = \"${Version.jvmTarget}\"" to "do not set kotlin jvm target to \"${Version.jvmTarget}\""
            ).forEach { (string, errorPostfix) ->
                val filtered = lines.filter { it.contains(string) }
                check(filtered.size == 1) { "Script by path ${file.absolutePath} $errorPostfix!" }
            }
        }
        val forbiddenFileNames = setOf(".DS_Store")
        rootDir.onFileRecurse {
            if (!it.isDirectory) check(!forbiddenFileNames.contains(it.name)) {
                "File by path ${it.absolutePath} is forbidden!"
            }
        }
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

task("verifyLicense") {
    doLast {
        val file = File(rootDir, "README.md")
        val text = file.requireFilledText()
        // todo
    }
}

repositories.mavenCentral() // com.pinterest.ktlint

val kotlinLint: Configuration by configurations.creating

dependencies {
    kotlinLint(D.Kotlin.lint.notation()) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

task<JavaExec>("verifyCodeStyle") {
    classpath = kotlinLint
    mainClass.set("com.pinterest.ktlint.Main")
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
        "--reporter=html,output=${File(buildDir, "reports/analysis/style/html/report.html")}"
    )
}
