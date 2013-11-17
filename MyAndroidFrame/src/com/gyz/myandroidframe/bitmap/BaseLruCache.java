package com.gyz.myandroidframe.bitmap;

import android.graphics.Bitmap;
/**
 * lru缓存策略接口(Memory/Local)
 * @author guoyuanzhuang
 *
 */
public interface BaseLruCache {
	public void put(String key, Bitmap bitmap);
	
	public Bitmap getBitmap(String key);
	
	public void evictAll();

	public void remove(String key);
}
