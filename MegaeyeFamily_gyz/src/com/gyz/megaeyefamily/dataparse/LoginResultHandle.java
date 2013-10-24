package com.gyz.megaeyefamily.dataparse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.gyz.megaeyefamily.bean.LoginResult;
import com.gyz.megaeyefamily.bean.MegaeyeInfo;

public class LoginResultHandle extends DefaultHandler {
	private String[] returns = { "Authorization", "RetMsg", "PuName",
			"PuId-ChannelNo", "PtzFlag", "Online", "UserId" };

	private String cq = "ItemList"; // 长期
	private String infos = "return";

	// 标签名称
	private String nodeName = null;

	// DoctorOrders对象
	public MegaeyeInfo megaeyeInfo = null;
	public List<MegaeyeInfo> megaeyeInfoList = null;
	public LoginResult loginResult = null;

	/**
	 * 接收文档的开始的通知
	 */
	@Override
	public void startDocument() throws SAXException {
		Log.e("LoginResultHandle解析", "**********解析开始******************");
		megaeyeInfoList = new ArrayList<MegaeyeInfo>();
	}

	/**
	 * 接收文档的开始的通知
	 */
	@Override
	public void endDocument() throws SAXException {
		Log.e("LoginResultHandler解析", "**********解析结束******************");
		returns = null;
		cq = null;
		nodeName = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		nodeName = localName;
		if (infos.equals(localName)) {
			loginResult = new LoginResult();
		} else if (cq.equals(localName)) {
			megaeyeInfo = new MegaeyeInfo();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if (nodeName != null) {
			String temp = new String(ch, start, length);
			if (nodeName.equals(returns[0])) {
				loginResult.code = Integer.valueOf(temp);
			} else if (nodeName.equals(returns[1])) {
				loginResult.erroMsg = temp;
			} else if (nodeName.equals(returns[2])) {
				megaeyeInfo.puName = temp;
			} else if (nodeName.equals(returns[3])) {
				megaeyeInfo.channelNo = temp;
			} else if (nodeName.equals(returns[4])) {
				megaeyeInfo.ptzFlag = Integer.valueOf(temp);
			} else if (nodeName.equals(returns[5])) {
				megaeyeInfo.online = Integer.valueOf(temp);
			} else if (nodeName.equals(returns[6])) {
				megaeyeInfo.userId = temp;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (infos.equals(localName)) {
			loginResult.megaeyeInfoList = megaeyeInfoList;
		} else if (cq.equals(localName)) {
			megaeyeInfoList.add(megaeyeInfo);
			megaeyeInfo = null;
		}
		nodeName = localName;
	}
}
