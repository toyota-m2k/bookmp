package io.github.toyota32k.kmp.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.ext.getFullName
import kotlin.getValue
import kotlin.jvm.JvmOverloads
import kotlin.reflect.KClass
import kotlin.time.Clock

open class UtLog(
    protected val leafTag:String,
    val parent:UtLog?=null,
    omissiveNamespace:String?=null,
    protected val outputClassName:Boolean=true,
    protected val outputMethodName:Boolean=true): KoinComponent {

    companion object {
        var logLevel: LogLevel = LogLevel.DEBUG
        var throwOnAssert:Boolean = true

        private fun hierarchicTag(leafTag:String, parent: UtLog?):String {
            return if(parent!=null) {
                "${hierarchicTag(parent.leafTag, parent.parent)}.${leafTag}"
            } else {
                leafTag
            }
        }
    }

    val logger: IUtPlatformLogger by inject()
    val tag:String = hierarchicTag(leafTag, parent)
    val omissiveNamespace:String? = omissiveNamespace ?: parent?.omissiveNamespace

    private fun compose(message:String?):String {
        return logger.compose(message, omissiveNamespace, outputClassName, outputMethodName)
    }


    @JvmOverloads
    fun debug(msg: String?=null) {
        if(logLevel <= LogLevel.DEBUG) {
            logger.writeLog(LogLevel.DEBUG, tag, compose(msg))
        }
    }
    fun debug(fn:()->String?) {
        if(logLevel<=LogLevel.DEBUG) {
            logger.writeLog(LogLevel.DEBUG, tag, compose(fn()?:return))
        }
    }
    fun debug(flag:Boolean, fn:()->String) {
        if(flag && logLevel<=LogLevel.DEBUG) {
            logger.writeLog(LogLevel.DEBUG, tag, compose(fn()))
        }
    }

    @JvmOverloads
    fun warn(msg: String?=null) {
        if(logLevel<=LogLevel.WARN) {
            logger.writeLog(LogLevel.WARN, tag, compose(msg))
        }
    }

    @JvmOverloads
    fun error(msg: String?=null) {
        logger.writeLog(LogLevel.ERROR, tag, compose(msg))
    }

    @JvmOverloads
    fun error(e:Throwable, msg:String?=null) {
        error(msg)
        e.message?.also { msg->
            error(msg)
        }
        error(e.stackTraceToString())
    }

    @JvmOverloads
    fun info(msg: String?=null) {
        if(logLevel<=LogLevel.INFO) {
            logger.writeLog(LogLevel.INFO, tag, compose(msg))
        }
    }

    @JvmOverloads
    fun verbose(msg: String?=null) {
        if(logLevel<=LogLevel.VERBOSE) {
            logger.writeLog(LogLevel.VERBOSE, tag, compose(msg))
        }
    }
    fun verbose(fn: () -> String?) {
        if(logLevel<=LogLevel.VERBOSE) {
            logger.writeLog(LogLevel.VERBOSE, tag, compose(fn()?:return))
        }
    }
    fun verbose(flag:Boolean, fn:()->String) {
        if(flag && logLevel<=LogLevel.VERBOSE) {
            logger.writeLog(LogLevel.VERBOSE, tag, compose(fn()))
        }
    }

    @JvmOverloads
    fun stackTrace(e:Throwable, msg:String?=null) {
        if(msg!=null) {
            error(msg)
        }
        e.message?.also { msg->
            error(msg)
        }
        error(e.stackTraceToString())
    }
    @JvmOverloads
    fun stackTrace(level: LogLevel, e:Throwable, msg:String?=null) {
        if (logLevel <= level) {
            if (msg != null) {
                print(level, msg)
            }
            e.message?.also { msg ->
                print(level, msg)
            }
            print(level, e.stackTraceToString())
        }
    }

    @JvmOverloads
    fun print(level:LogLevel, msg:String?=null) {
        if(logLevel <= level) {
            logger.writeLog(level, tag, compose(msg))
        }
    }

    @JvmOverloads
    fun assert(chk:Boolean, msg:String?=null) {
        if(!chk) {
            stackTrace(Exception("assertion failed."), msg)
        }
    }

    @JvmOverloads
    fun assertStrongly(chk:Boolean, msg:String?=null) {
        if(!chk) {
            stackTrace(Exception("assertion failed."), msg)
            if (throwOnAssert) {
                // デバッグ版なら例外を投げる
                throw AssertionError(compose(msg))
            }
        }
    }

    @JvmOverloads
    fun scopeWatch(msg:String?=null, level:LogLevel=LogLevel.DEBUG) : AutoCloseable {
        val composed = compose(msg)
        logger.writeLog(level, tag, "$composed - enter")
        return ScopeWatcher { logger.writeLog(level, tag, "$composed - exit") }
    }

    private class ScopeWatcher(val leaving:()->Unit) : AutoCloseable {
        override fun close() {
            leaving()
        }
    }

    inline fun <T> scopeCheck(msg:String?=null, level: LogLevel=LogLevel.DEBUG, fn:()->T):T {
        return try {
            print(level, "$msg - enter")
            fn()
        } finally {
            print(level, "$msg - exit")
        }
    }

    @JvmOverloads
    inline fun <T> chronos(tag:String="TIME", msg:String="", level: LogLevel=LogLevel.DEBUG,  fn:()->T):T {
        return if (level >= logLevel) {
            UtChronos(this, tag = tag, logLevel = level).measure(msg) {
                fn()
            }
        } else fn()
    }
}

/**
 * 時間計測用ログ出力クラス
 */
open class UtChronos @JvmOverloads constructor(callerLogger:UtLog, tag:String="TIME", val logLevel:LogLevel = LogLevel.DEBUG) {
    var utLog = UtLog(tag, callerLogger)
    var prev: Long = 0
    var start: Long = 0

    init {
        reset()
    }

    fun now():Long = Clock.System.now().toEpochMilliseconds()

    fun reset() {
        prev = now()
        start = prev
    }

    private val lapTime: Long
        get() {
            val c = now()
            val d = c - prev
            prev = c
            return d
        }
    private val totalTime: Long get() = now() - start

    @JvmOverloads
    fun total(msg: String = "") {
        utLog.print(logLevel, "total = ${formatMS(totalTime)} $msg")
    }

    fun resetLap() {
        prev = now()
    }

    fun formatMS(t: Long): String {
        return "${t / 1000f} sec"
    }

    open fun formatLap(msg:String):String {
        return "lap = ${formatMS(lapTime)} $msg"
    }

    open fun formatEnter(msg:String):String {
        return "enter $msg"
    }
    open fun formatExit(msg:String, begin:Long, end:Long):String {
        return "exit ${formatMS(end - begin)} $msg"
    }

    @JvmOverloads
    fun lap(msg: String = "") {
        utLog.print(logLevel, formatLap(msg))
    }

    @JvmOverloads
    inline fun <T> measure(msg: String = "", fn: () -> T): T {
        utLog.print(logLevel, formatEnter(msg))
        val begin = now()
        return try {
            fn()
        } finally {
            utLog.print(logLevel, formatExit(msg, begin,now()))
        }
    }
}