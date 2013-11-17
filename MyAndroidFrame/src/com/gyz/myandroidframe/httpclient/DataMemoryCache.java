package com.gyz.myandroidframe.httpclient;

import android.util.LruCache;

/**
 * 数据内存缓存实现
 * 
 * @author guoyuanzhuang
 * 
 */
public class DataMemoryCache implements BaseDataCache {
	private LruCache<String, String> mObjectMemoryCache;

	public DataMemoryCache(int cacheSize) {
		mObjectMemoryCache = new LruCache<String, String>(
				cacheSize) {
			protected int sizeOf(String key, String value) {
				return value.length();
			};
		};
	}

	@Override
	public void put(String key, String data) {
		// TODO Auto-generated method stub
		mObjectMemoryCache.put(key, data);
	}

	@Override
	public String getString(String key) {
		// TODO Auto-generated method stub
		return mObjectMemoryCache.get(key);
	}

	@Override
	public void evictAll() {
		// TODO Auto-generated method stub
		mObjectMemoryCache.evictAll();
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		mObjectMemoryCache.remove(key);
	}

}
