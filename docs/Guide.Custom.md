# 📖 NAS 오퍼월 Android SDK - 개발자 정의 UI 개발 가이드
이 문서는 NAS 오퍼월 SDK 개발자 정의 UI 연동 가이드를 제공합니다.

오퍼월 UI 표시 방식은 다음 두가지를 지원합니다.

- `내장 UI` : 미리 만들어진 UI를 사용하는 방식으로, 별도의 UI 개발 없이 쉽게 연동할 수 있습니다.
- `개발자 정의 UI` : 개발자가 UI를 직접 만들어서 연동할 수 있는 방식으로, 개발자 앱의 UI에 맞게 자유롭게 구성할 수 있습니다.

`내장 UI`를 사용 하려면, [📖 내장 UI 개발 가이드](Guide.Embed.md) 문서를 참고해 주시기 랍니다.

## 목차
- [📝⠀업데이트](#-업데이트)
- [👤 개발자/매체 등록](#-개발자매체-등록)
- [💾 설치하기](#-설치하기)
  - [NasWallKit 추가](#-NasWallKit-추가)
  - [AndroidManifest.xml 설정](#-androidmanifestxml-설정)
- [🚀 초기화](#-초기화)
  - [개발자 서버에서 적립금 관리 시](#-개발자-서버에서-적립금-관리-시)
  - [NAS 서버에서 적립금 관리 시](#-NAS-서버에서-적립금-관리-시)
- [📱 광고 표시](#-광고-표시)
  - [광고 목록 조회](#-광고-목록-조회)
  - [광고 상세 설명 조회](#-광고-상세-설명-조회)
  - [광고 참여](#-광고-참여)
  - [문의하기](#-문의하기)
- [💰 적립금](#-적립금)
  - [획득 가능 총 적립금 조회](#-획득-가능-총-적립금-조회)
  - [보유 적립금 조회](#-보유-적립금-조회-NAS-서버에서-적립금-관리-시-사용) *(NAS 서버에서 적립금 관리 시 사용)*
  - [아이템 목록 조회](#-아이템-목록-조회-NAS-서버에서-적립금-관리-시-사용) *(NAS 서버에서 적립금 관리 시 사용)*
  - [아이템 구입](#-아이템-구입-NAS-서버에서-적립금-관리-시-사용) *(NAS 서버에서 적립금 관리 시 사용)*
- [📦 기타](#-기타)
  - [테마 설정](#-테마-설정)
  - [Preview 전용 데이터 로드 강제 실패 처리 여부 설정](#-Preview-전용-데이터-로드-강제-실패-처리-여부-설정)
- [📘⠀정의](#-정의)
  - [NasWallAdListType](#-NasWallAdListType)
  - [NasWallAdList](#-NasWallAdList)
  - [NasWallAdInfo](#-NasWallAdInfo)
  - [NasWallPointInfo](#-NasWallPointInfo)
  - [NasWallItemList](#-NasWallItemList)
  - [NasWallItemInfo](#-NasWallItemInfo)
- [📖⠀다른 문서](#-다른-문서)
- [🔗⠀다른 플렛폼 SDK](#-다른-플렛폼-sdk)

## 📝 업데이트
- `v2.0.0`
  - Major 버전 업데이트로 인해 연동 방식의 대대적인 변경이 있습니다.
  - Kotlin 기반의 코드로 변경되었습니다.
  - Kotlin Compose 기반의 새로운 예제 프로그램이 제공됩니다.
  - 오퍼월 UI가 새롭게 변경되었습니다.
  - 변경된 자세한 연동 방법은 개발 가이드 문서를 참고해주세요.


- [`전체 업데이트 목록 보기`](Update.md)

## 👤 개발자/매체 등록
[오퍼월 적용 가이드 문서](https://github.com/mafin-global/nas-offerwall#%EF%B8%8F-%EA%B0%9C%EB%B0%9C%EC%9E%90-%EB%93%B1%EB%A1%9D) 를 참고해주세요.

## 💾 설치하기

### 🔹 ***NasWallKit 추가***
프로젝트 레벨의 `settings.gradle` 파일에 Maven Repository를 추가합니다.

groovy
```gradle
...
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // 추가
        maven { url "https://ow.appang.kr/repository" }
    }
}
...
```

kotlin
```kotlin
...
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // 추가
        maven(url = "https://ow.appang.kr/repository")
    }
}
...
```

앱의 `build.gradle`(`app/build.gradle`) 파일에 NasWallKit 모듈을 추가합니다.

groovy
```gradle
...
dependencies {
    ...
    // 추가
    implementation "kr.mafin:naswallkit:2.0.0"
}
...
```

kotlin
```kotlin
...
dependencies {
    ...
    // 추가
    implementation("kr.mafin:naswallkit:2.0.0")
}
...
```

### 🔹 ***AndroidManifest.xml 설정***
`AndroidManifest.xml`의 `manifest`에 권한을 추가합니다.

```xml
<manifest>
    ...
    <queries>
        <intent>
            <action android:name="android.intent.action.MAIN" />
        </intent>
    </queries>
    
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <uses-permission android:name="com.google.android.gms.permission.AD_ID" />
    ...
</manifest>
```

`AndroidManifest.xml`의 `manifest`>`application` 에 activity 정보를 추가합니다.
```xml
<manifest>
    ...
    <application>
        ...
        <activity android:name="kr.mafin.naswallkit.NasWallActivity" android:exported="true" />
    </application>
</manifest>
```

## 🚀 초기화
SDK를 초기화합니다.

*❗ SDK의 다른 모든 함수를 호출하기 전에 초기화 함수를 가장 먼저 호출해야합니다.*   
*❗ 앱 내에서 회원이 로그아웃 후 다른 계정으로 로그인하는 경우, 새로 로그인한 회원 정보를 기준으로 다시 초기화 함수를 호출해야합니다.*

### 🔹 *개발자 서버에서 적립금 관리 시*

```kotlin
fun initializeDeveloperServer(
    context: Context,
    appKey: String,
    userData: String,
    testMode: Boolean,
    isPreview: Boolean,
    handler: ((NasWallError?) -> Unit)?
): Unit
```

- `context`: Context 지정합니다.
- `appKey`: 앱의 32자리 고유 키를 지정합니다. NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 확인할 수 있습니다.
- `userData`: 회원 ID 등의 적립금 지급에 필요한 고유한 회원 정보를 지정합니다. 광고 참여 완료 시 개발자 서버로 콜백 호출될 때 함께 제공됩니다.
- `testMode`: `true` 로 설정 시 테스트 광고가 표시됩니다.
- `isPreview`: `Jetpack Compose` 이용 시, 미리보기(Preview) 여부를 지정합니다. 일반적으로 `@Composable` 내에서 `LocalInspectionMode.current` 값을 지정하면 됩니다.
- `handler`: 처리 완료 시 호출되는 핸들러입니다.

*사용 예시*
```kotlin
NasWall.initializeDeveloperServer(
    context = context,
    appKey = "32자리 앱 Key",
    userData = "회원 데이터",
    testMode = false,
    isPreview = false,
) { error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

### 🔹 *NAS 서버에서 적립금 관리 시*
```kotlin
fun initializeNasServer(
    context: Context,
    appKey: String,
    userId: String,
    testMode: Boolean,
    isPreview: Boolean,
    handler: ((NasWallError?) -> Unit)?
): Unit
```

- `context`: Context 지정합니다.
- `appKey`: 앱의 32자리 고유 키를 지정합니다. NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 확인할 수 있습니다.
- `userId`: 회원의 고유한 ID를 지정합니다. "적립금 조회", "아이템 구입" 시 여기서 지정한 회원 ID를 기준으로 적용됩니다.
- `testMode`: `true` 로 설정 시 테스트 광고가 표시됩니다.
- `isPreview`: `Jetpack Compose` 이용 시, 미리보기(Preview) 여부를 지정합니다. 일반적으로 `@Composable` 내에서 `LocalInspectionMode.current` 값을 지정하면 됩니다.
- `handler`: 처리 완료 시 호출되는 핸들러입니다.

*사용 예시*
```kotlin
NasWall.initializeNasServer(
    context = context,
    appKey = "32자리 앱 Key",
    userId = "회원 ID",
    testMode = false,
    isPreview = false,
) { error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

## 📱 광고 표시

개발자 정의 UI로 광고를 표시하고, 광고에 참여하는 자세한 방법은 예제 프로그램을 참고해주시기 바랍니다.

### 🔹 *광고 목록 조회*
광고 목록을 조회합니다.

*❗ Compose Preview 상태에서는 라이브 데이터가 아닌 Preview 전용 데이터가 조회됩니다.*

```kotlin
fun adList(
    listType: NasWallAdListType,
    handler: (adList: NasWallAdList?, error: NasWallError?) -> Unit
): Unit
```

- `listType`: 조회 할 광고 목록 구분을 지정합니다.
  - `NasWallAdListType.BASIC`: 참여적립 광고 목록 (CPI, CPE, CPA, CPC 등)
  - `NasWallAdListType.CPS`: 쇼핑적립 광고 목록 (CPS)
  - `NasWallAdListType.CPQ`: 퀴즈적립 광고 목록 (CPQ)
- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `adList`: 조회 성공 시 광고 목록이 제공됩니다.
  - `error`: 조회 실패 시 오류 정보가 제공됩니다.

*사용 예시*
```kotlin
NasWall.adList(NasWallAdListType.BASIC) { adList: NasWallAdList?, error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```
### 🔹 *광고 상세 설명 조회*
광고 상세 설명을 조회합니다.

*❗ Compose Preview 상태에서는 라이브 데이터가 아닌 Preview 전용 데이터가 조회됩니다.*

```kotlin
fun adDescription(
    adInfo: NasWallAdInfo,
    handler: (description: String?, error: NasWallError?) -> Unit
): Unit
```

- `adInfo`: [광고 목록 조회](#-광고-목록-조회) 함수를 통해 획득한 광고 목록 중 조회할 광고 정보를 지정합니다.
- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `description`: 조회 성공 시 광고 상세 정보가 제공됩니다.
  - `error`: 조회 실패 시 오류 정보가 제공됩니다.

*사용 예시*
```kotlin
NasWall.adDescription(adInfo) { description: String?, error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

### 🔹 *광고 참여*
광고에 참여합니다.

*❗ Compose Preview 상태에서는 참여할 수 없습니다. 또한 에뮬레이터에서도 참여할 수 없습니다.*

```kotlin
fun joinAd(
    activity: Activity,
    adInfo: NasWallAdInfo,
    handler: (error: NasWallError?) -> Unit
): Unit
```

- `activity`: Activity 를 지정합니다.
- `adInfo`: [광고 목록 조회](#-광고-목록-조회) 함수를 통해 획득한 광고 목록 중 참여할 광고 정보를 지정합니다.
- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `error`: 참여 실패 시 오류 정보가 제공됩니다.

```kotlin
NasWall.joinAd(activity, adInfo) { error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

### 🔹 *문의하기 (임베드 오퍼월 전용)*
문의하기(전체 화면)를 표시합니다.

```kotlin
fun openCs(
    activity: Activity,
    handler: (error: NasWallError?) -> Unit,
    closeHandler: (() -> Unit)? = null
): Unit
```

- `activity`: `Activity`를 지정합니다.
- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `error`: 문의하기 열기 실패 시 오류 정보가 제공됩니다.
- `closeHandler`: 문의하기가 종료된 후 호출되는 핸들러입니다.

*사용 예시*
```kotlin
NasWall.openCs(
    activity = activity,
    handler = { error: NasWallError? ->
        if (error == null) {
            // 성공 시 처리 코드
        } else {
            // 실패 시 처리 코드
        }
    },
    closeHandler = {
        // 문의하기 종료 시 처리 코드
    }
)
```

## 💰 적립금

### 🔹 *획득 가능 총 적립금 조회*
획득 가능한 총 적립금을 조회합니다.

*❗ Compose Preview 상태에서는 라이브 데이터가 아닌 Preview 전용 데이터가 조회됩니다.*

```kotlin
fun totalPoint(
    handler: (pointInfo: NasWallPointInfo?, error: NasWallError?) -> Unit
): Unit
```

- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `pointInfo`: 조회 성공 시 적립금 정보가 제공됩니다.
  - `error`: 조회 실패 시 오류 정보가 제공됩니다.

*사용 예시*
```kotlin
NasWall.totalPoint { pointInfo: NasWallPointInfo?, error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

### 🔹 *보유 적립금 조회 (NAS 서버에서 적립금 관리 시 사용)*
NAS 서버에서 적립금을 관리하는 경우, 회원이 보유한 적립금을 조회합니다.

"적립금 관리 서버"가 "NAS 서버 사용"으로 설정된 경우에만 사용합니다.  "적립금 관리 서버" 설정은 NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 설정을 통해 확인 및 변경할 수 있습니다.

*❗ Compose Preview 상태에서는 라이브 데이터가 아닌 Preview 전용 데이터가 조회됩니다.*   
*❗ [초기화](#-초기화) 함수를 통해 지정한 `userId(회원 ID)` 를 기준으로 조회됩니다.*

```kotlin
fun userPoint(
    handler: (pointInfo: NasWallPointInfo?, error: NasWallError?) -> Unit
): Unit
```
- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `pointInfo`: 조회 성공 시 적립금 정보가 제공됩니다.
  - `error`: 조회 실패 시 오류 정보가 제공됩니다.

*사용 예시*
```kotlin
NasWall.userPoint { pointInfo: NasWallPointInfo?, error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

### 🔹 *아이템 목록 조회 (NAS 서버에서 적립금 관리 시 사용)*
NAS 서버에서 적립금을 관리하는 경우, 구입 가능한 아이템 목록을 조회합니다.

"적립금 관리 서버"가 "NAS 서버 사용"으로 설정된 경우에만 사용합니다.  "적립금 관리 서버" 설정은 NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 설정을 통해 확인 및 변경할 수 있습니다.

*❗ Compose Preview 상태에서는 라이브 데이터가 아닌 Preview 전용 데이터가 조회됩니다.*

```kotlin
fun itemList(
    handler: (itemList: NasWallItemList?, error: NasWallError?) -> Unit
): Unit
```

- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `itemList`: 조회 성공 시 아이템 목록이 제공됩니다.
  - `error`: 조회 실패 시 오류 정보가 제공됩니다.

*사용 예시*
```kotlin
NasWall.itemList { itemList: NasWallItemList?, error: NasWallError? ->
    if (error == null) {
        // 성공 시 처리 코드
    } else {
        // 실패 시 처리 코드
    }
}
```

### 🔹 *아이템 구입 (NAS 서버에서 적립금 관리 시 사용)*
NAS 서버에서 적립금을 관리하는 경우, 보유 적립금을 이용하여 아이템을 구입합니다.

"적립금 관리 서버"가 "NAS 서버 사용"으로 설정된 경우에만 사용합니다.  "적립금 관리 서버" 설정은 NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 설정을 통해 확인 및 변경할 수 있습니다.

*❗ Compose Preview 상태에서는 라이브 데이터가 아닌 Preview 전용 데이터가 조회됩니다.*   
*❗ [초기화](#-초기화) 함수를 통해 지정한 `userId(회원 ID)` 를 기준으로 구입됩니다.*

```kotlin
fun purchaseItem(
    itemId: Int,
    qty: Int,
    handler: (NasWallPointInfo?, error: NasWallError?) -> Unit
): Unit
```
- `itemId`: 구입 할 아이템 ID를 지정합니다. NAS 개발자 홈페이지의 "매체 관리" 메뉴에서 아이템을 등록하면, 아이템 ID를 확인할 수 있습니다.
- `qty`: 구입 수량을 지정합니다.
- `handler`: 처리 완료 시 호출되는 핸들러입니다.
  - `pointInfo`: 구입 성공 시 구입 금액을 차감한 잔여 적립금 정보가 제공됩니다.
  - `error`: 구입 실패 시 오류 정보가 제공됩니다.

*사용 예시*
```kotlin
NasWall.purchaseItem(itemId = itemId, qty = 1) { pointInfo: NasWallPointInfo?, error: NasWallError? ->
    if (error == null) {
        // 성공 시 실행 코드
    } else {
        // 실패 시 실행 코드
    }
}
```

## 📦 기타

### 🔹 *테마 설정*
앱의 다크 테마 여부를 지정합니다. 팝업, 임베드 오퍼월 표시될 때, 설정한 테마를 기준으로 배경색이 표시됩니다.

```kotlin
fun setIsDarkTheme(isDarkTheme: Boolean): Unit
```

- `isDarkTheme`: 다크 테마 여부를 지정합니다.

*사용 예시*
```kotlin
// 라이트 테마
NasWall.setIsDarkTheme(false)

// 다크 테마
NasWall.setIsDarkTheme(true)
```

### 🔹 *Preview 전용 데이터 로드 강제 실패 처리 여부 설정*
Compose Preview 모드에서 Preview 전용 데이터 로드 시 강제로 실패 처리할지 여부를 설정합니다.   
Preview 상태에서 데이터 로드 실패 시의 화면 표시를 확인하기 위해 사용합니다.

```kotlin
fun debugPreviewDataForceFail(forceFail: Boolean): Unit
```

- `forceFail`: 강제 실패 여부를 지정합니다.

*사용 예시*
```kotlin
NasWall.debugPreviewDataForceFail(true)
```

## 📘 정의

### 🔹 *NasWallAdListType*
광고 목록 구분
```kotlin
enum class NasWallAdListType : kotlin.Enum<kr.mafin.naswallkit.define.NasWallAdListType> {
    BASIC,    // 참여적립 (CPI, CPE, CPA, CPC 등)
    CPS,      // 쇼핑적립 (CPS)
    CPQ;      // 퀴즈적립 (CPQ)
}
```

### 🔹 *NasWallAdList*
광고 목록
```kotlin
typealias NasWallAdList = List<NasWallAdInfo>
```

### 🔹 *NasWallAdInfo*
광고 정보
```kotlin
class NasWallAdInfo {
    val id: Int                       // ID
    val title: String                 // 광고명
    val missionText: String           // 미션
    val iconUrl: String               // 아이콘 URL
    val adPrice: String               // 참여 비용
    val rewardPrice: Int              // 적립금
    val rewardUnit: String            // 적립금 단위
    val category: NasWallAdCategory   // 카테고리
}
```

### 🔹 *NasWallPointInfo*
적립금 정보
```kotlin
class NasWallPointInfo {
    val point: Int          // 적립금
    val unit: String        // 적립금 단위
    fun toString(): String  // "(적립금)(단위)" 예)"35,270원"
}
```

### 🔹 *NasWallItemList*
아이템 목록
```kotlin
typealias NasWallItemList = List<NasWallItemInfo>
```

### 🔹 *NasWallItemInfo*
아이템 정보
```kotlin
class NasWallItemInfo {
    val id: Int         // ID
    val name: String    // 이름
    val price: Int      // 가격
    val unit: String    // 가격 단위
}
```

## 📖 다른 문서
- [`내장 UI 개발 가이드`](Guide.Embed.md) : 미리 만들어진 UI를 사용하는 방식으로, 별도의 UI 개발 없이 쉽게 연동할 수 있습니다.
- [`업데이트`](Update.md) : SDK 업데이트 정보를 제공합니다.

## 🔗 다른 플렛폼 SDK
- [`iOS SDK`](https://github.com/mafin-global/nas-offerwall-ios)
- [`Unity SDK`](https://github.com/mafin-global/nas-offerwall-unity)
