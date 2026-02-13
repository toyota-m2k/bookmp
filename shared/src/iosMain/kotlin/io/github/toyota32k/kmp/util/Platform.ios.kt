package io.github.toyota32k.kmp.util

import platform.UIKit.UIDevice

actual fun platform():Platform = object:Platform {
    override val name = "iOS"
//    override val name: String = "iOS " + UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}