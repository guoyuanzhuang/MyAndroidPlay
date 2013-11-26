package com.gyz.myandroidframe.db;

import java.util.HashMap;
import java.util.Map;

public class PluginsColumns extends BaseTableColumns {
	public static final String TABLE_NAME = "plugins";
	//
	public static final String USER_URL = "url";
	public static final String USER_CONTENT = "content";
	public static final String USER_TIME = "update_time";
	//
	private static final Map<String, String> mColumnMap = new HashMap<String, String>();
	static {
		mColumnMap.put(_ID, "integer primary key autoincrement");
		mColumnMap.put(USER_URL, "varchar(100) not null");
		mColumnMap.put(USER_CONTENT, "text not null");
		mColumnMap.put(USER_URL, "localtime");
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
