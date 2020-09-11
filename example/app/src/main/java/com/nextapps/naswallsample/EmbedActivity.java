package com.nextapps.naswallsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.nextapps.naswall.NASWall;

public class EmbedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_embed);
		
		RelativeLayout embedView = (RelativeLayout)findViewById(R.id.embedView);
		NASWall.embed(this, embedView, MainActivity.USER_DATA);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		NASWall.embedOnResume();
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == NASWall.ACTIVITY_RESULT_IMAGE_PICKER) {
			NASWall.embedOnActivityResult(this, requestCode, resultCode, data);
		}
	}
}
