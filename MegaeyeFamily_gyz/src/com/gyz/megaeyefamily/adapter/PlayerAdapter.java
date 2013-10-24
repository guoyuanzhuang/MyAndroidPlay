package com.gyz.megaeyefamily.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gyz.megaeyefamily.ui.R;
import com.gyz.megaeyefamily.view.AutoScrollTextView;

public class PlayerAdapter extends BaseAdapter {

	public List<File> fileList = null;
	LayoutInflater inFlater = null;

	public PlayerAdapter(Context context) {
		inFlater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (fileList == null)
			return 0;
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (fileList == null)
			return null;
		return fileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Viewhold {
		public ImageView img;
		// public TextView textview_name;
		AutoScrollTextView runTextView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewhold hodler = null;
		if (convertView == null) {
			hodler = new Viewhold();
			convertView = inFlater
					.inflate(R.layout.file_bowser_item_view, null);
			hodler.img = (ImageView) convertView
					.findViewById(R.id.imageView_title);
			// hodler.textview_name = (TextView) convertView
			// .findViewById(R.id.textview_name);
			hodler.runTextView = (AutoScrollTextView) convertView
					.findViewById(R.id.textview_name);
//			int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
//			int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
//			hodler.runTextView.measure(w, h);  
//			int height =hodler.runTextView.getMeasuredHeight();  
//			int width =hodler.runTextView.getMeasuredWidth();  
//			Log.e("PlayerAdapter", "\n"+height+","+width);  
			convertView.setTag(hodler);
		} else {
			hodler = (Viewhold) convertView.getTag();
		}

		// 跑马灯
		String filename = fileList.get(position).getName();
		String fname = filename.substring(0, filename.lastIndexOf("."));
		Log.e("PlayerAdapter", fname);
		hodler.runTextView.setText(fname);
		hodler.runTextView.init(100);
		hodler.runTextView.startScroll();
		hodler.img.setBackgroundResource(R.drawable.filevideo);

		return convertView;
	}

}
