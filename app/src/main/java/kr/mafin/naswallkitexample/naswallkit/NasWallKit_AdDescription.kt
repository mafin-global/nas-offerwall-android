package kr.mafin.naswallkitexample.naswallkit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallAdInfo
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 광고 상세 설명 조회 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitAdDescription: NasWallKitBase<String>(), Parcelable {
    /***********************************************************************************************
     * 광고 상세 설명 조회 실행
     *
     * @param adInfo 광고 정보
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(adInfo: NasWallAdInfo, handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        NasWall.adDescription(adInfo = adInfo) { description: String?, error: NasWallError? ->
            if (error != null) {
                lle("광고 상세 설명 조회 실패", error)
                this.fail(error, handler)
            } else {
                lls("광고 상세 설명 조회 성공")
                this.success(description, handler)
            }
        }
    }
}