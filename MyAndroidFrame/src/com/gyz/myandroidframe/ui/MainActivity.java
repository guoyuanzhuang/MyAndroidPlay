package com.gyz.myandroidframe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.httpclient.HttpHandler;

public class MainActivity extends Activity {

	Handler mHandler = new HttpHandler(this) {

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		this.getFileStreamPath(name)
//		this.open
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
