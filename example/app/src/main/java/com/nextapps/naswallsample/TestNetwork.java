/*
* Copyright NextApps., All rights reserved.
*
* This software is the confidential and proprietary information of NextApps. ("Confidential Information").
*/
package com.nextapps.naswallsample;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

final class TestNetwork {
	///
	/// Enum
	///
	
	enum MODE {
		HTML,
		DATA
	}
	
	///
	/// Variables
	///
	MODE _mode;
	Object _tag;
	OnNetworkListener _onNetworkListener;
	String _html;
	ByteArrayOutputStream _data;
	
	Object getTag() { return _tag; }
	void setTag(Object tag) { _tag = tag; }
	
	String getHtml() { return _html; }
	ByteArrayOutputStream getData() { return _data; }
	
	///
	/// Method
	///
	void htmlFromURL(String url, OnNetworkListener l) {
		_onNetworkListener = l;
		_mode = MODE.HTML;
		_html = "";

		NetworkTask networkTask = new NetworkTask();
		networkTask.execute(url);
	}
	
	void dataFromURL(String url, OnNetworkListener l) {
		_onNetworkListener = l;
		_mode = MODE.DATA;
		_data = null;
		
		NetworkTask networkTask = new NetworkTask();
		networkTask.execute(url);
	}
	
	///
	/// AsyncTask
	///
	private class NetworkTask extends AsyncTask<String, Boolean, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				try {
					URL url = new URL(params[0]);
					HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
					urlConn.setAllowUserInteraction(false);
					urlConn.connect();
					BufferedInputStream bis = new BufferedInputStream(urlConn.getInputStream());

					ByteArrayOutputStream baf = new ByteArrayOutputStream();
					int read = 0;
					int bufSize = 512;
					byte[] buffer = new byte[bufSize];
					while(true){
					     read = bis.read(buffer);
					     if(read==-1){
					          break;
					     }
					     baf.write(buffer, 0, read);
//					     baf.append(buffer, 0, read);
					}
				
					if (_mode == MODE.HTML)
						TestNetwork.this._html = new String(baf.toByteArray(), "UTF-8");
					else if (_mode == MODE.DATA)
						TestNetwork.this._data = baf;

					publishProgress(true);
				} catch (Exception e) {
				    publishProgress(false);
				}
			} catch (Exception e) {
				publishProgress(false);
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Boolean... progress) {
			if (progress[0]) {
				if (_onNetworkListener != null)
					_onNetworkListener.OnSuccess(TestNetwork.this);
			} else {
				if (_onNetworkListener != null)
					_onNetworkListener.OnError(TestNetwork.this);
			}
		}
	}
	
	///
	/// Listener
	///
	abstract static interface OnNetworkListener {
		public abstract void OnSuccess(TestNetwork sender);
		public abstract void OnError(TestNetwork sender);
	}
}
