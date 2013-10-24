package com.gyz.megaeyefamily.remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class HttpURLRequest {

	private HttpURLConnection httpUrlConnection;
	private int timeOut = 7 * 1000;
	private int responseCode;
	private String lock = "lock";
	private InputStream contentInput;
	public boolean isLogin = false;
	public static String urlHead = "http://218.80.254.70:8080/axis2/services/MyService.MyServiceHttpSoap11Endpoint/";

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * 
	 * */
	public boolean httpGetRequest(String url, DefaultHandler handler) {
		boolean isConnection = false;
		try {
			if (url != null) {
				synchronized (lock) {
					URL httpUrl = new URL(url);
					httpUrlConnection = (HttpURLConnection) httpUrl
							.openConnection();
					httpUrlConnection.setConnectTimeout(timeOut);
					if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						httpRequestForResource(handler);
						isConnection = true;
					}
					lock.notify();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return isConnection;
		}
		return isConnection;
	}

	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * httpPost请求
	 * 
	 * @param urlStr
	 *            连接地址
	 * @param valuesMap
	 *            连接参数
	 * 
	 * */
	@SuppressWarnings("finally")
	public boolean onGetHttpUrlPostState(String urlStr,
			Map<String, String> valuesMap, DefaultHandler handler) {
		boolean isSuccess = false;
		String param = null;
		try {

			HttpPost httpPost = new HttpPost(urlStr);
			byte[] prmByte;
			try {
				Iterator<Map.Entry<String, String>> it = valuesMap.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> me = (Map.Entry<String, String>) it
							.next();
					param = valuesMap.get(me.getKey());
				}
				prmByte = param.getBytes("UTF-8");
				InputStream is = new ByteArrayInputStream(prmByte, 0,
						prmByte.length);
				InputStreamEntity inp = new InputStreamEntity(is,
						prmByte.length);
				httpPost.setEntity(inp);
				httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
				httpPost.addHeader("Accept-Encoding", "gzip,deflate");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
			HttpConnectionParams.setSoTimeout(httpParams, 7000);
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			Log.e("responseCode", responseCode + "");
			if (responseCode == HttpURLConnection.HTTP_OK) {
				if (isLogin) {
					// InstructionsThread.showSteup(1);
				}

				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				// strResult = URLDecoder.decode(strResult, "utf-8");
				int a = strResult.indexOf("<ns:return>");
				int b = strResult.indexOf("</ns:return>");
				String s = null;

				if (a > 0) {
					s = strResult.substring(a + "<ns:return>".length(), b);
					s = s.replaceAll("&lt;", "<");
					s = s.replaceAll("&gt;", ">");
				}
				try {
					Log.e("log", s + "");
					if (s != null) {
						contentInput = new ByteArrayInputStream(s.getBytes());
					} else {
						// InstructionsThread.canPlay = false;
					}

				} catch (Exception e) {
					e.printStackTrace();
					// 在不返回数据的情况下，捕获异常并设置为false标识登录失败
					isSuccess = false;
				}
				if (s != null) {
					isSuccess = true;
				}

				if (isSuccess) {
					httpRequestForResource(handler);
				}

			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess = false;
		} finally {
			return isSuccess;
		}
	}

	/*
	 *
	 * */
	public void ParseSource(DefaultHandler handler, InputSource inputSource) {

		try {

			if (inputSource != null) {

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxPa = factory.newSAXParser();
				XMLReader reader = saxPa.getXMLReader();
				reader.setContentHandler(handler);
				reader.parse(inputSource);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * */
	@SuppressWarnings("finally")
	public InputSource httpRequestForResource(DefaultHandler handler) {
		InputSource inputPource = null;
		DefaultHandler xmlHandler = handler;

		boolean isAppSelf = false;
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxPa = factory.newSAXParser();
			XMLReader reader = saxPa.getXMLReader();

			if (xmlHandler == null) {
				isAppSelf = true;
				xmlHandler = new XMLParseHandler();
			}

			reader.setContentHandler(xmlHandler);
			reader.parse(new InputSource(contentInput));

			if (isLogin) {
				// InstructionsThread.showSteup(2);
			}

			if (isAppSelf) {
				responseCode = Integer
						.parseInt(((XMLParseHandler) xmlHandler).xml);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return inputPource;
		}
	}

	class XMLParseHandler extends DefaultHandler {
		private String tagName;
		private StringBuffer buf = new StringBuffer();
		public String xml;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if ("return".equals(tagName)) {
				buf.append(new String(ch, start, length));
			}
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
			xml = buf.toString();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			tagName = localName;
		}
	}
}
