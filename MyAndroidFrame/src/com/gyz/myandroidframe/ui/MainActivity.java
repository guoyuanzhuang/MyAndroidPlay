package com.gyz.myandroidframe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.httpdata.HttpHandler;
import com.gyz.myandroidframe.util.DownloadUtil;

public class MainActivity extends Activity {
	DownloadUtil mDownload;

	Handler mHandler = new HttpHandler(this) {

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDownload = new DownloadUtil(this);
		mDownload
				.startDownload("美丽不说",
						"http://www.kuaidadi.com/resources/apk/kuaidisiji_2.1_24635.apk");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDownload.destroyBrodcast();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
