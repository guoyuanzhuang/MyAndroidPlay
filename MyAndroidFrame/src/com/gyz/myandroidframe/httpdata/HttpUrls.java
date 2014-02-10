package com.gyz.myandroidframe.httpdata;
/**
 * URL类：定义HTTP交互方式和协议
 * @author guoyuanzhuang
 *
 */
public class HttpUrls {
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	public final static String HOST = "www.oschina.net";
	private final static String URL_SPLITTER = "/";
	//登录链接
	public final static String LOGIN_VALIDATE_HTTPS = HTTPS + HOST + URL_SPLITTER + "action/api/login_validate";
}
