package io.github.toyota32k.kmp.boo.bookmp.ui

import IMediaItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import io.github.aakira.napier.Napier
import io.github.kdroidfilter.composemediaplayer.VideoPlayerError
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState
import io.github.toyota32k.kmp.boo.bookmp.getPlatform
import kotlinx.coroutines.delay

@Composable
fun PlayerPane(
    videoItem: IMediaItem?,
    showMenuButton: Boolean,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // プレーヤーの状態を保持
    val playerState = rememberVideoPlayerState()
    playerState.error?.let { error ->
        val msg = when (error) {
            is VideoPlayerError.CodecError -> "Codec: " + error.message
            is VideoPlayerError.NetworkError -> "Network: " + error.message
            is VideoPlayerError.UnknownError -> "Unknown: " + error.message
            is VideoPlayerError.SourceError -> "Source: " + error.message
            else -> "Unknown error"
        }
        println("Error detected: $msg")
        playerState.clearError()
    }
    // 動画が切り替わったときに新しい URL をセットして再生
    LaunchedEffect(videoItem) {
        videoItem?.let {
            println("set: ${it.title}")
//            playerState.openUri(it.url)
        }
    }

    // JVM版で発生する不具合（再生を開始しても描画が更新されない）を回避するためのパッチ
    val isJvm = getPlatform().isJvm
    var frameTrigger by remember { mutableStateOf(0) }
    if (isJvm) {
        LaunchedEffect(playerState.isPlaying) {
            // isPlaying が 初めて trueになったとき、frameTriggerをインクリメントして再描画させる
            if (playerState.isPlaying && frameTrigger == 0) {
                frameTrigger++
            }
        }
    }

    Box(modifier = modifier.background(Color.Black)) {
        if (videoItem != null) {
            if (isJvm) {
                // JVM版の不具合回避パッチ適用版
                key (frameTrigger) {
                    Player(
                        modifier = Modifier.fillMaxSize(),
                        playerState = playerState,
                        seekPatch = isJvm
                    )
                }
            } else {
                // JVM以外（Android)の場合は、逆に上のパッチを当てると無限ループに陥ってしまい描画されなくなるので、分岐する。
                Player(
                    modifier = Modifier.fillMaxSize(),
                    playerState = playerState,
                    seekPatch = isJvm
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

@Composable
fun Player(
    modifier: Modifier = Modifier,
    playerState: VideoPlayerState,
    seekPatch:Boolean // JVM版の不具合回避
) {
    VideoPlayerSurface(
        modifier = modifier,
        playerState = playerState,
    ) {
//        Napier.d("Player: VideoPlayerSurface content")
        // This overlay will always be visible
        Box(modifier = Modifier.fillMaxSize()) {
            // You can customize the UI based on fullscreen state
            if (playerState.isFullscreen) {
                // Fullscreen UI
                IconButton(
                    onClick = { playerState.toggleFullscreen() },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "Exit Fullscreen",
                        tint = Color.White
                    )
                }
            } else {
                // Regular UI
                // 下部コントロールバー
                var seekingByUser:Float? by remember {mutableStateOf(null)}
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    // シークバーを追加
                    Slider(
                        value = seekingByUser ?: playerState.sliderPos,
                        valueRange = 0f..1000f,
                        onValueChange = { newValue ->
                            if (!seekPatch) {
                                // 公式ドキュメントの方法に従う
                                playerState.sliderPos = newValue
                            } else {
                                seekingByUser = newValue
                                println("seeking: $newValue")
                            }
                        },
                        onValueChangeFinished = {
                            val pos = seekingByUser ?: playerState.sliderPos
                            playerState.seekTo(pos)
                            seekingByUser = null
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        // スライダーの色をカスタマイズ（お好みで）
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Red,
                            activeTrackColor = Color.Red,
                            inactiveTrackColor = Color.Gray
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 再生・一時停止ボタン
                        IconButton(onClick = {
                            if (playerState.isPlaying) playerState.pause() else playerState.play()
                        }) {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "${playerState.positionText} / ${playerState.durationText}",
                            color = Color.White
                        )

//                        Column {
//                            Text("sliderPos: ${playerState.sliderPos}", color = Color.Yellow)
//                            Text("currentPosition: ${playerState.currentTime}", color = Color.Yellow)
//                            Text("duration: ${playerState.metadata.duration}", color = Color.Yellow)
//                        }                        // ここに再生時間（00:00 / 05:00）などを追加するのもアリです
                    }
                }
            }
        }
    }

}