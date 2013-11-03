package com.gyz.myandroidframe.dataparse;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.bean.BaseEntity;
import com.gyz.myandroidframe.bean.User;
import com.gyz.myandroidframe.bean.User.UserParse;
import com.gyz.myandroidframe.util.StringUtils;

/**
 * User 用户解析实现类
 * 
 * @author guoyuanzhuang
 * 
 */
public class UserParseImp implements UserParse {
	private String[] tagArr = { "location", "name", "followers", "fans",
			"score", "portrait" };

	@Override
	public User getUsers(InputStream stream) throws AppException {
		// TODO Auto-generated method stub
		User user = new User();
		try {
			// 获得XmlPullParser解析器
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(stream, BaseEntity.UTF8);
			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:
					// 如果是标签开始，则说明需要实例化对象了
					if (tag.equalsIgnoreCase(tagArr[0])) {
						user.setLocation(xmlParser.nextText());
					} else if (tag.equalsIgnoreCase(tagArr[1])) {
						user.setName(xmlParser.nextText());
					} else if (tag.equalsIgnoreCase(tagArr[2])) {
						user.setFollowers(StringUtils.toInt(
								xmlParser.nextText(), 0));
					} else if (tag.equalsIgnoreCase(tagArr[3])) {
						user.setFans(StringUtils.toInt(xmlParser.nextText(), 0));
					} else if (tag.equalsIgnoreCase(tagArr[4])) {
						user.setScore(StringUtils.toInt(xmlParser.nextText(), 0));
					} else if (tag.equalsIgnoreCase(tagArr[5])) {
						user.setFace(xmlParser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}
			stream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw AppException.xml(e);
		}
		return user;
	}

}
