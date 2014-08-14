package com.gyz.myandroidframe.httprequest.base;

import java.io.Serializable;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.gyz.myandroidframe.app.AppLog;

/**
 * http请求handler封装
 * 
 * @author guoyuanzhuang
 * 
 */
public class HttpHandler extends Handler {
	private final String tag = this.getClass().getName();

	public static final int HTTP_START = 100;
	public static final int HTTP_ERROR = 101;
	public static final int HTTP_SUCCESS = 102;

	private Context context;
	private ProgressDialog progressDialog;

	public HttpHandler(Context context) {
		this.context = context;
	}

	protected void httpStart() {
		progressDialog = ProgressDialog.show(context, "Please Wait...",
				"processing...", true);
	}

	protected void httpSucced(Serializable serObject) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected void httpFailed(Exception exception) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected void otherHandleMessage(Message message) {
	}

	public void handleMessage(Message message) {
		switch (message.what) {
		case HTTP_START: // connection start
			AppLog.e(tag, "http connection start...");
			httpStart();
			break;
		case HTTP_SUCCESS: // connection success
			progressDialog.dismiss();
			Serializable response = (Serializable) message.obj;
			httpSucced(response);
			break;
		case HTTP_ERROR: // connection error
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Exception e = (Exception) message.obj;
			AppLog.e(tag, "http error:" + e.getMessage());
			e.printStackTrace();
			//处理异常
			httpFailed(e);
			break;
		}
		otherHandleMessage(message);
	}

}
