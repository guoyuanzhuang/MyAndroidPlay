package com.gyz.myandroidframe.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.app.AppManager;
import com.gyz.myandroidframe.dialog.LoadingDialog;
import com.gyz.myandroidframe.httpdata.HttpConnectUtils;
import com.gyz.myandroidframe.httpdata.HttpHandler;
import com.gyz.myandroidframe.ui.BaseActivity.TitleOnClick;

public class MainActivity extends BaseActivity implements TitleOnClick {
	final String tag = this.getClass().getName();
	// ProgressDialog loadingDialog = null;

	Handler mHandler = new HttpHandler(this) {

		@Override
		protected void httpSucced(JSONObject jObject) {
			// TODO Auto-generated method stub
			super.httpSucced(jObject);
		}

		@Override
		protected void httpFailed(JSONObject jObject) {
			// TODO Auto-generated method stub
			super.httpFailed(jObject);
		}

		@Override
		protected void otherHandleMessage(Message message) {
			// TODO Auto-generated method stub
			super.otherHandleMessage(message);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setBaseContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(this);
		init_view();
	}

	// 初始化UI
	protected void init_view() {
		isShowLeftMenu(true);
		setLeftBtnImage(R.drawable.back_btn);
		// setLeftBtnText("左边");
		isShowRightMenu(true);
		setRightBtnImage(R.drawable.warning_btn);
		// setRightBtnText("右边");
		setTitleOnClick(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		HttpConnectUtils httpConnect = new HttpConnectUtils(this, mHandler);
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("", ""));
//		httpConnect.create(HttpConnectUtils.DOGET, url, null);
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
			AppLog.i(tag, "title_left_view");
			break;
		case R.id.title_right_view:
			AppLog.i(tag, "title_right_view");
			break;
		}
	}

}
