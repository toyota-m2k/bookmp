package io.github.toyota32k.kmp.boo.bookmp.model

import io.github.toyota32k.kmp.boo.bookmp.platform.IPfStore
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MediaSource(override val mediaList: List<IMediaItem>) : IMediaSource {

    companion object:KoinComponent {
        private val videoExtensions = setOf("mp4", "mkv", "mov", "avi", "webm")
        suspend fun getLocalSource(): IMediaSource? {
            val pf: IPfStore = get()
            val list = if (pf.isSupportDirectory) {
                pf.pickDirectory("Video Files")?.children
            } else {
                pf.pickFiles("Video Files", FileKitType.Video)
            }?.filter {
                !it.isDirectory && videoExtensions.contains(it.extension)
            }?.map {
                MediaFileItem(it.name, it.nameWithoutExtension, it.platformFile)
            }
            if (list.isNullOrEmpty()) {
                return null
            }
            return MediaSource(list)
        }
    }
}