package kr.mafin.naswallkitexample.naswallkit

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kr.mafin.naswallkit.NasWall
import kr.mafin.naswallkit.define.NasWallAdList
import kr.mafin.naswallkit.define.NasWallAdListType
import kr.mafin.naswallkit.define.NasWallError
import kr.mafin.naswallkitexample.common.lle
import kr.mafin.naswallkitexample.common.lls

/***************************************************************************************************
 * 광고 목록 조회 - NasWallKit 연동
 **************************************************************************************************/
@Parcelize
class NasWallKitAdList: NasWallKitBase<NasWallAdList>(), Parcelable {
    /** 마지막 조회 성공한 광고 목록 구분 (내부 수정용) */
    @IgnoredOnParcel
    private var listTypeValue = MutableStateFlow<NasWallAdListType?>(null)

    /** 마지막 조회 성공한 광고 목록 구분 */
    @IgnoredOnParcel
    val listType: StateFlow<NasWallAdListType?> = listTypeValue

    /***********************************************************************************************
     * 광고 목록 조회 실행
     *
     * @param listType 광고 목록 조회 구분
     * @param handler 처리 완료 핸들러
     **********************************************************************************************/
    fun run(listType: NasWallAdListType, handler: ((error: NasWallError?) -> Unit)? = null) {
        this.loading()

        NasWall.adList(listType) { adList: NasWallAdList?, error: NasWallError? ->
            if (error != null) {
                lle("광고 목록 조회 실패", error)
                this.fail(error, handler)
            } else {
                lls("광고 목록 조회 성공")
                listTypeValue.value = listType
                this.success(adList, handler)
            }
        }
    }
}