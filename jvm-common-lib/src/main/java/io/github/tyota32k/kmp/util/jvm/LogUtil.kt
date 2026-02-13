package io.github.tyota32k.kmp.util.jvm

class LogUtil(vararg relevantClasses:Class<*>) {
    /**
     * ロガー出力時にコールスタックから除外するクラス名
     */
    private val mRelevantClassNames = mutableListOf<String>(LogUtil::class.java.name, *relevantClasses.map {it.name}.toTypedArray())
    val loggerRelevantClassNames: List<String> get() = mRelevantClassNames
    fun addLoggerRelevantClasses(vararg clazz:Class<*>) {
        mRelevantClassNames.addAll( clazz.map { it.name } )
    }

    private fun stripNamespace(classname:String, omissiveNamespace:String?):String {
        if(!omissiveNamespace.isNullOrBlank() && classname.startsWith(omissiveNamespace)) {
            return classname.substring(omissiveNamespace.length)
        } else {
            return classname
        }
    }

    private fun getCallerInfo():StackTraceElement {
        val stacks = Throwable().stackTrace  // Thread.currentThread().stackTrace  Throwable().stackTraceの方が速いらしい。
        return stacks.first { stack->
            stack.className.let { name->
                loggerRelevantClassNames.all { !name.startsWith(it) }
            }
        }
    }

    fun compose(message:String?, omissiveNamespace: String?=null, outputClassName:Boolean=true, outputMethodName:Boolean=true):String {
        return if(outputClassName||outputMethodName) {
            val e = getCallerInfo()
            if(!outputClassName) {
                if(message!=null) "${e.methodName}: $message" else e.methodName
            } else if(!outputMethodName) {
                if(message!=null) "${stripNamespace(e.className, omissiveNamespace)}: ${message}" else stripNamespace(e.className, omissiveNamespace)
            } else {
                if(message!=null) "${stripNamespace(e.className, omissiveNamespace)}.${e.methodName}: ${message}" else "${stripNamespace(e.className, omissiveNamespace)}.${e.methodName}"
            }
        } else {
            message ?: ""
        }
    }
}