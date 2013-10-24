package com.gyz.megaeyefamily.util;

import java.io.File;

import android.app.Activity;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SystemInfUtil {

	private static String LOGGER;

	/**
	 * 判断是否存在sd卡
	 * 
	 * @return
	 */
	public static boolean isSDExist() {
		String storageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(storageState)) {
			Log.d(LOGGER, "SDcard is exist!");
			return true;
		} else {
			Log.d(LOGGER, "No SDcard!");
			return false;
		}
	}

	/**
	 * 获取手机分辨率
	 * 
	 * @param activity
	 * @return
	 */
	public static int[] getSystemResolution(Activity activity) {
		int[] resolution = new int[2];
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		resolution[0] = width;
		resolution[1] = height;
		return resolution;
	}
	
	/**
	 * 获取手机sd卡的剩余空间
	 * 
	 * @return
	 */
	public static long getFreeStorage() {
		long available = 0;
		/* 判断存储卡是否插入 */
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			/* 取得SD CARD文件路径一般是/sdcard */
			File path = Environment.getExternalStorageDirectory();

			/* StatFs看文件系统空间使用状况 */
			StatFs statFs = new StatFs(path.getPath());
			/* Block的size */
			long blockSize = statFs.getBlockSize();
			/* 总Block数量 */
			//long totalBlocks = statFs.getBlockCount();
			/* 已使用的Block数量 */
			long availableBlocks = statFs.getAvailableBlocks();

			// String[] total = fileSize(totalBlocks * blockSize);
			available = availableBlocks * blockSize;

		} else if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_REMOVED)) {
			available = 0;
		}
		return available;
	}
}
