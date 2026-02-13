package io.github.toyota32k.kmp.util

import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.runtime.Debugging
import kotlin.reflect.KClass

private class IOSLogger : IUtPlatformLogger {
    private fun levelToEmoji(level: LogLevel): String {
        return when (level) {
            LogLevel.VERBOSE -> "⚪️"
            LogLevel.DEBUG -> "⚪️"
            LogLevel.INFO -> "🔵"
            LogLevel.WARN -> "⚠️"
            LogLevel.ERROR -> "🔴"
            LogLevel.FATAL -> "💀"
        }
    }

    override fun writeLog(level: LogLevel, tag: String, msg: String) {
        val emoji = levelToEmoji(level)
        NSLog("%s [%s] %s: %s", emoji, level.name, tag, msg)
    }

    private val mRelevantClassNames = listOf(IOSLogger::class, UtLog::class, UtChronos::class).mapNotNull { it.qualifiedName }.toMutableList()
    val loggerRelevantClassNames: List<String> get() = mRelevantClassNames
    fun addLoggerRelevantClasses(vararg clazz: KClass<*>) {
        mRelevantClassNames.addAll( clazz.mapNotNull { it.qualifiedName } )
    }

    private fun stripNamespace(classname:String, omissiveNamespace:String?):String {
        if(!omissiveNamespace.isNullOrBlank() && classname.startsWith(omissiveNamespace)) {
            return classname.substring(omissiveNamespace.length)
        } else {
            return classname
        }
    }

    data class CallSite(
        val className:String,
        val methodName: String
    )

    @OptIn(ExperimentalNativeApi::class)
    private fun getCallStack(): List<CallSite>? {
        return try {
            Throwable().getStackTrace().mapNotNull { stackTraceString ->
                // スタックトレース文字列を解析して CallSite オブジェクトを作成する
                // フォーマット例: "0   shared                              0x00000001028f799c kfun:io.github.toyota32k.kmp.util.IOSLogger#getCallStack(){} + 140"
                try {
                    val relevantPart = stackTraceString.substringAfter("kfun:").substringBeforeLast(" +")
                    if (relevantPart.isBlank()) return@mapNotNull null

                    val classAndMethod = relevantPart.substringBeforeLast("{}")
                    val parts = classAndMethod.split('#')
                    val className = parts.getOrNull(0) ?: return@mapNotNull null
                    val methodName = parts.getOrNull(1) ?: return@mapNotNull null

                    CallSite(
                        className = className,
                        methodName = methodName,
                    )
                } catch (e: Exception) {
                    null // 解析中に例外が発生した場合はnullを返す
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getCallerInfo():CallSite? {
        val stacks = getCallStack()
        return stacks?.first { stack->
            stack.className.let { name->
                loggerRelevantClassNames.all { !name.startsWith(it) }
            }
        }
    }

    override fun compose(message: String?, omissiveNamespace: String?, outputClassName: Boolean, outputMethodName: Boolean): String {
        return if(outputClassName||outputMethodName) {
            val e = getCallerInfo() ?: return message ?: ""
            if (!outputClassName) {
                if (message != null) "${e.methodName}: $message" else e.methodName
            } else if (!outputMethodName) {
                if (message != null) "${stripNamespace(e.className, omissiveNamespace)}: ${message}" else stripNamespace(e.className, omissiveNamespace)
            } else {
                if (message != null) "${stripNamespace(e.className, omissiveNamespace)}.${e.methodName}: ${message}" else "${stripNamespace(e.className, omissiveNamespace)}.${e.methodName}"
            }
        } else {
            message ?: ""
        }
    }
}

actual fun platformLoggerModule(): Module = module {
    single<IUtPlatformLogger> { IOSLogger() }
}