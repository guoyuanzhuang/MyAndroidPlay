package com.gyz.myandroidframe.httprequest.base;

import java.io.Serializable;

import android.util.LruCache;

/**
 * 数据内存缓存实现
 * 
 * @author guoyuanzhuang
 * 
 */
public class DataMemoryCache implements BaseDataCache {
	private LruCache<String, String> mStringMemoryCache;

	private LruCache<String, Serializable> mObjectMemoryCache;

	public DataMemoryCache(int cacheSize) {
//		mStringMemoryCache = new LruCache<String, String>(cacheSize) {
//			protected int sizeOf(String key, String value) {
//				return value.length();
//			};
//		};
		mObjectMemoryCache = new LruCache<String, Serializable>(cacheSize) {
			@Override
			protected int sizeOf(String key, Serializable value) {
				// TODO Auto-generated method stub
				return super.sizeOf(key, value);
			}
		};
	}

	@Override
	public void put(String key, String data) {
		// TODO Auto-generated method stub
		mStringMemoryCache.put(key, data);
	}

	@Override
	public String getString(String key) {
		// TODO Auto-generated method stub
		return mStringMemoryCache.get(key);
	}

	@Override
	public void evictAll() {
		// TODO Auto-generated method stub
//		mStringMemoryCache.evictAll();
		mObjectMemoryCache.evictAll();
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
//		mStringMemoryCache.remove(key);
		mObjectMemoryCache.remove(key);
	}

	@Override
	public boolean put(String key, Serializable data) {
		// TODO Auto-generated method stub
		mObjectMemoryCache.put(key, data);
		return true;
	}

	@Override
	public Serializable getObject(String key) {
		// TODO Auto-generated method stub
		return mObjectMemoryCache.get(key);
	}

}
