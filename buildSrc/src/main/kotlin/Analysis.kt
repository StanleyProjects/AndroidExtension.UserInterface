import java.io.File

object Analysis {
    class Issue(val message: String)

    class Result(val issues: Set<Issue>)
    private val EMPTY_RESULT = Result(issues = emptySet())

    class FileContain(val texts: Set<String>, val lines: Set<String>)

    fun check(file: File, environment: FileContain): Result {
        if (!file.exists()) {
            val message = "The file under the path \"${file.absolutePath}\" should exist!"
            return Result(setOf(Issue(message = message)))
        }
        if (file.isDirectory) {
            val message =
                "The file under the path \"${file.absolutePath}\" should not be a directory!"
            return Result(setOf(Issue(message = message)))
        }
        val text = try {
            file.requireFilledText()
        } catch (e: Throwable) {
            val message = "The file under the path \"${file.absolutePath}\" should contain text!"
            return Result(setOf(Issue(message = message)))
        }
        val issues = mutableSetOf<Issue>()
        environment.texts.forEach {
            if (!text.contains(it)) {
                val message = "The file under the path \"${file.absolutePath}\" contain \"$it\"!"
                issues.add(Issue(message))
            }
        }
        val lines = text.split(SystemUtil.newLine)
        environment.lines.forEach {
            if (!lines.contains(it)) {
                val message =
                    "The file under the path \"${file.absolutePath}\" contain \"$it\" line!"
                issues.add(Issue(message))
            }
        }
        if (issues.isNotEmpty()) {
            return Result(issues)
        }
        return EMPTY_RESULT
    }
}
