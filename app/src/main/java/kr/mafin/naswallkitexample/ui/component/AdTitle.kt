package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/***************************************************************************************************
 * 광고명
 *
 * @param title 광고명 - `NasWallAdInfo.title`
 * @param modifier `Modifier`
 **************************************************************************************************/
@Composable
fun AdTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "광고 제목" }
    )
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun AdTitlePreview() {
    AppContainer {
        Layout("AdTitle") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                AdIcon(url = "preview")

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    AdTitle(title = "판타지타운(레벨27달성) TikTok Lite 쿠팡 친구 카카오톡 채널 추가 Save your Pet (이틀 연속 출석체크 완료)")
                    AdPriceMissionText(adPrice = "무료", missionText = "메인화면 도달하기")
                }
            }
        }
    }
}