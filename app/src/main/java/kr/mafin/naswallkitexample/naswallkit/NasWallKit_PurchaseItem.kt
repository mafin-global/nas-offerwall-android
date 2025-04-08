package kr.mafin.naswallkitexample.naswallkit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkit.define.NasWallPointInfo
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 아이템 구입 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitPurchaseItem: NasWallKitBase<NasWallPointInfo>(), Parcelable {
    /***********************************************************************************************
     * 아이템 구입 실행
     *
     * @param itemId 아이템 ID - `NasWallItemInfo.id`
     * @Param qty 구입 수량
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(itemId: Int, qty: Int = 1, handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        NasWall.purchaseItem(itemId, qty) { pointInfo: NasWallPointInfo?, error: NasWallError? ->
            if (error != null) {
                lle("아이템 구입 실패", error)
                this.fail(error, handler)
            } else {
                lls("아이템 구입 성공 | ${pointInfo!!}")
                this.success(pointInfo, handler)
            }
        }
    }
}