package com.gyz.megaeyefamily.ui;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyz.megaeyefamily.adapter.PlayerAdapter;
import com.gyz.megaeyefamily.bean.BMPImage;
import com.gyz.megaeyefamily.remote.PlayRecordThread;
import com.mmg.mliveplayer.jni.LivePlayer;

public class RecordPlayer extends Activity {

	// 20091211
	private static final int VideoWidth = 704;
	private static final int VideoHeight = 576;
	private LivePlayer myLivePlayer = null;
	private byte[] onenalbytes = new byte[VideoWidth * VideoHeight * 3];
	private byte[] Buffer = null;
	private int nOutBuffLen = 0;
	private BMPImage bmpHeader = new BMPImage(VideoWidth, VideoHeight);

	private PlayRecordThread recordPlayerThread = null;

	private Intent intent = null;
	// 20091211
	private float scaleW = 1, scaleH = 1;
	private int nid = 0;
	private Matrix matrix;
	private Bitmap bm;
	// 控件声明
	private ImageView player;
	private TextView videoName;
	private ImageButton back;
	private ImageButton pause;

	// 录像播放控制
	public boolean playGate = true;
	public boolean pauseGate = false;
	// guoyuanzhuang:130424
	private HorizontalScrollView myScrollView;
	GridView play_gridview;
	PlayerAdapter megaeyeAdapter = null;

	// private int cWidth = 150;
	// private int hSpacing = 10;
	// private int possion = -1;

