package com.gyz.megaeyefamily.util;

import java.text.SimpleDateFormat;
import java.util.Date;
public class TimeTools {
	/**
	 * 得到“月月日日”的当前时间
	 * 
	 * @return
	 */
	public static String getTimestamp() {
		java.text.SimpleDateFormat formater = new SimpleDateFormat("MMdd");
		// "yyyy-MM-dd HH:mm:ss"
		return formater.format(new Date());
	}
	
	/**
	 * 得到“月月日日”的当前时间
	 * 
	 * @return
	 */
	public static String getCurTime() {
		java.text.SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		// "yyyy-MM-dd HH:mm:ss"
		return formater.format(new Date());
	}

	/**
	 * 得到毫秒级的当前时间
	 * 
	 * @return
	 */
	public static String getTimestamp_mmm() {
		java.text.SimpleDateFormat formater = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss:mmm");
		return formater.format(new Date());
	}

	/**
	 * 返回自1970 年 1 月 1 日午夜以来的毫秒数
	 * 
	 * @return
	 */
	public static String getTimestamp_long() {
		Date dt = new Date();
		return Long.toString(dt.getTime());
	}

	/**
	 * 得到当前时间之前time毫秒的时间值，返回秒级别
	 * 
	 * @param time
	 * @return
	 */
	public static String getBeforTimestamp(long time) {
		java.text.SimpleDateFormat formater = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date dt = new Date();
		dt.setTime(dt.getTime() - time);
		return formater.format(dt);
	}

	/**
	 * 得到当前时间之后time毫秒的时间,返回秒级别1
	 * 
	 * @param time
	 * @return
	 */
	public static String getAfterTimestamp(long time) {
		java.text.SimpleDateFormat formater = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date dt = new Date();
		dt.setTime(dt.getTime() + time);
		return formater.format(dt);
	}

}
