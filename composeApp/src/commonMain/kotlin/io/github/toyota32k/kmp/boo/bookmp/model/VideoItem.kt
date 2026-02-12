import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.vinceglb.filekit.PlatformFile

interface IMediaItem {
    val id: String
    val title: String
}
interface IMediaRemoteItem: IMediaItem {
    val url: String
}
interface IMediaFileItem: IMediaItem {
    val file: PlatformFile
}

fun VideoPlayerState.load(item: IMediaItem) {
    when (item) {
        is IMediaRemoteItem -> {
            this.openUri(item.url)
        }
        is IMediaFileItem -> {
            this.openFile(item.file)
        }
    }
}

data class VideoFileItem  (
    override val id: String,
    override val title: String,
    override val file: PlatformFile,
) : IMediaFileItem

// サンプルデータも同じパッケージに置いておくと、今は使いやすいです
//val sampleVideoList = listOf(
//    VideoItem("1", "Big Buck Bunny", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
//    VideoItem("2", "Elephant's Dream", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
//    VideoItem("3", "For Bigger Blazes", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
//)