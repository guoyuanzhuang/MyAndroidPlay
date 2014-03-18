package com.gyz.myandroidframe.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.widget.RemoteViews;

/**
 * 自定义通知类，用于管理程序中通知
 * 
 * @author guoyuanzhuang
 * 
 */
public class NotificationUtil extends Notification {

	public Context mContext;

	public NotificationUtil(Parcel parcel) {
		super(parcel);
		// TODO Auto-generated constructor stub
	}

	public NotificationUtil(int icon, CharSequence tickerText, long when) {
		super(icon, tickerText, when);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param context
	 * @param icon
	 *            通知图标
	 * @param tickerText
	 *            通知滚动描述
	 * @param flags
	 *            通知显示类型
	 * @param layoutId
	 *            通知布局
	 * @param targetClass
	 *            点击通知跳转
	 */
	public NotificationUtil(Context context, int icon, int tickerText,
			int flags, int layoutId, Class<?> targetClass) {
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.mContext = context;
		this.icon = icon;
		this.tickerText = context.getResources().getString(tickerText);
		this.flags = flags;
		// | DEFAULT_VIBRATE
		// this.defaults = Notification.DEFAULT_SOUND;		//通知声音
		RemoteViews mRemoteView = new RemoteViews(context.getPackageName(),
				layoutId);
		this.contentView = mRemoteView;
		Intent intent = new Intent(context, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		this.contentIntent = pIntent;
	}
}
