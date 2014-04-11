package com.gyz.myandroidframe.dataparse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.bean.RSSItem;
import com.gyz.myandroidframe.bean.RSSItem.RSSParse;

public class RSSItemsParseImp implements RSSParse {

	@Override
	public List<RSSItem> getRSSItems(InputStream stream) throws AppException {
		// TODO Auto-generated method stub
		List<RSSItem> RSSItems = new ArrayList<RSSItem>();
		RSSItem rssItem = null;
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(stream, "UTF-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					if (tag.equalsIgnoreCase("item")) {
						rssItem = new RSSItem();
					} else if (rssItem != null) {
						if (tag.equalsIgnoreCase("title")) {
							rssItem.setTitle(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("description")) {
							rssItem.setDescription(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("link")) {
							rssItem.setLink(xmlParser.nextText());
						} else if (tag.equalsIgnoreCase("pubDate")) {
							rssItem.setPubDate(xmlParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					if (tag.equalsIgnoreCase("item") && rssItem != null) {
						RSSItems.add(rssItem);
						rssItem = null;
					}
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
			stream.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw AppException.xml(e);
		}
		return RSSItems;
	}

}
