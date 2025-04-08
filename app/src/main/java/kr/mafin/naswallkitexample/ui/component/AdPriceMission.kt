package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState

/***************************************************************************************************
 * 광고 참여 가격 및 미션 텍스트
 *
 * @param adPrice 광고 참여 가격 - `NasWallAdInfo.adPrice`
 * @param missionText 미션 텍스트 - `NasWallAdInfo.missionText`
 * @param modifier `Modifier`
 **************************************************************************************************/
@Composable
fun AdPriceMissionText(adPrice: String, missionText: String, modifier: Modifier = Modifier) {
    /** Preview 여부 */
    val isPreview = LocalAppState.current.isPreview

    /** 광고 참여 가격 표시 여부 (Preview 이거나 무료가 아닐 때만 표시) */
    val shouldShowAdPrice = isPreview || adPrice != "무료"

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
    ) {
        // 광고 참여 가격
        if (shouldShowAdPrice) {
            Text(
                text = adPrice,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 3.dp)
            )
        }
        // 미션 텍스트
        Text(
            text = missionText,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 13.sp
            ),
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun AdPriceMissionTextPreview() {
    AppContainer {
        Layout("AdPriceMission") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                AdIcon(url = "preview")

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    AdTitle(title = "판타지타운(레벨27달성)")
                    AdPriceMissionText(adPrice = "무료", missionText = "레벨 27 달성[최초경험자만/7일이내 달성]")
                }
            }
        }
    }
}