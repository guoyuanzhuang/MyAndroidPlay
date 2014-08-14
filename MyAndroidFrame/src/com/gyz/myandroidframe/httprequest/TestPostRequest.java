package com.gyz.myandroidframe.httprequest;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.httprequest.base.HttpConnectPools;

public class TestPostRequest extends HttpConnectPools{

	public TestPostRequest(Context mContext, Handler mHandler) {
		super(mContext, mHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getHttpUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getMethod() {
		// TODO Auto-generated method stub
		return HttpConnectPools.DOPOST;
	}

	@Override
	protected HashMap<String, String> getPostTextContentMap() {
		// TODO Auto-generated method stub
		return super.getPostTextContentMap();
	}

	@Override
	protected HashMap<String, String> getPostAudioPathMap() {
		// TODO Auto-generated method stub
		return super.getPostAudioPathMap();
	}

	@Override
	protected HashMap<String, String> getPostImagePathMap() {
		// TODO Auto-generated method stub
		return super.getPostImagePathMap();
	}

	@Override
	protected Serializable parseReponseData(InputStream mInputStream)
			throws AppException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
