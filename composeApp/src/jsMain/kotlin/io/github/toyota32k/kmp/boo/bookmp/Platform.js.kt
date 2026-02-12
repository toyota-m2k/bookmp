package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.ui.text.platform.Font
import okio.FileSystem

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()
