package com.gyz.megaeyefamily.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FileUtils {
	private final static String TAG = "FileUtils";
	public static String SDPATH = Environment.getExternalStorageDirectory()
			.getPath() + "/";

	/**
	 * SD卡上创建文件
	 * 
	 * @param FileName
	 *            文件名称
	 * @return 该文件
	 */
	public static File createSDFile(String FileName) {
		File file = new File(SDPATH + FileName);
		try {
			if (isSDExist()) {
				if (!file.exists()) {
					file.createNewFile();
				}
			} else {
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * SD卡上创建目录 //是否存在//是否为目录
	 * 
	 * @param dirName
	 *            文件目录
	 * @return 该目录 && dir.isDirectory()
	 */
	public static boolean createSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		if (isSDExist()) {
			if (!dir.exists()) {
				return dir.mkdirs();
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 检查sd卡是否存在
	 * 
	 * @return true:存在SD卡、false：不存在SD卡
	 */
	public static boolean isSDExist() {
		String storageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(storageState)) {
			Log.e(TAG, "SDcard is exist!");
			return true;
		}
		return false;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 *            文件名称
	 */
	public static void deleteFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 文件下载 new
	 * 
	 * @param url
	 *            下载地址
	 * @param dir
	 *            目录
	 * @param filename
	 *            文件名
	 * @param handler
	 * @throws IOException
	 */
	public static int flag = 0; // 结束标志

	public static boolean download_file(String url, File saveFile,
			Handler handler) throws IOException {
		flag = 0;
		long fileSize = 0; // 文件大小
		long downLoadFileSize = 0; // 每次读取后的大小
		int numread = 0; // 每次读取的大小
		int result = 0; // 返回的%
		InputStream is = null;
		FileOutputStream fos = null;
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		is = conn.getInputStream();
		fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		fos = new FileOutputStream(saveFile);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
		do {
			if (flag == 0) {
				// 循环读取
				numread = is.read(buf);
				if (numread == -1) {
					break;
				}
				fos.write(buf, 0, numread);
				downLoadFileSize += numread;
				long temp = downLoadFileSize * 100 / fileSize;
				result = (int)temp;
				Message msg = new Message();
				msg.what = 100;
				msg.arg1 = result; // 赋值更新进度条
				handler.sendMessage(msg);
			} else {
				break;
			}
		} while (true);
		try {
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}
		return saveFile.length() == fileSize;
	}

	/**
	 * 下载文件
	 * 
	 * @param downloadUrl
	 *            下载地址
	 * @param saveFile
	 *            保存的文件
	 * @param handler
	 * @param isdownload_state
	 *            下载状态
	 * @return 文件大小
	 * @throws Exception
	 */
	public static long downloadFile(String downloadUrl, File saveFile,
			Handler handler, boolean isdownload_state) throws Exception {
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 0;
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();// 总大小
			// Log.e("download", "updateTotalSize=" + updateTotalSize);
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("conection net 404！");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile);
			byte[] buf = new byte[10240];
			int readSize = -1;
			while ((readSize = is.read(buf)) != -1 && isdownload_state == true) {
				fos.write(buf, 0, readSize);
				// 通知更新进度
				totalSize += readSize;
				int tmp = (int) (totalSize * 100 / updateTotalSize);
				// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
				if (downloadCount == 0 || tmp - 10 > downloadCount) {
					downloadCount += 10;
					Message msg = new Message();
					msg.what = 100;
					msg.arg1 = downloadCount;
					handler.sendMessage(msg);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}
}
