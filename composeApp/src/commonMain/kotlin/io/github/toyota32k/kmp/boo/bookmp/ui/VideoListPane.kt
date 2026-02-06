package io.github.toyota32k.kmp.boo.bookmp.ui

import VideoItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sampleVideoList

@Composable
fun VideoListPane(
    modifier: Modifier,
    onVideoClick: (VideoItem) -> Unit // 動画が選択された時のコールバック
    ) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sampleVideoList.size) { index ->
                val video = sampleVideoList[index]
                VideoListItem(
                    video = video,
                    onClick = { onVideoClick(video) }
                )
            }
        }
    }
}

@Composable
fun VideoListItem(video: VideoItem, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(video.title) },
        supportingContent = { Text("ID: ${video.id}") },
        modifier = Modifier.clickable { onClick() }
    )
}