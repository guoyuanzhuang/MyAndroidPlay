package com.gyz.myandroidframe.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyz.myandroidframe.R;

/**
 * 有标题栏的 activity 基类
 * 
 * @author guoyuanzhuang
 * 
 */
public class BaseActivity extends AbsBaseActivity {
	public final String tag = this.getClass().getName();
	// 左边
	protected LinearLayout title_left_view;
	protected ImageView title_left_img;
	protected TextView title_left_tv;
	// 右边
	protected LinearLayout title_right_view;
	protected ImageView title_right_img;
	protected TextView title_right_tv;
	// 中间
	protected LinearLayout title_center_view;
	protected TextView title_center_tv;
	// 内容
	protected FrameLayout titile_content_view;
	//网络异常
	protected FrameLayout title_exception_view;
	protected View exceptionView;
	protected TextView networkexception_message_tv;
	protected Button networkexception_entry_btn;
	// listener
	protected TitleOnClick mTitleOnClick;

	// click interface
	public interface TitleOnClick {
		void onClick(View v);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_title_layout);
	}

	// 初始化View
	protected void setBaseContentView(int contentId) {
		// 左边
		title_left_view = (LinearLayout) findViewById(R.id.title_left_view);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_left_tv = (TextView) findViewById(R.id.title_left_tv);
		title_left_view.setOnClickListener(listener);
		// 右边
		title_right_view = (LinearLayout) findViewById(R.id.title_right_view);
		title_right_img = (ImageView) findViewById(R.id.title_right_img);
		title_right_tv = (TextView) findViewById(R.id.title_right_tv);
		title_right_view.setOnClickListener(listener);
		// 中间
		title_center_view = (LinearLayout) findViewById(R.id.title_center_view);
		title_center_tv = (TextView) findViewById(R.id.title_center_tv);
		title_center_tv.setText(R.string.app_name);
		// 内容
		titile_content_view = (FrameLayout) findViewById(R.id.titile_content_view);
		View contentView = getLayoutInflater().inflate(contentId, null);
		titile_content_view.addView(contentView);
		//网络异常
		title_exception_view = (FrameLayout)findViewById(R.id.title_exception_view);
		title_exception_view.setVisibility(View.GONE);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mTitleOnClick != null)
				mTitleOnClick.onClick(v);
		}
	};

	// btn click
	protected void setTitleOnClick(TitleOnClick titleOnClick) {
		this.mTitleOnClick = titleOnClick;
	}

	// left btn text
	protected void setLeftBtnText(String text) {
		this.title_left_tv.setText(text);
	}

	// left btn img
	protected void setLeftBtnImage(int resId) {
		this.title_left_img.setImageResource(resId);
	}

	// right btn text
	protected void setRightBtnText(String text) {
		this.title_right_tv.setText(text);
	}

	// right btn img
	protected void setRightBtnImage(int resId) {
		this.title_right_img.setImageResource(resId);
	}

	// center textView text
	protected void setCenterText(String text) {
		this.title_center_tv.setText(text);
	}

	// is show left menu
	protected void isShowLeftMenu(boolean isShow) {
		if (isShow)
			this.title_left_view.setVisibility(View.VISIBLE);
		else
			this.title_left_view.setVisibility(View.GONE);
	}

	// is show right menu
	protected void isShowRightMenu(boolean isShow) {
		if (isShow)
			this.title_right_view.setVisibility(View.VISIBLE);
		else
			this.title_right_view.setVisibility(View.GONE);
	}
	//show ExceptionView or show not Connection View
	protected void showNetExceptionView(boolean isException){
		title_exception_view.removeAllViews();
		exceptionView = getLayoutInflater().inflate(R.layout.common_networkexception_layout, null);
		networkexception_message_tv = (TextView)exceptionView.findViewById(R.id.networkexception_message_tv);
		networkexception_entry_btn = (Button)exceptionView.findViewById(R.id.networkexception_entry_btn);
		if(isException){
			networkexception_message_tv.setText(R.string.http_exception_error);
			networkexception_entry_btn.setText(R.string.networkexc_retry);
			networkexception_entry_btn.setOnClickListener(listener);
		}else{
			networkexception_message_tv.setText(R.string.network_not_connected);
			networkexception_entry_btn.setText(R.string.networkexc_setting);
			networkexception_entry_btn.setOnClickListener(listener);
		}
		title_exception_view.addView(exceptionView);
		title_exception_view.setVisibility(View.VISIBLE);
	}
	// hide exception views
	protected void hideNetExcptionView(){
		title_exception_view.removeAllViews();
		title_exception_view.setVisibility(View.GONE);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
