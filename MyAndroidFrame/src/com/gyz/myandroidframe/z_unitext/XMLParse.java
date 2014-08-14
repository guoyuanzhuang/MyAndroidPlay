package com.gyz.myandroidframe.z_unitext;

import java.io.ByteArrayInputStream;

import android.test.AndroidTestCase;
import android.util.Log;

import com.gyz.myandroidframe.app.AppException;
import com.gyz.myandroidframe.app.AppLog;
import com.gyz.myandroidframe.bean.BaseEntity;
import com.gyz.myandroidframe.bean.BaseEntity.ResponseMarkParse;
import com.gyz.myandroidframe.httpparse.BaseEntityImp;

public class XMLParse extends AndroidTestCase {
	/**
	 * <Response ReturnCode="0" ReturnMsg="成功"> <SubscribeContentRsp>
	 * <isOrdered>false</isOrdered> <SSOURL></SSOURL> <billingURL></billingURL>
	 * </SubscribeContentRsp> </Response>
	 */
	StringBuffer sb = new StringBuffer();

	public XMLParse() {
		sb.append("<Response ReturnCode=\"0\" ReturnMsg=\"成功\">");
		sb.append("<SubscribeContentRsp>");
		sb.append("<isOrdered>false</isOrdered>");
		sb.append("</SubscribeContentRsp>");
		sb.append("</Response>");
		Log.e("tag", sb.toString());
	}

	public void getReponseMark() {
		ResponseMarkParse rmp = new BaseEntityImp();
		ByteArrayInputStream stream = new ByteArrayInputStream(sb.toString().getBytes());
		try {
			BaseEntity be = rmp.getResponseMark(stream);
			if(be != null){
				AppLog.e("tag", be.returnCode + "");
				AppLog.e("tag", be.returnMsg + "");
			}
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
