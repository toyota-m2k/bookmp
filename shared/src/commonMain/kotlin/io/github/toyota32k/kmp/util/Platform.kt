package io.github.toyota32k.kmp.util

interface Platform {
    val name: String
    val isAndroid: Boolean
        get() = name.startsWith("Android")
    val isJvm: Boolean
        get() = name.startsWith("JVM")
    val isIos: Boolean
        get() = name.startsWith("iOS")
    val isJs: Boolean
        get() = name.endsWith("/JS")
    val isWasm: Boolean
        get() = name.endsWith("/Wasm")
    val isWeb: Boolean
        get() = isJs || isWasm
}

expect fun platform(): Platform
