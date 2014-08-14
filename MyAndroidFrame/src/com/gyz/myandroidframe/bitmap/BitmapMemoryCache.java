package com.gyz.myandroidframe.bitmap;


import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 
 * @ClassName BitmapMemoryCache 
 * @Description 图片内存缓存实现
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:41:58 
 *
 */
public class BitmapMemoryCache implements BaseLruCache {
	private LruCache<String, Bitmap> mBitmapCache;
	
	
	public BitmapMemoryCache(int cacheSize) {
		// TODO Auto-generated constructor stub
		mBitmapCache = new LruCache<String, Bitmap>(
				cacheSize){
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			};
		};
	}


	@Override
	public void put(String key, Bitmap bitmap) {
		// TODO Auto-generated method stub
		mBitmapCache.put(key, bitmap);
	}

	@Override
	public Bitmap getBitmap(String key) {
		// TODO Auto-generated method stub
		return mBitmapCache.get(key);
	}

	@Override
	public void evictAll() {
		// TODO Auto-generated method stub
		mBitmapCache.evictAll();
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		mBitmapCache.remove(key);
	}
}
