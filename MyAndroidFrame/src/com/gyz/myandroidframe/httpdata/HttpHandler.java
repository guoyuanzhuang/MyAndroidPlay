package com.gyz.myandroidframe.httpdata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.bean.BaseEntity;

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

	protected void httpSucced(JSONObject jObject) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected void httpFailed(JSONObject jObject) {
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
			String response = (String) message.obj;
			AppLog.e(tag, "http connection return." + response);
			//服务器返回数据错误码拦截
			try {
				InputStream is = new ByteArrayInputStream(response.getBytes());
				XmlPullParser xmlParser = Xml.newPullParser();
				xmlParser.setInput(is, BaseEntity.UTF8);
				String tag = xmlParser.getName();
				if (tag.equalsIgnoreCase("returnCode")) {
					int reponseCode = Integer.parseInt(xmlParser.nextText());
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case HTTP_ERROR: // connection error
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Exception e = (Exception) message.obj;
			e.printStackTrace();
			AppLog.e(tag, "http error:" + e.getMessage());
			AppException appException = AppException.network(e);
			Toast.makeText(context, appException.getMessage(),
					Toast.LENGTH_LONG).show();
			break;
		}
		otherHandleMessage(message);
	}

	/**
	 * try { JSONObject jObject = new JSONObject(response == null ? "" :
	 * response.trim()); if ("true".equals(jObject.getString("success"))) { //
	 * operate // success Toast.makeText(context, "operate succeed:" +
	 * jObject.getString("msg"), Toast.LENGTH_SHORT).show();
	 * httpSucced(jObject); } else { Toast.makeText(context, "operate fialed:" +
	 * jObject.getString("msg"), Toast.LENGTH_LONG).show(); httpFailed(jObject);
	 * } } catch (JSONException e1) { if (progressDialog != null &&
	 * progressDialog.isShowing()) { progressDialog.dismiss(); }
	 * e1.printStackTrace(); Toast.makeText(context,
	 * "Response data is not json data", Toast.LENGTH_LONG).show(); }
	 */
}
