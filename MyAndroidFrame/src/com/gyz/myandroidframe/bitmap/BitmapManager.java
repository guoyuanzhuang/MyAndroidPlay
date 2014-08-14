package com.gyz.myandroidframe.bitmap;

import java.io.InputStream;
import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.httprequest.base.HttpConnectPools;
import com.gyz.myandroidframe.httprequest.base.HttpHandler;

/**
 * 
 * @ClassName BitmapManager 
 * @Description 异步图片加载工具类
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:41:26 
 * @user 
 * BitmapManager bitmapManager = new BitmapManager(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.def)); 
 * bitmapManager.loadBitmap(url, imageview);
 */
public class BitmapManager {
	private final String tag = this.getClass().getName();
	private Bitmap defaultBmp;
	private BitmapCacheFactory mCacheManager;
	private Context mContext;
	
	// user of need not cache 
	//	public BitmapManager() {
	//	}
	
	public BitmapManager(Context mContext, Bitmap def) {
		this.defaultBmp = def;
		this.mContext = mContext;
		mCacheManager = BitmapCacheFactory.getCacheInstance(mContext);
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void loadBitmap(String url, ImageView imageView) {
		loadBitmap(url, imageView, this.defaultBmp, 0, 0);
	}

	/**
	 * 加载图片-可指定显示图片的高宽
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp,
			int width, int height) {
		AppLog.i(tag, "bitmap start reading cache :" + url);
		Bitmap bitmap = mCacheManager.getMemory(url);
		if (bitmap != null) {
			// 显示缓存图片
			AppLog.i(tag, "bitmap memory cache is success ：" + bitmap.getRowBytes()*bitmap.getHeight());
			imageView.setImageBitmap(bitmap);
		} else {
			AppLog.i(tag, "bitmap memory cache is null");
			bitmap = mCacheManager.getDisk(url);
			if (bitmap != null) {
				AppLog.i(tag, "bitmap local cache is success ：" + bitmap.getRowBytes()*bitmap.getHeight());
				imageView.setImageBitmap(bitmap);
			} else {
				// 线程加载网络图片
				AppLog.i(tag, "bitmap local cache is null");
				imageView.setImageBitmap(defaultBmp);
				downloadBitmap(url, imageView, width, height);
			}
		}
	}

	/**
	 * 异步从网络中加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void downloadBitmap(final String url, final ImageView imageView,
			final int width, final int height) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == HttpHandler.HTTP_SUCCESS) {
					if (msg.obj != null) {
						Bitmap bmp = (Bitmap) msg.obj;
						AppLog.i(tag, "Bitmap download success: " + bmp.getRowBytes()*bmp.getHeight());
						if (width > 0 && height > 0) {
							// 指定显示图片的高宽
							bmp = Bitmap.createScaledBitmap(bmp, width, height,
									true);
						}
						imageView.setImageBitmap(bmp);
						if(mCacheManager != null){
							mCacheManager.putMemory(url, bmp);		//写缓存
							mCacheManager.putDisk(url, bmp);		//写内存
						}
					}
				} else if (msg.what == HttpHandler.HTTP_ERROR) {
					AppLog.e(tag, "Bitmap download error: " + url);
				}
			}
		};
		ImageHttpConnect imageHttpConnect = new ImageHttpConnect(mContext, handler);
		imageHttpConnect.setRequestUrl(url);
		imageHttpConnect.create();
	}
	
	private void recycleBitmap(){
		
	}
	
	/**
	 * 图片下载线程
	 * @author guoyuanzhuang
	 *
	 */
	private class ImageHttpConnect extends HttpConnectPools{
		private String requestUrl;
		public void setRequestUrl(String url){
			this.requestUrl = url;
		}

		public ImageHttpConnect(Context mContext, Handler mHandler) {
			super(mContext, mHandler);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String getHttpUrl() {
			// TODO Auto-generated method stub
			return requestUrl;
		}

		@Override
		protected Serializable parseReponseData(InputStream mInputStream)
				throws AppException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected int getMethod() {
			// TODO Auto-generated method stub
			return HttpConnectPools.DOBITMAP;
		}
	}
}