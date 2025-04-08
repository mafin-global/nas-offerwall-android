package kr.mafin.naswallkitexample.ui.composition_local

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

/** (Composition Local) 다크 테마 여부 */
val LocalIsDarkTheme: ProvidableCompositionLocal<MutableState<Boolean>> =
    compositionLocalOf { error("값이 제공되지 않았습니다.") }

/** 앱 상태 정보 */
data class AppState(
    val backgroundColor: Color,
    val textColor: Color,
    val isPreview: Boolean,
    val navController: NavHostController,
)

/** (Composition Local) 앱 상태 정보 */
val LocalAppState = compositionLocalOf<AppState> { error("값이 제공되지 않았습니다.") }