package io.github.toyota32k.kmp.boo.bookmp.model

import IMediaItem
import VideoFileItem
import io.github.toyota32k.kmp.boo.bookmp.platformFileSystem
import io.github.vinceglb.filekit.PlatformFile
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

class LocalMediaSource(private val directoryPath: String, private val fileSystem: FileSystem= platformFileSystem) : IMediaSource {
    private val videoExtensions = setOf(".mp4", ".mkv", ".mov", ".avi", ".webm")

    // プラットフォーム固有のファイルシステム（Android/JVM/iOSで自動切り替え）

    override val mediaList: List<IMediaItem> by lazy {
        try {
            val path = directoryPath.toPath()

            // ディレクトリ内のファイルを列挙
            fileSystem.list(path)
                .filter { p ->
                    // 拡張子チェック
                    videoExtensions.any { ext ->
                        p.name.lowercase().endsWith(ext)
                    }
                }
                .map { p ->
                    VideoFileItem(
                        id = p.name,
                        title = p.name.substringBeforeLast("."),
                        // OkioのPathからURI形式に変換（プラットフォーム間の差異を吸収）
                        file = PlatformFile(p.toString()),
                    )
                }
                .sortedBy { it.title }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 各プラットフォームでパスをURLに変換するヘルパー（expect/actualにするのが理想）
    private fun Path.toFileUrl(): String {
        return "file://$this"
    }
}