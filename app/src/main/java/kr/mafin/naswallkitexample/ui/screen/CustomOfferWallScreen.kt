package kr.mafin.naswallkitexample.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallAdCategory
import kr.mafin.naswallkit.define.NasWallAdInfo
import kr.mafin.naswallkit.define.NasWallAdList
import kr.mafin.naswallkit.define.NasWallAdListType
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lli
import kr.mafin.naswallkitexample.common.lls
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.naswallkit.NasWallKitInitialize
import kr.mafin.naswallkitexample.naswallkit.NasWallKitAdList
import kr.mafin.naswallkitexample.ui.component.AdIcon
import kr.mafin.naswallkitexample.ui.component.AdPriceMissionText
import kr.mafin.naswallkitexample.ui.component.AdTitle
import kr.mafin.naswallkitexample.ui.component.ErrorRetry
import kr.mafin.naswallkitexample.ui.component.Layout
import kr.mafin.naswallkitexample.ui.component.NoDataView
import kr.mafin.naswallkitexample.ui.component.AppContainer
import kr.mafin.naswallkitexample.ui.component.LoadingView
import kr.mafin.naswallkitexample.ui.dialog.AdDetailDialog

/***************************************************************************************************
 * 광고 목록 구분 목록
 **************************************************************************************************/
private val listTypeList = listOf(NasWallAdListType.BASIC, NasWallAdListType.CPS, NasWallAdListType.CPQ)

/***************************************************************************************************
 * 개발자 정의 UI 화면
 **************************************************************************************************/
@Composable
fun CustomOfferWallScreen() {
    /** Activity */
    val activity = LocalActivity.current

    /** Preview 여부 */
    val isPreview = LocalAppState.current.isPreview

    /** NasWallKit - 아이템 목록 조회 */
    val nwkAdList = rememberSaveable { NasWallKitAdList() }
    /** 아이템 목록 조회 상태 */
    val adListStatus by nwkAdList.status.collectAsStateWithLifecycle()
    /** 아이템 목록 조회 오류 */
    val adListError by nwkAdList.error.collectAsStateWithLifecycle()
    /** 아이템 목록 */
    val adList by nwkAdList.data.collectAsStateWithLifecycle()
    /** 광고 목록 구분 */
    val listType by nwkAdList.listType.collectAsStateWithLifecycle()

    /** 선택한 광고 목록 구분 */
    val (activeListType, setSelectedListType) = remember { mutableStateOf(NasWallAdListType.BASIC) }
    /** 카테고리 목록 */
    val (categoryList, setCategoryList) = remember { mutableStateOf(listOf<Int>()) }
    /** 선택한 카테고리 ID */
    val (activeCategoryId, setSelectedCategoryId) = remember { mutableIntStateOf(0) }
    /** 선택된 카테고리의 광고 목록 */
    val (filteredAdList, setFilteredAdList) = remember { mutableStateOf<NasWallAdList?>(null) }

    /** 광고 상세 화면 표시 여부 */
    val (isShowAdDetail, setIsShowAdDetail) = remember { mutableStateOf(false) }
    /** 광고 상세 화면에 표시할 광고 정보 */
    val (adDetailAdInfo, setAdDetailAdInfo) = remember { mutableStateOf<NasWallAdInfo?>(null) }

    /***********************************************************************************************
     * 카테고리 목록 업데이트
     **********************************************************************************************/
    fun updateCategory(list: NasWallAdList) {
        val newCategoryList = mutableListOf<Int>()
        list.forEach { info ->
            val categoryId = info.category.id
            if (!newCategoryList.contains(categoryId)) {
                newCategoryList.add(categoryId)
            }
        }
        setCategoryList(newCategoryList)
        if (activeCategoryId > 0) {
            setSelectedCategoryId(0)
        }
    }

    /***********************************************************************************************
     * 광고 목록 조회
     **********************************************************************************************/
    fun loadAdList(listType: NasWallAdListType? = null) {
        nwkAdList.run(listType ?: activeListType) { error ->
            if (isPreview && error == null) {
                // Preview 일 때는 필터링 광고 목록, 카테고리 목록 업데이트 바로 적용
                setFilteredAdList(nwkAdList.data.value!!.toList())
                updateCategory(nwkAdList.data.value!!)
            }
        }
    }

    /***********************************************************************************************
     * 광고 상세화면 표시
     **********************************************************************************************/
    fun showAdDetail(info: NasWallAdInfo) {
        setIsShowAdDetail(true)
        setAdDetailAdInfo(info)
    }

    /***********************************************************************************************
     * 문의하기 열기
     **********************************************************************************************/
    fun openCs() {
        activity?.let {
            NasWall.openCs(
                activity = activity,
                handler = { error ->
                    if (error != null) {
                        lle("문의하기 열기 실패", error)
                    } else {
                        lls("문의하기 열기 성공")
                    }
                },
                closeHandler = {
                    lli("문의하기 종료")
                }
            )
        }
    }

    /***********************************************************************************************
     * 광고 목록 구분 변경 시, 광고 목록 다시 조회
     **********************************************************************************************/
    LaunchedEffect(activeListType) {
        loadAdList(activeListType)
    }

    /***********************************************************************************************
     * 광고 목록 변경 시, 카테고리 업데이트
     **********************************************************************************************/
    LaunchedEffect(adList) {
        if (adList != null) {
            if (listType == NasWallAdListType.BASIC) {
                updateCategory(adList!!)
            }
        }
    }

    /***********************************************************************************************
     * 광고 목록, 카테고리 변경 시, 카테고리 별 광고 목록 필터링
     **********************************************************************************************/
    LaunchedEffect(adList, activeCategoryId) {
        if (adList != null) {
            if (listType == NasWallAdListType.BASIC && activeCategoryId > 0) {
                val newFilteredAdList = mutableListOf<NasWallAdInfo>()
                adList!!.forEach { info ->
                    if (info.category.id == activeCategoryId) {
                        newFilteredAdList.add(info)
                    }
                }
                setFilteredAdList(newFilteredAdList)
            } else {
                setFilteredAdList(adList!!.toList())
            }
        }
    }

    /***************************************************************************************************
     * 앱이 백그라운드에서 복귀 시 광고 목록을 다시 불러오기 위한 감시자 등록
     **************************************************************************************************/
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        var isBackground = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (isBackground) {
                        loadAdList()
                    }
                    isBackground = false
                }
                Lifecycle.Event.ON_STOP -> {
                    isBackground = true
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Layout(
        title = "개발자 정의 UI 오퍼월",
        actions = { color ->
            IconButton(onClick = { openCs() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = null,
                    tint = color
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            val lt = NasWallAdListType.BASIC
            val x1 = lt.id
            val a = activeListType.id
            val b = listType?.id

            // 광고 목록 구분
            SingleChoiceSegmentedButtonRow(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)) {
                listTypeList.forEachIndexed { index, listType ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = listTypeList.size
                        ),
                        onClick = {
                            setSelectedListType(listType)
                        },
                        selected = activeListType.id == listType.id,
                        enabled = !adListStatus.isLoading(),
                        label = {
                            Text(
                                text = listType.title,
                                fontSize = 13.sp,
                                lineHeight = 15.sp,
                            )
                        },
                        modifier = Modifier.defaultMinSize(minHeight = 35.dp)
                    )
                }
            }

            // 광고 목록
            Box(modifier = Modifier.fillMaxSize()) {
                if (adListStatus.isFail()) {
                    ErrorRetry(
                        error = adListError,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        loadAdList()
                    }
                } else if (adList != null) {
                    if (filteredAdList != null) {
                        if (filteredAdList.isEmpty()) {
                            NoDataView(
                                text = "참여 가능한 항목이 없습니다.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn {
                                // 카테고리
                                if (listType == NasWallAdListType.BASIC && categoryList.isNotEmpty()) {
                                    item {
                                        AdCategory(
                                            categoryList = categoryList,
                                            activeCategoryId = activeCategoryId,
                                            disabled = adListStatus.isLoading()
                                        ) { categoryId ->
                                            setSelectedCategoryId(categoryId)
                                        }
                                    }
                                }

                                // 광고 목록
                                filteredAdList.forEachIndexed { index, info ->
                                    item {
                                        if (index > 0) {
                                            HorizontalDivider(
                                                color = Color.Gray,
                                                modifier = Modifier
                                                    .padding(start = 75.dp)
                                                    .alpha(0.2F)
                                            )
                                        }
                                        AdItem(info, onClick = { info ->
                                            showAdDetail(info)
                                        })
                                    }
                                }
                            }
                        }
                    }
                }

                if (adListStatus.isIdleOrLoading()) {
                    LoadingView()
                }
            }
        }
    }

    /***********************************************************************************************
     * UI - 광고 상세 화면
     **********************************************************************************************/
    if (isShowAdDetail && adDetailAdInfo != null) {
        AdDetailDialog(adDetailAdInfo) {
            setIsShowAdDetail(false)
        }
    }
}

