package com.gyz.myandroidframe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.bean.TestItem;
import com.gyz.myandroidframe.db.DBHelper;
import com.gyz.myandroidframe.db.TestItemColumns;
/**
 * 
 * @author guoyuanzhuang
 *
 */
public class TestItemDao implements BaseDao {
	private final String tag = this.getClass().getName();
	private DBHelper mDBHelper;

	public TestItemDao(Context context) {
		mDBHelper = DBHelper.getInstance(context);
	}

	@Override
	public boolean insert(ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			long count = database.insert(TestItemColumns.TABLE_NAME, null, values);
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
			int count = database.delete(TestItemColumns.TABLE_NAME, whereClause,
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
			int count = database.update(TestItemColumns.TABLE_NAME, values,
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
			cursor = database.query(TestItemColumns.TABLE_NAME, null, selection,
					selectionArgs, null, null, null);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(tag, e.getMessage());
		}
		return cursor;
	}
	
	public List<TestItem> queryTestItems(String selection, String[] selectionArgs){
		List<TestItem> testItemList = new ArrayList<TestItem>();
		Cursor cursor = query(selection, selectionArgs);
		if(cursor != null && cursor.moveToNext()){
			TestItem testItems = new TestItem();
			testItems.setTitle(cursor.getString(cursor.getColumnIndex(TestItemColumns.TEST_TITLE)));
			testItems.setDescription(cursor.getString(cursor.getColumnIndex(TestItemColumns.TEST_DES)));
			testItems.setLink(cursor.getString(cursor.getColumnIndex(TestItemColumns.TEST_LINK)));
			testItems.setPubDate(cursor.getString(cursor.getColumnIndex(TestItemColumns.TEST_DATE)));
			testItemList.add(testItems);
		}
		return testItemList;
	}

}
