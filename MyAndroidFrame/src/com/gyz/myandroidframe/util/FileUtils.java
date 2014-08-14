package com.gyz.myandroidframe.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	 * 
	 * @param filePath
	 * @return
	 */
	public File createFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
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

	/**
	 * 将InputStream转换成String
	 * 
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String InputStreamTOString(InputStream in) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();

		/*
		 * try { int BUFFER_SIZE = 2048; ByteArrayOutputStream outStream = new
		 * ByteArrayOutputStream(); byte[] data = new byte[BUFFER_SIZE]; int
		 * count = -1; while((count = in.read(data,0,BUFFER_SIZE)) != -1)
		 * outStream.write(data, 0, count); data = null; return new
		 * String(outStream.toByteArray(),"utf-8"); } catch (Exception e) { //
		 * TODO: handle exception }
		 */
	}
}
