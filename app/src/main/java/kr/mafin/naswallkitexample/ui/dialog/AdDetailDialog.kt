package kr.mafin.naswallkitexample.ui.dialog

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallAdInfo
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.ui.composition_local.LocalIsDarkTheme
import kr.mafin.naswallkitexample.naswallkit.NasWallKitAdDescription
import kr.mafin.naswallkitexample.naswallkit.NasWallKitInitialize
import kr.mafin.naswallkitexample.naswallkit.NasWallKitJoinAd
import kr.mafin.naswallkitexample.ui.component.AdIcon
import kr.mafin.naswallkitexample.ui.component.AdPriceMissionText
import kr.mafin.naswallkitexample.ui.component.AdTitle
import kr.mafin.naswallkitexample.ui.component.Alert
import kr.mafin.naswallkitexample.ui.component.ErrorRetry
import kr.mafin.naswallkitexample.ui.component.NasWallKitStatusUI
import kr.mafin.naswallkitexample.ui.component.AppContainer
import kr.mafin.naswallkitexample.ui.component.LoadingView

/***************************************************************************************************
 * 광고 상세 화면 Dialog
 *
 * @param adInfo 광고 정보
 * @param onDismissRequest 닫기 요청 핸들러
 **************************************************************************************************/
@Composable
fun AdDetailDialog(adInfo: NasWallAdInfo, onDismissRequest: () -> Unit) {
    /** Context */
    val context = LocalContext.current
    /** Activity */
    val activity = context as? Activity
    /** 다크 테마 여부 */
    val isDarkTheme by LocalIsDarkTheme.current
    /** 배경 색상 */
    val backgroundColor = LocalAppState.current.backgroundColor

    /** NasWallKit - 광고 참여 */
    val nwkJoinAd = rememberSaveable { NasWallKitJoinAd() }
    /** 광고 참여 상태 */
    val joinAdStatus by nwkJoinAd.status.collectAsStateWithLifecycle()

    /** NasWallKit - 광고 상세 설명 조회 */
    val nwkAdDescription = rememberSaveable { NasWallKitAdDescription() }
    /** 광고 상세 설명 조회 상태 */
    val adDescriptionStatus by nwkAdDescription.status.collectAsStateWithLifecycle()

    /** 참여 실패 얼럿 표시 오류 정보 */
    var showJoinFailAlert by remember { mutableStateOf<NasWallError?>(null) }

    /***************************************************************************************************
     * 최초 로드 시. 광고 상세 설명 조회
     **************************************************************************************************/
    LaunchedEffect(Unit) {
        nwkAdDescription.run(adInfo)
    }

    /***********************************************************************************************
     * 광고 참여하기
     **********************************************************************************************/
    fun join() {
        activity?.let {
            nwkJoinAd.run(activity, adInfo) { error, isCancel ->
                if (isCancel) {
                    // 필요한 권한이 없어서 취소 된 경우, 추가 작업이 필요 없습니다.
                    // NasWallKit 내부적으로 권한 요청 팝업을 띄웁니다.
                } else {
                    if (error != null) {
                        // 참여 실패 시, 얼럿 표시
                        showJoinFailAlert = error
                    } else {
                        // 참여 성공 시, 광고 상세 화면 Dialog 종료 요청
                        onDismissRequest()
                    }
                }
            }
        }
    }

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            // 광고 참여 중이면, 하드웨어 back 버튼 차단
            dismissOnBackPress = !joinAdStatus.isLoading(),
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(backgroundColor)
            ) {
                // 타이틀 바
                TitleBar(onDismissRequest = onDismissRequest)

                // 광고 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDarkTheme) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.background
                        )
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                ) {
                    AdIcon(url = adInfo.iconUrl)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.weight(1F)
                    ) {
                        AdTitle(title = adInfo.title)

                        AdPriceMissionText(
                            adPrice = adInfo.adPrice,
                            missionText = adInfo.missionText
                        )

                        Text(
                            text = "${adInfo.rewardText()} 적립",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // 광고 상세 설명
                Description(adInfo, nwkAdDescription)

                // 참여하기/닫기 버튼
                if (adDescriptionStatus.isSuccessOrFail()) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(7.dp),
                        onClick = {
                            if (adDescriptionStatus.isSuccess()) {
                                join()
                            } else {
                                onDismissRequest()
                            }
                        }
                    ) {
                        Text(if (adDescriptionStatus.isSuccess()) "참여하기" else "닫기")
                    }
                }
            }

            // 광고 참여 중이면, 로딩 화면 표시
            if (joinAdStatus.isLoading()) {
                LoadingView()
            }
        }
    )

    // 참여 실패 얼럿
    if (showJoinFailAlert != null) {
        Alert(message = showJoinFailAlert!!.toString(), title = "참여 실패") {
            showJoinFailAlert = null
        }
    }
}

/***************************************************************************************************
 * (PRIVATE) 타이틀 바
 **************************************************************************************************/
@Composable
private fun TitleBar(onDismissRequest: () -> Unit) {
    /** 다크 테마 여부 */
    val isDarkTheme by LocalIsDarkTheme.current
    /** 배경 색상 */
    val backgroundColor = if (isDarkTheme) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.primary
    /** 글자 색상 */
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        // 제목
        Text(
            text = "참여하기",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.align(Alignment.Center)
        )

        // 닫기 버튼
        IconButton(
            onClick = onDismissRequest,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "닫기",
                tint = textColor
            )
        }
    }
}

/***************************************************************************************************
 * (PRIVATE) 광고 상세 설명
 **************************************************************************************************/
@Composable
private fun ColumnScope.Description(adInfo: NasWallAdInfo, nwkAdDescription: NasWallKitAdDescription) {
    /** 광고 상세 설명 조회 상태 */
    val status by nwkAdDescription.status.collectAsStateWithLifecycle()
    /** 광고 상세 설명 조회 오류 */
    val error by nwkAdDescription.error.collectAsStateWithLifecycle()
    /** 광고 상세 설명 */
    val description by nwkAdDescription.data.collectAsStateWithLifecycle()

    NasWallKitStatusUI(
        status = status,
        modifier = Modifier
            .weight(1F)
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        // 광고 상세 설명 조회 실패
        failContent = {
            ErrorRetry(error = error, modifier = Modifier.align(Alignment.Center)) {
                nwkAdDescription.run(adInfo)
            }
        },
        // 광고 상세 설명 조회 성공
        successContent = {
            if (description != null) {
                Text(
                    text = description!!,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = (14.5).sp,
                )
            }
        }
    )
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun AdDetailDialogPreview() {
    // 필요 시 true 로 설정 - Preview 전용 데이터 로드 시 강제로 실패 처리 할지 여부를 설정합니다.
    NasWall.debugPreviewDataForceFail(false)

    NasWallKitInitialize().run(LocalContext.current, LocalInspectionMode.current)

    AppContainer {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AdDetailDialog(NasWallAdInfo.sampleForPreview()) { }
        }
    }
}