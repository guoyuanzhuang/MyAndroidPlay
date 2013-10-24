package com.gyz.megaeyefamily.httprequest;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.gyz.megaeyefamily.bean.LoginResult;
import com.gyz.megaeyefamily.bean.MegaeyeInfo;
import com.gyz.megaeyefamily.bean.MegaeyePlayAddress;
import com.gyz.megaeyefamily.bean.WeatherInfoBean;
import com.gyz.megaeyefamily.dataparse.SAXService;
import com.gyz.megaeyefamily.httprequest.GalHttpRequest.GalHttpLoadImageCallBack;
import com.gyz.megaeyefamily.httprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import com.gyz.megaeyefamily.httprequest.GalHttpRequest.GalHttpRequestListener;

/**
 * 该类处理 所有相关 HTTP请求以及返回结果
 * 
 * @author guoyuanzhuang
 * 
 */
public class HttpRequestUtil {
	private String serverIp = "115.238.143.98/VideoInTrafficGZIP/services/";
	private static HttpRequestUtil httpRequestUtil = null; // 单例
	private SAXService xmlParseService = null;
	private GalHttpRequest request = null;
	// http 请求结果
	public List<WeatherInfoBean> weatherInfoList = null; // 天气预报
	public LoginResult mLoginResult = null; // 登录信息
	public MegaeyePlayAddress playAddress = null; // 获取rtsp
	// 请求HTTP链接
	public final String PATH_ISNINGBO = "http://" + serverIp
			+ "SmartHomeService/getlogin";
	public final String PATH_WEANTHER = "http://" + serverIp
			+ "WeatherWebServiceGZIP/getWeatherNews"; // 天气
	public final String PATH_LOGIN = "http://" + serverIp
			+ "SmartHomeService/getQQYIsLogin"; // 登录接口
	public final String PATH_PLAYVIDEO = "http://" + serverIp
			+ "SmartHomeService/getQQYPlayUrl";

	// 请求处理类型
	public final static int SYNC_WEATHERINFO = 10; // 天气
	public final static int SYNC_LOGIN = 20; // 登录
	public final static int SYNC_PLAYVIDEO = 30; // 获取视频rtsp
	public final static int SYNC_ISNINGBO = 40; // 是否宁波用户

	public HttpRequestUtil() {
		xmlParseService = SAXService.getParseInstance(); // 初始化解析器
	}

	/**
	 * 给请求类设置单例
	 * 
	 * @return HttpRequestUtil
	 */
	public static HttpRequestUtil getHttpInstance() {
		if (httpRequestUtil == null) {
			httpRequestUtil = new HttpRequestUtil(); // 初始化http请求
		}
		return httpRequestUtil;
	}

