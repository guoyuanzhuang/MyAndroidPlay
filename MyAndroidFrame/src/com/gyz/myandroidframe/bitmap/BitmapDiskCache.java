package com.gyz.myandroidframe.bitmap;

import java.io.File;


import android.graphics.Bitmap;
/**
 * 
 * @ClassName BitmapDiskCache 
 * @Description Bitmap本地缓存
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:40:37 
 *
 */
public class BitmapDiskCache implements BaseLruCache {
	private DiskLruCache mDiskLruCache;

	public BitmapDiskCache(File cacheDir, int cacheSize) {
		// TODO Auto-generated constructor stub
		mDiskLruCache = DiskLruCache.openCache(cacheDir, cacheSize);
	}

	@Override
	public void put(String key, Bitmap bitmap) {
		// TODO Auto-generated method stub
		mDiskLruCache.put(key, bitmap);
	}

	@Override
	public Bitmap getBitmap(String key) {
		// TODO Auto-generated method stub
		return mDiskLruCache.get(key);
	}

	@Override
	public void evictAll() {
		// TODO Auto-generated method stub
		mDiskLruCache.clearCache();
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		// mDiskLruCache.
	}
}
