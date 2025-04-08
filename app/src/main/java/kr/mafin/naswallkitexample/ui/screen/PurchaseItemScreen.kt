package kr.mafin.naswallkitexample.ui.screen

import android.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallItemInfo
import kr.mafin.naswallkitexample.naswallkit.NasWallKitInitialize
import kr.mafin.naswallkitexample.naswallkit.NasWallKitItemList
import kr.mafin.naswallkitexample.naswallkit.NasWallKitPurchaseItem
import kr.mafin.naswallkitexample.naswallkit.NasWallKitUserPoint
import kr.mafin.naswallkitexample.ui.component.ErrorRetry
import kr.mafin.naswallkitexample.ui.component.Layout
import kr.mafin.naswallkitexample.ui.component.LoadingView
import kr.mafin.naswallkitexample.ui.component.SectionList
import kr.mafin.naswallkitexample.ui.component.NasWallKitStatusUI
import kr.mafin.naswallkitexample.ui.component.NoDataView
import kr.mafin.naswallkitexample.ui.component.AppContainer
import kr.mafin.naswallkitexample.ui.component.SectionListIconStyle

@Composable
fun PurchaseItemScreen() {
    /** Context */
    val context = LocalContext.current

    /** NasWallKit - 회원 보유 적립금 조회 */
    val nwkUserPoint = rememberSaveable { NasWallKitUserPoint() }
    /** 회원 보유 적립금 조회 상태 */
    val userPointStatus by nwkUserPoint.status.collectAsStateWithLifecycle()
    /** 회원 보유 적립금 정보 */
    val userPoint by nwkUserPoint.data.collectAsStateWithLifecycle()

    /** NasWallKit - 아이템 목록 조회 */
    val nwkItemList = rememberSaveable { NasWallKitItemList() }
    /** 아이템 목록 조회 상태 */
    val itemListStatus by nwkItemList.status.collectAsStateWithLifecycle()
    /** 아이템 목록 조회 오류 */
    val itemListError by nwkItemList.error.collectAsStateWithLifecycle()
    /** 아이템 목록 */
    val itemList by nwkItemList.data.collectAsStateWithLifecycle()

    /** NasWallKit - 아이템 구입 */
    val nwkPurchaseItem = rememberSaveable { NasWallKitPurchaseItem() }
    /** 아이템 구입 상태 */
    val purchaseItemStatus by nwkPurchaseItem.status.collectAsStateWithLifecycle()

    /** 선택한 아이템 */
    val (selectedItem, setSelectedItem) = remember { mutableStateOf(null as NasWallItemInfo?) }

    /***********************************************************************************************
     * 아이템 목록 조회
     **********************************************************************************************/
    fun loadItemList() {
        nwkItemList.run { error ->
            val list = nwkItemList.data.value
            if (error == null && !list.isNullOrEmpty()) {
                // 첫 번째 아이템 기본 선택
                setSelectedItem(list.first())
            }
        }
    }

    /***********************************************************************************************
     * 아이템 구입
     **********************************************************************************************/
    fun purchaseItem(itemId: Int, qty: Int = 1) {
        nwkPurchaseItem.run(itemId, qty) { error ->
            if (error == null) {
                // 구입 성공 시 보유 적립금 다시 조회
                nwkUserPoint.run()
            } else {
                AlertDialog.Builder(context)
                    .setTitle("구입 실패")
                    .setMessage("(${error.code}) ${error.message}")
                    .setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    /***********************************************************************************************
     * 화면 로드 시, 보유 적립금, 아이템 목록 조회
     **********************************************************************************************/
    LaunchedEffect(Unit) {
        nwkUserPoint.run()
        loadItemList()
    }
    
    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Layout("아이템 구입", disabled = purchaseItemStatus.isLoading(), modifier = Modifier.fillMaxSize()) {
        NasWallKitStatusUI(
            status = itemListStatus,
            modifier = Modifier.fillMaxSize(),
            // 아이템 목록 조회 실패
            failContent = {
                ErrorRetry(error = itemListError, modifier = Modifier.align(Alignment.Center)) {
                    nwkItemList.run()
                }
            },
            // 아이템 목록 조회 성공
            successContent = {
                if (itemList != null) {
                    if (itemList!!.isEmpty()) {
                        // 아이템이 없는 경우
                        NoDataView(
                            text = "구입 가능한 아이템이 없습니다.\n\nNAS 개발자 홈페이지의 \"매체관리\" 메뉴에서 아이템을 등록해주세요.",
                            modifier = Modifier.align(Alignment.Center))
                    } else {
                        // 아이템이 있는 경우
                        Column(modifier = Modifier.fillMaxSize()) {
                            SectionList(divider = true, modifier = Modifier.weight(1F)) {
                                // 보유 적립금
                                section {
                                    item(
                                        icon = Icons.Default.Savings,
                                        iconColor = MaterialTheme.colorScheme.tertiary,
                                        iconStyle = SectionListIconStyle.RECTANGLE,
                                        title = "보유 적립금",
                                        disabled = userPointStatus.isLoading() || purchaseItemStatus.isLoading(),
                                        onClick = if (userPointStatus.isIdleOrFail()) {
                                            { loadItemList() }
                                        } else null,
                                        valueContent = {
                                            NasWallKitStatusUI(
                                                userPointStatus,
                                                loadingIndicatorSize = 20.dp,
                                                successContent = {
                                                    Text(userPoint!!.toString())
                                                },
                                                failContent = {
                                                    Text(
                                                        "재시도",
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }

                                // 아이템 목록
                                section {
                                    itemList!!.forEach { item ->
                                        item(
                                            icon = if (selectedItem?.id == item.id) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                            title = item.name,
                                            value = "${item.price}${item.unit}",
                                            disabled = purchaseItemStatus.isLoading(),
                                            onClick = {
                                                setSelectedItem(item)
                                            }
                                        )
                                    }
                                }
                            }

                            // 구입하기 버튼
                            Button(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .fillMaxWidth(),
                                enabled = selectedItem != null && !purchaseItemStatus.isLoading(),
                                onClick = {
                                    if (selectedItem != null) {
                                        purchaseItem(selectedItem.id)
                                    }
                                }
                            ) {
                                Text("구입하기")
                            }
                        }
                    }
                }
            },
        )

        // 구입 진행 중일 때, 로딩 화면 표시
        if (purchaseItemStatus.isLoading()) {
            LoadingView()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PurchaseItemScreenPreview() {
    // 필요 시 true 로 설정 - Preview 전용 데이터 로드 시 강제로 실패 처리 할지 여부를 설정합니다.
    NasWall.debugPreviewDataForceFail(false)

    NasWallKitInitialize().run(LocalContext.current, LocalInspectionMode.current)

    AppContainer {
        PurchaseItemScreen()
    }
}