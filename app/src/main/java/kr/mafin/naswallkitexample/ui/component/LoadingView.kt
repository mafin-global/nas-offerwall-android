package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/***************************************************************************************************
 * 부모 View 전체를 덮는 Loading Indicator 표시
 **************************************************************************************************/
@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // 클릭 차단
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {}
    ) {
        LoadingIndicator()
    }
}

/***************************************************************************************************
 * Preview
 **************************************************************************************************/
@Preview(showBackground = true)
@Composable
private fun LoadingViewPreview() {
    AppContainer {
        Layout("LoadingView") {
            LoadingView()
        }
    }
}