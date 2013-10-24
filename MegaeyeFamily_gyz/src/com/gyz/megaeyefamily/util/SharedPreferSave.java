package com.gyz.megaeyefamily.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gyz.megaeyefamily.bean.SettingInfo;

/**
 * 保存一些基本信息
 * 
 * @author guoyuanzhuang
 * 
 */
public class SharedPreferSave {
	public static final String PREFS_NAME = "settingdata";

	public static void setProperties(Context context, SettingInfo settingBean) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString("username", settingBean.username);
		editor.putString("password", settingBean.password);
		editor.putBoolean("ischecked", settingBean.isChecked);
		editor.putBoolean("ispassword", settingBean.isPassword);
		editor.commit();
	}

	public static SettingInfo getProperties(Context context) {
		SettingInfo settingBean = new SettingInfo();
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		settingBean.username = settings.getString("username", null);
		settingBean.password = settings.getString("password", null);
		settingBean.isChecked = settings.getBoolean("ischecked", false);
		settingBean.isPassword = settings.getBoolean("ispassword", false);
		return settingBean;
	}

	public static void delProperties(Context context) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		settings.edit().clear().commit();
	}
}
