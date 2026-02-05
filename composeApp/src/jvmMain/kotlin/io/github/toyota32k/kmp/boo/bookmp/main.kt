package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "bookmp",
    ) {
        App()
    }
}