/***************************************************************************************************
 * (PRIVATE) 광고 카테고리
 **************************************************************************************************/
@Composable
private fun AdCategory(
    categoryList: List<Int>,
    activeCategoryId: Int,
    disabled: Boolean,
    onClick: (categoryId: Int) -> Unit)
{
    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = categoryList.size + 1
                ),
                onClick = {
                    onClick(0)
                },
                selected = activeCategoryId == 0,
                enabled = !disabled,
                label = {
                    Text(
                        text = "전체",
                        fontSize = 13.sp,
                        lineHeight = 15.sp,
                    )
                },
                modifier = Modifier.defaultMinSize(minHeight = 35.dp)
            )
            categoryList.forEachIndexed { index, categoryId ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index + 1,
                        count = categoryList.size + 1
                    ),
                    onClick = {
                        onClick(categoryId)
                    },
                    selected = activeCategoryId == categoryId,
                    enabled = !disabled,
                    label = {
                        Text(
                            text = NasWallAdCategory.fromId(
                                categoryId
                            ).title,
                            fontSize = 13.sp,
                            lineHeight = 15.sp,
                        )
                    },
                    modifier = Modifier.defaultMinSize(minHeight = 35.dp)
                )
            }
        }
    }
}

/***************************************************************************************************
 * (PRIVATE) 광고 아이템
 **************************************************************************************************/
@Composable
private fun AdItem(info: NasWallAdInfo, onClick: (info: NasWallAdInfo) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = Modifier
            .clickable {
                onClick(info)
            }
            .padding(7.dp)
    ) {
        // 아이콘
        AdIcon(url = info.iconUrl)

        // 제목, 참여 가격, 미션
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1F)
        ) {
            // 제목
            AdTitle(title = info.title)

            // 참여 가격, 미션
            AdPriceMissionText(adPrice = info.adPrice, missionText = info.missionText)
        }

        // 보상 적립금
        Text(
            text = info.rewardText(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 4.dp, horizontal = 10.dp)
                .widthIn(min = 50.dp)
        )
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun CustomOfferWallScreenPreview() {
    // 필요 시 true 로 설정 - Preview 전용 데이터 로드 시 강제로 실패 처리 할지 여부를 설정합니다.
    NasWall.debugPreviewDataForceFail(false)

    NasWallKitInitialize().run(LocalContext.current, LocalInspectionMode.current)

    AppContainer {
        CustomOfferWallScreen()
    }
}