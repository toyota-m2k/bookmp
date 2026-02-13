package io.github.toyota32k.kmp.boo.bookmp.platform

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import org.koin.core.module.Module
import org.koin.dsl.module

class WebPfStore(override val isSupportDirectory: Boolean=false) : IPfStore {
    override suspend fun pickDirectory(title: String): IPfDirectory? {
        return null
    }

    override suspend fun pickFiles(title: String, type: FileKitType): List<IPfFile>? {
        class WebFile(override val platformFile: PlatformFile) :IPfFile
        return FileKit.openFilePicker(type, mode = FileKitMode.Multiple(), title)?.map {
            WebFile(it)
        }
    }
}

actual fun pfStoreModule(): Module = module {
    single { WebPfStore() }
}