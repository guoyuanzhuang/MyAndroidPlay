package com.gyz.myandroidframe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyz.myandroidframe.R;

/**
 * activity 基类
 * 
 * @author guoyuanzhuang
 * 
 */
public class BaseActivity extends Activity {
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
	// listener
	protected TitleOnClick mTitleOnClick;
	//click interface
	public interface TitleOnClick {
		void onClick(View v);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.e(tag, "---------onCreate---------");
		setContentView(R.layout.common_title_layout);
	}

	// 初始化View
	public void setBaseContentView(int contentId) {
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
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mTitleOnClick != null)
				mTitleOnClick.onClick(v);
		}
	};

	//btn click 
	public void setTitleOnClick(TitleOnClick titleOnClick) {
		this.mTitleOnClick = titleOnClick;
	}
	//left btn text
	public void setLeftBtnText(String text){
		this.title_left_tv.setText(text);
	}
	//left btn img
	public void setLeftBtnImage(int resId){
		this.title_left_img.setImageResource(resId);
	}
	//right btn text
	public void setRightBtnText(String text){
		this.title_right_tv.setText(text);
	}
	//right btn img
	public void setRightBtnImage(int resId){
		this.title_right_img.setImageResource(resId);
	}
	//center textView text
	public void setCenterText(String text){
		this.title_center_tv.setText(text);
	}
}
