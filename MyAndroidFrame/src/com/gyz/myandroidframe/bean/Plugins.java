package com.gyz.myandroidframe.bean;

import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

public class Plugins extends BaseEntity{
	private String pluginDesc; // 插件描述
	private String pluginFileUrl;// 插件下载地址
	private String pluginIconUrl;// 插件图标下载地址
	private int pluginId; // 插件id
	private String pluginName; // 插件名称
	private String pluginPackName; // 插件包名
	private int pluginStarted;
	private String pluginVersion; // 插件版本
	private int pluginVersionCode; // 插件版本号
	private String pluginPackActivity;// 插件启动acitivity包名

	public interface PluginParse {
		List<Plugins> getPlugins(InputStream mInputStream) throws XmlPullParserException;
	}
	public Plugins() {
		
	}

	public String getPluginDesc() {
		return pluginDesc;
	}

	public void setPluginDesc(String pluginDesc) {
		this.pluginDesc = pluginDesc;
	}

	public String getPluginFileUrl() {
		return pluginFileUrl;
	}

	public void setPluginFileUrl(String pluginFileUrl) {
		this.pluginFileUrl = pluginFileUrl;
	}

	public String getPluginIconUrl() {
		return pluginIconUrl;
	}

	public void setPluginIconUrl(String pluginIconUrl) {
		this.pluginIconUrl = pluginIconUrl;
	}

	public int getPluginId() {
		return pluginId;
	}

	public void setPluginId(int pluginId) {
		this.pluginId = pluginId;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginPackName() {
		return pluginPackName;
	}

	public void setPluginPackName(String pluginPackName) {
		this.pluginPackName = pluginPackName;
	}

	public int getPluginStarted() {
		return pluginStarted;
	}

	public void setPluginStarted(int pluginStarted) {
		this.pluginStarted = pluginStarted;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public int getPluginVersionCode() {
		return pluginVersionCode;
	}

	public void setPluginVersionCode(int pluginVersionCode) {
		this.pluginVersionCode = pluginVersionCode;
	}

	public String getPluginPackActivity() {
		return pluginPackActivity;
	}

	public void setPluginPackActivity(String pluginPackActivity) {
		this.pluginPackActivity = pluginPackActivity;
	}

}
