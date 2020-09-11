# 📖 NAS 오퍼월 Android SDK - 개발자 정의 UI 개발 가이드
이 문서는 NAS 오퍼월 SDK 개발자 정의 UI 연동 가이드를 제공합니다.

오퍼월 UI 표시 방식은 다음 두가지를 지원합니다.

- `내장 UI` : 미리 만들어진 UI를 사용하는 방식으로, 별도의 UI 개발 없이 쉽게 연동할 수 있습니다.
- `개발자 정의 UI` : 개발자가 UI를 직접 만들어서 연동할 수 있는 방식으로, 개발자 앱의 UI에 맞게 자유롭게 구성할 수 있습니다.

`내장 UI`를 사용 하려면, [📖 내장 UI 개발 가이드](Guide.Embed.md) 문서를 참고해 주시기 랍니다.

## 목차
- [📝⠀업데이트](#-업데이트)
- [❗⠀주의사항](#-주의사항)
- [👤️⠀개발자 등록](#%EF%B8%8F-개발자-등록)
- [🎲⠀매체 등록](#-매체-등록)
    - [적립금 관리 서버](#적립금-관리-서버)
    - [리워드 금액 단위](#리워드-금액-단위)
    - [리워드 금액 비율](#리워드-금액-비율)
    - [콜백 URL 등록](#콜백-url-등록-개발자-서버에서-적립금-관리-시-사용) _(개발자 서버에서 적립금 관리 시 사용)_
    - [아이템 등록](#아이템-등록-nas-서버에서-적립금-관리-시-사용) _(NAS 서버에서 적립금 관리 시 사용)_
- [🚀⠀SDK 연동](#-sdk-연동)
    - [라이브러리 추가](#라이브러리-추가)
    - [AndroidManifest.xml 설정](#androidmanifestxml-설정)
    - [초기화](#초기화)
    - [광고 목록 가져오기](#광고-목록-가져오기)
    - [광고 참여](#광고-참여)
    - [광고 상세 설명글](#광고-상세-설명글)
    - [적립금 조회](#적립금-조회-nas-서버에서-적립금-관리-시-사용) _(NAS 서버에서 적립금 관리 시 사용)_
    - [적립금 사용 (아이템 구매)](#적립금-사용-아이템-구매-nas-서버에서-적립금-관리-시-사용) _(NAS 서버에서 적립금 관리 시 사용)_
    
## 📝 업데이트
- [`2020년 3월 31일`](Update.md#2020년-3월-31일)
    - 통신 관련 버그 수정
- [`2020년 1월 30일`](Update.md#2020년-1월-30일---내장-ui) - _내장 UI_
    - foreground 시 새로고침되지 않는 버그 수정
- [`2020년 1월 28일`](Update.md#2020년-1월-28일---내장-ui) - _내장 UI_
    - 환경에 따라 오퍼월이 보이지 않는 현상 수정
- [전체 업데이트 목록 보기](Update.md)

## ❗ 주의사항
`개발자 정의 UI` 사용 시,
`잠금화면`, `앱의 홈 화면` 에서 [`광고 목록 가져오기 API`](#광고-목록-가져오기)를 호출해서는 안됩니다.

이 화면에서 [`광고 목록 가져오기 API`](#광고-목록-가져오기) 사용 시, 과중한 API 호출로 인해 서버에 많은 부하가 발생합니다.

이런 경우, 광고 목록이 `차단` 당할 수 있으니 주의해 주시기 바랍니다.

## 👤️ 개발자 등록
NAS 오퍼월 연동을 위해서는 먼저 개발자 등록을 해야합니다.

[NAS 홈페이지](http://www.appang.kr/nas) 에 접속하여 `회원가입` 버튼을 눌러 가입합니다.

## 🎲 매체 등록
NAS 오퍼월 연동을 위해서는 연동할 매체 등록 해야합니다.

[NAS 홈페이지](http://www.appang.kr/nas) 에 로그인 후 `신규매체 등록` 버튼을 눌러 매체를 등록합니다.

매체를 등록하면 32자리 `앱 KEY`가 발급되며, SDK 초기화 시 필요한 값입니다.
`매체 관리` 메뉴에서 등록한 매체의 우측 `more` 버튼을 눌러 `앱 KEY 복사` 를 눌러 `앱 KEY`를 복사할 수 있습니다.

### `적립금 관리 서버`
사용자 적립금을 NAS 서버에서 관리할지 아니면 개발자 서버에서 관리할지를 선택합니다.

- `NAS 서버 사용` : 개발자가 서버를 가지고있지 않고, NAS 서버에서 사용자의 적립금을 관리할 경우 선택합니다. SDK를 통해 적립금의 확인 및 사용이 가능합니다.
- `개발자 서버 사용` : 개발자가 서버에서 사용자 적립금을 직접 관리할 경우 선택합니다. 사용자가 적립금 충전 시 개발자 서버의 콜백 URL로 통보해줍니다.

### `리워드 금액 단위`
리워드 금액 단위는 오퍼월 에서 사용자에게 보이는 리워드 금액의 단위입니다.

만약 광고 참여 완료 시 사용자에게 100골드를 준다면 `골드`를 입력하고, 100원을 준다면 `원`을 입력합니다

### `리워드 금액 비율`
리워드 금액 비율은 광고 참여 완료 시 개발자가 얻게되는 수익 중에서 사용자에게 리워드로 줄 금액의 비율(%)입니다.

만약 사용자가 광고참여 시 개발자가 받게되는 수익이 200원이고, 사용자에게 100원을 주고 싶으면 리워드 금액 비율을 `50`으로 입력합니다.

각 광고마다 개발자에게 지급되는 금액이 다르기 때문에, 사용자에게 지급하는 실제 금액은 `콜백 URL` 호출 시 함께 전송됩니다.

### `콜백 URL 등록` _(개발자 서버에서 적립금 관리 시 사용)_
적립금 관리 서버를 `개발자 서버 사용` 으로 선택한 경우 설정할 수 있습니다.

`콜백 URL`은 NAS 서버에서 개발자 서버로 호출하는 URL로 사용자가 광고 `참여 완료` 시 호출됩니다. `콜백 URL`에서 사용자에게 적립금등의 혜택을 지급하도록 구성하시기 바랍니다.
콜백 URL은 `앱 설정` 화면의 `보상형 기본` 탭에서 설정할 수 있습니다.

`콜백 URL`에는 아래의 값들이 제공됩니다.

이 값들은 개발자 서버로 호출 시 실제 값으로 변환됩니다. `콜백 URL` 입력 시 반드시 `대괄호`까지 입력하시기 바랍니다.

- `[SEQ_ID]` : 적립 고유 ID
- `[USER_DATA]` : 회원 정의 데이터
- `[PRICE]` : 광고 단가 (매체 수익금)
- `[REWARD]` : 리워드 금액 (오퍼월에서 참여한 경우에만 값이 있음)
- `[AD_ID]` : 광고 ID
- `[AD_KEY]` : 광고 KEY
- `[AD_NAME]` : 광고명
- `[AD_TYPE]` : 광고구분 (CPI, CPE, CPA, CPC, FACEBOOK)
- `[USER_ADID]` : 사용자 기기 36자리 광고 ID (Android : ADID, iOS : IDFA)
- `[USER_IP]` : 사용자 IP 주소

```
http://server.kr/callback.asp?sid=[SEQ_ID]&ud=[USER_DATA]&p=[PRICE]&r=[REWARD]&ai=[AD_ID]&ak=[AD_KEY]&n=[AD_NAME]&t=[AD_TYPE]&adid=[USER_ADID]&ip=[USER_IP]`
```

개발자 서버의 웹페이지가 `HTTP 200` 상태값을 리턴하면 `콜백 URL`을 더 이상 호출하지 않고 중지됩니다.
만약 `HTTP 200` 이외의 상태값이 리턴되면 `최대 5번`까지 재시도하여 호출하고, `5번` 오류가 발생하면 더 이상 호출하지 않습니다.

오류 횟수에 따른 `재호출` 시간 간격은 아래와 같습니다.

- `1회 오류 시` : 오류 시점부터 5분 후 재호출
- `2회 오류 시` : 오류 시점부터 10분 후 재호출
- `3회 오류 시` : 오류 시점부터 25분 후 재호출
- `4회 오류 시` : 오류 시점부터 30분 후 재호출
- `5회 오류 시` : 오류 시점부터 8시간 후 재호출
- `6회 오류 시` : 호출 중단

> ***적립금 중복 지급 방지를 위한 처리***
> - 콜백 중복 호출 시, 적립금 중복 지급을 방지하기 위해, `[SEQ_ID]`를 기준으로 중복지급을 막아주시기 바랍니다.
>
> - 동일 사용자에게 적립금 중복 지급을 방지하기 위해, 개발사의 `회원 ID` 와 `[AD_ID]` 값을 기준으로 중복지급을 막아주시기 바랍니다.

### `아이템 등록` _(NAS 서버에서 적립금 관리 시 사용)_
아이템은 적립금 관리 서버를 `NAS 서버 사용` 으로 선택한 경우, 사용자가 적립금을 사용(차감)할 때 필요합니다.

아이템을 등록하고 SDK의 `아이템 구매 함수`를 이용하면, 사용자의 적립금을 사용(차감)할 수 있습니다.

`매체 관리` 메뉴에서 등록한 매체의 우측 `more` 버튼을 누르고, `아이템 관리` 를 눌러 아이템 관리 창을 띄웁니다.

아이템 관리 창에서 `아이템 추가` 버튼을 눌러 아이템을 등록합니다.

- `아이템 이름` : 아이템의 이름을 입력합니다.
- `가격` : 아이템의 가격을 입력합니다. 사용자가 아이템 구매 시 입력한 가격 만큼 적립금이 차감됩니다.

아이템 등록 후 아이템 목록에서 `아이템 ID` 를 확인할 수 있습니다.
`아이템 ID`는 SDK의 `아이템 구매 함수`를 호출할 때 필요한 값입니다.
등록된 아이템 정보는 `수정` 버튼을 눌러 언제든지 변경할 수 있습니다.

## 🚀 SDK 연동

### `라이브러리 추가`
`/app/libs` 폴더의 `NASWall.jar` 파일을 추가합니다.

> NAS SDK 에는 이미 Proguard가 적용되어 있습니다. Proguard 적용 시 NAS SDK는 제외시켜야 정상 작동합니다.
> ```
> # NAS SDK Proguard
> -dontwarn com.nextapps.naswall.**
> -keep class com.nextapps.naswall.** {
>     *;
> }
> ```

### `AndroidManifest.xml 설정`
`AndroidManifest.xml` 에 다음 권한을 추가합니다.

```
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

### `광고 목록 가져오기`
`NASWall.getAdList(Context context, String userData, OnAdListListener l)` 함수를 호출하여 광고 목록을 가져올 수 있습니다.

- ***개발자 서버에서 적립금 관리 시 사용***
    > `USER_DATA`에 개발자가 사용자를 구분하기 위한 값을 입력합니다. 광고 참여 완료 후 개발자 서버로 `콜백 URL` 호출 시. `[USER_DATA]` 파라메터로 전달됩니다. 

- ***NAS 서버에서 적립금 관리 시 사용***
    > `USER_DATA`에 `초기화 함수` 호출 시 사용한 `userId`를 입력합니다.

- 성공 시 `OnAdListListener.OnSuccess(ArrayList adList)` 이벤트가 발생합니다.
    - `adList` : 광고 목록

- 실패 시 `OnAdListListener.OnError(int errorCode)` 이벤트가 발생합니다.
    - `errorCode` : 오류 코드
        - `-99999` : 파라메터 오류
        - `-30001` : 콜백 URL이 등록되지 않았음. 앱 설정에서 콜백 URL을 등록해야함. (개발자 서버에서 적립금을 관리하는 경우)
        - `그외` : 기타 오류

> 광고 목록은 반드시 `Activity`의 `onResume` 에서 가져와야 사용자의 광고 참여가 정상적으로 완료됩니다.<br/>
광고 목록 가져오기 함수 호출 시 설치형 광고의 설치 여부를 자동으로 판단해 참여 완료가 되기 때문에, `onResume` 에서 광고 목록을 가져와서 UI를 재구성 해야합니다.

```
Context context = this;
String userData = "USER_DATA";
NASWall.getAdList(context, userData, new OnAdListListener() {
    @Override
    public void OnSuccess(ArrayList adList) {
        // 광고목록 가져오기 성공
        for (NASWallAdInfo adInfo : adList) {
            adInfo.getTitle(); //광고명
            adInfo.getIconUrl(); //아이콘 Url
            adInfo.getMissionText(); //참여방법
            adInfo.getAdPrice(); //참여비용
            adInfo.getRewardPrice(); //적립금
            adInfo.getRewardUnit(); // 적립금단위
        }
    }
    
    @Override
    public void OnError(int errorCode) {
        // 광고목록 불러오기 실패
    }
});
```

***NAS 이용문의 추가***

반드시 광고목록 하단에 `NAS 이용문의`를 추가하여, 사용자들이 불편사항을 `NAS에 직접` 문의할 수 있도록 합니다. (자세한 사용 방법은 예제 프로그램을 참고하세요)

```
LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
AdListFooter v = (AdListFooter)li.inflate(R.layout.ad_list_footer, null);
v.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View arg0) {
        NASWall.openCS(CustomWallActivity.this); // NAS 이용문의 띄우기
    }
});
v.setBitmap(NASWall.getCSBitmap()); // NAS 이용문의 이미지 설정
getListView().addFooterView(v);
```

***타겟팅 광고 노출 방법***
    
기본적으로 오퍼월에는 타겟팅 광고는 노출되지 않습니다.
사용자의 `연령` 또는 `성별` 정보가 있는 경우, 아래와 같은 방법으로 타겟팅 광고를 노출시킬 수 있습니다.

```
Context context = this;
String userData = "USER_DATA";
int age = 20; // 연령 (연령 정보가 없을 경우 0 으로 설정)
SEX sex = SEX.SEX_MALE; // 성별 (SEX_UNKNOWN=성별정보없음, SEX_MALE=남자, SEX_FEMALE=여자)
NASWall.getAdList(context, userData, age, sex, new OnAdListListener() {
    @Override
    public void OnSuccess(ArrayList adList) {
        // 광고목록 가져오기 성공
        for (NASWallAdInfo adInfo : adList) {
            adInfo.getTitle(); //광고명
            adInfo.getIconUrl(); //아이콘 Url
            adInfo.getMissionText(); //참여방법
            adInfo.getAdPrice(); //참여비용
            adInfo.getRewardPrice(); //적립금
            adInfo.getRewardUnit(); // 적립금단위
        }
    }
    
    @Override
    public void OnError(int errorCode) {
        // 광고목록 불러오기 실패
    }
});
```

### `광고 참여`
`NASWall.joinAd(Context context, NASWallAdInfo adInfo, OnJoinAdListener l)` 함수를 호출하여 광고에 참여할 수 있습니다.

`adInfo`는 `NASWall.getAdList` 함수를 호출하여 받은 광고 정보를 사용합니다.

- 성공 시 `OnJoinAdListener.OnSuccess(NASWallAdInfo adInfo, String url)` 이벤트가 발생합니다.
    - `adInfo` : 참여 광고 정보
    - `url` : 참여 URL

- 실패 시 `OnJoinAdListener.OnError(NASWallAdInfo adInfo, int errorCode)` 이벤트가 발생합니다.
    - `adInfo` : 참여 광고 정보
    - `errorCode` : 오류 코드
        - `-11` : NAS 서버에서 적립금 관리하는 경우 사용자 ID를 지정하지 않았음 (초기화 시 사용자 ID를 지정해야함)
        - `-12` : 개발자 서버에서 적립금 관리하는 경우 사용자 ID를 지정했음 (초기화 시 사용자 ID를 지정하지 말아야함)
        - `-10001` : 광고 종료됨
        - `-20001` : 이미 참여 완료한 광고
        - `-99999` : 파라메터 오류
        - `그외` : 기타 오류

- 완료 시 `OnJoinAdListener.OnComplete(NASWallAdInfo adInfo)` 이벤트가 발생합니다. `OnSuccess` 또는 `OnError` 호출 뒤 호출됩니다.
    - `adInfo` : 참여 광고 정보

```
final ProgressDialog progressDialog = ProgressDialog.show(this, null, "처리중...");

NASWall.joinAd(context, adInfo, new OnJoinAdListener() {
    @Override
    public void OnSuccess(NASWallAdInfo adInfo, String url) {
        // 참여 성공
        boolean isSuccess = false;
        try {
            Intent intent = Intent.parseUri(url, 0);
            if (intent != null) {
                startActivity(intent);
                isSuccess = true;
            }
        } catch (Exception e) { }
            
        if (!isSuccess) {
            Toast.makeText(CustomWallActivity.this,
                "캠페인에 참여할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
            
        progressDialog.dismiss();
    }
    
    @Override
    public void OnError(NASWallAdInfo adInfo, int errorCode) {
        // 참여 실패
        String message = "[" + errorCode + "] ";
        switch (errorCode) {
        case -10001:
            message += "종료된 캠페인입니다.";
            break;
        case -20001:
            message += "이미 참여한 캠페인입니다.";
            break;
        default:
            message += "캠페인에 참여할 수 없습니다.";
            break;
        }

        Toast.makeText(CustomWallActivity.this, message, 
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnComplete(NASWallAdInfo adInfo) {
        // 참여 처리 완료
        progressDialog.dismiss();
    }
});
```

### `광고 상세 설명글`
`NASWall.getAdDescription(Context context, NASWallAdInfo adInfo, OnAdDescriptionListener l)` 함수를 호출하여 광고의 상세 설명글을 가져올 수 있습니다.

`adInfo`는 `NASWall.getAdList` 함수를 호출하여 받은 광고 정보를 사용합니다.

- 성공 시 `OnAdDescriptionListener.OnSuccess(NASWallAdInfo adInfo, String description)` 이벤트가 발생합니다.
    - `adInfo` : 참여 광고 정보
    - `description` : 상세 설명글

- 실패 시 `OnAdDescriptionListener.OnError(NASWallAdInfo adInfo, int errorCode)` 이벤트가 발생합니다.
    - `adInfo` : 참여 광고 정보
    - `errorCode` : 오류 코드
        - `-99999` : 파라메터 오류
        - `-1` : 없는 광고
        - `그외값` : 기타 오류

```
NASWall.getAdDescription(context, adInfo, new OnAdDescriptionListener() {
    @Override
    public void OnSuccess(NASWallAdInfo adInfo, String description) {
        // 광고 상세 설명글 성공
        Toast.makeText(CustomWallActivity.this, description, 
            Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void OnError(NASWallAdInfo adInfo, int errorCode) {
        // 광고 상세 설명글 실패
        String message = "[" + errorCode + "] ";
        switch (errorCode) {
        case -1:
            message += "사용할 수 없는 캠페인입니다.";
            break;
        default:
            message += "광고 상세 설명글을 가져올 수 없습니다.";
            break;
        }
            
        Toast.makeText(CustomWallActivity.this, message, 
            Toast.LENGTH_SHORT).show();
    }
});
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
