package com.gyz.myandroidframe.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.httpdata.HttpHandler;
import com.gyz.myandroidframe.util.DownloadUtil;

public class MainActivity extends BaseActivity {
	DownloadUtil mDownload;

	Handler mHandler = new HttpHandler(this) {

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setBaseContentView(R.layout.activity_main);
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
