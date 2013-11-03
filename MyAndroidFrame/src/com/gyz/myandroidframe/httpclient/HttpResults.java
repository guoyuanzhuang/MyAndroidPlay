package com.gyz.myandroidframe.httpclient;

import java.io.InputStream;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.bean.User;
import com.gyz.myandroidframe.bean.User.UserParse;
import com.gyz.myandroidframe.dataparse.UserParseImp;

/**
 * 网络请求入口
 * 
 * @author guoyuanzhuang
 * 
 */
public class HttpResults {

	/**
	 * 获取用户信息
	 * @param username
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public static User getUserInfo(String username, String pwd)
			throws AppException {
		NameValuePair[] nameValuePairs = {
				new BasicNameValuePair("username", username),
				new BasicNameValuePair("pwd", pwd),
				new BasicNameValuePair("keep_login", "1") };
		InputStream is = HttpRequest.postHttpURLConnection(
				HttpUrls.LOGIN_VALIDATE_HTTPS, nameValuePairs);
		UserParse mUserParse = new UserParseImp();
		return (is == null) ? null : mUserParse.getUsers(is);
	}
}
