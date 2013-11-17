package com.gyz.myandroidframe.bean;

import java.io.InputStream;

/**
 * 公共错误信息类
 * 
 * @author guoyuanzhuang
 * 
 */
public class CommonError extends BaseEntity {
	private int errorCode; // 错误码
	private String errorMsg;// 错误信息

	public interface ErrorParse {
		CommonError getErrorInfo(InputStream stream);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
