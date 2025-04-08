package kr.mafin.naswallkitexample.naswallkit

import android.app.Activity
import android.os.Parcelable
import android.view.ViewGroup
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 임베드 오퍼월 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitEmbedOfferWall: NasWallKitBase<Any?>(), Parcelable {
    /***********************************************************************************************
     * 임베드 오퍼월 실행
     *
     * @param activity `Activity`
     * @param parent 임베드 할 부모 `ViewGroup`
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(activity: Activity, parent: ViewGroup, handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        NasWall.embedOfferWall(
            activity = activity,
            parent = parent
        ) { error: NasWallError? ->
            if (error != null) {
                lle("임베드 오퍼월 실패", error)
                this.fail(error, handler)
            } else {
                lls("임베드 오퍼월 성공")
                this.success(null, handler)
            }
        }
    }
}