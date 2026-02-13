package io.github.toyota32k.kmp.boo.bookmp.ui

import io.github.toyota32k.kmp.boo.bookmp.model.IMediaItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.toyota32k.kmp.boo.bookmp.model.EmptyMediaSource
import io.github.toyota32k.kmp.boo.bookmp.model.IMediaSource
import io.github.toyota32k.kmp.util.UtLog

@Composable
fun VideoListPane(
    modifier: Modifier,
    mediaSource: IMediaSource,
    selectedVideo: IMediaItem?,
    onAddVideo: () -> Unit,
    onClearAll: ()->Unit,
    onVideoClick: (IMediaItem) -> Unit // 動画が選択された時のコールバック
    ) {
    val videos = mediaSource.mediaList
    val scope = rememberCoroutineScope()
    val logger = UtLog("List", null, "io.github.toyota32k.kmp.boo.bookmp.ui")

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier= Modifier.fillMaxSize()) {
//        logger.debug("Surface...")
            if (videos.isEmpty()) {
                // 動画が見つからない場合の表示
                Box(contentAlignment = Alignment.Center, modifier= Modifier.weight(1f).fillMaxWidth()) {
                    Text("動画ファイルが見つかりません", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp) // 下部に少し余白
                ) {
                    items(videos.size) { index ->
                        val video = videos[index]
                        VideoListItem(
                            video = video,
                            isSelected = video == selectedVideo,
                            onClick = { onVideoClick(video) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
            Row(modifier=Modifier.align(Alignment.End)) {
//                IconButton(
//                    onClick = {}
//                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.ic_star),
//                        contentDescription = "お気に入り"
//                    )
//                }
                IconButton(
                    onClick = onClearAll
                ) {
                    Icon(Icons.Default.ClearAll, contentDescription = "Clear")
                }
                IconButton(
                    onClick = onAddVideo,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}

@Composable
fun VideoListItem(
    video: IMediaItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 選択されている場合は背景色を変える
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = video.title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun VideoListPanePreview() {
    MaterialTheme {
        VideoListPane(mediaSource = EmptyMediaSource, selectedVideo = null, modifier = Modifier.fillMaxSize(), onAddVideo = {}, onClearAll = {}, onVideoClick = {})
    }
}