package com.gyz.myandroidframe.httpdata;

import java.io.InputStream;

/**
 * http 请求监听
 * @author guoyuanzhuang
 * 
 */
@Deprecated
public interface HttpListener {
	@Deprecated
	void httpStart();
	@Deprecated
	Object httpFinish(InputStream stream, boolean isCache);
	@Deprecated
	void httpFailed(Exception exc);
}
