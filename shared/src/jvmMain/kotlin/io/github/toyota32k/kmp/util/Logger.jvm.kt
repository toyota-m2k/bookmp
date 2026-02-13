package io.github.toyota32k.kmp.util

import io.github.tyota32k.kmp.util.jvm.LogUtil
import org.koin.core.module.Module
import org.koin.dsl.module
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private class JvmLogger : IUtPlatformLogger {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    // ANSI カラーコード
    private val ANSI_RESET = "\u001B[0m"
    private val ANSI_BLACK = "\u001B[30m"
    private val ANSI_RED = "\u001B[31m"
    private val ANSI_GREEN = "\u001B[32m"
    private val ANSI_YELLOW = "\u001B[33m"
    private val ANSI_BLUE = "\u001B[34m"
    private val ANSI_MAGENTA = "\u001B[35m"
    private val ANSI_CYAN = "\u001B[36m"
    private val ANSI_WHITE = "\u001B[37m"

    override fun writeLog(level: LogLevel, tag: String, msg: String) {
        val color = when (level) {
            LogLevel.VERBOSE -> ANSI_CYAN
            LogLevel.DEBUG -> ANSI_BLUE
            LogLevel.INFO -> ANSI_WHITE
            LogLevel.WARN -> ANSI_YELLOW
            LogLevel.ERROR, LogLevel.FATAL -> ANSI_RED
        }
        val timestamp = LocalDateTime.now().format(formatter)
        println("$timestamp [$color${level.name.uppercase()}$ANSI_RESET] $tag: $msg")
    }
    val util = LogUtil(this::class.java, UtLog::class.java)
    override fun compose(message: String?, omissiveNamespace: String?, outputClassName: Boolean, outputMethodName: Boolean): String {
        return util.compose(message, omissiveNamespace, outputClassName, outputMethodName)
    }
}

actual fun platformLoggerModule(): Module = module {
    single<IUtPlatformLogger> { JvmLogger() }
}
