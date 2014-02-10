package com.gyz.myandroidframe.httpdata;

/**
 * 数据缓存策略接口(Memory/Local)
 * @author guoyuanzhuang
 *
 */
public interface BaseDataCache {
	public void put(String key, String data);
	
	public String getString(String key);
	
	public void evictAll();

	public void remove(String key);
}
