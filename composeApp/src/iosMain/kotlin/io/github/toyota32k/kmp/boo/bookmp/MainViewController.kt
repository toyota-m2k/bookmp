package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.ui.window.ComposeUIViewController
import io.github.toyota32k.kmp.boo.bookmp.platform.appInitKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

fun MainViewController() {
    appInitKoin {
        logger(PrintLogger(Level.INFO))
    }
    ComposeUIViewController { App() }
}