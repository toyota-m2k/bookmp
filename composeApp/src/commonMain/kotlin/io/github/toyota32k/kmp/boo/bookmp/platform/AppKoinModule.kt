package io.github.toyota32k.kmp.boo.bookmp.platform

import io.github.toyota32k.kmp.util.libInitKoin
import org.koin.core.KoinApplication

fun appInitKoin(appDeclaration: KoinApplication.() -> Unit) {
    libInitKoin {
        appDeclaration()
        modules(pfStoreModule())
    }
}