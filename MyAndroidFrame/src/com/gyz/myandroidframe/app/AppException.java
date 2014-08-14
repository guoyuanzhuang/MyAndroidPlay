package com.gyz.myandroidframe.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;

import org.apache.http.HttpException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.gyz.myandroidframe.R;

/**
 * 
 * @ClassName AppException 
 * @Description 应用程序异常类:用于处理程序异常
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:34:34 
 *
 */
public class AppException extends Exception implements UncaughtExceptionHandler {

	private final static boolean Debug = false;// 是否保存错误日志

	/** 定义异常类型 */
	public final static byte TYPE_HTTP_UNCONNECT = 0x01;//无网络
	public final static byte TYPE_HTTP_ERROR = 0x04;	//网络异常
	public final static byte TYPE_XML_ERROR = 0x05;			//xml解析错误

	private byte type;
	private int code;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private AppException() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		if (Debug) {
			this.saveErrorLog(excp);
		}
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}
	
	/**
	 * 获取APP异常崩溃处理对象
	 * 
	 * @param context
	 * @return
	 */
	public static AppException getAppExceptionHandler() {
		return new AppException();
	}

	/**
	 * 捕捉网络异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException network(Exception e) {
		if (e instanceof NetworkErrorException) {			//无网络链接
			return new AppException(TYPE_HTTP_UNCONNECT, 0, e);
		}else{			//网络异常
			return new AppException(TYPE_HTTP_ERROR, 0, e);
		}
	}
	
	/**
	 * xml解析异常
	 * 
	 * @param e
	 * @return
	 */
	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML_ERROR, 0, e);
	}

	/**
	 * 提示友好的错误信息
	 * 
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		switch (this.getType()) {
		case TYPE_HTTP_ERROR:
			Toast.makeText(ctx, R.string.http_exception_error,
					Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_UNCONNECT:
			Toast.makeText(ctx, R.string.network_not_connected,
					Toast.LENGTH_SHORT).show();
			break;
		case TYPE_XML_ERROR:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	/**
	 * 保存异常日志
	 * 
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/OSChina/Log/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			// 没有挂载SD卡，无法写文件
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println("--------------------" + (new Date().toLocaleString())
					+ "---------------------");
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}


	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}

	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * 
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		final Context context = AppManager.getAppManager().currentActivity();

		if (context == null) {
			return false;
		}

		final String crashReport = getCrashReport(context, ex);
		// 显示异常信息&发送报告
		new Thread() {
			public void run() {
				Looper.prepare();
				UIHelper.sendAppCrashReport(context, crashReport);
				Looper.loop();
			}

		}.start();
		return true;
	}

	/**
	 * 获取APP崩溃异常报告
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((AppContext) context.getApplicationContext())
				.getPackageInfo();
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "("
				+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
				+ "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}
}
