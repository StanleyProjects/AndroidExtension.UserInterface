import java.io.File

object Analysis {
    enum class FileError { EXIST, DIRECTORY, TEXT }

    fun check(file: File, onError: (FileError) -> Set<String>, onText: (String) -> Set<String>): Set<String> {
        if (!file.exists()) {
            return onError(FileError.EXIST)
        }
        if (file.isDirectory) {
            return onError(FileError.DIRECTORY)
        }
        val text = try {
            file.requireFilledText()
        } catch (e: Throwable) {
            return onError(FileError.TEXT)
        }
        return onText(text)
    }
}
