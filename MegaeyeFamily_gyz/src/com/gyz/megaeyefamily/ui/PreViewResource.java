package com.gyz.megaeyefamily.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyz.megaeyefamily.adapter.GridViewAdapter;
import com.gyz.megaeyefamily.bean.SettingBean;
import com.gyz.megaeyefamily.util.FileUtils;

/**
 * 图像库
 * 
 * @author guoyuanzhuang
 * 
 */
public class PreViewResource extends Activity {
	private SettingBean settingBean;
	public static LinkedList<File> video = null; // 视频
	public static LinkedList<File> imgFiles = null; // 图片
	private static String rootPath = "/sdcard/";// 根目录
	public static List<File> fileList = null; // 所有文件

	private Toast toastinfo = null;

	private ImageButton clear_btn, dateBtn;
	private GridView img_GridView = null;
	private GridViewAdapter mAdapter = null;
	private RelativeLayout date_layout = null;
	private DatePicker date = null;
	private View date_dialog_layout = null;
	private ImageButton backBtn = null;

	// private String selectDateStr = null;

	private int removeDelete = -1;

	private SettingBean setBean = SettingBean.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pre_resource_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止自动锁屏
		initUI();
		init_parms();
	}

	private void initUI() {
		// imgURI = new LinkedList<File>();
		RelativeLayout videoplay_lately_top = (RelativeLayout) findViewById(R.id.videoplay_lately_top);
		backBtn = (ImageButton) videoplay_lately_top
				.findViewById(R.id.title_exit_ib);
		clear_btn = (ImageButton) videoplay_lately_top
				.findViewById(R.id.title_set_ib);
		TextView title = (TextView) videoplay_lately_top
				.findViewById(R.id.title_mind_tv);
		title.setText("图像库");

		backBtn.setBackgroundResource(R.drawable.backplay);
		clear_btn.setBackgroundResource(R.drawable.de_selector);

		date_layout = (RelativeLayout) findViewById(R.id.date_layout);
		dateBtn = (ImageButton) findViewById(R.id.date_change);
		//
		img_GridView = (GridView) findViewById(R.id.img_gridView);

		clear_btn.setOnClickListener(basicListener);
		dateBtn.setOnClickListener(basicListener);
		backBtn.setOnClickListener(basicListener);
		img_GridView.setOnItemLongClickListener(itemLonglistener);
		img_GridView.setOnItemClickListener(itemListener);
	}

	private void init_parms() {
		if (!FileUtils.isSDExist()) {
			show_message("检测到您的SD卡不能用，无法查看图片和视频");
			return;
		}
		settingBean = SettingBean.getInstance();// 配置文件
		// 动态获取图片和录像的路径
		rootPath = settingBean.getImageSaveAdd(this);
		if (rootPath == null) {
			rootPath = "/sdcard";
		}
		File fileTemp = new File(rootPath);
		boolean isDir = fileTemp.isDirectory();
		if (isDir && rootPath.subSequence(0, 7).equals("/sdcard")) {
		} else if (isDir && rootPath.subSequence(0, 4).equals("/mnt")) {
		} else {
			show_message("没有视频或图片文件!");
			rootPath = "/sdcard";
		}
		File f = new File(rootPath);
		File[] fileArray = f.listFiles();
		if (fileArray != null && fileArray.length > 0) {
			imgFiles = new LinkedList<File>();
			video = new LinkedList<File>();
			fileList = new ArrayList<File>();
			for (int i = 0; i < fileArray.length; i++) {
				if (fileArray[i].isFile()) {
					fileList.add(fileArray[i]);
					String filename = fileArray[i].getName();
					String rename = filename.substring(filename
							.lastIndexOf(".") + 1);
					if (rename.equals("3gp")) {
						video.add(fileArray[i]);
					} else if (rename.equals("jpg")) {
						imgFiles.add(fileArray[i]);
					}
				}
			}
			mAdapter = new GridViewAdapter(this);
			mAdapter.fileArray = fileList;
			img_GridView.setAdapter(mAdapter);
		} else {
			show_message("找不到文件!");
			return;
		}
	}

	/**
	 * 长按子项 删除 操作
	 */
	public OnItemLongClickListener itemLonglistener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			removeDelete = position;
			showDialog(PRESS_LONG_DELETE);
			return false;
		}

	};
	/**
	 * 
	 */
	public OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			File file = fileList.get(position);
			String filepath = file.getPath();
			String filename = file.getName();
			String rename = filename.substring(filename.lastIndexOf(".") + 1);
			if ("jpg".equals(rename)) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putCharSequence("file_path", filepath);
				bundle.putCharSequence("file_name", filename);
				bundle.putInt("imgIndex", position);
				intent.putExtras(bundle);
				intent.setClass(PreViewResource.this, FileDetail.class);
				startActivity(intent);

			} else if ("3gp".equals(rename)) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("videoIndex", position);
				bundle.putCharSequence("file_path", filepath);
				bundle.putCharSequence("file_name", filename);
				intent.putExtras(bundle);
				intent.setClass(PreViewResource.this, RecordPlayer.class);
				startActivity(intent);
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	private OnClickListener basicListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.title_set_ib:
				showDialog(DELETE_DIALOG);
				break;
			case R.id.title_exit_ib:
				finish();
				break;
			}
		}
	};

	public final int DELETE_DIALOG = 4193542;
	public final int PRESS_LONG_DELETE = 4196458;

	@Override
	protected Dialog onCreateDialog(int id) {
		if (DELETE_DIALOG == id) {
			new AlertDialog.Builder(PreViewResource.this)
					.setTitle("温馨提示")
					.setMessage("您确定要清楚当前所有数据吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									int a = fileList.size();
									while (a > 0) {
										a--;
										fileList.get(a).delete();
										fileList.remove(a);
									}
									mAdapter.fileArray = fileList;
									mAdapter.notifyDataSetChanged();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).setCancelable(false).show();
		} else if (PRESS_LONG_DELETE == id) {
			new AlertDialog.Builder(PreViewResource.this)
					.setTitle("温馨提示")
					.setMessage("您确定要删除当前文件吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									fileList.get(removeDelete).delete();
									fileList.remove(removeDelete);
									mAdapter.fileArray = fileList;
									mAdapter.notifyDataSetChanged();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).setCancelable(false).show();
		}
		return null;
	}

	/**
	 * Toast
	 * 
	 * @param message
	 */
	private synchronized void show_message(String message) {
		if (message != null && message.trim().length() > 0) {
			if (toastinfo == null) {
				toastinfo = Toast.makeText(this, message, Toast.LENGTH_LONG);
				toastinfo.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toastinfo.cancel();// 取消上次显示消息
				toastinfo.setText(message);// 设置显示消息
			}
			toastinfo.show();
		}
	}

}
