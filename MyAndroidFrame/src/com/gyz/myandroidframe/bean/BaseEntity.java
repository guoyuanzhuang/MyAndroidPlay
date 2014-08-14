package com.gyz.myandroidframe.bean;

import java.io.InputStream;
import java.io.Serializable;
import com.gyz.myandroidframe.app.AppException;
/**
 * 
 * @ClassName BaseEntity 
 * @Description 实体基类
 * @author guoyuanzhuang@gmail.com 
 * @date 2014-4-20 上午12:31:56 
 *
 */
public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6595580076518749594L;

	public final static String UTF8 = "UTF-8";
	
	//返回码
	public int returnCode = 0;
	//返回信息
	public String returnMsg = "";
	
	public interface ResponseMarkParse{
		BaseEntity getResponseMark(InputStream mInputStream) throws AppException;
	}
}
