package com.gyz.myandroidframe.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @ClassName AppContext 
 * @Description 应用程序入口:用于保存一些全局应用配置信息
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:33:30 
 *
 */
public class AppContext extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}
}
