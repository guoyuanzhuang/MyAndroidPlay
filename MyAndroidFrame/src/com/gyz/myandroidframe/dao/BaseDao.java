package com.gyz.myandroidframe.dao;

import android.content.ContentValues;
import android.database.Cursor;

public interface BaseDao {
	public boolean insert(ContentValues values);

	public boolean delete(String whereClause, String[] whereArgs);

	public boolean update(ContentValues values, String whereClause,
			String[] whereArgs);

	public Cursor query(String selection, String[] selectionArgs);
}
