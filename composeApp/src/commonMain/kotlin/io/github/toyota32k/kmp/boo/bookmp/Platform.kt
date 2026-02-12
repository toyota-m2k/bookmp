package io.github.toyota32k.kmp.boo.bookmp

import okio.FileSystem


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
    val isWeb: Boolean
        get() = isJs || isWasm
}

expect fun getPlatform(): Platform

expect val platformFileSystem: FileSystem