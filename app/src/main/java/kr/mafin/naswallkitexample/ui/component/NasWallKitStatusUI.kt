package kr.mafin.naswallkitexample.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kr.mafin.naswallkitexample.naswallkit.NasWallKitStatus

/***************************************************************************************************
 * NasWallKit 연동 상태에 따른 UI 표시
 *
 * `loadingContent`를 지정하지 않으면,
 *  기본적으로 `NasWallKitStatus.LOADING` 상태에서 `CircularProgressIndicator`를 표시합니다.
 *
 * @param status 연동 상태
 * @param modifier `Modifier`
 * @param idleContent `NasWallKitStatus.IDLE` 상태에서 표시할 컨텐츠
 * @param loadingIndicatorSize `CircularProgressIndicator` 의 크기 지정
 * @param loadingContent `NasWallKitStatus.LOADING` 상태에서 표시할 컨텐츠
 * @param failContent `NasWallKitStatus.FAIL` 상태에서 표시할 컨텐츠
 * @param successContent `NasWallKitStatus.SUCCESS` 상태에서 표시할 컨텐츠
 **************************************************************************************************/
@Composable
fun NasWallKitStatusUI(
    status: NasWallKitStatus,
    modifier: Modifier = Modifier,
    idleContent: (@Composable BoxScope.() -> Unit)? = null,
    loadingIndicatorSize: Dp? = null,
    loadingContent: (@Composable BoxScope.() -> Unit)? = null,
    failContent: @Composable (BoxScope.() -> Unit)? = null,
    successContent: @Composable (BoxScope.() -> Unit)? = null,
) {
    Box (modifier = modifier) {
        when (status) {
            NasWallKitStatus.IDLE -> {
                idleContent?.invoke(this)
            }

            NasWallKitStatus.LOADING -> {
                if (loadingContent != null) {
                    loadingContent()
                } else {
                    // 기본 Progress
                    LoadingIndicator(size = loadingIndicatorSize)
                }
            }

            NasWallKitStatus.SUCCESS -> {
                successContent?.invoke(this)
            }

            NasWallKitStatus.FAIL -> {
                failContent?.invoke(this)
            }
        }
    }
}