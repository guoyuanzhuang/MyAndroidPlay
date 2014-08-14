package com.gyz.myandroidframe.db;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

public class TestItemColumns extends BaseTableColumns {
	public static final String TABLE_NAME = "testitems";
	//
	public static final String TEST_TITLE = "title";
	public static final String TEST_LINK = "link";
	public static final String TEST_DES = "description";
	public static final String TEST_DATE = "pudate";
	//
	private static final Map<String, String> mColumnMap = new HashMap<String, String>();
	static {
		mColumnMap.put(_ID, "integer primary key autoincrement");
		mColumnMap.put(TEST_TITLE, "varchar(100) not null");
		mColumnMap.put(TEST_LINK, "text not null");
		mColumnMap.put(TEST_DES, "text not null");
		mColumnMap.put(TEST_DATE, "localtime");
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> getTableMap() {
		// TODO Auto-generated method stub
		return mColumnMap;
	}
}
