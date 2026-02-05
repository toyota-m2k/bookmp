package io.github.toyota32k.kmp.boo.bookmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform