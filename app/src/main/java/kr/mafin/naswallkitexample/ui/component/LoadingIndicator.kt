package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/***************************************************************************************************
 * 원형 로딩 인디케이터
 *
 * @param modifier `Modifier`
 * @param size 크기
 **************************************************************************************************/
@Composable
fun BoxScope.LoadingIndicator(modifier: Modifier = Modifier, size: Dp? = null) {
    var finalModifier = modifier.align(Alignment.Center)
    if (size != null) {
        finalModifier = finalModifier.size(size)
    }
    CircularProgressIndicator(
        modifier = finalModifier,
        color = MaterialTheme.colorScheme.primary
    )
}

/***************************************************************************************************
 * 원형 로딩 인디케이터
 *
 * @param modifier `Modifier`
 * @param size 크기
 **************************************************************************************************/
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier, size: Dp? = null) {
    CircularProgressIndicator(
        modifier = if (size != null) modifier.size(size) else modifier,
        color = MaterialTheme.colorScheme.primary
    )
}