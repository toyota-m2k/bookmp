package io.github.toyota32k.kmp.boo.bookmp.platform

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.mimeType.MimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.size
import org.koin.core.module.Module

interface IPfFile {
    val platformFile: PlatformFile
    val name: String get() = platformFile.name
    val extension: String get() = platformFile.extension
    val nameWithoutExtension: String get() = platformFile.nameWithoutExtension
    val size: Long get() = platformFile.size()
    val mimeType: MimeType? get() = platformFile.mimeType()
    val isDirectory:Boolean get() = false
}

interface IPfDirectory : IPfFile {
    val children: List<IPfFile>
    override val isDirectory:Boolean get() = true
}

interface IPfStore {
    val isSupportDirectory: Boolean
    suspend fun pickDirectory(title:String): IPfDirectory?
    suspend fun pickFiles(title:String, type:FileKitType): List<IPfFile>?
}

expect fun pfStoreModule(): Module
