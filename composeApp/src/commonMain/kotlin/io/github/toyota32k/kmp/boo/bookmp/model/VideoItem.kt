data class VideoItem(
    val id: String,
    val title: String,
    val url: String,
    val thumbnailUrl: String? = null,
    val duration: Long = 0L
)

// サンプルデータも同じパッケージに置いておくと、今は使いやすいです
val sampleVideoList = listOf(
    VideoItem("1", "Big Buck Bunny", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
    VideoItem("2", "Elephant's Dream", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
    VideoItem("3", "For Bigger Blazes", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
)