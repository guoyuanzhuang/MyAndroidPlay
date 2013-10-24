package com.gyz.megaeyefamily.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyz.megaeyefamily.bean.SettingBean;

public class FileDetail extends Activity {

	/* 相关变量声明 */
	private SettingBean settingBean = SettingBean.getInstance();
	private ImageView imageFileView;// 所操作放大缩小的图片
	private ImageButton bigBtn;// 放大按钮
	private ImageButton smallBtn;// 缩小按钮
	private TextView fileNameView;// 显示文件名的Text
	private ImageButton brs_back;// 返回按钮
	private ImageButton brs_delete;// 删除按钮
	private ImageButton next_img;
	private ImageButton old_img;
	private Bitmap bmp; // 生成图片的属性
	private float scaleWidth = 1;
	private float scaleHeight = 1;
	private int bmpWidth;
	private int bmpHeight;
	double hh_bili;
	double hw_bili;

	private int imgIndex = -1;
	private Intent intent;
	private String fileName;

	int height;
	int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		/* 载入filedetail.xml Layout */
		setContentView(R.layout.pic_show_layout);
		// SysApplication.getInstance().addActivity(this); //20121112-hll

		/* 初始化手机比例 */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止自动锁屏
		settingBean = SettingBean.getInstance();// 配置文件
		hh_bili = settingBean.getHh_bili();
		hw_bili = settingBean.getHw_bili();

		bigBtn = (ImageButton) findViewById(R.id.bigBtn);

		Display dis = getWindowManager().getDefaultDisplay();
		height = dis.getHeight();
		width = dis.getWidth();
		
		smallBtn = (ImageButton) findViewById(R.id.smallBtn);

		// 返回,删除,文件名

		//
		RelativeLayout pic_show_top = (RelativeLayout) findViewById(R.id.pic_show_top);
		brs_delete = (ImageButton) pic_show_top.findViewById(R.id.title_set_ib);
		brs_back = (ImageButton) pic_show_top.findViewById(R.id.title_exit_ib);
		((TextView)pic_show_top.findViewById(R.id.title_mind_tv)).setText("图片查看");
		brs_back.setBackgroundResource(R.drawable.backplay);
		brs_delete.setBackgroundResource(R.drawable.de_selector);
		
		old_img = (ImageButton) findViewById(R.id.picShow_next_btn);
		next_img = (ImageButton) findViewById(R.id.picShow_ago_btn);

		fileNameView = (TextView) findViewById(R.id.fileNameView);
		
		/* 进入Activity获得第一张传入的图片并显示 */

		imageFileView = (ImageView) findViewById(R.id.myImageView);

		intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		fileName = bundle.getString("file_name");
		imgIndex = bundle.getInt("imgIndex");
		selectImg(imgIndex);

		// /* 显示文件名的横条 */
		fileNameView.setText(fileName);

		/* 缩小按钮onClickListener */
		bigBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				big();
			}
		});

		/* 放大按钮onClickListener */
		smallBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				small();
			}
		});

		// 删除按钮
		brs_delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!noImg) {
					new AlertDialog.Builder(FileDetail.this)
							.setTitle("注意!")
							.setMessage("确定要删除文件吗?")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

											if (imgIndex >= 0) {

												Log.e("数组大小",
														PreViewResource.imgFiles
																.size() + "");
												String path = PreViewResource.imgFiles
														.get(imgIndex)
														.getPath();
												File file = new File(path);
												/* 删除文件 */
												file.delete();
												int a = PreViewResource.fileList
														.size();
												for (int j = 0; j < a; j++) {
													String p = PreViewResource.fileList
															.get(j).getPath();
													if (path.equals(p)) {
														PreViewResource.fileList
																.remove(j);
														break;
													}

												}

												PreViewResource.imgFiles
														.remove(imgIndex);

												if (imgIndex + 1 < PreViewResource.imgFiles
														.size()) {

													imgIndex = imgIndex + 1;
													selectImg(imgIndex);

												} else {

													imgIndex = imgIndex - 1;
													if (imgIndex == -1) {
														imgIndex = 0;
														selectImg(imgIndex);
													} else {
														selectImg(imgIndex);
													}

												}

											}

										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}

									}).show();
				} else {
					brs_delete.setOnClickListener(null);
				}
			}
		});

		old_img.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (imgIndex - 1 >= 0) {
					imgIndex = imgIndex - 1;
					selectImg(imgIndex);
				} else {
					Toast.makeText(FileDetail.this, "已是第一张", 1000).show();
				}

			}
		});

		next_img.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (imgIndex + 1 < PreViewResource.imgFiles.size()) {
					imgIndex = imgIndex + 1;
					selectImg(imgIndex);
				} else {
					Toast.makeText(FileDetail.this, "已是最后一张", 1000).show();
				}

			}
		});

		// 返回按钮
		brs_back.setOnClickListener(new TextView.OnClickListener() {

			public void onClick(View v) {
				/* 结束这个activity */
				FileDetail.this.finish();
			}
		});

	}

	boolean noImg = false;

	public void selectImg(int imgIndex) {
		Log.e("index1", imgIndex + "");
		Log.e("sum", PreViewResource.imgFiles.size() + "");
		if (PreViewResource.imgFiles.size() != 0) {

			File file = PreViewResource.imgFiles.get(imgIndex);
			bmp = BitmapFactory.decodeFile(file.getPath());
			String filename = file.getName();
			String rename = filename.substring(filename.lastIndexOf(".") + 1);
			if ("jpg".equals(rename)) {

				bmpWidth = bmp.getWidth();
				bmpHeight = bmp.getHeight();
				imageFileView.setImageBitmap(bmp);

				/* 显示文件名的横条 */
				fileNameView.setText(filename);
			}
		} else {
			noImg = true;
			imageFileView.setImageBitmap(null);
			/* 显示文件名的横条 */
			fileNameView.setText("已无图片");
		}

	};

	boolean isSmall = false;
	boolean isBig = false;

	/* 图片缩小的method */
	private void small() {
		if (!isSmall) {
			/* 设置图片缩小的比例 */
			double scale = 0.8;
			/* 计算出这次要缩小的比例 */
			scaleWidth = (float) (scaleWidth * scale);
			scaleHeight = (float) (scaleHeight * scale);

			/* 产生reSize后的Bitmap对象 */
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,
					bmpHeight, matrix, true);

			if (resizeBmp.getWidth() > width / 4) {
				isBig = false;
				imageFileView.setImageBitmap(resizeBmp);
			} else {
				isBig = false;
				isSmall = true;
			}

		}

	}

	/* 图片放大的method */
	private void big() {

		if (!isBig) {
			/* 设置图片放大的比例 */
			double scale = 1.25;
			/* 计算这次要放大的比例 */
			scaleWidth = (float) (scaleWidth * scale);
			scaleHeight = (float) (scaleHeight * scale);

			/* 产生reSize后的Bitmap对象 */
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,
					bmpHeight, matrix, true);
			imageFileView.setImageBitmap(resizeBmp);

			if (resizeBmp.getWidth() < width) {
				isSmall = false;
				imageFileView.setImageBitmap(resizeBmp);
			} else {
				isSmall = false;
				isBig = true;
			}

		}

	}

	protected void onDestroy() {
		super.onDestroy();

	}

	// 重写activity跳转方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Bundle bunde = data.getExtras();
		switch (resultCode) {
		case RESULT_OK:
			// n = bunde.getInt("currPage");
			break;
		case 10:
			this.setResult(10);
			finish();
		default:
			break;
		}
	}

}
