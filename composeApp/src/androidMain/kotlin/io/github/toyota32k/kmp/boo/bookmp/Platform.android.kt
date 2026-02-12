package io.github.toyota32k.kmp.boo.bookmp

import android.os.Build
import okio.FileSystem

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual val platformFileSystem: FileSystem
    get() = FileSystem.SYSTEM