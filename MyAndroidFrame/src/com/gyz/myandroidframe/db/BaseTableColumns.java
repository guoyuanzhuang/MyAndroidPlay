package com.gyz.myandroidframe.db;

import java.util.ArrayList;
import java.util.Map;

import android.provider.BaseColumns;

public abstract class BaseTableColumns implements BaseColumns {
	//ContentProvider
	public static final String AUTHORITY = "com.gyz.myandroidframe.dao";
	
	public static final String[] SUBCLASSES = new String[] { "com.gyz.myandroidframe.db.TestItemColumns" };

	public String getTableCreateor() {
		return getTableCreator(getTableName(), getTableMap());
	}

	@SuppressWarnings("unchecked")
	public static final Class<BaseTableColumns>[] getSubClasses() {
		ArrayList<Class<BaseTableColumns>> classes = new ArrayList<Class<BaseTableColumns>>();
		Class<BaseTableColumns> subClass = null;
		for (int i = 0; i < SUBCLASSES.length; i++) {
			try {
				subClass = (Class<BaseTableColumns>) Class.forName(SUBCLASSES[i]);
				classes.add(subClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}
		return classes.toArray(new Class[0]);
	}

	private static final String getTableCreator(String tableName,
			Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		String value = null;
		StringBuilder creator = new StringBuilder();
		creator.append("CREATE TABLE ").append(tableName).append("( ");
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			value = map.get(keys[i]);
			creator.append(keys[i]).append(" ");
			creator.append(value);
			if (i < length - 1) {
				creator.append(",");
			}
		}
		creator.append(")");
		return creator.toString();
	}

	abstract public String getTableName();

	abstract protected Map<String, String> getTableMap();

}
