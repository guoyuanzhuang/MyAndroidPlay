package com.gyz.myandroidframe.httprequest.base;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.app.AppThreadPools;
import com.gyz.myandroidframe.util.FileUtils;
import com.gyz.myandroidframe.util.NetworkUtil;

/**
 * HTTP连接帮助类：用于HTTP线程池处理 再此封装Runnable对象以便外层调用可自由扩展线程对象
 * 
 * @author guoyuanzhuang
 * @think
 * 
 */
public abstract class HttpConnectPools implements Runnable {
	private final String tag = this.getClass().getName();
	// 请求类型
	public static final int DOGET = 1000;
	public static final int DOPOST = 1001;
	public static final int DOBITMAP = 1002;
	// 初始化参数
	private Handler mHandler; // 处理请求结果
	private Context mContext;
	// 请求必须的参数
	private int doMethod; // get or post
	private String requestUrl; // 请求url
	private boolean isCacheEnable = true; // 是否读取缓存
	// private boolean isRefresh = false; // 是否强制刷新
	// 缓存参数
	private DataCacheFactory mDataCacheFactory;

	// private ;

	public HttpConnectPools(Context mContext, Handler mHandler) {
		this.mHandler = mHandler;
		this.mContext = mContext;
		mDataCacheFactory = DataCacheFactory.getDataCacheInstance(mContext);
	}

	public void create() {
		if (!NetworkUtil.isNetworkConnected(mContext)) {
			// 无网络连接
			mHandler.sendMessage(Message.obtain(mHandler,
					HttpHandler.HTTP_ERROR, new NetworkErrorException()));
			return;
		}
		this.doMethod = getMethod();
		this.requestUrl = getHttpUrl();
		AppThreadPools.execute(this);
	}

	public void setCacheEnable(boolean isCacheEnable) {
		this.isCacheEnable = isCacheEnable;
	}

	// public void setRefresh(boolean isRefresh) {
	// this.isRefresh = isRefresh;
	// }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		mHandler.sendMessage(Message.obtain(mHandler, HttpHandler.HTTP_START));
		try {
			switch (doMethod) {
			case DOGET:
				String httpUrl = httpAssembleParams(requestUrl, getRequestParams());
				String cacheKey = httpUrl.hashCode() + "";
				if(isDoGetNeedRequestHttp(isCacheEnable, cacheKey)){
					InputStream responseResults = HttpConnectApi.httpRequestDoGet(httpUrl);
					Serializable responseObj = parseReponseData(responseResults);
//					HttpConnectApi.printLog(FileUtils.InputStreamTOString(responseResults));
					if(responseObj != null){
						if(mHandler != null){
							mHandler.sendMessage(Message.obtain(mHandler, HttpHandler.HTTP_SUCCESS, responseObj));
						}
						mDataCacheFactory.setMemory(cacheKey, responseObj);
						mDataCacheFactory.setDisk(cacheKey, responseObj);
					}
				}
				break;
			case DOPOST:
				InputStream responseResults = HttpConnectApi.httpRequestDoPost(requestUrl, getPostTextContentMap(), getPostImagePathMap(), getPostVideoPathMap());
				Serializable responseObj = parseReponseData(responseResults);
				if(mHandler != null){
					mHandler.sendMessage(Message.obtain(mHandler,HttpHandler.HTTP_SUCCESS, responseObj));
				}
				break;
			case DOBITMAP:
				Bitmap bitmap = HttpConnectApi.httpRequestDoBitmap(requestUrl);
				if(mHandler != null && bitmap != null){
					mHandler.sendMessage(Message.obtain(mHandler,HttpHandler.HTTP_SUCCESS, bitmap));
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			if(mHandler != null){
				mHandler.sendMessage(Message.obtain(mHandler, HttpHandler.HTTP_ERROR, e));
			}
		}
	}
	/**
	 * 判断DoGet请求是否需要走网络
	 * @param isCacheEnable
	 * @param cacheKey
	 * @return
	 */
	private boolean isDoGetNeedRequestHttp(boolean isCacheEnable, String cacheKey){
		if (isCacheEnable) { // 读取缓存
			AppLog.i(tag, "start reading cache ：" + cacheKey);
			Serializable results = mDataCacheFactory.getObjectMemory(cacheKey);
			if (results != null) {
				AppLog.i(tag, "memory cache is success ：" + results.toString());
				if(mHandler != null){
					mHandler.sendMessage(Message.obtain(mHandler, HttpHandler.HTTP_SUCCESS, results));
				}
			} else {
				AppLog.i(tag, "memory cache is null");
				results = mDataCacheFactory.getObjectDisk(cacheKey);
				if (results != null) {
					AppLog.i(tag, "local cache is success ：" + results.toString());
					if(mHandler != null){
						mHandler.sendMessage(Message.obtain(mHandler, HttpHandler.HTTP_SUCCESS, results));
					}
					boolean isInvalid = mDataCacheFactory.isDiskInvalid(cacheKey);
					if (isInvalid) { // 缓存失效走网络
						AppLog.i(tag, "local cache is invalid");
						return true;
					}
				} else {
					// 无缓存走网络
					AppLog.i(tag, "local cache is null");
					return true;
				}
			}
		} else {
			// 不读缓存走网络
			AppLog.i(tag, "isCacheEnable == false");
			return true;
		}
		return false;
	}
	
	/**
	 * HTTP 参数 组装
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	private String httpAssembleParams(String url, List<NameValuePair> nameValuePairs) {
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
		return sb.toString();
	}

	/**
	 * Get Http Request Url
	 * @return
	 */
	protected abstract String getHttpUrl();

	/**
	 * Parse Http Return Data
	 * @param mInputStream
	 * @return
	 * @throws AppException
	 */
	protected abstract Serializable parseReponseData(InputStream mInputStream)
			throws AppException;

	/**
	 * Get Http Request Type
	 * @return
	 */
	protected abstract int getMethod();
	
	/**
	 * DoGet Request Params Get
	 * @return
	 */
	protected List<NameValuePair> getRequestParams(){
		return null;
	}
	
	protected HashMap<String, String> getPostTextContentMap(){
		return null;
	}
	
	/**
	 * DoPost Get Audio Upload Path
	 * @return
	 */
	protected HashMap<String, String> getPostAudioPathMap(){
		return null;
	}

	/**
	 * DoPost Get Image Upload Path
	 * @return
	 */
	protected HashMap<String, String> getPostImagePathMap(){
		return null;
	}

	/**
	 * DoPost Get Video Upload Path
	 * @return
	 */
	protected HashMap<String, String> getPostVideoPathMap(){
		return null;
	}
	
}
