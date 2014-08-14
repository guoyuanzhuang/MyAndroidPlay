package com.gyz.myandroidframe.ui;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.adapter.MainNewsAdapter;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.bean.TestItem;
import com.gyz.myandroidframe.httprequest.TestItemRequest;
import com.gyz.myandroidframe.httprequest.base.HttpHandler;
import com.gyz.myandroidframe.ui.BaseActivity.TitleOnClick;

public class MainActivity extends BaseActivity implements TitleOnClick {
	final String tag = this.getClass().getName();
	ListView main_news_lv;
	//
	List<TestItem> rssList;
	MainNewsAdapter newsAdapter;
	
	Handler mHandler = new HttpHandler(this) {

		@Override
		protected void httpSucced(Serializable serObject) {
			// TODO Auto-generated method stub
			super.httpSucced(serObject);
			rssList = (List<TestItem>)serObject;
			if(rssList != null && rssList.size() > 0){
				newsAdapter.setRSSList(rssList);
				newsAdapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void httpFailed(Exception exception) {
			// TODO Auto-generated method stub
			super.httpFailed(exception);
			AppLog.e(tag, exception.getMessage());
//			if (exception instanceof NetworkErrorException) {
//				
//			}else{
//				
//			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.main_layout);
		init_view();
	}

	// 初始化UI
	protected void init_view() {
		isShowLeftMenu(true);
		setLeftBtnImage(R.drawable.back_btn);
		isShowRightMenu(true);
		setRightBtnImage(R.drawable.warning_btn);
		setTitleOnClick(this);
		main_news_lv = (ListView)findViewById(R.id.main_news_lv);
		newsAdapter = new MainNewsAdapter(this);
		main_news_lv.setAdapter(newsAdapter);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
			TestItemRequest rssaaRequest = new TestItemRequest(this, mHandler);
//			rssaaRequest.setCacheEnable(true);
			rssaaRequest.create();
			break;
		case R.id.title_right_view:
			AppLog.i(tag, "title_right_view");
			
			break;
		}
	}
}
