package kr.mafin.naswallkitexample.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdUnits
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lli
import kr.mafin.naswallkitexample.common.lls
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.ui.route.Route
import kr.mafin.naswallkitexample.naswallkit.NasWallKitEnv
import kr.mafin.naswallkitexample.naswallkit.NasWallKitInitialize
import kr.mafin.naswallkitexample.naswallkit.NasWallKitServerType
import kr.mafin.naswallkitexample.naswallkit.NasWallKitStatus
import kr.mafin.naswallkitexample.naswallkit.NasWallKitTotalPoint
import kr.mafin.naswallkitexample.naswallkit.NasWallKitUserPoint
import kr.mafin.naswallkitexample.ui.component.ErrorRetry
import kr.mafin.naswallkitexample.ui.component.Layout
import kr.mafin.naswallkitexample.ui.component.SectionList
import kr.mafin.naswallkitexample.ui.component.NasWallKitStatusUI
import kr.mafin.naswallkitexample.ui.component.SectionListIconStyle
import kr.mafin.naswallkitexample.ui.component.AppContainer
import kr.mafin.naswallkitexample.ui.component.SectionScope

@Composable
fun MainScreen() {
    /** Context */
    val context = LocalContext.current
    /** Activity */
    val activity = LocalActivity.current
    /** Navigation Controller */
    val navController = LocalAppState.current.navController
    /** Preview 여부 */
    val isPreview = LocalAppState.current.isPreview

    /** NasWallIit - 초기화 */
    val nwkInitialize = rememberSaveable { NasWallKitInitialize() }
    /** 초기화 상태 */
    val initializeStatus by nwkInitialize.status.collectAsStateWithLifecycle()
    /** 초기화 오류 */
    val initializeError by nwkInitialize.error.collectAsStateWithLifecycle()

    /** NasWallKit - 획득 가능 총 적립금 조회 */
    val nwkTotalPoint = rememberSaveable { NasWallKitTotalPoint() }
    /** NasWallKit - 회원 보유 적립금 조회 */
    val nwkUserPoint = rememberSaveable { NasWallKitUserPoint() }

    /** 설정 정보 상세 내용 표시 여부 */
    var isShowSettingsDetail by remember { mutableStateOf(isPreview) }

    /***********************************************************************************************
     * LaunchedEffect - NasWallKit 초기화
     **********************************************************************************************/
    LaunchedEffect(Unit) {
        if (nwkInitialize.status.value == NasWallKitStatus.IDLE) {
            nwkInitialize.run(context, isPreview)
        }
    }

    /***************************************************************************************************
     * 팝업 오퍼월 열기
     **************************************************************************************************/
    fun popupOfferWall() {
        if (activity != null) {
            NasWall.openPopupOfferWall(
                activity = activity,
                handler = { error ->
                    if (error == null) {
                        lls("팝업 오퍼월 열기 성공")
                    } else {
                        lle("팝업 오퍼월 열기 실패", error)
                    }
                },
                closeHandler = {
                    lli("팝업 오퍼월 종료")
                }
            )
        }
    }

    /***************************************************************************************************
     * 문의하기 열기
     **************************************************************************************************/
    fun openCs() {
        if (activity != null) {
            NasWall.openCs(
                activity = activity,
                handler = { error ->
                    if (error == null) {
                        lls("문의하기 열기 성공")
                    } else {
                        lle("문의하기 열기 실패", error)
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
    Layout("NAS 오퍼월 SDK") {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            NasWallKitStatusUI(
                status = initializeStatus,
                failContent = {
                    // 초기화 실패 시 UI
                    ErrorRetry(
                        msg = "NasWallKit 초기화 실패",
                        error = initializeError,
                        showSecretMsg = true
                    )
                },
                successContent = {
                    // 초기화 성공 시 UI
                    SectionList(
                        divider = true,
                        iconStyle = SectionListIconStyle.RECTANGLE,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        // 설정 정보
                        section {
                            Settings(isShowSettingsDetail) {
                                isShowSettingsDetail = !isShowSettingsDetail
                            }
                            SettingsDetail(isShowSettingsDetail)
                        }

                        // 획득 가능 총 적립금
                        section {
                            TotalPoint(nwkTotalPoint)
                        }

                        // 팝업 오퍼월, 임베드 오퍼월
                        section("내장 UI") {
                            item(
                                icon = Icons.Default.AdUnits,
                                iconColor = MaterialTheme.colorScheme.primary,
                                title = "팝업 오퍼월",
                                onClick = { popupOfferWall() }
                            )
                            item(
                                icon = Icons.Default.Screenshot,
                                iconColor = MaterialTheme.colorScheme.primary,
                                title = "임베드 오퍼월",
                                onClick = { navController.navigate(Route.EMBED_OFFER_WALL.id) }
                            )
                        }

                        // 개발자 정의 UI 오퍼월, 문의하기
                        section("개발자 정의 UI") {
                            item(
                                icon = Icons.Default.Mode,
                                iconColor = MaterialTheme.colorScheme.primary,
                                title = "개발자 정의 UI 오퍼월",
                                onClick = { navController.navigate(Route.CUSTOM_OFFER_WALL.id) }
                            )
                            item(
                                icon = Icons.Default.QuestionMark,
                                iconColor = MaterialTheme.colorScheme.primary,
                                title = "문의하기",
                                onClick = { openCs() }
                            )
                        }

                        // NAS 서버에서 적립금 관리 시 - 보유 적립금, 아이템 구입
                        if (NasWallKitEnv.SERVER_TYPE === NasWallKitServerType.NAS) {
                            section(title = "적립금") {
                                UserPoint(nwkUserPoint)

                                item(
                                    icon = Icons.Default.ShoppingCart,
                                    iconColor = MaterialTheme.colorScheme.tertiary,
                                    title = "아이템 구입",
                                    onClick = { navController.navigate(Route.PURCHASE_ITEM.id) }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

/***************************************************************************************************
 * (PRIVATE) 설정 정보
 **************************************************************************************************/
@Composable
private fun SectionScope.Settings(isShow: Boolean, onClick: () -> Unit) {

    item(
        icon = Icons.Default.Settings,
        iconColor = Color.Gray,
        title = "설정 정보 (${if (NasWallKitEnv.TEST_MODE) "테스트 모드" else "라이브 모드"})",
        valueContent = {
            Icon(
                imageVector = if (isShow) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick
    )
}

/***************************************************************************************************
 * (PRIVATE) 설정 상세 정보
 **************************************************************************************************/
@Composable
private fun SectionScope.SettingsDetail(show: Boolean) {
    val dividerColor = LocalAppState.current.backgroundColor

    item(
        hidden = !show,
        titleContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(start = 20.dp)
            ) {
                SettingsDetailItem("앱 KEY", NasWallKitEnv.APP_KEY)
                HorizontalDivider(color = dividerColor)
                SettingsDetailItem("적립금 관리 서버", NasWallKitEnv.SERVER_TYPE.title)
                HorizontalDivider(color = dividerColor)
                if (NasWallKitEnv.SERVER_TYPE == NasWallKitServerType.NAS) {
                    SettingsDetailItem("회원 ID", NasWallKitEnv.userId)
                } else {
                    SettingsDetailItem("회원 데이터", NasWallKitEnv.userData)
                }
            }
        }
    )
}

@Composable
private fun SettingsDetailItem(title: String, value: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alpha(0.7F)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/***************************************************************************************************
 * (PRIVATE) 획득 가능 총 적입금
 **************************************************************************************************/
@Composable
private fun SectionScope.TotalPoint(nwkTotalPoint: NasWallKitTotalPoint) {
    /** 획득 가능 총 적립금 조회 상태 */
    val totalPointStatus by nwkTotalPoint.status.collectAsStateWithLifecycle()
    /** 획득 가능 총 적립금 정보 */
    val totalPoint by nwkTotalPoint.data.collectAsStateWithLifecycle()

    item(
        icon = Icons.Default.Savings,
        iconColor = MaterialTheme.colorScheme.secondary,
        title = "획득 가능 총 적립금",
        disabled = totalPointStatus.isLoading(),
        onClick = if (totalPointStatus.isIdleOrFail()) { { nwkTotalPoint.run() } } else null,
        valueContent = {
            NasWallKitStatusUI(
                totalPointStatus,
                loadingIndicatorSize = 20.dp,
                idleContent = {
                    Text("조회", color = MaterialTheme.colorScheme.primary)
                },
                successContent = { Text(totalPoint!!.toString()) },
                failContent = {
                    Text("재시도", color = MaterialTheme.colorScheme.primary)
                }
            )
        }
    )
}

/***************************************************************************************************
 * (PRIVATE) 보유 적립금
 **************************************************************************************************/
@Composable
private fun SectionScope.UserPoint(nwkUserPoint: NasWallKitUserPoint) {
    /** 회원 보유 적립금 조회 상태 */
    val userPointStatus by nwkUserPoint.status.collectAsStateWithLifecycle()
    /** 회원 보유 적립금 정보 */
    val userPoint by nwkUserPoint.data.collectAsStateWithLifecycle()

    item(
        icon = Icons.Default.Savings,
        iconColor = MaterialTheme.colorScheme.tertiary,
        title = "보유 적립금",
        disabled = userPointStatus.isLoading(),
        onClick = if (userPointStatus.isIdleOrFail()) {{ nwkUserPoint.run() }} else null,
        valueContent = {
            NasWallKitStatusUI(
                userPointStatus,
                loadingIndicatorSize = 20.dp,
                idleContent = {
                    Text("조회", color = MaterialTheme.colorScheme.primary)
                },
                successContent = {
                    Text(userPoint!!.toString())
                },
                failContent = {
                    Text("재시도", color = MaterialTheme.colorScheme.primary)
                }
            )
        }
    )
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    // 필요 시 true 로 설정 - Preview 전용 데이터 로드 시 강제로 실패 처리 할지 여부를 설정합니다.
    NasWall.debugPreviewDataForceFail(false)

    AppContainer(isDarkTheme = false) {
        MainScreen()
    }
}