package kr.mafin.naswallkitexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkitexample.common.llInit
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.ui.route.Route
import kr.mafin.naswallkitexample.ui.component.AppContainer
import kr.mafin.naswallkitexample.ui.screen.CustomOfferWallScreen
import kr.mafin.naswallkitexample.ui.screen.EmbedOfferWallScreen
import kr.mafin.naswallkitexample.ui.screen.MainScreen
import kr.mafin.naswallkitexample.ui.screen.PurchaseItemScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 디버그 모드에서 로그 출력 초기화
        llInit(this)

        enableEdgeToEdge()
        setContent {
            AppContainer {
                NavHost(navController = LocalAppState.current.navController, startDestination = Route.MAIN.id) {
                    composable(Route.MAIN.id) {
                        MainScreen()
                    }
                    composable(Route.EMBED_OFFER_WALL.id) {
                        EmbedOfferWallScreen()
                    }
                    composable(Route.PURCHASE_ITEM.id) {
                        PurchaseItemScreen()
                    }
                    composable(Route.CUSTOM_OFFER_WALL.id) {
                        CustomOfferWallScreen()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    // 필요 시 true 로 설정 - Preview 전용 데이터 로드 시 강제로 실패 처리 할지 여부를 설정합니다.
    NasWall.debugPreviewDataForceFail(false)

    AppContainer {
        MainScreen()
    }
}