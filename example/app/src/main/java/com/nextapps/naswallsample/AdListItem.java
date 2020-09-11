package com.nextapps.naswallsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nextapps.naswall.NASWallAdInfo;
import com.nextapps.naswallsample.TestNetwork.OnNetworkListener;

import java.util.HashMap;

public class AdListItem extends RelativeLayout {
	
	// 아이콘은 매번 인터넷에서 다운로드 하지 않고 한번 다운로드한 아이콘은 메모리에 캐시한다.
	static HashMap<String, Bitmap> iconCache = new HashMap<String, Bitmap>();
	
	TestNetwork iconNetwork = null;
	
	public AdListItem(Context context) {
		this(context, null);
	}
	public AdListItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public AdListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDetachedFromWindow() {
		iconNetwork = null;
		
		super.onDetachedFromWindow();
	}
	
	public void updateInfo(final NASWallAdInfo adInfo) {
		final ImageView iconImageView = (ImageView)findViewById(R.id.iconImageView);
		TextView titleTextView = (TextView)findViewById(R.id.titleTextView);
		TextView missionTextView = (TextView)findViewById(R.id.missionTextView);
		TextView rewardTextView = (TextView)findViewById(R.id.rewardTextView);

		titleTextView.setText(adInfo.getTitle());
		missionTextView.setText("[" + adInfo.getAdPrice() + "] " + adInfo.getMissionText());
		rewardTextView.setText("보상 : " + adInfo.getRewardPrice() + adInfo.getRewardUnit());
		
		// 캐시된 아이콘 있는지 검사
		if (iconCache.containsKey(adInfo.getIconUrl())) {
			// 캐시된 아이콘을 불러온다. 
			iconImageView.setImageBitmap(iconCache.get(adInfo.getIconUrl()));
		} else {
			// 아이콘을 인터넷에서 다운로드한다.
			iconImageView.setImageBitmap(null);
			
			iconNetwork = new TestNetwork();
			iconNetwork.dataFromURL(adInfo.getIconUrl(), new OnNetworkListener() {
				
				@Override
				public void OnSuccess(TestNetwork sender) {
					byte[] buffer = sender.getData().toByteArray();
					Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
					if (bitmap != null) {
						iconCache.put(adInfo.getIconUrl(), bitmap);
						
						if (sender == iconNetwork) {
							iconImageView.setImageBitmap(bitmap);
						}
					}
				}
				
				@Override
				public void OnError(TestNetwork sender) {
					
				}
			});
		}
	}

}
