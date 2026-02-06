package io.github.toyota32k.kmp.boo.bookmp

interface Platform {
    val name: String
    val isAndroid: Boolean
        get() = name.startsWith("Android")
    val isJvm: Boolean
        get() = name.startsWith("Java")
    val isIos: Boolean
        get() = name.startsWith("iOS")
    val isJs: Boolean
        get() = name.endsWith("/JS")
    val isWasm: Boolean
        get() = name.endsWith("/Wasm")
}

expect fun getPlatform(): Platform