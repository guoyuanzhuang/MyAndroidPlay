package com.gyz.megaeyefamily.dataparse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.gyz.megaeyefamily.bean.MegaeyePlayAddress;

public class PlayAddressHandle extends DefaultHandler {
	private String[] returns = { "PtzUrl", "VideoPlayUrl" };
	private String infos = "return";
	private String nodeName = null;
	public MegaeyePlayAddress playAddress = null;
	
	StringBuffer readData = new StringBuffer();

	@Override
	public void startDocument() throws SAXException {
		Log.e("PlayAddressHandle解析", "**********解析开始******************");
	}

	@Override
	public void endDocument() throws SAXException {
		returns = null;
		nodeName = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		nodeName = localName;
		if (infos.equals(localName)) {
			playAddress = new MegaeyePlayAddress();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if (nodeName != null) {
			readData.append(new String(ch, start, length));
			if (nodeName.equals(returns[0])) {
				playAddress.ptzUrl = readData.toString();
			} else if (nodeName.equals(returns[1])) {
				playAddress.playUrl = readData.toString();
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		nodeName = localName;
		readData.delete(0, readData.length());
	}
}
