package com.gyz.megaeyefamily.bean;

import java.util.List;

public class LoginResult {
	public int code;			//返回结果		0:成功 1：失败    2：非电信手机
	public String erroMsg;				//错误
	public List<MegaeyeInfo> megaeyeInfoList;
	 
}
