package io.github.toyota32k.kmp.boo.bookmp

import okio.FileSystem
import okio.FileSystem.Companion.SYSTEM
import okio.SYSTEM

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val platformFileSystem: FileSystem = SYSTEM