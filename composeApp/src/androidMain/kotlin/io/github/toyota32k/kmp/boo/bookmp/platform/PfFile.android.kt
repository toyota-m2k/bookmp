package io.github.toyota32k.kmp.boo.bookmp.platform

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPfStore(override val isSupportDirectory: Boolean=true) : IPfStore {
    class AndroidPfFile(override val platformFile: PlatformFile) : IPfFile
    class AndroidPfDirectory(override val platformFile: PlatformFile) : IPfDirectory {
        override val children: List<IPfFile>
            get() {
                if (!platformFile.isDirectory()) return emptyList()
                var result: List<IPfFile>? = null
                platformFile.list { list->
                    result = list.map { if(it.isDirectory()) AndroidPfDirectory(it) else AndroidPfFile(it) }
                }
                return result ?: emptyList()
            }
    }
    override suspend fun pickDirectory(title:String): IPfDirectory? {
        val dir = FileKit.openDirectoryPicker(title) ?: return null
        return AndroidPfDirectory(dir)
    }

    override suspend fun pickFiles(title:String, type: FileKitType): List<IPfFile>? {
        return FileKit.openFilePicker(type, mode = FileKitMode.Multiple(), title,) ?.map {
            AndroidPfFile(it)
        }
    }
}

actual fun pfStoreModule(): Module = module {
    single<IPfStore> { AndroidPfStore() }
}
