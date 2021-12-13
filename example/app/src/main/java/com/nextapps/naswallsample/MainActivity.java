package com.nextapps.naswallsample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.nextapps.naswall.NASWall;
import com.nextapps.naswall.NASWall.OnCloseListener;
import com.nextapps.naswall.NASWall.OnPurchaseItemListener;
import com.nextapps.naswall.NASWall.OnUserPointListener;

import java.util.ArrayList;

public class MainActivity extends Activity {
	
	// Const
	private static final int PERMISSION_REQUEST_CODE = 21539;

	// 아래의 값을 설정하세요.
	public static String USER_DATA = "ud";
	public static final boolean TEST_MODE = false;
	
	// NAS 서버에서 적립금을 관리하는 경우 아래의 값을 설정하세요.
	public static String USER_ID = "";
	public static String ITEM_ID = "";

	// Layout
	private LinearLayout _permissionLayout;
	private LinearLayout _contentLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_permissionLayout = (LinearLayout)findViewById(R.id.permissionLayout);
		_contentLayout = (LinearLayout)findViewById(R.id.contentLayout);
		
		_permissionLayout.setVisibility(View.GONE);
		_contentLayout.setVisibility(View.GONE);
		
		((Button)findViewById(R.id.requestPermissionButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkPermissions();
			}
		});
		
		// 안드로이드 6.0 이상에서 권한체크
		checkPermissions();
	}
	
	@Override
	@SuppressLint("NewApi")
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_CODE) {
			boolean allGranted = true;
			for (int i = 0; i < permissions.length; ++i) {
				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
					allGranted = false;
					break;
				}
			}
			if (allGranted) {
				_contentLayout.setVisibility(View.VISIBLE);
				_permissionLayout.setVisibility(View.GONE);
				init();
			} else {
				_permissionLayout.setVisibility(View.VISIBLE);
				_contentLayout.setVisibility(View.GONE);
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	
	@SuppressLint("NewApi")
	private void checkPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ArrayList<String> permissionList = new ArrayList<String>();
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
				permissionList.add(Manifest.permission.INTERNET);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
				permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)
				permissionList.add(Manifest.permission.GET_ACCOUNTS);
			
			if (permissionList.size() > 0) {
				String[] permissions = new String[permissionList.size()];
				permissions = permissionList.toArray(permissions);
				ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
			} else {
				_contentLayout.setVisibility(View.VISIBLE);
				init();
			}
		} else {
			_contentLayout.setVisibility(View.VISIBLE);
			init();
		}
	}
	
	private void init() {
		// 초기화 완료 Listener 등록
		NASWall.setOnInitListener(this.onNASWallInit);
		
		// 초기화
//		NASWall.init(this, TEST_MODE, USER_ID); // NAS 서버에서 적립금 관리하는 경우
		NASWall.init(this, TEST_MODE); // 개발자 서버에서 적립금 관리하는 경우
	}
	
	private NASWall.OnInitListener onNASWallInit = new NASWall.OnInitListener() {
		
		@Override
		public void OnSuccess() {
			// 내장 오퍼월 Close 이벤트 등록
			NASWall.setOnCloseListener(new OnCloseListener() {
				
				@Override
				public void OnClose() {
					Toast.makeText(MainActivity.this, "NASWall - closed", Toast.LENGTH_SHORT).show();
				}
			});
			
			((Button)findViewById(R.id.offerWallButton)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 내장 오퍼월 (팝업) 보기
					NASWall.open(MainActivity.this, USER_DATA);
				}
			});
			
			((Button)findViewById(R.id.offerWallEmbedButton)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 내장 오퍼월 (임베드) 보기
					Intent intent = new Intent(MainActivity.this, EmbedActivity.class);
					startActivity(intent);
				}
			});
			
			((Button)findViewById(R.id.customWallButton)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 사용자 정의 오퍼월 보기
					Intent intent = new Intent(MainActivity.this, CustomWallActivity.class);
					startActivity(intent);
				}
			});
			
			((Button)findViewById(R.id.getPointButton)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// 적립금 조회
					NASWall.getUserPoint(MainActivity.this, new OnUserPointListener() {
						
						@Override
						public void OnSuccess(String userId, int point, String unit) {
							Toast.makeText(MainActivity.this, "적립금 조회가 완료되었습니다.\n남은 적립금 : " + point + " " + unit, Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void OnError(String userId, int errorCode) {
							Toast.makeText(MainActivity.this, "적립금 조회 중 오류가 발생했습니다.\n오류 코드 : " + errorCode, Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			
			((Button)findViewById(R.id.usePointButton)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// 아이템 구매 (적립금 사용)
					NASWall.purchaseItem(MainActivity.this, ITEM_ID, new OnPurchaseItemListener() {
						
						@Override
						public void OnSuccess(String userId, String itemId, int purchaseCount, int point, String unit) {
							Toast.makeText(MainActivity.this, "아이템 구매가 완료되었습니다.\n남은 적립금 : " + point + " " + unit, Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void OnNotEnoughPoint(String userId, String itemId, int purchaseCount){
							Toast.makeText(MainActivity.this, "적립금이 부족해서 아이템을 구매할 수 없습니다.", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void OnError(String userId, String itemId, int purchaseCount, int errorCode) {
							Toast.makeText(MainActivity.this, "아이템 구매 시 오류가 발생했습니다.\n오류 코드 : " + errorCode, Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		}
	};
}
