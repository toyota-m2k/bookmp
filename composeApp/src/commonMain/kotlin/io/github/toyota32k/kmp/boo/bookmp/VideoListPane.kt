package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@Composable
fun VideoListPane(modifier: Modifier) {
    Box( modifier = modifier.background(Color.Red.copy(alpha=0.5f)))
}