package io.github.toyota32k.kmp.util

import android.util.Log
import io.github.tyota32k.kmp.util.jvm.LogUtil
import org.koin.core.module.Module
import org.koin.dsl.module

internal class AndroidLogger: IUtPlatformLogger {
    override fun writeLog(level: LogLevel, tag: String, msg: String) {
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, msg)
            LogLevel.ERROR -> Log.e(tag, msg)
            LogLevel.INFO -> Log.i(tag, msg)
            LogLevel.VERBOSE -> Log.v(tag, msg)
            LogLevel.WARN -> Log.w(tag, msg)
            LogLevel.FATAL -> Log.e(tag, msg)
        }
    }
    val util = LogUtil(this::class.java, UtLog::class.java)
    override fun compose(message: String?, omissiveNamespace: String?, outputClassName: Boolean, outputMethodName: Boolean): String {
        return util.compose(message, omissiveNamespace, outputClassName, outputMethodName)
    }

}

actual fun platformLoggerModule(): Module = module {
    single<IUtPlatformLogger> { AndroidLogger() }
}


