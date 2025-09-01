# 📝 NAS 오퍼월 Android SDK 업데이트
NAS 오퍼월 Android SDK의 업데이트 정보입니다.

## `v2.0.4`
  - 16KB 페이지 크기를 지원합니다.

## `v2.0.3`
  - 최소 지원 SDK (minSdk) 버전이 22에서 21로 변경되었습니다.

## `v2.0.2`
  - 팝업 오퍼월에서 특정 광고 참여 시 동작이 없이 참여가 되지 않는 문제가 수정되었습니다.

## `v2.0.1`
  - 버그가 수정되었습니다.

## `v2.0.0`
  - Major 버전 업데이트로 인해 연동 방식의 대대적인 변경이 있습니다.
  - Kotlin 기반의 코드로 변경되었습니다.
  - Kotlin Compose 기반의 새로운 예제 프로그램이 제공됩니다.
  - 오퍼월 UI가 새롭게 변경되었습니다.
  - 변경된 자세한 연동 방법은 개발 가이드 문서를 참고해주세요.

## `2023년 6월 7일`
- 내부 버그 수정

## `2023년 5월 24일`
- 신규 IP 정책 반영을 위한 wi-fi 상태 체크 기능 추가
- (필수) ACCESS_NETWORK_STATE 권한 추가 필요
- (사용자 정의 UI) -9968, -9969 오류코드 추가

## `2022년 11월 22일`
- Android SDK 33 대응
 
## `2021년 11월 12일`
- 일부폰에서 광고 참여 시 -99992 오류 발생하는 문제 수정

## `2021년 10월 21일`
- IMEI 수집 제거

## `2021년 6월 23일`
- 사용자 휴대폰번호 수집 제거

## `2020년 10월 5일`
- USER_DATA 값 URL Encode 처리 (+ 문자 등이 포함되어 있을 때, 적립 콜백 호출 시 ud 값이 잘못 반환되는 문제 수정)
    
## `2020년 6월 26일`
- 테스트 모드와 관련된 버그 수정
    
## `2019년 10월 14일`
- Android Q(10) 대응 버그 수정
    
## `2019년 9월 19일`
- Android Q(10) 대응
- 초기화 완료 이벤트 추가 (NASWall.setOnInitListener)

## `2018년 12월 5일`
- 인텐트 스키마 도용 취약점이 수정되었습니다.

## 📖 다른 문서
- [내장 UI 개발 가이드](Guide.Embed.md) : 미리 만들어진 UI를 사용하는 방식으로, 별도의 UI 개발 없이 쉽게 연동할 수 있습니다.
- [개발자 정의 UI 개발 가이드](Guide.Custom.md) : 개발자가 UI를 직접 만들어서 연동할 수 있는 방식으로, 개발자 앱의 UI에 맞게 자유롭게 구성할 수 있습니다.

## 🔗 다른 플렛폼 SDK
- [iOS SDK](https://github.com/mafin-global/nas-offerwall-ios)
- [React Native SDK](https://github.com/mafin-global/nas-offerwall-react-native)
- [Unity SDK](https://github.com/mafin-global/nas-offerwall-unity)
