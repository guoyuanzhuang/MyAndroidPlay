package com.gyz.myandroidframe.widgets;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
/**
 * Toast 自定义
 * @author guoyuanzhuang@gmail.com
 *
 */
public class ToastView extends Toast{

	public ToastView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		super.setView(view);
	}

	@Override
	public void setGravity(int gravity, int xOffset, int yOffset) {
		// TODO Auto-generated method stub
		super.setGravity(gravity, xOffset, yOffset);
	}

	@Override
	public void setText(int resId) {
		// TODO Auto-generated method stub
		super.setText(resId);
	}

	@Override
	public void setText(CharSequence s) {
		// TODO Auto-generated method stub
		super.setText(s);
	}
	
	
}
