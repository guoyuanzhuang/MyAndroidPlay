package com.gyz.myandroidframe.util;

import java.io.File;

import android.os.Environment;

/**
 * 文件处理工具类
 * 
 */
public class FileUtils {

	/**
	 * 判断当前时候挂载存储卡
	 * 
	 * @return
	 */
	public static boolean hasStorageCard() {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static boolean createDir(String dir) {
		try {
			File f = new File(dir);
			if (!f.exists()) {
				return f.mkdirs();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	/**
	 * 创建文件
	 * @param filePath
	 * @return
	 */
	public File createFile(String filePath) {
		try {
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			return file;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static void delFile(String filePath) {
		try {
			File f = new File(filePath);
			if (f.isFile()) {
				f.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
