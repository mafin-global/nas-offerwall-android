package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkitexample.ui.composition_local.AppState
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.ui.composition_local.LocalIsDarkTheme
import kr.mafin.naswallkitexample.ui.theme.AppTheme

/***************************************************************************************************
 * 앱의 기본 컨테이너
 *
 * Activity 및 Preview 에서 사용
 *
 * 항상 Activity, Preview 의 최상단에 위치해야 함
 *
 * @param isDarkTheme 다크 테마를 강제로 적용할 때 사용 (Preview 에서 다크 테마 확인을 위해 사용)
 * @param content 컨텐츠
 **************************************************************************************************/
@Composable
fun AppContainer(isDarkTheme: Boolean? = null, content: @Composable () -> Unit) {
    /** System UI Controller - 시스템 Status Bar, Navigation Bar 색상 변경을 위해 사용 */
    val systemUiController = rememberSystemUiController()
    /** Navigation Controller */
    val navController = rememberNavController()
    /** 시스템 다크 테마 여부 */
    val isSystemDarkTheme = isSystemInDarkTheme()

    /** Navigation Controller - Composition Local */
    val localNavController = remember { navController }
    /** 앱 다크 테마 여부 - Composition Local */
    val localIsDarkThemeState = rememberSaveable { mutableStateOf(isDarkTheme ?: isSystemDarkTheme) }
    /** Preview 여부 - Composition Local */
    val localIsPreview = LocalInspectionMode.current

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    // Composition Local 설정 - Preview 여부, 다크 테마 여부
    CompositionLocalProvider(
        LocalIsDarkTheme provides localIsDarkThemeState
    ) {
        /** 다크 테마 여부 */
        val localIsDarkTheme by LocalIsDarkTheme.current

        // 테마 변경 시, NasWallKit 테마 설정
        LaunchedEffect(localIsDarkTheme) {
            NasWall.setIsDarkTheme(localIsDarkTheme)
        }

        AppTheme(darkTheme = localIsDarkTheme, dynamicColor = false) {
            /** 기본 배경 색상 */
            val backgroundColor =
                if (localIsDarkTheme) MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.surfaceContainer
            /** 기본 텍스트 색상 */
            val textColor =
                if (localIsDarkTheme) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onSurface

            LaunchedEffect(localIsDarkTheme) {
                systemUiController.setSystemBarsColor(
                    color = backgroundColor,
                    darkIcons = !localIsDarkTheme
                )
            }

            CompositionLocalProvider(
                LocalAppState provides AppState(
                    backgroundColor = backgroundColor,
                    textColor = textColor,
                    isPreview = localIsPreview,
                    navController = localNavController
                )
            ) {
                content()
            }
        }
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview
@Composable
private fun AppContainerPreview() {
    AppContainer {
        Layout("AppContainer") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(10.dp)) {
                Text("Primary Text", color = MaterialTheme.colorScheme.primary)
                Text("Secondary Text", color = MaterialTheme.colorScheme.secondary)
                Text("Tertiary Text", color = MaterialTheme.colorScheme.tertiary)
                Button(onClick = {}) {
                    Text("Primary Button")
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Secondary Button")
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Tertiary Button")
                }
                FilledTonalButton(onClick = {}) {
                    Text("FilledTonalButton")
                }
                OutlinedButton(onClick = {}) {
                    Text("OutlinedButton")
                }
                ElevatedButton(onClick = {}) {
                    Text("ElevatedButton")
                }
                TextButton(onClick = {}) {
                    Text("TextButton")
                }
            }
        }
    }
}