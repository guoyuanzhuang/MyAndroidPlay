package com.gyz.myandroidframe.ui;

import com.gyz.myandroidframe.app.AppManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
/**
 * 所有  Activity 基类
 * @author Administrator
 *
 */
public class AbsBaseActivity extends Activity{
	public final String tag = this.getClass().getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(tag, "---------onCreate---------");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(tag, "---------onDestroy---------");
		AppManager.getAppManager().finishActivity();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
}
