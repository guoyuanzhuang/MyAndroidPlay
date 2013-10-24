package com.gyz.megaeyefamily.bean;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingBean {

	private String defImgPathAdd = "/sdcard/megaeyefamily";

	// 0 UDP 1 tcp
	public int netType = 0;

	public static int screenHeight;
	public static int screenWidth;

	public static long uploadData = 0;

	public int streamType = 0;

	private boolean inited = false;// 是否已经初始化
	private String userName;// 用户名
	private String password;// 密码
	private boolean isSave;// 是否保存
	private int streamingType;// 视频流类型
	private String serverAdd;// 服务器地址
	private String prefix;// 客户域名
	private String imageSaveAdd;// 图片录像保存地址
	private int ptStepSize;// 云台步长（Pan/Tilt）步长
	private int zStepSize;// 镜头（Zoom）步长
	private String version;
	private boolean isPrompt;// 设置是否登录页面是否提示
	private int resolution;// 分辨率级别

	private HashMap<String, String> hadLoveVideo;
	private HashMap<String, String> playLatelyVideoMap;
	private SharedPreferences loveVideoSharePer;

	private byte[] databytes;

	public byte[] getDatabytes() {
		return databytes;
	}

	public void setDatabytes(byte[] databytes) {
		this.databytes = databytes;
	}

	public SharedPreferences getLoveVideoSharePerfer(Context context) {

		if (loveVideoSharePer == null) {
			loveVideoSharePer = context.getSharedPreferences("loveVideo", 1);
		}
		return loveVideoSharePer;
	}

	public boolean isPrompt() {
		return isPrompt;
	}

	public void setPrompt(boolean isPrompt) {
		this.isPrompt = isPrompt;
	}

	private double hh_bili;
	private double hw_bili;
	private int p_width;
	private int p_height;

	public HashMap<String, String> getPlayLatelyVideoMap() {
		if (playLatelyVideoMap == null) {
			playLatelyVideoMap = new HashMap<String, String>();
		}

		return playLatelyVideoMap;
	}

	public HashMap<String, String> getHadLoveVideo() {
		if (hadLoveVideo == null) {
			hadLoveVideo = new HashMap<String, String>();
		}

		return hadLoveVideo;
	}

	public int getP_width() {
		return p_width;
	}

	public void setP_width(int pWidth) {
		p_width = pWidth;
	}

	public int getP_height() {
		return p_height;
	}

	public void setP_height(int pHeight) {
		p_height = pHeight;
	}

	public double getHh_bili() {
		return hh_bili;
	}

	public void setHh_bili(double hhBili) {
		hh_bili = hhBili;
	}

	public double getHw_bili() {
		return hw_bili;
	}

	public void setHw_bili(double hwBili) {
		hw_bili = hwBili;
	}

	private SettingBean() {
		super();
	}

	// 只产生一个实例
	private static SettingBean instance = null;

	// 获取到实例
	public static synchronized SettingBean getInstance() {
		if (instance == null) {
			instance = new SettingBean();
		}
		return instance;
	}

	public boolean isInited() {
		return inited;
	}

	public void setInited(boolean inited) {
		this.inited = inited;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public int getStreamingType() {
		return streamingType;
	}

	public void setStreamingType(int streamingType) {
		this.streamingType = streamingType;
	}

	public String getServerAdd() {
		return serverAdd;
	}

	public void setServerAdd(String serverAdd) {
		this.serverAdd = serverAdd;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getImageSaveAdd(Context context) {
		imageSaveAdd = context.getSharedPreferences("imgPath", 0).getString(
				"imgPathAdd", defImgPathAdd);
		return imageSaveAdd;
	}

	public void setImageSaveAdd(String imageSaveAdd, Context context) {
		context.getSharedPreferences("imgPath", 0).edit()
				.putString("imgPathAdd", imageSaveAdd).commit();
		this.imageSaveAdd = imageSaveAdd;
	}

	public int getPtStepSize() {
		return ptStepSize;
	}

	public void setPtStepSize(int ptStepSize) {
		this.ptStepSize = ptStepSize;
	}

	public int getZStepSize() {
		return zStepSize;
	}

	public void setZStepSize(int stepSize) {
		zStepSize = stepSize;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
}
