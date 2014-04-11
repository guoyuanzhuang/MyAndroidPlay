package com.gyz.myandroidframe.dataparse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.bean.Plugins;
import com.gyz.myandroidframe.bean.Plugins.PluginParse;

public class PluginsParseImp implements PluginParse {

	@Override
	public List<Plugins> getPlugins(InputStream mInputStream)
			throws XmlPullParserException {
		List<Plugins> pluginList = new ArrayList<Plugins>();
		Plugins plugin = null;
		try {
			// 获得XmlPullParser解析器
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(mInputStream, "UTF-8");
			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equalsIgnoreCase("plugin")) {
						plugin = new Plugins();
					} else if (plugin != null) {
						if (tag.equalsIgnoreCase("pluginDesc")) {
							plugin.setPluginDesc(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pluginFileUrl")) {
							plugin.setPluginFileUrl(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pluginIconUrl")) {
							plugin.setPluginIconUrl(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pluginId")) {
							plugin.setPluginId(Integer.parseInt(xmlParser
									.nextText()));
						} else if (tag.equalsIgnoreCase("pluginName")) {
							plugin.setPluginName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pluginPackName")) {
							plugin.setPluginPackName(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pluginStarted")) {
							plugin.setPluginStarted(Integer.parseInt(xmlParser
									.nextText()));
						} else if (tag.equalsIgnoreCase("pluginVersion")) {
							plugin.setPluginVersion(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pluginVersionCode")) {
							plugin.setPluginVersionCode(Integer
									.parseInt(xmlParser.nextText()));
						} else if (tag.equalsIgnoreCase("pluginPackActivity")) {
							plugin.setPluginPackActivity(xmlParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					if (tag.equalsIgnoreCase("plugin") && plugin != null) {
						pluginList.add(plugin);
						plugin = null;
					}
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
			mInputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
			AppException.xml(e);
		}
		return pluginList;
	}

}
