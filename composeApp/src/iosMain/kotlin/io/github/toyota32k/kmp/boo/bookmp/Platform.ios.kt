package io.github.toyota32k.kmp.boo.bookmp

import okio.FileSystem
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = "iOS " + UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual val platformFileSystem: FileSystem
    get() = FileSystem.SYSTEM