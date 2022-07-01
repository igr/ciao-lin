package ac.obl.shaolin.four

import java.io.File
import java.util.concurrent.TimeUnit

fun runCommand(
    cmd: String,
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS,
): String? = runCatching {
    ProcessBuilder("\\s".toRegex().split(cmd))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .also {
            if (timeoutAmount != 0L) {
                it.waitFor(timeoutAmount, timeoutUnit)
            }
        }
        .inputStream.bufferedReader().readText()
}.onFailure { it.printStackTrace() }.getOrNull()
