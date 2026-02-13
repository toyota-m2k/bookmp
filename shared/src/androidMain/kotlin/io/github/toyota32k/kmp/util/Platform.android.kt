package io.github.toyota32k.kmp.util

actual fun platform():Platform = object:Platform {
    override val name = "Android"
}