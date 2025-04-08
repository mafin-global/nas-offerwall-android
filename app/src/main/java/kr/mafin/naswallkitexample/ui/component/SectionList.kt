package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState
import kr.mafin.naswallkitexample.ui.composition_local.LocalIsDarkTheme

/***************************************************************************************************
 * LazyColumn 기반의 섹션을 구성할 수 있는 List
 *
 * @param modifier `Modifier`
 * @param iconStyle 아이콘 스타일
 **************************************************************************************************/
@Composable
fun SectionList(
    modifier: Modifier = Modifier,
    iconStyle: SectionListIconStyle? = null,
    divider: Boolean = false,
    content: SectionListScope.(textColor: Color) -> Unit
) {
    /** 다크 테마 여부 */
    val isDarkTheme by LocalIsDarkTheme.current
    /** 배경 색상 */
    val backgroundColor = LocalAppState.current.backgroundColor
    /** 섹션 배경 색상 */
    val sectionBackgroundColor =
        if (isDarkTheme) MaterialTheme.colorScheme.surfaceContainer
        else MaterialTheme.colorScheme.background
    /** 글자 색상 */
    val textColor =
        if (isDarkTheme) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onBackground

    // section 목록 조회
    val sectionListScope = SectionListScopeImpl()
    sectionListScope.content(textColor)
    val sectionList = sectionListScope.getItems()

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    LazyColumn(
        modifier = Modifier
            .background(backgroundColor)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        // Section 표시
        sectionList.forEachIndexed { index, sectionInfo ->
            item(key = "${index}_${sectionInfo.updater.intValue}") {
                // Section 제목
                if (sectionInfo.title != null) {
                    Text(
                        text = sectionInfo.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
                    )
                }

                // Section 의 Item 목록 조회
                val itemContent = sectionInfo.content
                val sectionScope = SectionScopeImpl()
                sectionScope.itemContent()
                val items = sectionScope.getItems()

                // Item 생성 후 Item 이 업데이트 되면, Section UI를 업데이트 시켜주기 위해 updater 설정
                sectionScope.updater = sectionInfo.updater

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                        .background(
                            color = sectionBackgroundColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .then(sectionInfo.modifier)
                ) {
                    // Section 의 Item 표시
                    val filteredItems = items.filter { sectionItemInfo -> !sectionItemInfo.hidden }
                    filteredItems.forEachIndexed { sectionItemIndex, sectionItemInfo ->
                        /** Section 의 Modifier */
                        var sectionItemModifier = Modifier.fillMaxWidth()
                        if (!sectionItemInfo.disabled && sectionItemInfo.onClick != null) {
                            sectionItemModifier = sectionItemModifier.clickable(onClick = sectionItemInfo.onClick)
                        }
                        sectionItemModifier = sectionItemModifier.padding(16.dp)
                        if (sectionItemInfo.disabled) {
                            sectionItemModifier = sectionItemModifier.alpha(0.5F)
                        }

                        /** 최종 아이콘 스타일 */
                        val finalIconStyle = sectionItemInfo.iconStyle ?: sectionInfo.iconStyle ?: iconStyle ?: SectionListIconStyle.NORMAL
                        /** 최종 아이콘 색상 */
                        val finalIconColor = sectionItemInfo.iconColor ?: textColor
                        /** 최종 제목 색상 */
                        val finalTitleColor = sectionItemInfo.titleColor ?: textColor

                        if (sectionItemInfo.isCustom) {
                            // 커스텀 Item
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .then(sectionItemInfo.modifier)
                            ) {
                                sectionItemInfo.content?.invoke(this)
                            }
                        } else {
                            // 기본 Item
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = sectionItemModifier.then(sectionItemInfo.modifier),
                            ) {
                                // Item 아이콘
                                if (sectionItemInfo.icon != null) {
                                    when (finalIconStyle) {
                                        SectionListIconStyle.NORMAL -> Icon(
                                            imageVector = sectionItemInfo.icon,
                                            contentDescription = null,
                                            tint = finalIconColor
                                        )

                                        SectionListIconStyle.RECTANGLE -> Box(
                                            modifier = Modifier
                                                .background(
                                                    color = finalIconColor,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .padding(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = sectionItemInfo.icon,
                                                contentDescription = null,
                                                tint = sectionBackgroundColor,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }

                                // Item 제목
                                if (sectionItemInfo.title != null) {
                                    Text(
                                        text = sectionItemInfo.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = finalTitleColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else if (sectionItemInfo.titleContent != null) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        sectionItemInfo.titleContent.invoke(this)
                                    }
                                }

                                // Item 값
                                if (sectionItemInfo.value != null) {
                                    Text(
                                        text = sectionItemInfo.value,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = finalTitleColor,
                                    )
                                } else if (sectionItemInfo.valueContent != null) {
                                    Column {
                                        sectionItemInfo.valueContent.invoke(this)
                                    }
                                }
                            }
                        }

                        // Item 구분선
                        if ((divider || sectionInfo.divider) && sectionItemIndex < filteredItems.size - 1) {
                            var paddingStart = 0.dp
                            if (sectionItemInfo.icon != null) {
                                paddingStart = when (finalIconStyle) {
                                    SectionListIconStyle.NORMAL -> 50.dp
                                    SectionListIconStyle.RECTANGLE -> 55.dp
                                }
                            }
                            HorizontalDivider(
                                color = backgroundColor,
                                thickness = 1.dp,
                                modifier = Modifier.padding(start = paddingStart)
                            )
                        }
                    }
                }
            }
        }
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun SectionListPreview() {
    AppContainer {
        Layout(title = "SectionList", modifier = Modifier.fillMaxSize()) {
            SectionList(divider = true) {
                section {
                    item(title = "Title")
                    item(titleContent = {
                        Column {
                            Text(text = "Custom Title")
                            Text(
                                text = "Custom Title",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    })
                }
                section(title = "Icon") {
                    item(
                        icon = Icons.Default.Settings,
                        iconColor = MaterialTheme.colorScheme.tertiary,
                        title = "Title"
                    )
                    item(
                        icon = Icons.Default.Settings,
                        iconColor = MaterialTheme.colorScheme.primary,
                        iconStyle = SectionListIconStyle.RECTANGLE,
                        title = "Title"
                    )
                }
                section(title = "Value") {
                    item(
                        title = "Title",
                        value = "Value"
                    )
                    item(
                        title = "Title",
                        valueContent = {
                            Text(
                                text = "Custom Value",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
                section(title = "Variant") {
                    item(
                        icon = Icons.Default.Settings,
                        iconColor = MaterialTheme.colorScheme.primary,
                        iconStyle = SectionListIconStyle.RECTANGLE,
                        title = "Title",
                        value = "Value"
                    )
                    item(
                        icon = Icons.Default.Settings,
                        iconColor = MaterialTheme.colorScheme.primary,
                        iconStyle = SectionListIconStyle.RECTANGLE,
                        titleContent = {
                            Text(
                                text = "Custom Title",
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Custom Title",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Custom Title",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        valueContent = {
                            Text(
                                text = "Custom Value",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Custom Value",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Custom Value",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }
        }
    }
}

/***************************************************************************************************
 * (PRIVATE) SectionInfo
 **************************************************************************************************/
private class SectionInfo(
    val modifier: Modifier = Modifier,
    val title: String? = null,
    val iconStyle: SectionListIconStyle? = null,
    val divider: Boolean = false,
    val updater: MutableIntState,
    val content: @Composable SectionScope.() -> Unit
)

/***************************************************************************************************
 * (PRIVATE) SectionItemInfo
 **************************************************************************************************/
private class SectionItemInfo(
    val modifier: Modifier = Modifier,
    val isCustom: Boolean = false,
    val icon: ImageVector? = null,
    val iconColor: Color? = null,
    val iconStyle: SectionListIconStyle? = null,
    val title: String? = null,
    val titleContent: (@Composable ColumnScope.() -> Unit)? = null,
    val titleColor: Color? = null,
    val value: String? = null,
    val valueContent: (@Composable ColumnScope.() -> Unit)? = null,
    val content: (@Composable ColumnScope.() -> Unit)? = null,
    val disabled: Boolean = false,
    val hidden: Boolean = false,
    val onClick: (() -> Unit)? = null,
)


/***************************************************************************************************
 * SectionList 아이콘 스타일
 **************************************************************************************************/
enum class SectionListIconStyle {
    /** 기본 */
    NORMAL,
    /** 사각형 */
    RECTANGLE
}

/***************************************************************************************************
 * SectionListScope
 **************************************************************************************************/
interface SectionListScope {
    fun section(
        title: String? = null,
        iconStyle: SectionListIconStyle? = null,
        divider: Boolean = false,
        content: @Composable SectionScope.() -> Unit
    ) {
        error("The method is not implemented")
    }
}

/***************************************************************************************************
 * (PRIVATE) SectionListScopeImpl
 **************************************************************************************************/
private class SectionListScopeImpl : SectionListScope {
    private val items = mutableListOf<SectionInfo>()

    override fun section(
        title: String?,
        iconStyle: SectionListIconStyle?,
        divider: Boolean,
        content: @Composable SectionScope.() -> Unit
    ) {
        val updater: MutableIntState = mutableIntStateOf(0)

        items.add(SectionInfo(
            title = title,
            iconStyle = iconStyle,
            divider = divider,
            updater = updater,
            content = content
        ))
    }

    fun getItems(): List<SectionInfo> = items
}

/***************************************************************************************************
 * SectionScope
 **************************************************************************************************/
interface SectionScope {
    fun item(
        modifier: Modifier = Modifier,
        icon: ImageVector? = null,
        iconColor: Color? = null,
        iconStyle: SectionListIconStyle? = null,
        title: String? = null,
        titleContent: (@Composable ColumnScope.() -> Unit)? = null,
        titleColor: Color? = null,
        value: String? = null,
        valueContent: (@Composable ColumnScope.() -> Unit)? = null,
        disabled: Boolean = false,
        hidden: Boolean = false,
        onClick: (() -> Unit)? = null
    ) {
        error("The method is not implemented")
    }

    fun customItem(
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        content: (@Composable ColumnScope.() -> Unit)
    ) {
        error("The method is not implemented")
    }
}

/***************************************************************************************************
 * (PRIVATE) SectionScopeImpl
 **************************************************************************************************/
private class SectionScopeImpl(): SectionScope {
    private val items = mutableListOf<SectionItemInfo>()
    // item 의 속성 변경 시 UI를 업데이트 시켜주기 위한 변수
    var updater: MutableState<Int>? = null

    override fun item(
        modifier: Modifier,
        icon: ImageVector?,
        iconColor: Color?,
        iconStyle: SectionListIconStyle?,
        title: String?,
        titleContent: (@Composable ColumnScope.() -> Unit)?,
        titleColor: Color?,
        value: String?,
        valueContent: (@Composable ColumnScope.() -> Unit)?,
        disabled: Boolean,
        hidden: Boolean,
        onClick: (() -> Unit)?
    ) {
        if (updater != null) {
            updater!!.value += 1
        }

        items.add(SectionItemInfo(
            isCustom = false,
            modifier = modifier,
            icon = icon,
            iconColor = iconColor,
            iconStyle = iconStyle,
            title = title,
            titleContent = titleContent,
            titleColor = titleColor,
            value = value,
            valueContent = valueContent,
            disabled = disabled,
            hidden = hidden,
            onClick = onClick
        ))
    }

    override fun customItem(
        modifier: Modifier,
        onClick: (() -> Unit)?,
        content: @Composable ColumnScope.() -> Unit
    ) {
        if (updater != null) {
            updater!!.value += 1
        }

        items.add(SectionItemInfo(
            isCustom = true,
            modifier = modifier,
            content = content,
            onClick = onClick
        ))
    }

    fun getItems(): List<SectionItemInfo> = items
}