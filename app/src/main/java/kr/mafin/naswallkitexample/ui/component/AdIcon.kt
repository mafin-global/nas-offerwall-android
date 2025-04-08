package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import kr.mafin.naswallkitexample.R
import kr.mafin.naswallkitexample.ui.composition_local.LocalAppState

/***************************************************************************************************
 * 광고 아이콘
 *
 * @param url 광고 아이콘 URL
 * @param modifier `Modifier`
 * @param size 크기(DP)
 **************************************************************************************************/
@Composable
fun AdIcon(url: String, modifier: Modifier = Modifier, size: Int = 60) {
    /** Preview 여부 */
    val isPreview = LocalAppState.current.isPreview
    /** 이미지 Modifier */
    val imageModifier = modifier
        .size(size.dp)
        .clip(RoundedCornerShape((size / 4).dp))

    /***********************************************************************************************
     * UI
     **********************************************************************************************/
    Image(
        painter = if (isPreview) {
            painterResource(id = R.drawable.ic_ad_default)
        } else {
            rememberAsyncImagePainter(url)
        },
        contentDescription = null,
        modifier = imageModifier
    )
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview
@Composable
private fun AdIconPreview() {
    AppContainer {
        Layout("AdIcon") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(5.dp)
            ) {
                AdIcon(url = "preview")
                AdIcon(url = "preview", size = 120)
                AdIcon(url = "preview", size = 180)
            }
        }
    }
}