package com.gyz.megaeyefamily.app;

import com.gyz.megaeyefamily.ui.R;
import com.gyz.megaeyefamily.ui.R.drawable;

import android.app.Application;

public class MegaeyeFamilyApplication extends Application {
	public static final int[] weathers_png = { R.drawable.a0, R.drawable.a1,
			R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5,
			R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9,
			R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13,
			R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17,
			R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21,
			R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25,
			R.drawable.a26, R.drawable.a27, R.drawable.a28, R.drawable.a29,
			R.drawable.a30, R.drawable.a31, R.drawable.nothing };// 天气预报图片
	// 视频播放错误码
	public static final int ERROR01 = 0;
	public static final int ERROR02 = 1;
	public static final int ERROR03 = 2;
	public static final int ERROR04 = 3;
	public static final int ERROR05 = 4;
	public static final int ERROR06 = 5;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
}
