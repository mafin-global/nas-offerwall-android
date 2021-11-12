# 📖 NAS 오퍼월 Android SDK - 내장 UI 개발 가이드
이 문서는 NAS 오퍼월 SDK 내장 UI 연동 가이드를 제공합니다.

오퍼월 UI 표시 방식은 다음 두가지를 지원합니다.

- `내장 UI` : 미리 만들어진 UI를 사용하는 방식으로, 별도의 UI 개발 없이 쉽게 연동할 수 있습니다.
- `개발자 정의 UI` : 개발자가 UI를 직접 만들어서 연동할 수 있는 방식으로, 개발자 앱의 UI에 맞게 자유롭게 구성할 수 있습니다.

`개발자 정의 UI`를 사용 하려면, [📖 개발자 정의 UI 개발 가이드](Guide.Custom.md) 문서를 참고해 주시기 랍니다.

## 목차
- [📝⠀업데이트](#-업데이트)
- [👤️⠀개발자/매체 등록](#%EF%B8%8F-개발자-매체-등록)
- [🚀⠀SDK 연동](#-sdk-연동)
    - [라이브러리 추가](#라이브러리-추가)
    - [AndroidManifest.xml 설정](#androidmanifestxml-설정)
    - [초기화](#초기화)
    - [팝업 오퍼월 띄우기](#팝업-오퍼월-띄우기)
    - [임베드 오퍼월 삽입](#임베드-오퍼월-삽입)
    - [적립금 조회](#적립금-조회-nas-서버에서-적립금-관리-시-사용) _(NAS 서버에서 적립금 관리 시 사용)_
    - [적립금 사용 (아이템 구매)](#적립금-사용-아이템-구매-nas-서버에서-적립금-관리-시-사용) _(NAS 서버에서 적립금 관리 시 사용)_
- [📖⠀다른 문서](-다른-문서)
- [🔗⠀다른 플렛폼 SDK](-다른-플렛폼-sdk)

## 📝 업데이트
- [`2021년 11월 12일`](Update.md#2021년-11월-12일)
  - 일부폰에서 광고 참여 시 -99992 오류 발생하는 문제 수정
- [`2021년 10월 21일`](Update.md#2021년-10월-21일)
  - IMEI 수집 제거
- [`2021년 6월 23일`](Update.md#2021년-6월-23일)
  - 사용자 휴대폰번호 수집 제거
- [`2020년 10월 5일`](Update.md#2020년-10월-5일)
    - USER_DATA 값 URL Encode 처리 (+ 문자 등이 포함되어 있을 때, 적립 콜백 호출 시 ud 값이 잘못 반환되는 문제 수정)
- [`2020년 6월 26일`](Update.md#2020년-6월-26일)
    - 테스트 모드와 관련된 버그 수정
- [`2019년 10월 14일`](Update.md#2019년-10월-14일)
    - Android Q(10) 대응 버그 수정
- [`2019년 9월 19일`](Update.md#2019년-9월-19일)
    - Android Q(10) 대응
    - 초기화 완료 이벤트 추가 (NASWall.setOnInitListener)
- [`전체 업데이트 목록 보기`](Update.md)

## 👤️ 개발자/매체 등록
[오퍼월 적용 가이드 문서](https://github.com/mafin-global/nas-offerwall#%EF%B8%8F-%EA%B0%9C%EB%B0%9C%EC%9E%90-%EB%93%B1%EB%A1%9D) 를 참고해주세요.

## 🚀 SDK 연동

### `라이브러리 추가`
`/app/libs` 폴더의 `NASWall.jar` 파일을 추가합니다.

> NAS SDK 에는 이미 Proguard가 적용되어 있습니다. Proguard 적용 시 NAS SDK는 제외시켜야 정상 작동합니다.
>
> ```
> # NAS SDK Proguard
> -dontwarn com.nextapps.naswall.**
> -keep class com.nextapps.naswall.** {
>     *;
> }
> ```

### `AndroidManifest.xml 설정`
`AndroidManifest.xml` 에 다음 항목을 추가합니다.

```
<queries>
    <intent>
        <action android:name="android.intent.action.MAIN" />
    </intent>
</queries>

<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
```

> 프로젝트의 Target SDK를 23(Android 6.0) 이상으로 지정 한 경우, 사용자가 위의 권한을 허가하지 않으면 오퍼월이 정상적으로 작동하지 않을 수 있습니다.
안드로이드 6.0 이상에서는 오퍼월을 사용하기전에 위의 권한들이 모두 허용되어 있는지 먼저 확인 후 사용하시기 바랍니다. 만약 권한이 없다면 사용자에게 권한 요청을 하시기 바랍니다.
사용자에게 권한을 요청하는 방법은 다음 [공식 문서](http://developer.android.com/intl/ko/training/permissions/requesting.html) 를 참고하세요.

`AndroidManifest.xml`에 `앱 Key`를 설정합니다.
`[APP_KEY]`부분을 앱 등록 후 받은 32자리 키로 대체합니다.
```
<meta-data android:name="naswall_app_key" android:value="[APP_KEY]" />
```

`AndroidManifest.xml` 의 `application` 에 다음 정보를 추가합니다.
```
<activity android:name="com.nextapps.naswall.NASWallBrowser" android:configChanges="keyboardHidden|orientation|screenSize">
    <intent-filter>
       <action android:name="android.intent.action.VIEW"/>
       <category android:name="android.intent.category.DEFAULT"/>
       <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>
</activity>

<activity android:name="com.nextapps.naswall.NASWall" android:configChanges="keyboardHidden|orientation" />
```

### `초기화`
앱 실행 부분에 `초기화 함수`를 호출합니다.

`testMode`는 개발 테스트 버전인 경우에만 `true` 를 입력하고, 배포 버전에서는 `false` 를 입력합니다.

- ***개발자 서버에서 적립금 관리 시 사용***
    ```
    Context context = this;
    boolean testMode = false;
    
    NASWall.init(context, testMode);
    ```
 
- ***NAS 서버에서 적립금 관리 시 사용***
    > `사용자 ID`는 사용자를 구분하기 위한 고유값입니다. NAS 서버에서 `사용자 ID` 별로 적립금이 쌓이기 때문에 사용자별로 고유한 값을 입력해야합니다.
    ```
    Context context = this; 
    boolean testMode = false; 
    String userid = "사용자 ID";
    
    NASWall.init(context, testMode, userId);
    ```

### `팝업 오퍼월 띄우기`
`팝업` 형식으로 오퍼월을 보여주고자 하는 경우에 사용합니다.

- ***개발자 서버에서 적립금 관리 시 사용***
    > `USER_DATA`에 개발자가 사용자를 구분하기 위한 값을 입력합니다. 광고 참여 완료 후 개발자 서버로 `콜백 URL` 호출 시. `[USER_DATA]` 파라메터로 전달됩니다. 

- ***NAS 서버에서 적립금 관리 시 사용***
    > `USER_DATA`에 `초기화 함수` 호출 시 사용한 `userId`를 입력합니다.

```
Activity activity = this;
String userData = "USER_DATA";
NASWall.open(activity, userData);
```

***타겟팅 광고 노출 방법***
    
기본적으로 오퍼월에는 타겟팅 광고는 노출되지 않습니다.
사용자의 `연령` 또는 `성별` 정보가 있는 경우, 아래와 같은 방법으로 타겟팅 광고를 노출시킬 수 있습니다.

```
Activity activity = this;
String userData = "USER_DATA";
int age = 20; // 연령 (연령 정보가 없을 경우 0 으로 설정)
SEX sex = SEX.SEX_MALE; // 성별 (SEX_UNKNOWN=성별정보없음, SEX_MALE=남자, SEX_FEMALE=여자)
NASWall.open(activity, userData, age, sex);
```

### `임베드 오퍼월 삽입`
오퍼월을 **특정 View** 내에 보여주고자 하는 경우 사용합니다.

`PARENT`에 임베드 오퍼월을 삽입하고자 하는 `View(ViewGroup)`를 입력합니다.

- ***개발자 서버에서 적립금 관리 시 사용***
    > `USER_DATA`에 개발자가 사용자를 구분하기 위한 값을 입력합니다. 광고 참여 완료 후 개발자 서버로 `콜백 URL` 호출 시. `[USER_DATA]` 파라메터로 전달됩니다. 

- ***NAS 서버에서 적립금 관리 시 사용***
    > `USER_DATA`에 `초기화 함수` 호출 시 사용한 `userId`를 입력합니다.

```
Activity activity = this;
ViewGroup parent = PARENT;
String userData = "USER_DATA";
NASWall.embed(parent, userData);
```

`PARENT`가 포함된 `Activity`의 `onResume` 에 다음 코드를 추가합니다.
```
@Override
protected void onResume() {
    NASWall.embedOnResume();
    super.onResume();
}
```

***타겟팅 광고 노출 방법***
    
기본적으로 오퍼월에는 타겟팅 광고는 노출되지 않습니다.
사용자의 `연령` 또는 `성별` 정보가 있는 경우, 아래와 같은 방법으로 타겟팅 광고를 노출시킬 수 있습니다.

```
Activity activity = this;
String userData = "USER_DATA";
int age = 20; // 연령 (연령 정보가 없을 경우 0 으로 설정)
SEX sex = SEX.SEX_MALE; // 성별 (SEX_UNKNOWN=성별정보없음, SEX_MALE=남자, SEX_FEMALE=여자)
NASWall.open(activity, userData, age, sex);
```

### `적립금 조회` _(NAS 서버에서 적립금 관리 시 사용)_
`NASWall.getUserPoint(Context context, OnUserPointListener l)` 함수를 호출하여 사용자 적립금을 조회할 수 있습니다.

- 성공 시 `OnUserPointListener.OnSuccess(String userId, int point, String unit)` 이벤트가 발생합니다.
    - `userId` : 사용자 ID
    - `point` : 적립 금액
    - `unit` : 적립 금액 단위

- 실패 시 `OnUserPointListener.OnError(String userId, int errorCode)` 이벤트가 발생합니다.
    - `userId` : 사용자 ID
    - `errorCode` : 오류 코드

```
NASWall.getUserPoint(context, new OnUserPointListener() {
    @Override
    public void OnSuccess(String userId, int point, String unit) {
        Toast.makeText(context, "적립금 조회가 완료되었습니다.\n남은 적립금 : " + point + " " + unit, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void OnError(String userId, int code) {
        Toast.makeText(context, "적립금 조회 중 오류가 발생했습니다.\n오류 코드 : " + code, Toast.LENGTH_SHORT).show();
    }
});
```

### `적립금 사용 (아이템 구매)` _(NAS 서버에서 적립금 관리 시 사용)_
`NASWall.purchaseItem(Context context, String itemId, OnPurchaseItemListener l)` 함수를 호출하여 아이템을 구매하고 사용자 적립금을 사용할 수 있습니다.

`NASWall.purchaseItem(Context context, String itemId, int purchaseCount, OnPurchaseItemListener l)` 함수를 사용하면 구매 수량을 지정하여 구매할 수 있습니다.

- 성공 시 `OnPurchaseItemListener.OnSuccess(String userId, String itemId, int purchaseCount, int point, String unit)` 이벤트가 발생합니다.
    - `userId` : 사용자 ID
    - `itemId` : 아이템 ID
    - `purchaseCount` : 구매 수량
    - `point` : 구매 후 남은 적립 금액
    - `unit` : 적립 금액 단위

- 실패 시 `OnPurchaseItemListener.OnError(String userId, String itemId, int purchaseCount, int errorCode)` 이벤트가 발생합니다.
    - `userId` : 사용자 ID
    - `itemId` : 아이템 ID
    - `purchaseCount` : 구매 수량
    - `errorCode` : 오류 코드
    
- 적립금이 부족 시 `OnPurchaseItemListener.OnNotEnoughPoint(tring userId, String itemId, int purchaseCount)` 이벤트가 발생합니다.
    - `userId` : 사용자 ID
    - `itemId` : 아이템 ID
    - `purchaseCount` : 구매 수량

```
NASWall.purchaseItem(context, "아이템 ID", new OnPurchaseItemListener() {
    @Override
    public void OnSuccess(String userId, String itemId, int purchaseCount, int point, String unit) {
        Toast.makeText(context, "아이템 구매가 완료되었습니다.\n남은 적립금 : " + point + " " + unit, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void OnNotEnoughPoint(String userId, String itemId, int purchaseCount){
        Toast.makeText(context, "적립금이 부족해서 아이템을 구매할 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void OnError(String userId, String itemId, int purchaseCount, int errorCode) {
        Toast.makeText(context, "아이템 구매 시 오류가 발생했습니다.\n오류 코드 : " + errorCode, Toast.LENGTH_SHORT).show();
    }
});
```

## 📖 다른 문서
- [`개발자 정의 UI 개발 가이드`](Guide.Custom.md) : 개발자가 UI를 직접 만들어서 연동할 수 있는 방식으로, 개발자 앱의 UI에 맞게 자유롭게 구성할 수 있습니다.
- [`업데이트`](Update.md) : SDK 업데이트 정보를 제공합니다.

## 🔗 다른 플렛폼 SDK
- [`iOS SDK`](https://github.com/mafin-global/nas-offerwall-ios)
- [`Unity SDK`](https://github.com/mafin-global/nas-offerwall-unity)
