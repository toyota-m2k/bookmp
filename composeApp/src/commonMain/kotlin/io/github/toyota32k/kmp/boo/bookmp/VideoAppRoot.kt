package io.github.toyota32k.kmp.boo.bookmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoAppRoot() {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    var isListVisible by remember { mutableStateOf(true) }
    val isWideScreen = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    // ドロワーの状態管理（OpenかClosedか）
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(isWideScreen) {
        if (isWideScreen && drawerState.isOpen) {
            // ワイド画面になった、かつドロワーが開いていたら閉じる
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isWideScreen, // ワイド画面ではスワイプ無効
        drawerContent = {
            // ここに VideoListPane を置く
            ModalDrawerSheet {
                VideoListPane(modifier = Modifier.fillMaxHeight().fillMaxWidth())
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("My Video Player") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (isWideScreen) {
                                    isListVisible = !isListVisible // Wide時は変数を切り替え
                                } else {
                                    drawerState.open() // Narrow時はドロワーを開く
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "メニュー")
                        }             // モバイル（非ワイド）時のみメニューボタンを表示
                    }
                )
            }
        ) { innerPadding ->
            // innerPadding を使うことで、TopBar と被らない位置にコンテンツを配置できる
            Box(Modifier.padding(innerPadding)) {
                if (isWideScreen) {
                    // デスクトップなら「ドロワー」ではなく、自前で Row で並べる
                    Row {
// リスト部分をアニメーション化
                        AnimatedVisibility(
                            visible = isListVisible,
                            enter = expandHorizontally() + fadeIn(), // 横に広がりながらフェードイン
                            exit = shrinkHorizontally() + fadeOut()  // 横に縮みながらフェードアウト
                        ) {
                            VideoListPane(modifier = Modifier.width(300.dp).fillMaxHeight())
                        }
                        PlayerPane(modifier = Modifier.weight(1f).fillMaxHeight())
                    }
                } else {
                    // スマホならプレーヤーのみ（リストはドロワーで出てくる）
                    PlayerPane(modifier = Modifier.fillMaxSize().fillMaxHeight())
                }
            }
        }
    }
}