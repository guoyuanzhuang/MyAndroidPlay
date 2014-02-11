package com.gyz.myandroidframe.util;

import java.io.File;
import java.net.URI;

import com.gyz.myandroidframe.app.AppConfig;
import com.gyz.myandroidframe.app.AppLog;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

/**
 * 应用程序下载工具类(调用系统下载器)
 * 
 * @author guoyuanzhuang
 * {@code mDownload = new DownloadUtil(this);
		  mDownload.startDownload("美丽不说", "http://www.kuaidadi.com/resources/apk/kuaidisiji_2.1_24635.apk");}
 * 
 */
public class DownloadUtil {
	private Context mContext;
	private static DownloadUtil mDownloadUtil;
	private CompleteReceiver completeReceiver;
	protected DownloadManager downloadManager;

	// protected long downloadId;

	public static DownloadUtil getDownloadUtil(Context context) {
		if (mDownloadUtil == null) {
			mDownloadUtil = new DownloadUtil(context);
		}
		return mDownloadUtil;
	}

	public DownloadUtil(Context context) {
		this.mContext = context;
		downloadManager = (DownloadManager) context
				.getSystemService(context.DOWNLOAD_SERVICE);
		/** register download success broadcast **/
		completeReceiver = new CompleteReceiver();
		context.registerReceiver(completeReceiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	/**
	 * 处理下载完成
	 * 
	 * @author guoyuanzhuang
	 * 
	 */
	class CompleteReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			long completeDownloadId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			int downloadStatus = queryDownloadStatus(completeDownloadId);
			if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
				String filePath = queryDownloadPath(completeDownloadId);
				installAPK(context, filePath);
			}
		}
	};

	/**
	 * 开启下载任务
	 * 
	 * @param title
	 *            任务标题
	 * @param url
	 *            下载链接
	 * @param fileDir
	 *            保存目录
	 * @param fileName
	 *            保存文件名称
	 */
	public long startDownload(String title, String url, String fileDir,
			String fileName) {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));
		request.setDestinationInExternalPublicDir(fileDir, fileName);
		request.setTitle(title);// "美丽说"
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(false);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setMimeType("application/cn.trinea.download.file");
		return downloadManager.enqueue(request);
	}

	/**
	 * 开始下载任务(default)
	 * 
	 * @param title
	 * @param url
	 * @return
	 * 
	 * 
	 */
	public long startDownload(String title, String url) {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));
		boolean isDir = FileUtils.createDir(AppConfig.DOWNLOAD_PATH);// 创建默认目录
		if (!isDir) {
			return -1;
		}
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		AppLog.e("tag", fileName);
		request.setDestinationInExternalPublicDir(AppConfig.DOWNLOAD_PATH_RE, fileName);
		request.setTitle(title);// "美丽说"
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//完成通知
		request.setVisibleInDownloadsUi(false);			//下载页面
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setMimeType("application/cn.trinea.download.file");
		return downloadManager.enqueue(request);
	}

	/**
	 * 根据downloadid查询当前下载状态
	 * 
	 * @param downloadId
	 * @return
	 */
	public int queryDownloadStatus(long downloadId) {
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		int result = -1;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getInt(c
						.getColumnIndex(DownloadManager.COLUMN_STATUS));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}

	/**
	 * 根据download获取文件路径
	 * 
	 * @param downloadId
	 * @return
	 */
	public String queryDownloadPath(long downloadId) {
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		String path = null;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				String url = c.getString(c
						.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
				URI uri = new URI(url);
				path = uri.getPath();
			}
		} catch (Exception e) {

		} finally {
			if (c != null) {
				c.close();
			}
		}
		return path;
	}

	/**
	 * 安装apk文件
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public boolean installAPK(Context context, String filePath) {
		try {
			// 检测apk合法性
			PackageInfo plocalObject = mContext.getPackageManager()
					.getPackageArchiveInfo(filePath,
							PackageManager.GET_ACTIVITIES);
			if (plocalObject.activities.length > 0) {
				// install
				Intent i = new Intent(Intent.ACTION_VIEW);
				File file = new File(filePath);
				if (file != null && file.length() > 0 && file.exists()
						&& file.isFile()) {
					i.setDataAndType(Uri.parse("file://" + filePath),
							"application/vnd.android.package-archive");
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 注销广播
	 */
	public void destroyBrodcast() {
		if (completeReceiver != null) {
			mContext.unregisterReceiver(completeReceiver);
			completeReceiver = null;
		}
		downloadManager = null;
		mContext = null;
	}

}
