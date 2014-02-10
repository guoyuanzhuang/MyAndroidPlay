package com.gyz.myandroidframe.bitmap;

import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.app.AppThreadPools;
import com.gyz.myandroidframe.httpdata.HttpConnectUtils;
import com.gyz.myandroidframe.httpdata.HttpHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


/**
 * 异步图片加载工具类
 * @author guoyuanzhuang
 * @date 2013-11-17
 * BitmapFactory.decodeResource(context.getResources(),
 * R.drawable.loading)
 *
 */
public class BitmapManager {
	private final String tag = this.getClass().getName();
	private Bitmap defaultBmp;
	private BitmapCacheFactory mCacheManager;
	private Context mContext;

	public BitmapManager() {
	}

	public BitmapManager(Context mContext, Bitmap def) {
		this.defaultBmp = def;
		mCacheManager = BitmapCacheFactory.getCacheInstance(mContext);
	}

	/**
	 * 设置默认图片
	 * 
	 * @param bmp
	 */
	public void setDefaultBmp(Bitmap bmp) {
		defaultBmp = bmp;
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
	 * 加载图片-可设置加载失败后显示的默认图片
	 * 
	 * @param url
	 * @param imageView
	 * @param defaultBmp
	 */
	public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {
		loadBitmap(url, imageView, defaultBmp, 0, 0);
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
		// imageViews.put(imageView, url);
		Bitmap bitmap = mCacheManager.getMemory(url);
		if (bitmap != null) {
			// 显示缓存图片
			imageView.setImageBitmap(bitmap);
		} else {
			bitmap = mCacheManager.getDisk(url);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			} else {
				// 线程加载网络图片
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
					AppLog.e(tag, "Bitmap download success: " + url);
					if (msg.obj != null) {
						Bitmap bmp = (Bitmap) msg.obj;
						if (width > 0 && height > 0) {
							// 指定显示图片的高宽
							bmp = Bitmap.createScaledBitmap(bmp, width, height,
									true);
						}
						imageView.setImageBitmap(bmp);
						mCacheManager.putMemory(url, bmp);		//写缓存
						mCacheManager.putDisk(url, bmp);		//写内存
					}
				} else if (msg.what == HttpHandler.HTTP_ERROR) {
					AppLog.e(tag, "Bitmap download error: " + url);
				}
			}
		};
		
		HttpConnectUtils hcu = new HttpConnectUtils(mContext, handler);
		hcu.setCacheEnable(false);
		hcu.create(HttpConnectUtils.DOBITMAP, url, null);
	}
}