	/**
	 * 异步处理 HTTP返回String
	 * 
	 * @param urlStr
	 *            请求url
	 * @param mContext
	 * @param feedPair
	 *            参数组装
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void asynRequestStr(String urlStr, Context mContext,
			final Handler handler, final int requestType) {
		// 异步请求String
		request = GalHttpRequest.requestWithURL(mContext, urlStr);
		// 第一次调用startAsynRequestString或者startAsynRequestBitmap必须在主线程调用
		// 因为只有在主线程中调用才可以初始化GalHttprequest内部的全局句柄Handler
		request.startAsynRequestString(new GalHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				// 该部分允许于UI线程
				if (requestType == SYNC_WEATHERINFO) {
					weatherInfoList = xmlParseService.onGetWeatherInfo(text);
				}
				handler.sendEmptyMessage(requestType);
			}
		});
	}

	/**
	 * 异步处理 HTTP返回String
	 * 
	 * @param urlStr
	 *            请求url
	 * @param mContext
	 * @param feedPair
	 *            参数组装
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 * @param feedPair
	 *            参数
	 */
	public void asynRequestStr(String urlStr, Context mContext,
			final Handler handler, final int requestType,
			NameValuePair... feedPair) {
		// 异步请求String
		request = GalHttpRequest.requestWithURL(mContext, urlStr, feedPair);
		// 第一次调用startAsynRequestString或者startAsynRequestBitmap必须在主线程调用
		// 因为只有在主线程中调用才可以初始化GalHttprequest内部的全局句柄Handler
		request.startAsynRequestString(new GalHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				// 该部分允许于UI线程
				if (requestType == SYNC_LOGIN) {
					mLoginResult = xmlParseService.onGetLoginResult(text);
					// 摄像头在线排序
					if (mLoginResult != null) {
						if (mLoginResult.megaeyeInfoList != null
								&& mLoginResult.megaeyeInfoList.size() > 0) {
							Collections.sort(mLoginResult.megaeyeInfoList,
									new Comparator<MegaeyeInfo>() {
										@Override
										public int compare(MegaeyeInfo lhs,
												MegaeyeInfo rhs) {
											// TODO Auto-generated method stub
											return rhs.online - lhs.online;
										}
									});
						}
					}
				} else if (requestType == SYNC_PLAYVIDEO) {
					playAddress = xmlParseService.onGetPlayAddress(text);
				} else if (requestType == SYNC_ISNINGBO) {
					mLoginResult = xmlParseService.onGetLoginResult(text);
				}
				handler.sendEmptyMessage(requestType);
			}
		});
	}

	/**
	 * 异步处理HTTP请求返回inputstream
	 * 
	 * @param urlStr
	 *            请求链接
	 * @param mContext
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void asynRequestStream(String urlStr, Context mContext,
			final Handler handler, final int requestType) {
		request = GalHttpRequest.requestWithURL(mContext, urlStr);
		// 必须先设置回调函数，否则调用异步请求无效
		request.setListener(new GalHttpRequestListener() {
			@Override
			public void loadFinished(final InputStream is, boolean fromcache) {
				// 注意，由于返回的是InputStream，一般情况都需要长时间操作，所以，回调函数是在子线程调用
				// 因此使用handler
				handler.post(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(requestType);
					}
				});
			}

			@Override
			// 请求失败时，有可能可以从缓存里面读取数据返回
			public void loadFailed(final HttpResponse respone,
					InputStream cacheInputStream) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(requestType);
					}
				});
			}
		});
		request.startAsynchronous();
	}

	/**
	 * 异步处理HTTP请求返回Bitmap
	 * 
	 * @param urlStr
	 *            请求链接
	 * @param mContext
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void asynRequestBitmap(String urlStr, Context mContext,
			final Handler handler, final int requestType) {
		// 异步请求Bitmap
		request = GalHttpRequest.requestWithURL(mContext, urlStr);
		request.startAsynRequestBitmap(new GalHttpLoadImageCallBack() {
			@Override
			public void imageLoaded(Bitmap bitmap) {
				handler.sendEmptyMessage(requestType);
			}
		});
	}

	/**
	 * 异步处理HTTP POST请求返回String
	 * 
	 * @param urlStr
	 *            请求链接
	 * @param mContext
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void asynRequestPostStr(String urlStr, Context mContext,
			final Handler handler, final int requestType) {
		// 交给GalHttprequest自动组装url中的参数
		request = GalHttpRequest.requestWithURL(mContext, urlStr);
		// 设置post内容
		request.setPostValueForKey("name", "qiuscut");
		request.startAsynRequestString(new GalHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				// 该部分允许于UI线程
				handler.sendEmptyMessage(requestType);
			}
		});
	}

	/************************************************* 异步处理 *********************************************************/
	/**
	 * 同步处理HTTP请求返回InputStream
	 * 
	 * @param urlStr
	 *            请求链接
	 * @param mContext
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void syncRequestStream(String urlStr, Context mContext,
			Handler handler, int requestType) {
		request = GalHttpRequest.requestWithURL(mContext, urlStr);
		// 如果不检测缓存，则设置：
		// request.setCacheEnable(false);
		// 必须在调用startXXX()函数之前设置
		// 返回的缓存已经是ufferedInputStream类型
		InputStream is = request.startSynchronous();
		if (is != null) {
			handler.sendEmptyMessage(requestType);
		}
	}

	/**
	 * 同步处理HTTP请求返回String
	 * 
	 * @param urlStr
	 *            请求链接
	 * @param mContext
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void syncRequestStr(String urlStr, Context mContext,
			Handler handler, int requestType) {
		request = GalHttpRequest.requestWithURL(mContext, urlStr);
		// 根据服务器返回的状态读取内容，如果服务器内容没有改变，则直接读取缓存内容，如果服务器内容已经修改，则从服务器拉取数据
		// 并刷新缓存内容
		String string = request.startSyncRequestString();

		handler.sendEmptyMessage(requestType);
	}

	/**
	 * 同步处理HTTP请求返回Bitmap
	 * 
	 * @param urlStr
	 *            请求链接
	 * @param mContext
	 * @param handler
	 *            处理请求结果
	 * @param requestType
	 *            请求类型
	 */
	public void syncRequestBitmap(String urlStr, Context mContext,
			Handler handler, int requestType) {
		Header header = new BasicHeader("Accept-Language", "zh-cn,zh;q=0.5");
		// 支持添加自定义的Http Header请求
		request = GalHttpRequest.requestWithURL(mContext, urlStr,
				new Header[] { header });
		// 请求Bitmap，由于图片基本上不改变，因此如果存在缓存，则直接从缓存读取
		Bitmap bitmap = request.startSyncRequestBitmap();
		handler.sendEmptyMessage(requestType);
	}
}
