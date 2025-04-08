package kr.mafin.naswallkitexample.naswallkit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkit.define.NasWallItemList
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 아이템 목록 조회 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitItemList: NasWallKitBase<NasWallItemList>(), Parcelable {
    /***********************************************************************************************
     * 아이템 목록 조회 실행
     *
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        NasWall.itemList { itemList: NasWallItemList?, error: NasWallError? ->
            if (error != null) {
                lle("아이템 목록 조회 실패", error)
                this.fail(error, handler)
            } else {
                lls("아이템 목록 조회 성공")
                this.success(itemList, handler)
            }
        }
    }
}