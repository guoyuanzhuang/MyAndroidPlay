package com.gyz.myandroidframe.dao;

import android.content.ContentValues;
import android.database.Cursor;
/**
 * 
 * @ClassName BaseDao 
 * @Description SQLite 数据接口
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:43:18 
 *
 */
public interface BaseDao {
	public boolean insert(ContentValues values);

	public boolean delete(String whereClause, String[] whereArgs);

	public boolean update(ContentValues values, String whereClause,
			String[] whereArgs);

	public Cursor query(String selection, String[] selectionArgs);
}
