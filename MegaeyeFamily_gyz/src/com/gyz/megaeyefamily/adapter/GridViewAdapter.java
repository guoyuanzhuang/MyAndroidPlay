package com.gyz.megaeyefamily.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gyz.megaeyefamily.ui.R;
import com.gyz.megaeyefamily.view.AutoScrollTextView;

public class GridViewAdapter extends BaseAdapter {

	public List<File> fileArray = null;
	private LayoutInflater inFlater = null;
	private Context mContext;

	public GridViewAdapter(Context context) {
		this.mContext = context;
		inFlater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (fileArray == null)
			return 0;
		return fileArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (fileArray == null)
			return null;
		return fileArray.get(position);
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
			convertView.setTag(hodler);
		} else {
			hodler = (Viewhold) convertView.getTag();
		}

		// long modified = PreViewResource.imgURI.get(position).lastModified();
		// Date date = new Date(modified);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// hodler.textview_name.setText(sdf.format(date));
		String filename = fileArray.get(position).getName();
		if (filename.indexOf(".") > 0) {
			Log.e("GridViewAdapter", filename);
			String[] tempStr = filename.split("\\.");
			// 跑马灯
			hodler.runTextView.setText(tempStr[0]);
			hodler.runTextView.init(100);
			hodler.runTextView.startScroll();
			//
			Bitmap bm = null;
			if (tempStr[1].equals("3gp")) {
//				hodler.img.setBackgroundResource(R.drawable.filevideo);
				bm = BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.filevideo);
			} else {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 4;
				bm = BitmapFactory.decodeFile(
						fileArray.get(position).getPath(), opts);
			}
			hodler.img.setImageBitmap(bm);
		}
		return convertView;
	}
}
