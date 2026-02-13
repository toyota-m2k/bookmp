package io.github.toyota32k.kmp.boo.bookmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.toyota32k.kmp.boo.bookmp.App
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.DEBUG) Napier.base(DebugAntilog())
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        FileKit.init(this)

        setContent {
            App()
        }
    }
}
