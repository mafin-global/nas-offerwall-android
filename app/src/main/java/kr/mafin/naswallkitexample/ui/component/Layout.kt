package kr.mafin.naswallkitexample.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.ui.composition_local.LocalIsDarkTheme

/***************************************************************************************************
 * 기본 레이아웃
 *
 * `Scaffold` 기반의 앱 바, 기본 액션(테마변경) 버튼을 구성합니다.
 *
 * @param title 앱 바 타이틀
 * @param modifier `Modifier`
 * @param disabled 앱 바 비활성화 여부. `true` 로 설정하면, back 버튼 및 action 버튼이 비활성화 됩니다.
 * @param actions 액션 버튼 설정
 * @param content 컨텐츠
 **************************************************************************************************/
//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout(
    title: String,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    actions: (@Composable RowScope.(color: Color) -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    /** 다크 테마 여부 */
    val isDarkThemeState = LocalIsDarkTheme.current
    /** Navigation Controller */
    val navController = LocalAppState.current.navController
    /** Back 가능 여부 */
    val canGoBack = navController.previousBackStackEntry != null
    /** 배경 색상 */
    val backgroundColor = LocalAppState.current.backgroundColor
    /** 글자 색상 */
    val textColor = LocalAppState.current.textColor

    // disabled 값이 true 이면, 하드웨어 back 버튼 차단
    BackHandler(disabled) { }

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor,
                ),
                title = {
                    Text(text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton(enabled = !disabled, onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    if (actions != null) {
                        actions(MaterialTheme.colorScheme.primary)
                    } else {
                        IconButton(
                            enabled = !disabled,
                            onClick = { isDarkThemeState.value = !isDarkThemeState.value }) {
                            Icon(
                                imageVector = Icons.Default.ColorLens,
                                contentDescription = null,
                                modifier = Modifier.alpha(if (disabled) 0.4F else 1F),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .then(modifier)) {
            content()
        }
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun LayoutPreview() {
    AppContainer {
        Layout("Layout") {
            Text("Content")
        }
    }
}