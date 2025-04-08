package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.mafin.naswallkit.define.NasWallError

/***************************************************************************************************
 * 오류 메시지와 재시도 버튼
 *
 * `error`를 제공하면, 오류 코드가 표시되고, 오류 코드를 클릭하면, 숨겨진 오류 메시지가 표시됩니다.
 *
 * @param modifier `Modifier`
 * @param msg 오류 메시지
 * @param code 오류 코드
 * @param error `NasWallKit` 오류
 * @param showSecretMsg 숨겨진 오류 메시지 강제 표시 여부
 * @param onRetry 재시도 버튼 클릭 핸들러
 **************************************************************************************************/
@Composable
fun ErrorRetry(
    modifier: Modifier = Modifier,
    msg: String = "예상치 못한 문제가 발생했습니다.",
    code: Int? = null,
    error: NasWallError? = null,
    showSecretMsg: Boolean = false,
    onRetry: (() -> Unit)? = null
) {
    val errorCode = code ?: error?.code
    val secretMsg = error?.message
    val (isShowSecretMsg, setIsShowSecretMsg) = rememberSaveable { mutableStateOf(showSecretMsg) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Report,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(40.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (errorCode != null || (isShowSecretMsg && secretMsg != null)) {
                // 오류 코드, 숨겨진 오류 메시지
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    // 오류 코드
                    Text(
                        text = errorCode!!.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable(enabled = !isShowSecretMsg && secretMsg != null) {
                                setIsShowSecretMsg(true)
                            }
                    )

                    // 숨겨진 오류 메시지
                    if (isShowSecretMsg && secretMsg != null) {
                        Text(
                            secretMsg,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            // 일반 오류 메시지
            Text(
                text = if (onRetry == null) msg else "$msg\n잠시 후 재시도 해주세요.",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center)
        }

        // 재시도 버튼
        if (onRetry != null) {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults
                    .buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("재시도")
            }
        }
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview
@Composable
private fun ErrorRetryPreview() {
    AppContainer {
        Layout("ErrorRetry") {
            SectionList {
                section {
                    customItem(modifier = Modifier.padding(20.dp)) {
                        ErrorRetry(
                            msg = "정보를 불러올 수 없습니다.",
                        )
                    }
                }
                section {
                    customItem(modifier = Modifier.padding(20.dp)) {
                        ErrorRetry(
                            msg = "정보를 불러올 수 없습니다.",
                            code = -100,
                            onRetry = {}
                        )
                    }
                }
                section {
                    customItem(modifier = Modifier.padding(20.dp)) {
                        ErrorRetry(
                            msg = "정보를 불러올 수 없습니다.",
                            error = NasWallError(100, "서버와 통신 중 오류가 발생했습니다."),
                            showSecretMsg = true,
                            onRetry = {}
                        )
                    }
                }
            }
        }
    }
}