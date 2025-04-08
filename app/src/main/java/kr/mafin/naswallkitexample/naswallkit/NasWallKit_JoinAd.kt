package kr.mafin.naswallkitexample.naswallkit

import android.app.Activity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallAdInfo
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lli
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 광고 참여 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitJoinAd: NasWallKitBase<Any?>(), Parcelable {
    /***********************************************************************************************
     * 광고 참여 실행
     *
     * @param activity `Activity`
     * @param adInfo 광고 정보
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(activity: Activity, adInfo: NasWallAdInfo, handler: ((error: NasWallError?, isCancel: Boolean) -> Unit)? = null) {
        this.loading()

        NasWall.joinAd(activity, adInfo) { error: NasWallError? ->
            if (error != null) {
                if (error.isCancel()) {
                    lli("광고 참여 취소")
                    this.fail(error) {
                        handler?.invoke(error, true)
                    }
                } else {
                    lle("광고 참여 실패", error)
                    this.fail(error) {
                        handler?.invoke(error, false)
                    }
                }
            } else {
                lls("광고 참여 성공")
                this.success(null) {
                    handler?.invoke(null, false)
                }
            }
        }
    }
}