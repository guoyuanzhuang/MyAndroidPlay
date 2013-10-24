package com.gyz.megaeyefamily.dataparse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gyz.megaeyefamily.bean.WeatherInfoBean;

/**
 * 
 * 获取天气预报的信息
 * 
 * @author HaoZhiDong
 * 
 */
public class WeatherInfoHandle extends DefaultHandler {
	// WeatherBean对象解析全部标签
	private String[] returns = { "date", "details", "exponent", "temperature",
			"weatherP1", "weatherP2", "wind" };
	// WeatherBean对象解析开始标签
	private String infos = "weatherInfo";//weatherInfo
	// 标签名称
	private String nodeName = null;
	// WeatherBean对象
	private WeatherInfoBean bean = null;
	// WeatherBean集合
	public List<WeatherInfoBean> beans = null;
	private boolean isReadBlogInfo = false;
	// 获取结果值的StirngBuffer 防止chars一次性无法读取完整
	StringBuffer readData = new StringBuffer();

	/**
	 * 
	 * 接收文档的开始的通知
	 */
	@Override
	public void startDocument() throws SAXException {
		beans = new ArrayList<WeatherInfoBean>();
		isReadBlogInfo = false;
	}

	/**
	 * 接收元素开始的通知。 参数意义如下： uri ：元素的命名空间 localName ：元素的本地名称（不带前缀） qName
	 * ：元素的限定名（带前缀） atts ：元素的属性集合
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (infos.equals(localName)) {
			bean = new WeatherInfoBean();
		}
		isReadBlogInfo = true;
		nodeName = localName;
	}

	/**
	 * 接收字符数据的通知。 相当于获取节点上的节点值（nodeValue）
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if (nodeName != null && isReadBlogInfo) {
			String data = new String(ch, start, length);
			data = data.replace("#", "&");
			readData.append(data);
			if (nodeName.equals(returns[0])) {
				bean.date = readData.toString().trim();
			} else if (nodeName.equals(returns[1])) {
				bean.details = readData.toString().trim();
			} else if (nodeName.equals(returns[2])) {
				bean.exponent = readData.toString().trim();
			} else if (nodeName.equals(returns[3])) {
				bean.temperature = readData.toString().trim().replace("/", "~");// 修改温度格式
			} else if (nodeName.equals(returns[4])) {
				bean.weatherP1 = readData.toString().trim();
			} else if (nodeName.equals(returns[5])) {
				bean.weatherP2 = readData.toString().trim();
			} else if (nodeName.equals(returns[6])) {
				bean.wind = readData.toString().trim();
			}
		}
	}

	/**
	 * 接收元素的结尾的通知。 参数意义如下： uri ：元素的命名空间 localName ：元素的本地名称（不带前缀） qName
	 * ：元素的限定名（带前缀）
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (infos.equals(localName)) {
			if (bean.date != null) {
				beans.add(bean);
			}
			bean = null;
		}
		isReadBlogInfo = false;
		readData.delete(0, readData.length());
		nodeName = localName;
	}
}
