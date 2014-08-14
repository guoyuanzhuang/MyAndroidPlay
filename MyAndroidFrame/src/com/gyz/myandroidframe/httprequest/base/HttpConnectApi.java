package com.gyz.myandroidframe.httprequest.base;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.util.FileUtils;

public class HttpConnectApi {
	private static String tag = "HttpConnectApi";
	private static String CHARSET_UTF8 = HTTP.UTF_8;
	private static final int MAXLOGSIZE = 1000;  
	private static int TIMEOUT_CONNECTION = 10000; // 连接超时
	private static int TIMEOUT_SOCKET = 10000; // 返回超时
	private static HttpClient customerHttpClient;

	/**
	 * Http DoGet request
	 */
	public static InputStream httpRequestDoGet(String httpUrl) throws Exception {
		AppLog.i(tag, "request type：DOGET");
		AppLog.i(tag, "request URL：" + httpUrl);
		HttpGet httpRequest = new HttpGet(httpUrl);
		HttpClient httpclient = getHttpClient();
		HttpResponse httpResponse = httpclient.execute(httpRequest);
		// 请求成功
		int doGetCode = httpResponse.getStatusLine().getStatusCode();
		if (doGetCode != HttpStatus.SC_OK) {
			throw new HttpException();
		}
		HttpEntity resEntity = httpResponse.getEntity();
		InputStream responseResults = resEntity.getContent();
		httpclient.getConnectionManager().shutdown();
		return responseResults;
	}

	/**
	 * Http DoPost Request / commit String or file
	 */
	public static InputStream httpRequestDoPost(String httpUrl,
			HashMap<String, String> contentMap,
			HashMap<String, String> imagePathMap,
			HashMap<String, String> audioPathMap) throws Exception {
		AppLog.i(tag, "request type：DOPOST");
		AppLog.i(tag, "request url: " + httpUrl);
		StringBuilder postContent = new StringBuilder();
		final MultipartEntity multipartEntity = new MultipartEntity();
		/**** DoPost Commit Text *****/
		if (contentMap != null && contentMap.size() > 0) {
			Iterator<Entry<String, String>> iter = contentMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				postContent.append(String.format("[%s=%s]", key, value));
				multipartEntity.addPart(key,
						new StringBody(value, Charset.forName(CHARSET_UTF8)));
			}
		}
		/**** DoPost Commit image *****/
		if (imagePathMap != null && imagePathMap.size() > 0) {
			Iterator<Entry<String, String>> iter = contentMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				postContent.append(String.format("[%s=%s]", key, value));
				multipartEntity.addPart(key, new FileBody(new File(value),
						"image/*"));
			}
		}
		/**** DoPost Commit audio *****/
		if (audioPathMap != null && audioPathMap.size() > 0) {
			Iterator<Entry<String, String>> iter = contentMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				postContent.append(String.format("[%s=%s]", key, value));
				multipartEntity.addPart(key, new FileBody(new File(value),
						"audio/*"));
			}
		}
		AppLog.i(tag, "request content: " + postContent.toString());

		HttpPost httpPost = new HttpPost(httpUrl);
		httpPost.setEntity(multipartEntity);
		HttpClient client = getHttpClient();
		HttpResponse httpResponse = client.execute(httpPost);
		// 请求成功
		int doGetCode = httpResponse.getStatusLine().getStatusCode();
		if (doGetCode != HttpStatus.SC_OK) {
			throw new HttpException();
		}
		HttpEntity resEntity = httpResponse.getEntity();
		InputStream responseResults = resEntity.getContent();
		client.getConnectionManager().shutdown();
		return responseResults;
	}

	/**
	 * Http DoBitmap Request
	 */
	public static Bitmap httpRequestDoBitmap(String httpUrl) throws Exception {
		AppLog.i(tag, "request type：DOBITMAP");
		AppLog.i(tag, "request URL：" + httpUrl);
		Bitmap bitmap = null;
		HttpGet httpBitmap = new HttpGet(httpUrl);
		HttpClient clientBitmap = getHttpClient();
		HttpResponse responseBitmap = clientBitmap.execute(httpBitmap);
		BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
				responseBitmap.getEntity());
		try {
			bitmap = BitmapFactory.decodeStream(bufHttpEntity.getContent());
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			AppLog.e(tag, "Bitmap OutOfMemoryError");
		}
		
		clientBitmap.getConnectionManager().shutdown();
		return bitmap;
		// 图片缓存需要单独处理:1.需要载体VIEW；2.缓存策略(特殊性)
	}

	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	private static HttpClient getHttpClient() {
		if (customerHttpClient == null) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			String userAgent = getUserAgent();
			HttpProtocolParams.setUserAgent(params, userAgent);
			AppLog.i(tag, "setUserAgent : " + userAgent);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params,
					TIMEOUT_CONNECTION);
			/* 读取超时 */
			HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			return new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	/**
	 * 设置用户代理信息
	 * 
	 * @param mContext
	 * @return
	 */
	private static String getUserAgent() {
		StringBuilder ua = new StringBuilder("MyAndroidFrame");
		// ua.append('/' + info.versionName + '_' + info.versionCode);// App版本
		ua.append("/Android");// 手机系统平台
		ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
		ua.append("/" + android.os.Build.MODEL); // 手机型号
		return ua.toString();
	}
	
	/**
	 * 网络日志分段打印
	 * @param tempStr
	 */
	public static void printLog(String tempStr){
		for(int i = 0; i <= tempStr.length() / MAXLOGSIZE; i++) {      
			int start = i * MAXLOGSIZE;         
			int end = (i+1) * MAXLOGSIZE;        
			end = end > tempStr.length() ? tempStr.length() : end;     
			AppLog.i(tag, tempStr.substring(start, end));
		}
	}
}
