package com.gyz.myandroidframe.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.db.BaseTableColumns;
import com.gyz.myandroidframe.db.DBHelper;
import com.gyz.myandroidframe.db.TestItemColumns;

/**
 * ContentProvider
 * 封装：这里没有实现单条匹配，因为多条匹配中where条件可以直接筛选；这里考虑多个ContentProvider通过匹配器集成处理
 * 
 * @author guoyuanzhuang
 * 
 */
public class TestItemProvider extends ContentProvider {
	private final String tag = this.getClass().getName();
	public static final Uri uri = Uri.parse("content://"
			+ BaseTableColumns.AUTHORITY + "/" + TestItemColumns.TABLE_NAME);
	private static final UriMatcher URIMATCHER;
	private static final int PLUGIN = 0; // 单条匹配
	private static final int PLUGINS = 1; // 多条匹配
	static {
		URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URIMATCHER.addURI(BaseTableColumns.AUTHORITY,
				TestItemColumns.TABLE_NAME, PLUGINS);
		URIMATCHER.addURI(BaseTableColumns.AUTHORITY, TestItemColumns.TABLE_NAME
				+ "/#", PLUGIN);
	}
	private DBHelper mDBHelper = null;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDBHelper = DBHelper.getInstance(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		AppLog.e(tag, "insert--->多条记录匹配");
		Uri resultUri = null;
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			long results = database.insert(TestItemColumns.TABLE_NAME, null,
					values);
			resultUri = ContentUris.withAppendedId(uri, results);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return resultUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = -1;
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			count = database.delete(TestItemColumns.TABLE_NAME, selection,
					selectionArgs);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = -1;
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			database.update(TestItemColumns.TABLE_NAME, values, selection,
					selectionArgs);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
			if (database != null)
				database.close();
		}
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		SQLiteDatabase database = mDBHelper.getReadableDatabase();
		try {
			cursor = database.query(TestItemColumns.TABLE_NAME, null, selection,
					selectionArgs, null, null, sortOrder);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		} finally {
//			if (database != null)
//				database.close();
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int flag = URIMATCHER.match(uri);
		switch (flag) {
		case PLUGIN: // vnd.android.cursor.dir
			return "vnd.android.cursor.dir/" + TestItemColumns.TABLE_NAME;
		case PLUGINS:// vnd.android.cursor.item
			return "vnd.android.cursor.item/" + TestItemColumns.TABLE_NAME;
		default:
			break;
		}
		return null;
	}

}
