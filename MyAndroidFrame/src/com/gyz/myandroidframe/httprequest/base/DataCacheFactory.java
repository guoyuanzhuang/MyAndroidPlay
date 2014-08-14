package com.gyz.myandroidframe.httprequest.base;

import java.io.Serializable;

import android.content.Context;

public class DataCacheFactory {
	private static final int MEMORY_CACHE_SIZE = 1024 * 1024 * 4; // 内存4MB --- 本地缓存无限制
	
	public DataMemoryCache mDataMemoryCache; // 数据内存缓存
	public DataDiskCache mDataDiskCache; // 数据磁盘缓存

	public static DataCacheFactory mDataCacheManager;

	private Context mContext;
//	File cacheDir = mContext.getCacheDir();

	public DataCacheFactory(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mDataMemoryCache = new DataMemoryCache(MEMORY_CACHE_SIZE);
		mDataDiskCache = new DataDiskCache(mContext);
	}

	public static DataCacheFactory getDataCacheInstance(Context ctx) {
		if (mDataCacheManager == null) {
			mDataCacheManager = new DataCacheFactory(
					ctx.getApplicationContext());
		}
		return mDataCacheManager;
	}

	public void setMemory(String key, String results) {
		mDataMemoryCache.put(key, results);
	}
	public void setMemory(String key, Serializable results) {
		mDataMemoryCache.put(key, results);
	}

	public void setDisk(String key, String results) {
		mDataDiskCache.put(key, results);
	}
	public void setDisk(String key, Serializable results) {
		mDataDiskCache.put(key, results);
	}

	public String getMemory(String key) {
		return mDataMemoryCache.getString(key);
	}
	public Serializable getObjectMemory(String key) {
		return mDataMemoryCache.getObject(key);
	}

	public String getDisk(String key) {
		return mDataDiskCache.getString(key);
	}
	public Serializable getObjectDisk(String key) {
		return mDataDiskCache.getObject(key);
	}

	public void removeMemory(String key) {
		mDataMemoryCache.remove(key);
	}

	public void removeDisk(String key) {
		mDataDiskCache.remove(key);
	}

	public void evictAllMemory() {
		mDataMemoryCache.evictAll();
	}

	public void evictAllDisk() {
		mDataDiskCache.evictAll();
	}
	
	public boolean isDiskInvalid(String key){
		return mDataDiskCache.isCacheDataFailure(mContext, key);
	}
}
