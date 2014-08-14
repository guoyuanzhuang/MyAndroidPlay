package com.gyz.myandroidframe.httpparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.bean.TestItem;
import com.gyz.myandroidframe.bean.TestItem.TestParse;

public class TestItemsParseImp implements TestParse {

	@Override
	public List<TestItem> getTestItems(InputStream stream) throws AppException {
		// TODO Auto-generated method stub
		List<TestItem> RSSItems = new ArrayList<TestItem>();
		TestItem rssItem = null;
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(stream, "UTF-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equalsIgnoreCase("item")) {
						rssItem = new TestItem();
					} else if (rssItem != null) {
						if (tag.equalsIgnoreCase("title")) {
							rssItem.setTitle(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("description")) {
							rssItem.setDescription(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("imageurl")) {
							rssItem.setLink(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pubDate")) {
							rssItem.setPubDate(xmlParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					if (tag.equalsIgnoreCase("item") && rssItem != null) {
						AppLog.i(tag, rssItem.toString());
						RSSItems.add(rssItem);
						rssItem = null;
					}
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw AppException.xml(e);
		}finally{
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw AppException.xml(e);
				}
			}
		}
		return RSSItems;
	}

}
