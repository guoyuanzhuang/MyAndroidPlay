package com.gyz.myandroidframe.bitmap;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 缓存管理
 * 
 * @author guoyuanzhuang
 * 
 */
public class BitmapCacheFactory {
	private static final int MEMORY_CACHE_SIZE = 1024 * 1024 * 4; // 内存4MB
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50; // 本地50M

	public BitmapMemoryCache mBitmapMemoryCache; // bitmap内存缓存
	public BitmapDiskCache mLruDiskCache; // bitmap磁盘缓存

	public static BitmapCacheFactory mCacheManager;

	private Context mContext;
	File cacheDir = mContext.getCacheDir();

	public BitmapCacheFactory(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mBitmapMemoryCache = new BitmapMemoryCache(MEMORY_CACHE_SIZE);
		mLruDiskCache = new BitmapDiskCache(cacheDir, DISK_CACHE_SIZE);
	}

	public static BitmapCacheFactory getCacheInstance(Context ctx) {
		if (mCacheManager == null) {
			mCacheManager = new BitmapCacheFactory(ctx.getApplicationContext());
		}
		return mCacheManager;
	}

	public void putMemory(String key, Bitmap bitmap) {
		mBitmapMemoryCache.put(key, bitmap);
	}

	public void putDisk(String key, Bitmap bitmap) {
		mLruDiskCache.put(key, bitmap);
	}

	public Bitmap getMemory(String key) {
		return mBitmapMemoryCache.getBitmap(key);
	}

	public Bitmap getDisk(String key) {
		return mLruDiskCache.getBitmap(key);
	}

	public void removeMemory(String key) {
		mBitmapMemoryCache.remove(key);
	}

	public void removeDisk(String key) {
		mLruDiskCache.remove(key);
	}

	public void evictAllMemory() {
		mBitmapMemoryCache.evictAll();
	}

	public void evictAllDisk() {
		mLruDiskCache.evictAll();
	}
}
