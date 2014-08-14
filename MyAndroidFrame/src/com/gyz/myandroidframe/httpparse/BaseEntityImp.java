package com.gyz.myandroidframe.httpparse;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.bean.BaseEntity;
import com.gyz.myandroidframe.bean.BaseEntity.ResponseMarkParse;

/**
 * 解析服务器返回内容的有效性
 * @author guoyuanzhuang@gmail.com
 * <Response ReturnCode = "0" ReturnMsg = "Success">
 * .....
 * </Response>
 */
public class BaseEntityImp implements ResponseMarkParse {

	@Override
	public BaseEntity getResponseMark(InputStream mInputStream)
			throws AppException {
		// TODO Auto-generated method stub
		BaseEntity baseEntity = null;
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(mInputStream, BaseEntity.UTF8);
			// evtType = xmlParser.getEventType();
			int evtType = xmlParser.next();
			if (evtType == XmlPullParser.START_TAG) {
				if (xmlParser.getName().equalsIgnoreCase("Response")) {
					baseEntity = new BaseEntity();
					baseEntity.returnCode = Integer.parseInt(xmlParser
							.getAttributeValue(0));
					baseEntity.returnMsg = xmlParser.getAttributeValue(1);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw AppException.xml(e);
		} finally {
			if (mInputStream != null) {
				try {
					mInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw AppException.xml(e);
				}
			}
		}
		return baseEntity;
	}

}
