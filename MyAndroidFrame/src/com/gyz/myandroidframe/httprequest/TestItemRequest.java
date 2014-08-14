package com.gyz.myandroidframe.httprequest;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.bean.TestItem;
import com.gyz.myandroidframe.bean.TestItem.TestParse;
import com.gyz.myandroidframe.httpparse.TestItemsParseImp;
import com.gyz.myandroidframe.httprequest.base.HttpConnectPools;
import com.gyz.myandroidframe.httprequest.base.HttpUrls;

/**
 * DoGet 请求实例
 * 
 * @author guoyuanzhuang
 * 
 */
public class TestItemRequest extends HttpConnectPools {

	public TestItemRequest(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getMethod() {
		// TODO Auto-generated method stub
		return HttpConnectPools.DOGET;
	}

	@Override
	protected String getHttpUrl() {
		// TODO Auto-generated method stub
		return HttpUrls.HTTP_TEST_REQUEST;
	}
	
	//设置请求参数
	public void setRequestParams(String userId, String imsi, String version) {
		
	}

	@Override
	protected List<NameValuePair> getRequestParams() {
		// TODO Auto-generated method stub
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		return null;
	}

	@Override
	protected Serializable parseReponseData(InputStream mInputStream)
			throws AppException {
		// TODO Auto-generated method stub
		TestParse rssParse = new TestItemsParseImp();
		List<TestItem> rssItem = rssParse.getTestItems(mInputStream);
		return (Serializable) rssItem;
	}
}
