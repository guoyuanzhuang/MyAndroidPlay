package com.gyz.myandroidframe.bean;

import java.io.Serializable;

public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6595580076518749594L;

	public final static String UTF8 = "UTF-8";
	
	//错误码
	public int returnCode;
	//错误信息
	public String returnMsg = "";
}
