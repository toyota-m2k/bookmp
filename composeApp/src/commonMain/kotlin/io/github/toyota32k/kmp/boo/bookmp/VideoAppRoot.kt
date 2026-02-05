package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
@Preview
fun VideoAppRoot() {
    // 現在のウィンドウサイズ情報を取得
    val adaptiveInfo = currentWindowAdaptiveInfo()
    // ここで WindowWidthSizeClass.Expanded などと比較します
    val windowSize = adaptiveInfo.windowSizeClass
    // 「幅が少なくとも Medium（中画面）以上の境界を満たしているか」を判定
    // デスクトップやタブレット横置きなら True になります
    val isExpanded = windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    // 動画リストの開閉状態
    var isListVisible by remember { mutableStateOf(true) }

    if (isExpanded) {
        // 【デスクトップ/タブレット横】 左右並列レイアウト
        Row(Modifier.fillMaxSize().background(Color.Yellow)) {
            if (isListVisible) {
                VideoListPane(modifier = Modifier.width(100.dp).fillMaxHeight())
            }
            PlayerPane(modifier = Modifier.weight(1f).fillMaxHeight())
        }
    } else {
        // 【スマホ/タブレット縦】 重ね合わせレイアウト
        Box(Modifier.fillMaxSize().background(Color.Black)) {
            PlayerPane(modifier = Modifier.fillMaxSize())

            // アニメーション付きでリストを左から出す（Drawerのような挙動）
            AnimatedVisibility(
                visible = isListVisible,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                VideoListPane(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f).background(Color.Black.copy(alpha = 0.8f)))
            }
        }
    }
}