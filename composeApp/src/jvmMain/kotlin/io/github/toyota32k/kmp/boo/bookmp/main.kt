package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object ConsoleEncodingFix {
    @JvmStatic
    fun apply() {
        try {
            System.setOut(java.io.PrintStream(System.out, true, Charsets.UTF_8))
            System.setErr(java.io.PrintStream(System.err, true, Charsets.UTF_8))
        } catch (_: Exception) {}
        println("file.encoding=" + System.getProperty("file.encoding"))
        println("console.encoding=" + System.getProperty("console.encoding"))
        println("default.charset=" + java.nio.charset.Charset.defaultCharset())
    }
}
fun main() = application {
    ConsoleEncodingFix.apply()
    // Initialize Napier for JVM to log to standard output
    Napier.base(DebugAntilog())
    Window(
        onCloseRequest = ::exitApplication,
        title = "bookmp",
    ) {
        println("Desktop App started")
        Napier.d("Compose Window content started")
        App()
    }
}