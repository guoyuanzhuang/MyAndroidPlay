package com.gyz.myandroidframe.httprequest.base;

import java.io.Serializable;

/**
 * 数据缓存策略接口(Memory/Local)
 * @author guoyuanzhuang
 *
 */
public interface BaseDataCache {
	
	public void put(String key, String data);
	
	public boolean put(String key, Serializable data);
	
	public String getString(String key);
	
	public Serializable getObject(String key);
	
	public void evictAll();

	public void remove(String key);
}
