package com.gyz.myandroidframe.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * 应用程序配置初始化:用于保存程序相关信息和设置
 * 
 * @author guoyuanzhuang
 * 
 */
public class AppConfig {
	public static final String DIRNAME = "MyAndroidFrame";
	public static final String DOWNLOAD_DIRNAME = "download";
	public static final String APPINFO_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/" + DIRNAME; // 应用信息SD卡根路径
	public static final String DOWNLOAD_PATH = APPINFO_PATH + "/" + DOWNLOAD_DIRNAME; // 下载保存目录(绝对路径)
	public static final String DOWNLOAD_PATH_RE =  DIRNAME + "/" + DOWNLOAD_DIRNAME;//下载相对目录
	//
	private static final String CONFIG_NAME = "MyAndroidFrame_Config";
	public static AppConfig mAppConfig;

	public static AppConfig getInstance() {
		if (mAppConfig == null) {
			mAppConfig = new AppConfig();
		}
		return mAppConfig;
	}

	/**
	 * put SharedPreferences String
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putString(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get String def null
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		return getString(context, key, null);
	}

	/**
	 * get String def defaultValue
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}

	/**
	 * put SharedPreferences Int
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putInt(Context context, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	/**
	 * get int def -1
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getInt(Context context, String key) {
		return getInt(context, key, -1);
	}

	/**
	 * get int def defaultValue
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * put SharedPreferences long
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putLong(Context context, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	/**
	 * get Long def -1
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static long getLong(Context context, String key) {
		return getLong(context, key, -1);
	}

	/**
	 * get Long def defaultValue
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * put SharedPreferences Float
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putFloat(Context context, String key, float value) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	/**
	 * get Float def -1
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static float getFloat(Context context, String key) {
		return getFloat(context, key, -1);
	}

	/**
	 * get Float def defaultValue
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * put SharedPreferences Boolean
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get Boolean def false
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	/**
	 * get Boolean def defaultValue
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(CONFIG_NAME,
				Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);
	}

}
