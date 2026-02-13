package io.github.toyota32k.kmp.util

import org.koin.core.module.Module

enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
    ;
}

interface IUtLogger {
    fun writeLog(level:LogLevel, tag:String, msg:String)
}

interface IUtPlatformLogger:IUtLogger {
    fun compose(message:String?, omissiveNamespace:String?=null, outputClassName:Boolean=true, outputMethodName:Boolean=true):String
}

expect fun platformLoggerModule(): Module

//class UtLoggerChain : IUtLogger {
//    private var loggers: MutableList<IUtLogger> = mutableListOf(DebugLogger)
//
//    operator fun plus(logger: IUtLogger): UtLoggerChain
//            = apply {
//        synchronized(loggers) {
//            loggers.add(logger)
//        }
//    }
//    operator fun plusAssign(logger: IUtLogger) {
//        synchronized(loggers) {
//            loggers.add(logger)
//        }
//    }
//    operator fun minus(logger: IUtLogger): UtLoggerChain
//            = apply {
//        synchronized(loggers) {
//            loggers.remove(logger)
//        }
//    }
//    operator fun minusAssign(logger: IUtLogger) {
//        synchronized(loggers) {
//            loggers.remove(logger)
//        }
//    }
//
//    fun disableDefaultLogger():UtLoggerChain
//            = apply {
//        synchronized(loggers) {
//            loggers.remove(DebugLogger)
//        }
//    }
//
//    override fun writeLog(level: Int, tag: String, msg: String) {
//        loggers.forEach {
//            it.writeLog(level, tag, msg)
//        }
//    }
//}

//class OnMemoryLogger(val maxCount:Int = 1000) : IUtLogger {
//    data class LogEntry(val level: LogLevel, val tag:String, val msg:String) {
//        companion object {
//            fun levelToString(level:LogLevel):String {
//                return when(level) {
//                    LogLevel.Debug -> "[DEBUG]"
//                    LogLevel.Error -> "[ERROR]"
//                    LogLevel.Fatal -> "[FATAL]"
//                    LogLevel.Info -> "[INFO]"
//                    LogLevel.Verbose -> "[VERBOSE]"
//                    LogLevel.Warning -> "[WARN]"
//                }
//            }
//        }
//        override fun toString(): String {
//            return "${levelToString(level)} $tag: $msg"
//        }
//    }
//    private val list = ArrayDeque<LogEntry>(maxCount)
//    val logs:List<LogEntry> get() = list
//    override fun toString(): String {
//        return list.fold(StringBuffer()) { sb, e-> sb.append(e.toString()).append("\n") }.toString()
//    }
//
//    override fun writeLog(level:LogLevel, tag:String, msg:String) {
//        if(list.size>=maxCount) {
//            list.removeFirst()
//        }
//        list.add(LogEntry(level, tag, msg))
//    }
//}

//class FlowLogger(val flowCollector: FlowCollector<LogEntry> = MutableSharedFlow<LogEntry>(), val coroutineContext:CoroutineContext= Dispatchers.IO) : IUtLogger {
//    val scope = CoroutineScope(coroutineContext)
//    override fun writeLog(level:Int, tag:String, msg:String) {
//        scope.launch {
//            flowCollector.emit(LogEntry(level, tag, msg))
//        }
//    }
//}

//object DebugLogger : IUtLogger {
//    private fun printToSystemOut(tag: String, s: String): Int {
//        println("$tag: $s")
//        return 0
//    }
//    private fun target(level: Int): (String, String) -> Int {
//        if (!isAndroid) {
//            return this::printToSystemOut
//        }
//        return when (level) {
//            Log.DEBUG -> Log::d
//            Log.ERROR -> Log::e
//            Log.INFO -> Log::i
//            Log.WARN -> Log::w
//            else -> Log::v
//        }
//    }
//
//    override fun writeLog(level: Int, tag: String, msg: String) {
//        target(level)(tag, msg)
//    }
//}
