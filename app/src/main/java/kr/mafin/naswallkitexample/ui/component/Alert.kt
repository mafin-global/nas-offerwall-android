package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/***************************************************************************************************
 * 얼럿
 *
 * @param message 메시지
 * @param title 제목
 * @param onDismiss 닫기 핸들러
 **************************************************************************************************/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Alert(message: String, title: String? = null, onDismiss: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp, start = 15.dp, end = 15.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 제목
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    // 제목과 메시지 사이 공백
                    Spacer (modifier = Modifier.height(10.dp))
                }
                // 메시지
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                // 메시지와 확인 버튼 사이 공백
                Spacer (modifier = Modifier.height(24.dp))

                // 확인 버튼
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("확인")
                }
            }
        }
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview
@Composable
private fun AlertPreview() {
    AppContainer {
        Layout("Alert") {
            Alert(
                message = "(-100) 참여할 수 없습니다.",
                title = "참여 실패"
            ) {

            }
        }
    }
}