package com.nextapps.naswallsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AdListFooter extends RelativeLayout {
	
	public AdListFooter(Context context) {
		this(context, null);
	}
	public AdListFooter(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public AdListFooter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setBitmap(Bitmap bitmap) {
		ImageView v = (ImageView)findViewById(R.id.footer_image_view);
		v.setImageBitmap(bitmap);
	}
}
