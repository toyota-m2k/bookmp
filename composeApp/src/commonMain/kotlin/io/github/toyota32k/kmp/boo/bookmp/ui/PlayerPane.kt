package io.github.toyota32k.kmp.boo.bookmp.ui

import VideoItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState
import io.github.toyota32k.kmp.boo.bookmp.getPlatform
import kotlinx.coroutines.delay

@Composable
fun PlayerPane(
    videoItem: VideoItem?,
    showMenuButton: Boolean,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // プレーヤーの状態を保持
    val playerState = rememberVideoPlayerState()
    var frameTrigger by remember { mutableStateOf(0) }
    // 動画が切り替わったときに新しい URL をセットして再生
    LaunchedEffect(videoItem) {
        videoItem?.let {
            playerState.openUri(it.url)
        }
    }
    LaunchedEffect(playerState.isPlaying) {
        if (playerState.isPlaying && frameTrigger==0 && getPlatform().isJvm) {
            frameTrigger++
        }
    }
    Box(modifier = modifier.background(Color.Black)) {
        if (videoItem != null) {
            // ライブラリ提供の Composable
            key (playerState.isPlaying) {
                VideoPlayerSurface(
                    modifier = Modifier.fillMaxSize(),
                    playerState = playerState
                )
            }
        } else {
            Text(
                text = "動画を選択してください",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text ="loading=${playerState.isLoading} playing=${playerState.isPlaying}",
            color = Color.White,
            modifier = Modifier.align(Alignment.TopStart).background(Color.Black)
            )

        // ハンバーガーメニューボタン
        if (showMenuButton) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        }
    }
}