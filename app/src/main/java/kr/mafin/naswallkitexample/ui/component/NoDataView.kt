package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/***************************************************************************************************
 * 정보가 없는 경우, 아이콘 및 텍스트 표시
 *
 * @param text 표시 텍스트
 * @param modifier `Modifier`
 * @param icon 표시 아이콘
 **************************************************************************************************/
@Composable
fun NoDataView(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ManageSearch
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.8F)
            .padding(20.dp)
            .then(modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun NoDataViewPreview() {
    AppContainer {
        Layout(title = "NoDataView", modifier = Modifier.fillMaxSize()) {
            SectionList {
                section {
                    customItem {
                        NoDataView(text = "정보가 없습니다.")
                    }
                }
                section {
                    customItem {
                        NoDataView(text = "검색된 항목이 없습니다.", icon = Icons.Default.SearchOff)
                    }
                }
            }
        }
    }
}