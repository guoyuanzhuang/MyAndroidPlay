package com.gyz.myandroidframe.httpdata;

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
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.app.AppThreadPools;
import com.gyz.myandroidframe.util.NetworkUtil;

/**
 * HTTP连接帮助类：用于HTTP线程池处理 再此封装Runnable对象以便外层调用可自由扩展线程对象
 * 
 * @author guoyuanzhuang
 * @think
 * 
 */
public class HttpConnectUtils implements Runnable {
	private final String tag = this.getClass().getName();
	private final String CHARSET_UTF8 = HTTP.UTF_8;// 字符编码
	private final int TIMEOUT_CONNECTION = 10000; // 连接超时
	private final int TIMEOUT_SOCKET = 10000; // 返回超时
	// 请求类型
	public static final int DOGET = 1000;
	public static final int DOPOST = 1001;
	public static final int DOBITMAP = 1002;
	// 初始化参数
	private Handler mHandler; // 处理请求结果
	private Context mContext;
	// 请求参数
	private int doMethod; // get or post
	private String url; // 请求url
	private List<NameValuePair> nameValuePairs;// 请求参数
	private boolean isCacheEnable = true; // 是否读取缓存
	// private boolean isRefresh = false; // 是否强制刷新
	// 缓存参数
	private DataCacheFactory mDataCacheFactory;
	private String cacheKey;

	public HttpConnectUtils(Context mContext, Handler mHandler) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		mDataCacheFactory = DataCacheFactory.getDataCacheInstance(mContext);
	}

	public void create(int doMethod, String url,
			List<NameValuePair> nameValuePairs) {
		this.doMethod = doMethod;
		this.url = url;
		this.nameValuePairs = nameValuePairs;

		AppThreadPools.execute(this);
	}

	public void doGet(String url, List<NameValuePair> nameValuePairs) {
		create(DOGET, url, nameValuePairs);
	}

	public void doPost(String url, List<NameValuePair> nameValuePairs) {
		create(DOPOST, url, nameValuePairs);
	}

	public void setCacheEnable(boolean isCacheEnable) {
		this.isCacheEnable = isCacheEnable;
	}

	// public void setRefresh(boolean isRefresh) {
	// this.isRefresh = isRefresh;
	// }

	/**
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	private String getHttpUrl(String url, List<NameValuePair> nameValuePairs) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if (nameValuePairs != null && nameValuePairs.size() > 0) {
			sb.append("?");
			for (int i = 0; i < nameValuePairs.size(); i++) {
				if (i > 0) {
					sb.append("&");
				}
				sb.append(String.format("%s=%s", nameValuePairs.get(i)
						.getName(), nameValuePairs.get(i).getValue()));
			}
		}
		AppLog.e(tag, "request url:" + sb.toString());
		return sb.toString();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		cacheKey = getHttpUrl(url, nameValuePairs);
		if (isCacheEnable) { // 设置缓存
			// if (!isRefresh) { // 不刷新
			String results = mDataCacheFactory.getMemory(cacheKey);
			if (!TextUtils.isEmpty(results)) {
				mHandler.sendMessage(Message.obtain(mHandler,
						HttpHandler.HTTP_SUCCESS, results));
			} else {
				results = mDataCacheFactory.getDisk(cacheKey);
				if (!TextUtils.isEmpty(results)) {
					mHandler.sendMessage(Message.obtain(mHandler,
							HttpHandler.HTTP_SUCCESS, results));
					boolean isInvalid = mDataCacheFactory
							.isDiskInvalid(cacheKey);
					if (isInvalid) { // 缓存失效走网络
						startHttpRequest(doMethod, isCacheEnable);
					}
				} else {
					// 无缓存走网络
					startHttpRequest(doMethod, isCacheEnable);
				}
			}
			// } else {
			// // 刷新走网络
			// startHttpRequest(doMethod, isCacheEnable);
			// }
		} else {
			// 不缓存走网络
			startHttpRequest(doMethod, isCacheEnable);
		}
	}

	/**
	 * HTTP 请求封装
	 * 
	 * @param type
	 *            请求类型
	 */
	private void startHttpRequest(int type, boolean isCacheEnable) {
		try {
			mHandler.sendMessage(Message.obtain(mHandler,
					HttpHandler.HTTP_START));
			if (!NetworkUtil.isNetworkConnected(mContext)) {
				throw new NetworkErrorException(); // 无网络连接
			}

			HttpResponse httpResponse = null;
			switch (type) {
			case DOGET:
				String httpUrl = getHttpUrl(url, nameValuePairs);
				HttpGet httpRequest = new HttpGet(httpUrl);
				HttpClient httpclient = getHttpClient();
				httpResponse = httpclient.execute(httpRequest);
				break;
			case DOPOST:
				UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(
						nameValuePairs, CHARSET_UTF8);
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(urlEncoded);
				HttpClient client = getHttpClient();
				httpResponse = client.execute(httpPost);
				break;
			case DOBITMAP:
				HttpGet httpBitmap = new HttpGet(url);
				HttpClient clientBitmap = getHttpClient();
				HttpResponse responseBitmap = clientBitmap.execute(httpBitmap);
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
						responseBitmap.getEntity());
				Bitmap bm = BitmapFactory.decodeStream(bufHttpEntity
						.getContent());
				mHandler.sendMessage(Message.obtain(mHandler,
						HttpHandler.HTTP_SUCCESS, bm));
				// 图片缓存需要单独处理:1.需要载体VIEW；2.缓存策略(特殊性)
				break;
			default:
				break;
			}
			// Get or Post
			if (type != DOBITMAP) {
				// 请求成功
				int doGetCode = httpResponse.getStatusLine().getStatusCode();
				if (doGetCode != HttpStatus.SC_OK) {
					throw new Exception();
				}
				HttpEntity resEntity = httpResponse.getEntity();
				String results = EntityUtils.toString(resEntity, CHARSET_UTF8);
				mHandler.sendMessage(Message.obtain(mHandler,
						HttpHandler.HTTP_SUCCESS, results));
				AppLog.e(tag, results);
				// if (isCacheEnable) {
				mDataCacheFactory.setMemory(cacheKey, results);
				mDataCacheFactory.setDisk(cacheKey, results);
				// }
			}
		} catch (Exception e) {
			// TODO: handle exception
			mHandler.sendMessage(Message.obtain(mHandler,
					HttpHandler.HTTP_ERROR, e));
		}
	}

	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	private HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
		HttpProtocolParams.setUseExpectContinue(params, true);
		// HttpProtocolParams
		// .setUserAgent(
		// params, getUserAgent(appContext));
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);
		/* 读取超时 */
		HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}

	/**
	 * 获取用户设备基本信息
	 * 
	 * @param mContext
	 * @return
	 */
	private String appUserAgent; // http头中用户基本信息

	private String getUserAgent(Context mContext) {
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
