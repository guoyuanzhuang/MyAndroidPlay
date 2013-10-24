package com.gyz.megaeyefamily.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyz.megaeyefamily.bean.MegaeyeInfo;
import com.gyz.megaeyefamily.ui.R;

public class MegaeyeGridAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	public List<MegaeyeInfo> menuList = null;

	public MegaeyeGridAdapter(Context mContext) {
		super();
		this.mInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 控件 Holder 实体类
	 */
	class GridHolder {
		ImageView megaeyeitem_img_ib;
		TextView megaeyeitem_name_tv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (menuList == null) {
			return 0;
		}
		return menuList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	public int getChildWidth;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GridHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.megaeyeplayer_item, null);
			holder = new GridHolder();
			holder.megaeyeitem_img_ib = (ImageView) convertView
					.findViewById(R.id.megaeyeitem_img_ib);
			holder.megaeyeitem_name_tv = (TextView) convertView
					.findViewById(R.id.megaeyeitem_name_tv);
			convertView.setTag(holder);
		} else {
			holder = (GridHolder) convertView.getTag();
		}
		MegaeyeInfo megaeyeinfo = menuList.get(position);
		
		if (megaeyeinfo != null) {
//			Log.e("文件名称>>>>>>>>>>>>>>", megaeyeinfo.puName);
			holder.megaeyeitem_name_tv.setText(megaeyeinfo.puName);
			if (megaeyeinfo.tempBitmap != null) {
				holder.megaeyeitem_img_ib.setBackgroundResource(0);
				holder.megaeyeitem_img_ib
						.setImageBitmap(megaeyeinfo.tempBitmap);
			} else {
				holder.megaeyeitem_img_ib.setImageBitmap(null);
				holder.megaeyeitem_img_ib
						.setBackgroundResource(R.drawable.moren);
			}
		}
		return convertView;
	}

	public void exit() {
		menuList = null;
		notifyDataSetChanged();
		mInflater = null;
	}

}
