package com.gyz.megaeyefamily.dataparse;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.gyz.megaeyefamily.bean.LoginResult;
import com.gyz.megaeyefamily.bean.MegaeyePlayAddress;
import com.gyz.megaeyefamily.bean.WeatherInfoBean;
import com.gyz.megaeyefamily.util.Base64;
import com.gyz.megaeyefamily.util.GZipUtils;

public class SAXService {
	private static SAXService xmlParseService = null;

	/**
	 * 给解析类设置单例
	 * 
	 * @return SAXService
	 */
	public static SAXService getParseInstance() {
		if (xmlParseService == null) {
			xmlParseService = new SAXService();
		}
		return xmlParseService;
	}

	/**
	 * 处理返回结果，Gzip解压
	 * 
	 * @param results
	 * 
	 * @return 返回XML 实体的单一输入源
	 */
	public InputSource onGetGZIPXMLInputStream(String results) {
		InputSource source = null;
		try {
			results = results.replace("&amp;", "#");
			results = new String(results.getBytes(), "UTF-8");
			int index = results.indexOf("<ns:return>");
			int end = results.indexOf("</ns:return>");
			if (index > 0 && end > 0 && end > index) {
				index = index + 11;
				String temp_results = results.substring(index, end);// 获取base64信息
				byte[] temp_byte = Base64.decode(temp_results);// 将base64数据转换为bate数组，解析数据获取图片信息
				byte[] result_byte = GZipUtils.decompress(temp_byte);
				results = new String(result_byte, "UTF-8");
				System.out.println("解压之后的数据:" + results.trim());
				StringReader read = new StringReader(results.trim());
				source = new InputSource(read);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return source;
	}

	/**
	 * 请求天气信息
	 * 
	 * @param strxml
	 * @return
	 */
	public List<WeatherInfoBean> onGetWeatherInfo(String strxml) {
		List<WeatherInfoBean> weatherInfoList = null;
		InputSource input = onGetGZIPXMLInputStream(strxml);
		if (input != null) {
			try {
				SAXParserFactory sax = SAXParserFactory.newInstance();
				SAXParser parse = sax.newSAXParser();
				XMLReader xr = parse.getXMLReader();
				WeatherInfoHandle weatherHandle = new WeatherInfoHandle();
				xr.setContentHandler(weatherHandle);
				xr.parse(input);
				weatherInfoList = weatherHandle.beans;
			} catch (Exception e) {
				// TODO Auto-generated catch block;
				e.printStackTrace();
			}
		}
		return weatherInfoList;
	}

	/**
	 * 请求登录信息
	 * 
	 * @param strxml
	 * @return
	 */
	public LoginResult onGetLoginResult(String strxml) {
		LoginResult loginResult = null;
		InputSource input = onGetGZIPXMLInputStream(strxml);
		if (input != null) {
			try {
				SAXParserFactory sax = SAXParserFactory.newInstance();
				SAXParser parse = sax.newSAXParser();
				XMLReader xr = parse.getXMLReader();
				LoginResultHandle loginResultHandle = new LoginResultHandle();
				xr.setContentHandler(loginResultHandle);
				xr.parse(input);
				loginResult = loginResultHandle.loginResult;
			} catch (Exception e) {
				// TODO Auto-generated catch block;
				e.printStackTrace();
			}
		}
		return loginResult;
	}

	/**
	 * 获取视频播放地址
	 * 
	 * @param strxml
	 * @return
	 */
	public MegaeyePlayAddress onGetPlayAddress(String strxml) {
		MegaeyePlayAddress playAddress = null;
		InputSource input = onGetGZIPXMLInputStream(strxml);
		if (input != null) {
			try {
				SAXParserFactory sax = SAXParserFactory.newInstance();
				SAXParser parse = sax.newSAXParser();
				XMLReader xr = parse.getXMLReader();
				PlayAddressHandle playAddHandle = new PlayAddressHandle();
				xr.setContentHandler(playAddHandle);
				xr.parse(input);
				playAddress = playAddHandle.playAddress;
			} catch (Exception e) {
				// TODO Auto-generated catch block;
				e.printStackTrace();
			}
		}
		return playAddress;
	}
}
