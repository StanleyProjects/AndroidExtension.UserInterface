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

task("saveCommonInfo") {
    doLast {
        if (!buildDir.exists()) buildDir.mkdirs()
        val file = File(buildDir, "common.json")
        val map = mapOf(
            "version" to Version.name,
            "groupId" to Maven.groupId,
            "artifactId" to Maven.artifactId
        )
        val result = org.json.JSONObject().also {
            map.forEach { (key, value) ->
                it.put(key, value)
            }
        }.toString()
        file.delete()
        file.writeText(result)
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
        val versionBadge = MarkdownUtil.image(
            text = "version",
            url = BadgeUtil.url(
                label = "version",
                message = Version.name,
                color = "2962ff"
            )
        )
        val documentationBadge = MarkdownUtil.url(
            text = MarkdownUtil.image(
                text = "documentation",
                url = BadgeUtil.url(
                    label = "documentation",
                    labelColor = "2962ff"
                )
            ),
            value = "https://${Repository.owner}.github.io/${Repository.name}/documentation/${Version.name}"
        )
        fun check(file: File): Set<String> {
            if (!file.exists()) return setOf("the file does not exist")
            if (file.isDirectory) return setOf("the file is a directory")
            val text = file.readText(Charsets.UTF_8)
            if (text.isEmpty()) return setOf("the file does not contain text")
            val result = mutableSetOf<String>()
            setOf(projectCommon).forEach {
                if (!text.contains(it)) result.add("the file does not contain \"$it\"")
            }
            val lines = text.split(SystemUtil.newLine)
            setOf(versionBadge, documentationBadge).forEach {
                if (!lines.contains(it)) result.add("the file does not contain \"$it\" line")
            }
            return result
        }
        val issues = check(file = File(rootDir, "README.md"))
        val report = File(buildDir, "reports/analysis/readme/index.html").also {
            it.delete()
            it.parentFile!!.mkdirs()
        }
        if (issues.isEmpty()) {
            val message = "All checks of the file along the \"README.md\" were successful."
            report.writeText(message)
            println(message)
        } else {
            val text = """
<html>
<h3>The following problems were found while checking the <code>README.md</code>:</h3>
${issues.joinToString(prefix = "<ul>", postfix = "</ul>", separator = "\n") { "<li>$it</li>" }}
</html>
            """.trimIndent()
            report.writeText(text)
            error("Problems were found while checking the \"README.md\". See the report: ${report.absolutePath}")
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
        "--reporter=html,output=${File(buildDir, "reports/analysis/style/html/index.html")}"
    )
}