	void init_view() {
		myScrollView = (HorizontalScrollView) findViewById(R.id.myScrollView);
		myScrollView.setHorizontalScrollBarEnabled(false);
		play_gridview = (GridView) findViewById(R.id.play_gridview);
		megaeyeAdapter = new PlayerAdapter(this);
		megaeyeAdapter.fileList = PreViewResource.video;
		play_gridview.setAdapter(megaeyeAdapter);
		play_gridview.getLayoutParams().width = megaeyeAdapter.getCount() * 300;
		play_gridview.setNumColumns(megaeyeAdapter.getCount());
		play_gridview.setOnItemClickListener(itemListener);
		// LayoutParams params = new LayoutParams(megaeyeAdapter.getCount()
		// * (150 + 10), LayoutParams.WRAP_CONTENT);
		// play_gridview.setLayoutParams(params);
		// play_gridview.setColumnWidth(cWidth);
		// play_gridview.setHorizontalSpacing(hSpacing);
		// play_gridview.setStretchMode(GridView.NO_STRETCH);
		// play_gridview.setNumColumns(megaeyeAdapter.getCount());
	}

	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// possion = arg2;
			Log.e("录像本地地址>>>>>>>>>>>>>>>>", PreViewResource.video.get(arg2)
					.getPath());
			videoName.setText(PreViewResource.video.get(arg2).getName());
			recordPlayerThread.playStopOrStart = 1;
			PlayRecordThread.videoIsPause = true;
			if (recordPlayerThread != null) {
				recordPlayerThread.interrupt();
			}
			PlayRecordThread.videoIsPause = false;
			//
			if (myLivePlayer == null) {
				myLivePlayer = new LivePlayer();
			}
			recordPlayerThread = new PlayRecordThread(RecordPlayer.this,
					PreViewResource.video.get(arg2).getPath());
			recordPlayerThread.isRunning = true;
			recordPlayerThread.start();
			// loadDialog.cancel();
			// showDialog(LOADINGDIALOG);
			// handler.sendEmptyMessageDelayed(0, 2000);
		}
	};

	// private final int LOADINGDIALOG = 1; // 加载框
	// private ProgressDialog loadDialog = null;
	// protected Dialog onCreateDialog(int id, Bundle args) {
	// switch (id) {
	// case LOADINGDIALOG:
	// loadDialog = new ProgressDialog(RecordPlayer.this);
	// loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// loadDialog.setTitle("进度提示");
	// loadDialog.setMessage("正在切换视频,请稍后...");
	// loadDialog.setIndeterminate(false);
	// loadDialog.setCancelable(true);
	// return loadDialog;
	// default:
	// return null;
	// }
	// };

	// 当前页数
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(EXIT_DIALOG);
		}
		return true;
	}

	// 返回和关闭的监听
	OnClickListener closeNBackLsnr = new OnClickListener() {
		public void onClick(View v) {
			showDialog(EXIT_DIALOG);
		}
	};

	OnClickListener pauseLsnr = new OnClickListener() {

		public void onClick(View v) {
			if (pauseGate) {
				PlayRecordThread.videoIsPause = false;
				pause.setBackgroundResource(R.drawable.start);
				pauseGate = false;
			} else {
				PlayRecordThread.videoIsPause = true;
				pause.setBackgroundResource(R.drawable.pause);
				pauseGate = true;
			}
		}
	};

	// 20091211
	// 播放图像
	// 视频显示
	public static final ByteBuffer pRGBBuffer = ByteBuffer.allocate(VideoWidth
			* VideoHeight * 3 + 54);
	private Handler H = new Handler() {
		public void handleMessage(Message msg) {
			player.invalidate();
			if (nid == 0) {
				int nwidth = PlayRecordThread.width;
				int nheight = PlayRecordThread.height;
				if (nwidth > 0 && nheight > 0) {
					nid = 1;
					System.out.println("nwidth:" + nwidth + ",nheight:"
							+ nheight);
					bmpHeader = new BMPImage(nwidth, nheight);
					double scale = 1;// 2.22;
					scaleW = (float) (scaleW * scale);
					scaleH = (float) (scaleH * scale);
					matrix = new Matrix();
					matrix.postScale(scaleW, scaleH);
					matrix.postRotate(90);
				}
			}

			if (bmpHeader == null || onenalbytes == null) {
				return;
			}
			pRGBBuffer.clear();
			pRGBBuffer.put(bmpHeader.getByte());
			pRGBBuffer.put(onenalbytes);
			Buffer = pRGBBuffer.array();

			Camera camera = new Camera();
			camera.save();
			Matrix mtx = new Matrix();
			camera.rotateX(0);
			camera.rotateY(180);
			camera.rotateZ(0);
			camera.getMatrix(mtx);
			camera.restore();

			bm = BitmapFactory.decodeByteArray(Buffer, 0, Buffer.length);

			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					mtx, true);
			Matrix mtx2 = new Matrix();
			mtx2.postRotate(180, PlayRecordThread.width / 2,
					PlayRecordThread.height / 2);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					mtx2, true);
			player.setImageBitmap(bm);
		}
	};
	static int decodeframeFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_play_layout);
		// SysApplication.getInstance().addActivity(this); // 20121112-hll
		// 参数传递
		intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String filePath = bundle.getString("file_path");
		String fileName = bundle.getString("file_name");

		// 初始化视频播放器
		player = (ImageView) findViewById(R.id.video_play);
		// 视频文件名称显示
		RelativeLayout play_top = (RelativeLayout) findViewById(R.id.play_top);
		videoName = (TextView) play_top.findViewById(R.id.title_mind_tv);
		videoName.setText(fileName);
		back = (ImageButton) play_top.findViewById(R.id.title_exit_ib);
		((ImageButton) play_top.findViewById(R.id.title_set_ib))
				.setVisibility(View.GONE);
		back.setBackgroundResource(R.drawable.backplay);

		pause = (ImageButton) findViewById(R.id.performMenuBtn_play_);
		// 设置关闭和返回监听
		back.setOnClickListener(closeNBackLsnr);
		pause.setOnClickListener(pauseLsnr);
		init_view(); // 初始化
		myLivePlayer = new LivePlayer();
		decodeframeFlag = 1;
		//
		recordPlayerThread = new PlayRecordThread(this, filePath);
		recordPlayerThread.isRunning = true;
		recordPlayerThread.start();

	}

	// 解码函数
	public void decodeframe(byte[] nalbytes, int nInBuffLen) {
		if (decodeframeFlag == 1) {
			decodeframeFlag = 0;
			myLivePlayer.setDecoderFormat(PlayRecordThread.width,
					PlayRecordThread.height, PlayRecordThread.width,
					PlayRecordThread.height, 1);
			myLivePlayer.prepareFile();
		}
		nOutBuffLen = myLivePlayer.decodeFileVideoFrame(nalbytes, nInBuffLen,
				onenalbytes);

		if (nOutBuffLen > 0) {
			H.sendMessage(H.obtainMessage());// lei
		}
	}

	public final int EXIT_DIALOG = 4231787;

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (EXIT_DIALOG == id) {
			return new AlertDialog.Builder(RecordPlayer.this)
					.setTitle("温馨提示")
					// .setIcon(R.drawable.logo_s)
					.setMessage("您确定退出视频播放")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									exit();
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

	// 退出视频
	void exit() {
		playGate = false;
		recordPlayerThread.playStopOrStart = 1;
		PlayRecordThread.videoIsPause = true;
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (recordPlayerThread != null) {
			recordPlayerThread.interrupt();
		}
		PlayRecordThread.videoIsPause = false;
		if (myLivePlayer != null) {
			myLivePlayer.stopFile();
			myLivePlayer.releaseFile();
			myLivePlayer = null;
		}
		onenalbytes = null;
		if (bmpHeader != null) {
			// bmpHeader.clean();
			bmpHeader = null;
		}
		RecordPlayer.this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
