package com.gyz.myandroidframe.app;

/**
 * 应用程序配置初始化:用于保存程序相关信息和设置
 * 
 * @author guoyuanzhuang
 * 
 */
public class AppConfig {
	public static AppConfig mAppConfig;

	public static AppConfig getInstance() {
		if (mAppConfig == null) {
			mAppConfig = new AppConfig();
		}
		return mAppConfig;
	}
	
	
}
