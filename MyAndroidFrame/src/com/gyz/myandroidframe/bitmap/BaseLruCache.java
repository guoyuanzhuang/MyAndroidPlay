package com.gyz.myandroidframe.bitmap;

import android.graphics.Bitmap;

/**
 * 
 * @ClassName BaseLruCache 
 * @Description lru缓存策略接口(Memory/Local)
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:39:37 
 *
 */
public interface BaseLruCache {
	public void put(String key, Bitmap bitmap);
	
	public Bitmap getBitmap(String key);
	
	public void evictAll();

	public void remove(String key);
}
