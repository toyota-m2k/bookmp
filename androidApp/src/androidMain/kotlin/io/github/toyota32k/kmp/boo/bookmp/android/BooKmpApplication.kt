package io.github.toyota32k.kmp.boo.bookmp.android

import android.app.Application
import io.github.toyota32k.kmp.boo.bookmp.platform.appInitKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class BooKmpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        appInitKoin {
            androidLogger()
            androidContext(this@BooKmpApplication)
        }
    }
}
