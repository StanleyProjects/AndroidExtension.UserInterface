import java.io.File

fun checkFileExists(file: File) {
    check(file.exists()) { "File by path \"${file.absolutePath}\" is not exists!" }
}
