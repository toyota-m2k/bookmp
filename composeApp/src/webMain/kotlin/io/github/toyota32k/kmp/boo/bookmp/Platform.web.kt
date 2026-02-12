package io.github.toyota32k.kmp.boo.bookmp

import okio.FileSystem

actual val platformFileSystem: FileSystem
    get() = throw UnsupportedOperationException("FileSystem.SYSTEM is not supported on this platform.")