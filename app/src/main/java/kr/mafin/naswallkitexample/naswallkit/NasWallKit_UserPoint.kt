package kr.mafin.naswallkitexample.naswallkit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkit.define.NasWallPointInfo
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 회원 적립금 조회 - NasWallKit 연동 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitUserPoint: NasWallKitBase<NasWallPointInfo>(), Parcelable {
    /***********************************************************************************************
     * 회원 적립금 조회 실행
     *
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        NasWall.userPoint { pointInfo: NasWallPointInfo?, error: NasWallError? ->
            if (error != null) {
                lle("회원 적립금 조회 실패", error)
                this.fail(error, handler)
            } else {
                lls("회원 적립금 조회 성공 | ${pointInfo!!}")
                this.success(pointInfo, handler)
            }
        }
    }
}