package com.gyz.myandroidframe.app;

import android.util.Log;

/**
 * 应用程序Log管理 :用于控制程序Log输出
 * 
 * @author guoyuanzhuang
 * 
 * @thinking 1.标记日志的管理者；2.精确定位日志(所属类、方法、行)；3.标记日志所属AP；4.是否有必要进行文件管理
 * 
 */
public class AppLog {
	// i d e v w
	// 日志开关
	private final static boolean isToast = true;

	public static void i(String tag, String msg) {
		if (isToast) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isToast) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isToast) {
			Log.e(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (isToast) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (isToast) {
			Log.w(tag, msg);
		}
	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return "[" + Thread.currentThread().getId() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + "]";
		}
		return null;
	}
}
