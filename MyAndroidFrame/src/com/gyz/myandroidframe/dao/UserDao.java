package com.gyz.myandroidframe.dao;

import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.db.DBHelper;
import com.gyz.myandroidframe.db.PluginsColumns;
import com.gyz.myandroidframe.db.UserColumns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 
 * @author guoyuanzhuang
 *
 */
public class UserDao implements BaseDao {
	private final String tag = this.getClass().getName();
	private DBHelper mDBHelper;

	public UserDao(Context context) {
		mDBHelper = DBHelper.getInstance(context);
	}

	@Override
	public boolean insert(ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			long count = database.insert(UserColumns.TABLE_NAME, null, values);
			return count > 0;
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return false;
	}

	@Override
	public boolean delete(String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			int count = database.delete(UserColumns.TABLE_NAME, whereClause,
					whereArgs);
			return count > 0;
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return false;
	}

	@Override
	public boolean update(ContentValues values, String whereClause,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			int count = database.update(UserColumns.TABLE_NAME, values,
					whereClause, whereArgs);
			return count > 0;
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return false;
	}

	@Override
	public Cursor query(String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		SQLiteDatabase database = mDBHelper.getReadableDatabase();
		try {
			cursor = database.query(PluginsColumns.TABLE_NAME, null, selection,
					selectionArgs, null, null, null);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return cursor;
	}

}
