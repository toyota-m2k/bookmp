package io.github.toyota32k.kmp.util

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
//    includes(platformLoggerModule())
}

fun libInitKoin(appDeclaration: KoinApplication.() -> Unit) {
    startKoin {
        appDeclaration()
        modules(
            platformLoggerModule(),
            appModule)
    }
}