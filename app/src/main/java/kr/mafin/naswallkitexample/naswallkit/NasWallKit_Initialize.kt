package kr.mafin.naswallkitexample.naswallkit

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 초기화 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitInitialize: NasWallKitBase<Any?>(), Parcelable {
    /***********************************************************************************************
     * 초기화 실행
     *
     * @param context `Context`
     * @param isPreview `Jetpack Compose` 이용 시, 미리보기(Preview) 여부를 지정합니다. 일반적으로 `@Composable` 내에서 `LocalInspectionMode.current` 값을 지정하면 됩니다.
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(context: Context, isPreview: Boolean, handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        when (NasWallKitEnv.SERVER_TYPE) {
            // 개발자 서버에서 적립금 관리 시 초기화
            NasWallKitServerType.DEVELOPER -> {
                NasWall.initializeDeveloperServer(
                    context = context,
                    appKey = NasWallKitEnv.APP_KEY,
                    userData = NasWallKitEnv.userData,
                    testMode = NasWallKitEnv.TEST_MODE,
                    isPreview = isPreview
                ) { error: NasWallError? ->
                    if (error != null) {
                        lle("NasWallKit 초기화 실패", error = error)
                        fail(error, handler)
                    } else {
                        lls("NasWallKit 초기화 성공")
                        success(null, handler)
                    }
                }
            }
            // NAS 서버에서 적립금 관리 시 초기화
            NasWallKitServerType.NAS -> {
                NasWall.initializeNasServer(
                    context = context,
                    appKey = NasWallKitEnv.APP_KEY,
                    userId = NasWallKitEnv.userId,
                    testMode = NasWallKitEnv.TEST_MODE,
                    isPreview = isPreview
                ) { error: NasWallError? ->
                    if (error != null) {
                        lle("NasWallKit 초기화 실패", error = error)
                        fail(error, handler)
                    } else {
                        lls("NasWallKit 초기화 성공")
                        success(null, handler)
                    }
                }
            }
        }
    }
}