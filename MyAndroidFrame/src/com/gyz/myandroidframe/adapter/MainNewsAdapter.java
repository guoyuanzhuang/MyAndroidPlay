package com.gyz.myandroidframe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyz.myandroidframe.R;
import com.gyz.myandroidframe.bean.TestItem;
import com.gyz.myandroidframe.bitmap.BitmapManager;

public class MainNewsAdapter extends ContextBaseAdapter {
	protected Context mContext;
	private List<TestItem> rssList;

	public MainNewsAdapter(Context context) {
		this.mContext = context;
	}

	public void setRSSList(List<TestItem> rssList) {
		this.rssList = rssList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (rssList != null) {
			return rssList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (rssList != null) {
			return rssList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.main_news_items, null);
			holder = new ViewHolder();
			holder.news_photo_iv = (ImageView) convertView
					.findViewById(R.id.news_photo_iv);
			holder.news_title_tv = (TextView) convertView
					.findViewById(R.id.news_title_tv);
			holder.news_des_tv = (TextView) convertView
					.findViewById(R.id.news_des_tv);
			holder.news_date_tv = (TextView) convertView
					.findViewById(R.id.news_date_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TestItem rss = rssList.get(position);
		holder.news_title_tv.setText(rss.getTitle());
		holder.news_des_tv.setText(rss.getDescription());
		holder.news_date_tv.setText(rss.getPubDate());
		BitmapManager bitmapManager = new BitmapManager(mContext,
				BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.ic_launcher));
		bitmapManager.loadBitmap(rss.getLink(), holder.news_photo_iv);
		return convertView;
	}

	class ViewHolder {
		ImageView news_photo_iv;
		TextView news_title_tv;
		TextView news_des_tv;
		TextView news_date_tv;
	}

}
