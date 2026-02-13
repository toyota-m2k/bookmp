package io.github.toyota32k.kmp.boo.bookmp.model

interface IMediaSource {
    val mediaList: List<IMediaItem>
}

object EmptyMediaSource : IMediaSource {
    override val mediaList: List<IMediaItem> = emptyList()
}