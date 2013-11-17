package com.gyz.myandroidframe.httpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;

/**
 * 封装HTTP请求接口
 * 
 * @author guoyuanzhuang
 * @date 2013-11-3
 * 
 */
@Deprecated
public class HttpRequest {
	private static String tag = "HttpRequest";
	private static final String CHARSET_UTF8 = HTTP.UTF_8;// 字符编码
	private static HttpClient customerHttpClient; // httpClient

	private final static int TIMEOUT_CONNECTION = 10000; // 连接超时
	private final static int TIMEOUT_SOCKET = 10000; // 返回超时

	private static String appUserAgent; // http头中用户基本信息

	public HttpRequest() {
	}

	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private synchronized static HttpClient getHttpClient(Context context) {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// HttpProtocolParams
			// .setUserAgent(
			// params, getUserAgent(appContext));
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
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	/**
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	@Deprecated
	public static String getHttpUrl(String url, NameValuePair... nameValuePairs) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if (nameValuePairs != null && nameValuePairs.length > 0) {
			sb.append("?");
			for (int i = 0; i < nameValuePairs.length; i++) {
				if (i > 0) {
					sb.append("&");
				}
				sb.append(String.format("%s=%s", nameValuePairs[i].getName(),
						nameValuePairs[i].getValue()));
			}
		}
		AppLog.e(tag, "DoGet request url:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @return
	 */
	@Deprecated
	public static String getHttpUrl(NameValuePair... nameValuePairs) {
		StringBuilder sb = new StringBuilder();
		if (nameValuePairs != null && nameValuePairs.length > 0) {
			for (int i = 0; i < nameValuePairs.length; i++) {
				sb.append(String.format("%s=%s", nameValuePairs[i].getName(),
						nameValuePairs[i].getValue()));
			}
		}
		AppLog.e(tag, "HttpUrlConnection DoPost request url:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * @param nameValuePairs
	 * @return
	 */
	@Deprecated
	public static List<NameValuePair> postHttpEntity(
			NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (nameValuePairs != null) {
			for (int i = 0; i < nameValuePairs.length; i++) {
				params.add(nameValuePairs[i]);
				AppLog.e(
						tag,
						"DoPost request params entity:"
								+ nameValuePairs[i].getName() + "="
								+ nameValuePairs[i].getValue());
			}
		}
		return params;
	}

	/**
	 * HttpClient post方法
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 * @throws AppException
	 */
	@Deprecated
	public static InputStream postHttpClientRequest(Context context,
			String url, NameValuePair... nameValuePairs) throws AppException {
		try {
			List<NameValuePair> params = postHttpEntity(nameValuePairs);
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,
					CHARSET_UTF8);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(urlEncoded);
			HttpClient client = getHttpClient(context);
			HttpResponse response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw AppException.http(statusCode);
			}
			HttpEntity resEntity = response.getEntity();
			AppLog.e(tag, "DoPost request service return results:"
					+ EntityUtils.toString(resEntity, CHARSET_UTF8));
			return (resEntity == null) ? null : resEntity.getContent();
		} catch (IOException e) {
			// 发生网络异常：1、无效网络连接；2、交互协议不正确；3、返回内容格式问题
			throw AppException.network(e);
		}
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param nameValuePairs
	 * @return
	 * @throws AppException
	 */
	@Deprecated
	public static InputStream getHttpClientRequest(Context context, String url,
			NameValuePair... nameValuePairs) throws AppException {
		try {
			String httpUrl = getHttpUrl(url, nameValuePairs);
			// HttpGet连接对象
			HttpGet httpRequest = new HttpGet(httpUrl);
			// 取得HttpClient对象
			HttpClient httpclient = getHttpClient(context);
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw AppException.http(statusCode);
			}
			HttpEntity resEntity = httpResponse.getEntity();
			AppLog.e(
					tag,
					"DoGet request service return results:"
							+ EntityUtils.toString(resEntity, CHARSET_UTF8));
			return (resEntity == null) ? null : resEntity.getContent();
			// return (resEntity == null) ? null : new ByteArrayInputStream(
			// EntityUtils.toByteArray(resEntity));
		} catch (IOException e) {
			// 发生网络异常：1、无效网络连接；2、交互协议不正确；3、返回内容格式问题
			throw AppException.network(e);
		}
	}

	@Deprecated
	public static InputStream getHttpUrlConnection(String strUrl,
			NameValuePair... nameValuePairs) throws AppException {
		InputStream resultStream = null;

		try {
			String httpUrl = getHttpUrl(strUrl, nameValuePairs);
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(TIMEOUT_CONNECTION);
			conn.setReadTimeout(TIMEOUT_SOCKET);
			conn.setRequestProperty("accept", "*/*");
			int resCode = conn.getResponseCode();
			if (resCode != HttpURLConnection.HTTP_OK) {
				throw AppException.http(resCode);
			}
			conn.connect();
			resultStream = conn.getInputStream();
			InputStreamReader inReader = new InputStreamReader(resultStream);
			BufferedReader buffer = new BufferedReader(inReader);
			String results = null;
			while (buffer.readLine() != null) {
				results += buffer.readLine();
			}
			// 以下是解压缩的过程
			// GZIPInputStream gis = new GZIPInputStream(resultStream);
			// int l;
			// byte[] tmp = new byte[4096];
			// while ((l=gis.read(tmp))!=-1){
			// bt.append(tmp, 0, l);
			// }
			// resultString = new String(bt.toByteArray(),"utf-8");
			AppLog.e(tag, "DoGet request service return results:" + results);
			return resultStream;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw AppException.network(e);
		}
	}

	@Deprecated
	public static InputStream postHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs) throws AppException {

		InputStream resultStream = null;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			conn.setDoInput(true);
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true, 默认情况下是false;
			conn.setDoOutput(true);
			// 设定请求的方法为"POST"，默认是GET
			conn.setRequestMethod("POST");
			// 设置超时
			conn.setConnectTimeout(TIMEOUT_CONNECTION);
			conn.setReadTimeout(TIMEOUT_SOCKET);
			// Post 请求不能使用缓存
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			// 设定传送的内容类型是可序列化的java对象
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
			conn.connect();
			// 设置post请求参数体
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			String content = getHttpUrl(nameValuePairs);
			out.writeBytes(content);
			out.flush();
			out.close();

			resultStream = conn.getInputStream();
			InputStreamReader inStream = new InputStreamReader(resultStream);
			BufferedReader buffer = new BufferedReader(inStream);
			String results = null;
			while (buffer.readLine() != null) {
				results += buffer.readLine();
			}
			AppLog.e(tag, "DoPost request service return results:" + results);
			return resultStream;
		} catch (IOException e) {
			throw AppException.network(e);
		}
	}

	/**
	 * 获取用户设备基本信息
	 * 
	 * @param mContext
	 * @return
	 */
	@Deprecated
	private static String getUserAgent(Context mContext) {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder(
					mContext.getString(R.string.app_name));
			// ua.append('/'+appContext.getPackageInfo().versionName+'_'+appContext.getPackageInfo().versionCode);//App版本
			ua.append("/Android");// 手机系统平台
			ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
			ua.append("/" + android.os.Build.MODEL); // 手机型号
			// ua.append("/"+appContext.getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

}
