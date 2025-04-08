package kr.mafin.naswallkitexample.ui.screen

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lli
import kr.mafin.naswallkitexample.common.lls
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.naswallkit.NasWallKitEmbedOfferWall
import kr.mafin.naswallkitexample.naswallkit.NasWallKitStatus
import kr.mafin.naswallkitexample.ui.component.ErrorRetry
import kr.mafin.naswallkitexample.ui.component.Layout
import kr.mafin.naswallkitexample.ui.component.AppContainer
import kr.mafin.naswallkitexample.ui.component.LoadingIndicator

/***************************************************************************************************
 * 임베드 오퍼월 화면
 **************************************************************************************************/
@Composable
fun EmbedOfferWallScreen() {
    /** Activity */
    val activity = LocalActivity.current
    /** Preview 여부 */
    val isPreview = LocalAppState.current.isPreview
    /** 텍스트 색상 */
    val textColor = LocalAppState.current.textColor

    /** 오퍼월을 임베드 할 컨테이너 `ViewGroup`*/
    val embedOfferWallContainer: MutableState<ViewGroup?> = remember { mutableStateOf(null) }

    /** NasWallKit - 임베드 오퍼월 */
    val nwkEmbedOfferWall by remember { mutableStateOf(NasWallKitEmbedOfferWall()) }
    /** 임베드 상태 */
    val embedStatus by nwkEmbedOfferWall.status.collectAsStateWithLifecycle()
    /** 임베드 오류 */
    val embedError by nwkEmbedOfferWall.error.collectAsStateWithLifecycle()

    /***********************************************************************************************
     * 임베드 실행 - Preview 에서는 노출되지 않음
     **********************************************************************************************/
    fun runEmbed(parent: ViewGroup?, status: NasWallKitStatus) {
        if (activity != null) {
            if (parent != null && status in listOf(NasWallKitStatus.IDLE, NasWallKitStatus.FAIL)) {
                nwkEmbedOfferWall.run(activity, parent)
            }
        }
    }

    /***********************************************************************************************
     * 문의하기 열기
     **********************************************************************************************/
    fun openCs() {
        if (activity != null) {
            NasWall.openCs(
                activity = activity,
                handler = { error ->
                    if (error == null) {
                        lls("NasWall.openCs() 성공")
                    } else {
                        lle("NasWall.openCs() 실패")
                    }
                },
                closeHandler = {
                    lli("문의하기 종료")
                }
            )
        }
    }

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Layout(
        title = "임베드 오퍼월",
        actions = { color ->
            IconButton(onClick = { openCs() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = null,
                    tint = color
                )
            }
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isPreview) {
                Text(
                    text = "Preview 에서는 표시되지 않습니다.",
                    color = textColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                AndroidView(
                    factory = { context ->
                        FrameLayout(context).apply { }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { viewGroup ->
                        if (embedOfferWallContainer.value !== viewGroup) {
                            embedOfferWallContainer.value = viewGroup
                            runEmbed(viewGroup, embedStatus)
                        }
                    }
                )

                when (embedStatus) {
                    NasWallKitStatus.IDLE, NasWallKitStatus.LOADING -> LoadingIndicator()
                    NasWallKitStatus.SUCCESS -> {}
                    NasWallKitStatus.FAIL -> {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            ErrorRetry(error = embedError) {
                                runEmbed(embedOfferWallContainer.value, embedStatus)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmbedOfferWallScreenPreview() {
    // 필요 시 true 로 설정 - Preview 전용 데이터 로드 시 강제로 실패 처리 할지 여부를 설정합니다.
    NasWall.debugPreviewDataForceFail(false)

    AppContainer {
        EmbedOfferWallScreen()
    }
}