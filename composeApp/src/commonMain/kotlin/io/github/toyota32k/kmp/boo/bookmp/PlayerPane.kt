package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState

@Composable
fun PlayerPane(modifier: Modifier) {
    val videoPlayerState = rememberVideoPlayerState()

    Box(modifier = modifier.background(Color.Black)) {
        VideoPlayerSurface(
            modifier = Modifier.fillMaxSize(),
            playerState = videoPlayerState,
        )
    }
}