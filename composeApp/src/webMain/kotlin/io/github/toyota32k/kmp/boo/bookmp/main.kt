package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.toyota32k.kmp.boo.bookmp.platform.appInitKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    appInitKoin {

    }
    ComposeViewport {
        App()
    }
}