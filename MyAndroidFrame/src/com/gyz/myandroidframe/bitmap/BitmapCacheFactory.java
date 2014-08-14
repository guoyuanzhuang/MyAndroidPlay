package com.gyz.myandroidframe.bitmap;


import android.content.Context;
import android.graphics.Bitmap;

/**
 * 
 * @ClassName BitmapCacheFactory 
 * @Description Bitmap缓存工厂
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:39:56 
 *
 */
public class BitmapCacheFactory {
	private static final int MEMORY_CACHE_SIZE = 1024 * 1024 * 4; // 内存缓存4MB
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50; // 本地缓存50M

	public BitmapMemoryCache mBitmapMemoryCache; // bitmap内存缓存
	public BitmapDiskCache mLruDiskCache; // bitmap磁盘缓存

	public static BitmapCacheFactory mCacheManager;

//	private Context mContext;

	public BitmapCacheFactory(Context mContext) {
		// TODO Auto-generated constructor stub
//		this.mContext = mContext;
		mBitmapMemoryCache = new BitmapMemoryCache(MEMORY_CACHE_SIZE);
		mLruDiskCache = new BitmapDiskCache(mContext.getCacheDir(), DISK_CACHE_SIZE);
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
