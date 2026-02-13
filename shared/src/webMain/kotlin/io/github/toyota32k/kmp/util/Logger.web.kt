package io.github.toyota32k.kmp.util

import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.collections.getOrNull
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js
import kotlin.text.lines
import kotlin.text.trim

// ブラウザの console オブジェクトをKotlinから利用可能にする
private external val console: dynamic

private class ConsoleLogger : IUtPlatformLogger {
    override fun writeLog(level: LogLevel, tag: String, msg: String) {
        val message = "[${level.name}] $tag: $msg"
        when (level) {
            LogLevel.VERBOSE, LogLevel.DEBUG -> console.log(message)
            LogLevel.INFO -> console.info(message)
            LogLevel.WARN -> console.warn(message)
            LogLevel.ERROR, LogLevel.FATAL -> console.error(message)
        }
    }

    data class CallSite(
        val className:String,
        val methodName: String
    )

    companion object {
        private val regex = Regex(""".*\(.*?([^/)]+\.js:\d+:\d+)\)""")
        private const val relevants = "kotlin/bookmp-shared.js"
    }


    @OptIn(ExperimentalWasmJsInterop::class)
    private fun getCallStack(): String? {
        val stackRaw: dynamic = js("new Error().stack")
        return stackRaw as? String
    }
    override fun compose(message: String?, omissiveNamespace: String?, outputClassName: Boolean, outputMethodName: Boolean): String {
        val callerInfo = getCallStack()?.lines()?.firstOrNull { it.contains("at ") && !it.contains(relevants)} ?: return message ?: ""
        val sb = StringBuilder()
        if (outputMethodName) {
            val name = callerInfo.substringBefore(" (").substringAfter("at ").trim()
            if (name.isNotBlank()) {
                sb.append(name).append(" ")
            }
        }
        if (outputClassName) {
            val matchResult = regex.find(callerInfo)
            val location = matchResult?.groups?.get(1)?.value
            if (location != null) {
                sb.append(location).append(" ")
            }
        }
        sb.append(message ?: "")
        return sb.toString()
    }
}

actual fun platformLoggerModule(): Module = module {
    single<IUtPlatformLogger> { ConsoleLogger() }
}