package com.gyz.myandroidframe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public final String tag = this.getClass().getName();
	//
	public static String DBNAME = "myandroidframe.db";
	public static int DBVERSION = 1;

	public static DBHelper mDBHelper;

	public static DBHelper getInstance(Context context) {
		if (mDBHelper == null) {
			mDBHelper = new DBHelper(context);
		}
		return mDBHelper;
	}

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
		// TODO Auto-generated constructor stub
	}

	// 仅在创建数据库时执行一次,且调用前需执行getReadableDatabase/getWriteableDatabase
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.e(tag, "onCreate");
		operateTable(db, "");
	}

	public void operateTable(SQLiteDatabase db, String actionString) {
		Class<BaseTableColumns>[] columnsClasses = BaseTableColumns
				.getSubClasses();
		BaseTableColumns columns = null;

		for (int i = 0; i < columnsClasses.length; i++) {
			try {
				columns = columnsClasses[i].newInstance();
				if ("".equals(actionString) || actionString == null) {
					db.execSQL(columns.getTableCreateor());
				} else {
					db.execSQL(actionString + columns.getTableName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 仅在数据库版本更新时执行
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.e(tag, "onUpgrade");
		operateTable(db, "DROP TABLE IF EXISTS ");
		onCreate(db);
	}

}
