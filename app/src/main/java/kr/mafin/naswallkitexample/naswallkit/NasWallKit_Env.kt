package kr.mafin.naswallkitexample.naswallkit

/***************************************************************************************************
 * 적립금 관리 서버 구분
 **************************************************************************************************/
enum class NasWallKitServerType(val title: String) {
    /** 개발자 서버에서 적립금 관리 */
    DEVELOPER("개발자 서버"),
    /** NAS 서버에서 적립금 관리 */
    NAS("NAS 서버")
}

/***************************************************************************************************
 * NasWallKit 연동을 위한 환경 정보
 **************************************************************************************************/
@Deprecated("🔥 NasWallKit 연동을 위한 설정 정보를 입력해 주세요. 정보 입력 후 현재 줄을 삭제해 주세요. 🔥", level = DeprecationLevel.ERROR)
object NasWallKitEnv {
    /***********************************************************************************************
     * 테스트 모드 여부
     *
     * `true`로 설정 시 테스트 광고가 표시됩니다.
     **********************************************************************************************/
    const val TEST_MODE: Boolean = false

    /***********************************************************************************************
     * 앱 KEY
     *
     * NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 확인 가능합니다.
     **********************************************************************************************/
    const val APP_KEY: String = "32자리 앱 KEY"

    /***********************************************************************************************
     * 적립금 관리 서버
     *
     * "적립금 관리 서버" 설정은 NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 설정을 통해 확인 및 변경할 수 있습니다.
     **********************************************************************************************/
    val SERVER_TYPE: NasWallKitServerType = NasWallKitServerType.DEVELOPER

    /***********************************************************************************************
     * 회원 데이터
     *
     * "개발자 서버에서 적립금 관리" 시 사용됩니다.
     *
     * 회원 ID 등의 적립금 지급에 필요한 고유한 회원 정보를 지정합니다.
     * 광고 참여 완료 시 개발자 서버로 콜백 호출될 때 함께 제공됩니다.
     *
     * **❗️회원 로그인 시 값을 지정해주세요.**
     **********************************************************************************************/
    var userData: String = "YOUR_USER_DATA"

    /***********************************************************************************************
     * 회원 ID
     *
     * "NAS 서버에서 적립금 관리" 시 사용됩니다.
     *
     * 회원 ID 등의 고유한 회원 정보를 지정합니다.
     *
     * **❗️회원 로그인 시 값을 지정해주세요.**
     **********************************************************************************************/
    var userId: String = "YOUR_USER_ID"
}