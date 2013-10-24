package com.gyz.megaeyefamily.bean;

import android.graphics.Bitmap;

/**
 * 监控点基本信息
 * @author guoyuanzhuang
 *
 */
public class MegaeyeInfo {
	public String puName; // PU名称
	public String userId;	//用户id
	public String channelNo; // PU通道号
	public int ptzFlag; // 控制权限标示 0-无权限；1-有权限
	public int online; // 在线标示 0-不在线；1-在线
	public Bitmap tempBitmap = null;		
}
