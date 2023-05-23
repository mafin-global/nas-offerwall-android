package com.nextapps.naswallsample;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nextapps.naswall.NASWall;
import com.nextapps.naswall.NASWall.OnAdListListener;
import com.nextapps.naswall.NASWall.OnJoinAdListener;
import com.nextapps.naswall.NASWallAdInfo;

import java.util.ArrayList;

public class CustomWallActivity extends ListActivity {
	
	private ArrayList<NASWallAdInfo> adList = null;
	private AdArrayAdapter adapter;
	private boolean isAdListLoading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_wall);
		
		// NAS 오퍼월 초기화
		NASWall.init(this, MainActivity.TEST_MODE, MainActivity.USER_ID);
		
		// 하단에 이용문의 추가
		LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		AdListFooter v = (AdListFooter)li.inflate(R.layout.ad_list_footer, null);
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				NASWall.openCS(CustomWallActivity.this);
			}
		});
		v.setBitmap(NASWall.getCSBitmap());
		getListView().addFooterView(v);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// 광고 목록을 불러온다
		// 광고 목록을 불러올 때 설치형 광고의 설치여부를 자동으로 체크하기 때문에, onResume 에서 광고목록을 불러와야함
		if (!isAdListLoading) {
			isAdListLoading = true;
			
			final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Loading...");
			
			NASWall.getAdList(this, MainActivity.USER_DATA, new OnAdListListener() {
				
				@Override
				public void OnSuccess(ArrayList<NASWallAdInfo> adList) {
					// 광고목록 불러오기 성공
					CustomWallActivity.this.adList = adList;
					adapter = new AdArrayAdapter(CustomWallActivity.this, R.layout.ad_list_item, adList);
					setListAdapter(adapter);
					
					progressDialog.dismiss();
					isAdListLoading = false;
				}
				
				@Override
				public void OnError(int errorCode) {
					// 광고목록 불러오기 실패
					Toast.makeText(CustomWallActivity.this, "[" + errorCode + "] 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
					
					progressDialog.dismiss();
					isAdListLoading = false;
				}
			});
		}
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// 광고에 참여
		final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Loading...");
		
		NASWall.joinAd(this, adList.get(position), new OnJoinAdListener() {
			
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
					Toast.makeText(CustomWallActivity.this, "캠페인에 참여할 수 없습니다.", Toast.LENGTH_SHORT).show();
				}
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
				case -9968:
				case -9969:
					message += "Wi-Fi 환경에서는 참여가 불가능합니다.\nWi-Fi를 끄고 다시 시도해 주세요.";
					break;
				default:
					message += "캠페인에 참여할 수 없습니다.";
					break;
				}
				Toast.makeText(CustomWallActivity.this, message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnComplete(NASWallAdInfo arg0) {
				progressDialog.dismiss();
			}
		});
		
		super.onListItemClick(l, v, position, id);
	}
	
	private class AdArrayAdapter extends ArrayAdapter<NASWallAdInfo> {
		
		private ArrayList<NASWallAdInfo> adList;
		
		public AdArrayAdapter(Context context, int textViewResourceId, ArrayList<NASWallAdInfo> objects) {
			super(context, textViewResourceId, objects);
			this.adList = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AdListItem v = (AdListItem)convertView;
			if (v == null) {
				LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = (AdListItem)li.inflate(R.layout.ad_list_item, null);
			}
			
			NASWallAdInfo adInfo = adList.get(position);
			v.updateInfo(adInfo);

			return v;
		}
	}
}
