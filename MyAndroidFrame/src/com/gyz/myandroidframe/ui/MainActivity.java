package com.gyz.myandroidframe.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.httpdata.HttpHandler;
import com.gyz.myandroidframe.ui.BaseActivity.TitleOnClick;
import com.gyz.myandroidframe.util.DownloadUtil;

public class MainActivity extends BaseActivity implements TitleOnClick {

	Handler mHandler = new HttpHandler(this) {

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setBaseContentView(R.layout.activity_main);

	}

	// 初始化UI
	protected void init_view() {
		isShowLeftMenu(true);
		setLeftBtnImage(R.drawable.back_btn);
		isShowRightMenu(true);
		setRightBtnImage(R.drawable.setting_btn);
		setTitleOnClick(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_left_view:
			
			break;
		case R.id.title_right_view:

			break;
		}
	}

}
