package com.gyz.megaeyefamily.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gyz.megaeyefamily.adapter.MegaeyeGridAdapter;
import com.gyz.megaeyefamily.app.MegaeyeFamilyApplication;
import com.gyz.megaeyefamily.bean.MegaeyeInfo;
import com.gyz.megaeyefamily.bean.MegaeyePlayAddress;
import com.gyz.megaeyefamily.bean.SettingBean;
import com.gyz.megaeyefamily.httprequest.HttpRequestUtil;
import com.gyz.megaeyefamily.remote.PtzUdpClient;
import com.gyz.megaeyefamily.util.RandomUtil;
import com.gyz.megaeyefamily.util.SystemInfUtil;
import com.gyz.megaeyefamily.util.TimeTools;
import com.mmg.mliveplayer.jni.LivePlayer;

public class MegaeyePlayerActivity extends Activity {
	private final String tag = this.getClass().getName(); //
	
	public HttpRequestUtil httpRequestUtil = null;
	private Toast toastinfo = null;
	private MegaeyeGridAdapter megaeyeAdapter = null;
	private List<MegaeyeInfo> megaeyeInfoList = null; // 所有监控点信息
	private MegaeyePlayAddress mPlayAddress = null; // 当前播放地址
	private MegaeyeInfo megaeyeInfo = null; // 当前播放监控点信息
	private int videoIndex = -1; // 当前播放视频索引
	private int ptzFlag = -1; // 当前播放云平台控制标识 0:无权限 1：有权限
	public LivePlayer mLivePlayer; // 播放控制
	private PtzUdpClient ptzUdpClient; // 云台控制
	public SettingBean settingBean;
	public ListenerThread listenerThread = null; // 播放状态监听线程
	public String videoImage = "/sdcard/megaeyefamily/videoimage/"; // 视频图片
	/******************************** 全球眼移植 ****************************************/
	private DisplayMetrics dm = null;
	// 标题
	RelativeLayout play_title_layout;
	private ImageButton title_exit_ib;
	private ImageButton title_set_ib;
	// 播放区域
	RelativeLayout play_view_layout;
	private RelativeLayout.LayoutParams fulllayoutPrms; // 全屏
	private RelativeLayout.LayoutParams nofulllayoutPrms; // 退出全屏relative
	private android.view.ViewGroup.LayoutParams surfaceLayout; // 退出全屏surfeview
	private SurfaceView mySurfaceView;
	// 移动控制
	RelativeLayout play_remove_rl;
	ImageButton play_up_ib;
	ImageButton play_down_ib;
	ImageButton play_left_ib;
	ImageButton play_right_ib;
	// 前排功能控制
	ImageButton play_pause_ib;
	ImageButton play_camera_ib;
	ImageButton play_videotape_ib;
	ImageButton play_bright_ib;
	ImageButton play_dark_ib;
	ImageButton play_videoflow_ib;
	//
	private HorizontalScrollView mScrollView;
	GridView play_point_gv;

	public final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HttpRequestUtil.SYNC_PLAYVIDEO:
				mPlayAddress = httpRequestUtil.playAddress;
				if (mPlayAddress != null) {
					if (mPlayAddress.playUrl != null
							&& mPlayAddress.playUrl.length() > 0) {
						startLivePlayer(mPlayAddress.playUrl);
						//初始化播放器
						
						isLoadEnable(true); // 解锁功能键
						if (listenerThread == null) {
							listenerThread = new ListenerThread();
							listenerThread.start();
						}
						if (ptzFlag == 1) { // 有控制权限
							if (initPTZSocket(mPlayAddress.playUrl,
									mPlayAddress.ptzUrl)) {
								Log.e(tag, "初始化云台控制成功");
								isptzEnable(true);
							} else {
								Log.e(tag, "初始化云台控制出错");
								isptzEnable(false);
							}
						} else {
							Log.e(tag, "云台无控制权限");
							isptzEnable(false);
						}
					} else {
						show_message("视频地址获取失败");
					}
				} else {
					show_message("视频地址获取失败"); // 网络问题
				}
				// loadDialog.cancel(); // 取消加载
				break;
			case MegaeyeFamilyApplication.ERROR01:
				message = MegaeyePlayerActivity.this.getResources().getString(
						R.string.play_error_1);
				showDialog(ERRORDIALOG);
				break;
			case MegaeyeFamilyApplication.ERROR02:
				message = MegaeyePlayerActivity.this.getResources().getString(
						R.string.play_error_2);
				showDialog(ERRORDIALOG);
				break;
			case MegaeyeFamilyApplication.ERROR03:
				message = MegaeyePlayerActivity.this.getResources().getString(
						R.string.play_error_3);
				showDialog(ERRORDIALOG);
				break;
			case MegaeyeFamilyApplication.ERROR04:
				message = MegaeyePlayerActivity.this.getResources().getString(
						R.string.play_error_4);
				showDialog(ERRORDIALOG);
				break;
			case MegaeyeFamilyApplication.ERROR05:
				message = MegaeyePlayerActivity.this.getResources().getString(
						R.string.play_error_5);
				showDialog(ERRORDIALOG);
				break;
			case MegaeyeFamilyApplication.ERROR06:
				message = MegaeyePlayerActivity.this.getResources().getString(
						R.string.play_error_6);
				showDialog(ERRORDIALOG);
				break;
			case 100:
				if (loadDialog.isShowing()) {
					loadDialog.cancel();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.megaeyeplayer);
		init_parms();
		init_view();
		init_listener();
	}

	void init_parms() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止自动锁屏
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		httpRequestUtil = HttpRequestUtil.getHttpInstance();
		if (httpRequestUtil != null && httpRequestUtil.mLoginResult != null) {
			megaeyeInfoList = httpRequestUtil.mLoginResult.megaeyeInfoList;
		} else {
			show_message("未知错误,请重新登录再试"); // 程序重启了
		}
		//
		settingBean = SettingBean.getInstance();
		File file = new File(videoImage);
		File[] files = file.listFiles();
		if (megaeyeInfoList != null && megaeyeInfoList.size() > 0) {
			for (int i = 0; i < megaeyeInfoList.size(); i++) {
				if (files != null && files.length > 0) {
					for (int j = 0; j < files.length; j++) {
						if ((megaeyeInfoList.get(i).channelNo + ".jpg")
								.equals(files[j].getName())) {
							megaeyeInfoList.get(i).tempBitmap = BitmapFactory
									.decodeFile(files[j].getPath());
						}
					}
				}
			}
		}
	}

	// private int cWidth = 150;
	// private int hSpacing = 10;
	void init_view() {
		play_title_layout = (RelativeLayout) findViewById(R.id.play_title_layout);
		title_exit_ib = (ImageButton) play_title_layout
				.findViewById(R.id.title_exit_ib);
		title_set_ib = (ImageButton) play_title_layout
				.findViewById(R.id.title_set_ib);
		title_exit_ib.setBackgroundResource(R.drawable.backplay);
		title_set_ib.setBackgroundResource(R.drawable.folder);
		//
		mySurfaceView = (SurfaceView) findViewById(R.id.mysurfaceview);
		// mySurfaceView = new SurfaceView(this);
		play_view_layout = (RelativeLayout) findViewById(R.id.play_view_layout);
		// 全屏
		fulllayoutPrms = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		// 不全屏
		nofulllayoutPrms = (RelativeLayout.LayoutParams) play_view_layout
				.getLayoutParams();
		surfaceLayout = mySurfaceView.getLayoutParams();
		// 移动控制
		play_remove_rl = (RelativeLayout) findViewById(R.id.play_remove_rl);
		play_remove_rl.setVisibility(View.GONE);
		play_up_ib = (ImageButton) findViewById(R.id.play_up_ib);
		play_down_ib = (ImageButton) findViewById(R.id.play_down_ib);
		play_left_ib = (ImageButton) findViewById(R.id.play_left_ib);
		play_right_ib = (ImageButton) findViewById(R.id.play_right_ib);
		//
		play_pause_ib = (ImageButton) findViewById(R.id.play_pause_ib);
		play_camera_ib = (ImageButton) findViewById(R.id.play_camera_ib);
		play_videotape_ib = (ImageButton) findViewById(R.id.play_videotape_ib);
		play_bright_ib = (ImageButton) findViewById(R.id.play_bright_ib);
		play_dark_ib = (ImageButton) findViewById(R.id.play_dark_ib);
		play_videoflow_ib = (ImageButton) findViewById(R.id.play_videoflow_ib);
		//
		mScrollView = (HorizontalScrollView) findViewById(R.id.mScrollView);
		mScrollView.setHorizontalScrollBarEnabled(false);
		play_point_gv = (GridView) findViewById(R.id.play_point_gv);
		megaeyeAdapter = new MegaeyeGridAdapter(this);
		megaeyeAdapter.menuList = megaeyeInfoList;
		play_point_gv.setAdapter(megaeyeAdapter);
		if (megaeyeInfoList != null && megaeyeInfoList.size() > 0) {
			play_point_gv.getLayoutParams().width = megaeyeAdapter.getCount() * 300;
			play_point_gv.setNumColumns(megaeyeAdapter.getCount());
		}
		// LayoutParams params = new LayoutParams(megaeyeAdapter.getCount()
		// * (150 + 10), LayoutParams.WRAP_CONTENT);
		// play_point_gv.setLayoutParams(params);
		// play_point_gv.setColumnWidth(cWidth);
		// play_point_gv.setHorizontalSpacing(hSpacing);
		// play_point_gv.setStretchMode(GridView.NO_STRETCH);

		isptzEnable(false); // 不可控制
		isLoadEnable(false);
	}

	void init_listener() {
		play_view_layout.setOnClickListener(listener); // 全屏
		title_exit_ib.setOnClickListener(listener);
		title_set_ib.setOnClickListener(listener);
		play_pause_ib.setOnClickListener(listener);
		play_camera_ib.setOnClickListener(listener);
		play_videotape_ib.setOnClickListener(listener);
		play_bright_ib.setOnClickListener(listener);
		play_dark_ib.setOnClickListener(listener);
		play_videoflow_ib.setOnClickListener(listener);
		play_point_gv.setOnItemClickListener(itemListener);
		//
		play_up_ib.setOnClickListener(listener);
		play_down_ib.setOnClickListener(listener);
		play_left_ib.setOnClickListener(listener);
		play_right_ib.setOnClickListener(listener);
	}

	private boolean clickFlag = false;
	private boolean playFlag = true; // 播放状态
	private boolean screenFlag = false;// 全屏
	public int vidiconFlag = 0;// 录像开关
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.title_exit_ib:
				destoryResource();
				break;
			case R.id.title_set_ib:
				// 正在录像就退出
				if (vidiconFlag == 1) {
					endRecord();
				}
				exitLivePlayer(); // 退出视频播放
				isptzEnable(false); // 不可控制
				isLoadEnable(false);
				Intent intent = new Intent(MegaeyePlayerActivity.this,
						PreViewResource.class);
				startActivity(intent);
				break;
			case R.id.play_pause_ib: // 停止/播放
				if (playFlag) {// 播放
					playFlag = false;
					play_pause_ib.setBackgroundResource(R.drawable.beginbg);
					LivePlayer.isPause = true;
				} else {
					playFlag = true;
					play_pause_ib.setBackgroundResource(R.drawable.pausebg);
					LivePlayer.isPause = false;
				}
				break;
			case R.id.play_view_layout: // 全屏判断
				if (screenFlag) {
					screenFlag = false;
					mLivePlayer.rotate(0);
					play_view_layout.setLayoutParams(nofulllayoutPrms);
					mySurfaceView.setLayoutParams(surfaceLayout);
				} else {
					screenFlag = true;
					mLivePlayer.rotate(90);
					play_view_layout.setLayoutParams(fulllayoutPrms);
					mySurfaceView.setLayoutParams(fulllayoutPrms);
				}
				break;
			case R.id.play_camera_ib: // 拍照
				if (SystemInfUtil.isSDExist()) {
					// 判断是否存在SD卡
					long freeStorage = SystemInfUtil.getFreeStorage() / 1048576;// "MB"
					if (freeStorage < 2) {
						show_message("您好,检测到存储空间不足1MB，请检查！");
					} else {
						mLivePlayer.saveImage(settingBean
								.getImageSaveAdd(MegaeyePlayerActivity.this),
								megaeyeInfo.puName, TimeTools.getTimestamp(),
								RandomUtil.getRandomNum3bit());
						// 播发拍照声音
						MediaPlayer myPlayer1 = new MediaPlayer();
						AssetFileDescriptor fd = getResources()
								.openRawResourceFd(R.drawable.camera);
						try {
							myPlayer1.setDataSource(fd.getFileDescriptor(),
									fd.getStartOffset(), fd.getLength());
							myPlayer1.prepare();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						myPlayer1.start();// 声音提示
						// 文字提示
						show_message("您好，图像已保存到："
								+ settingBean
										.getImageSaveAdd(MegaeyePlayerActivity.this)
								+ "目录下，进入“图像”功能可对其进行管理。");
					}
				} else {
					show_message("您好，该业务需外接存储卡，请插入后再试!");
				}
				break;
			case R.id.play_videotape_ib: // 录像
				if (SystemInfUtil.isSDExist()) {
					// 判断是否存在SD卡
					long freeStorage = SystemInfUtil.getFreeStorage() / 1048576;// "MB"
					if (freeStorage < 2) {
						show_message("您好,检测到存储空间不足1MB，请检查！");
					} else {
						if (vidiconFlag == 0) {
							// baal 初始化录像参数
							play_videotape_ib
									.setBackgroundResource(R.drawable.videotapebged);
							vidiconFlag = 1;
							StringBuffer picName = new StringBuffer();
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"yyyyMMdd");
							String date = dateFormat.format(new Date());
							dateFormat = null;// 20120820-hll
							File file = new File(
									settingBean
											.getImageSaveAdd(MegaeyePlayerActivity.this));
							if (!file.exists())
								file.mkdir();
							picName.append(
									settingBean
											.getImageSaveAdd(MegaeyePlayerActivity.this))
									.append(java.io.File.separator)
									.append(megaeyeInfo.puName)
									.append(TimeTools.getTimestamp())
									.append(RandomUtil.getRandomNum3bit())
									.append(".3gp");
							Log.i("录像存放的路径", picName.toString());
							// 调用录视频方法
							mLivePlayer.startRecord(picName.toString());
							picName.delete(0, picName.length());
							picName = null;// 20120820-hll
							file = null;// 20120820-hll
							Log.i("录像调用后", "//////////////");
							show_message("开始录像 . . . ");
						} else if (vidiconFlag == 1) {
							endRecord();
						}
					}
				} else {
					show_message("您好，该业务需外接存储卡，请插入后再试!");
				}
				break;
			case R.id.play_bright_ib: // 变亮
				new Thread() {
					public void run() {
						Log.e(tag, "变亮");
						ptzUdpClient.ptzAction(5);
					};
				}.start();

				break;
			case R.id.play_dark_ib: // 变暗
				new Thread() {
					public void run() {
						Log.e(tag, "变暗");
						ptzUdpClient.ptzAction(6);
					};
				}.start();

				break;
			case R.id.play_videoflow_ib: // 移动
				if (clickFlag) {
					play_videoflow_ib
							.setBackgroundResource(R.drawable.videoflow);
					play_remove_rl.setVisibility(View.GONE);
					clickFlag = false;
				} else {
					play_videoflow_ib
							.setBackgroundResource(R.drawable.videoflowed);
					play_remove_rl.setVisibility(View.VISIBLE);
					clickFlag = true;
				}
				break;
			case R.id.play_up_ib: // 上
				new Thread() {
					public void run() {
						Log.e(tag, "向上");
						ptzUdpClient.ptzAction(1);
					};
				}.start();
				break;
			case R.id.play_down_ib:
				new Thread() {
					public void run() {
						Log.e(tag, "向上");
						ptzUdpClient.ptzAction(2);
					};
				}.start();
				break;
			case R.id.play_left_ib:
				new Thread() {
					public void run() {
						Log.e(tag, "向左");
						ptzUdpClient.ptzAction(3);
					};
				}.start();

				break;
			case R.id.play_right_ib:
				new Thread() {
					public void run() {
						Log.e(tag, "向右");
						ptzUdpClient.ptzAction(4);
					};
				}.start();

				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int possion,
				long arg3) {
			// TODO Auto-generated method stub
			Log.e(tag, "position = " + possion);
			message = "正在获取视频地址...";
			showDialog(LOADINGDIALOG); //
			handler.sendEmptyMessageDelayed(100, 3000);
			megaeyeInfo = megaeyeInfoList.get(possion);
			if (megaeyeInfo.online == 1) {
				// 处理前一次点击视频
				if (videoIndex != -1) {
					MegaeyeInfo frontMegaeye = megaeyeInfoList.get(videoIndex);
					Bitmap tempMap = photograph(videoImage,
							frontMegaeye.channelNo + ".jpg");
					megaeyeInfoList.get(videoIndex).tempBitmap = tempMap;
					megaeyeAdapter.menuList = megaeyeInfoList;
					megaeyeAdapter.notifyDataSetChanged();
				}
				// 处理点击后的视频
				videoIndex = possion;
				ptzFlag = megaeyeInfo.ptzFlag; // 云台控制句柄
				NameValuePair parm0 = new BasicNameValuePair("PuId_ChannelNo",
						megaeyeInfo.channelNo);
				NameValuePair parm1 = new BasicNameValuePair("PuProperty",
						ptzFlag + "");
				NameValuePair parm2 = new BasicNameValuePair("M_cuIp",
						"0.0.0.0");
				NameValuePair parm3 = new BasicNameValuePair("StreamingType",
						"0");
				httpRequestUtil.asynRequestStr(httpRequestUtil.PATH_PLAYVIDEO,
						MegaeyePlayerActivity.this, handler,
						HttpRequestUtil.SYNC_PLAYVIDEO, new NameValuePair[] {
								parm0, parm1, parm2, parm3 });
				if (vidiconFlag == 1) {
					endRecord(); // 终止视频录像
				}
				exitLivePlayer(); // 关闭LivePlayer
			} else {
				show_message("当前摄像头不在线!");
				if (loadDialog.isShowing()) {
					loadDialog.cancel();
				}
			}
		}
	};

	/**
	 * 播放rtsp地址
	 * 
	 * @param rtspStr
	 */
	void startLivePlayer(String rtspStr) {
		if (mLivePlayer == null) {
			mLivePlayer = new LivePlayer(this);
		}
		mLivePlayer.setSurfaceHolder(mySurfaceView.getHolder());
		mLivePlayer.setSource(rtspStr);
		mLivePlayer.setRtpOverTcp(1); // 0 UDP 1 tcp
		mLivePlayer.prepare();// 20120723
		mLivePlayer.start();
	}

	// 关闭释放LivePlayer
	void exitLivePlayer() {
		if (mLivePlayer != null) {
			mLivePlayer.endRecord();
			mLivePlayer.stopHearts();
			mLivePlayer.stop();
			mLivePlayer.release();
			mLivePlayer = null;
		}
	}

	/**
	 * 摄像头控制初始化
	 * 
	 * @param lastUrl
	 * @param rtspStr
	 */
	private boolean initPTZSocket(String rtspStr, String ptzUrl) {
		String serverIp = null;
		int serverPort = 0;
		String userName = null;
		String destID = null;
		if (ptzUrl.indexOf(":") > 0) {
			String[] serverArray = ptzUrl.split(":");
			serverIp = serverArray[0];
			serverPort = Integer.parseInt(serverArray[1]);
		}
		userName = megaeyeInfo.userId;
		destID = megaeyeInfo.channelNo;
		if (serverIp != null && serverIp.length() > 0 && userName != null
				&& userName.length() > 0 && serverPort != 0 && destID != null
				&& destID.length() > 0) {
			ptzUdpClient = new PtzUdpClient(this, serverIp, serverPort,
					userName, destID, 5, 5);
			return true;
		}
		return false;
	}

	/**
	 * 根据摄像头控制状态判断UI
	 * 
	 * @param isptzControl
	 */
	void isptzEnable(boolean isptzControl) {
		if (isptzControl) {
			play_up_ib.setEnabled(true);
			play_down_ib.setEnabled(true);
			play_left_ib.setEnabled(true);
			play_right_ib.setEnabled(true);
			play_bright_ib.setEnabled(true);
			play_dark_ib.setEnabled(true);
		} else {
			play_up_ib.setEnabled(false);
			play_down_ib.setEnabled(false);
			play_left_ib.setEnabled(false);
			play_right_ib.setEnabled(false);
			play_bright_ib.setEnabled(false);
			play_dark_ib.setEnabled(false);
		}
	}

	/**
	 * 根据加载状态来确定功能操作(非云平台)
	 * 
	 * @param isEnable
	 */
	void isLoadEnable(boolean isenable) {
		if (isenable) {
			play_pause_ib.setEnabled(true);
			play_camera_ib.setEnabled(true);
			play_videotape_ib.setEnabled(true);
			play_view_layout.setEnabled(true);
		} else {
			play_view_layout.setEnabled(false);
			play_pause_ib.setEnabled(false);
			play_camera_ib.setEnabled(false);
			play_videotape_ib.setEnabled(false);
		}
	}

	// 终止视频录制
	void endRecord() {
		vidiconFlag = 0;
		play_videotape_ib.setBackgroundResource(R.drawable.videotapebg);
		mLivePlayer.endRecord();
		show_message("您好，录像已保存到："
				+ settingBean.getImageSaveAdd(MegaeyePlayerActivity.this)
				+ "目录下，进入“图像”功能可对其进行管理。");
	}

	public Bitmap photograph(String path, String filename) {
		Bitmap tempBitmap = null;
		if (SystemInfUtil.isSDExist()) {
			// 判断是否存在SD卡
			long freeStorage = SystemInfUtil.getFreeStorage() / 1048576;// "MB"
			if (freeStorage < 2) {
				show_message("您好,检测到存储空间不足1MB，请检查！");
			} else {
				if (mLivePlayer != null) {
					tempBitmap = mLivePlayer.saveImage(path, filename);
				}
			}
		} else {
			show_message("您好，该业务需外接存储卡，请插入后再试!");
		}
		return tempBitmap;
	}

	private final int LOADINGDIALOG = 1; // 加载框
	private final int ERRORDIALOG = 2; // 错误框
	private ProgressDialog loadDialog = null;
	private String message = "";

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LOADINGDIALOG:
			loadDialog = new ProgressDialog(MegaeyePlayerActivity.this);
			loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			loadDialog.setTitle("进度提示");
			loadDialog.setMessage(message);
			loadDialog.setIndeterminate(false);
			loadDialog.setCancelable(true);
			return loadDialog;
		case ERRORDIALOG:
			return new Builder(MegaeyePlayerActivity.this)
					.setTitle("温馨提示")
					.setIcon(R.drawable.jiankong)
					.setMessage(message)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
									removeDialog(ERRORDIALOG);
								}
							}).setCancelable(false).create();

		default:
			return null;
		}
	};

	/**
	 * toast
	 * 
	 * @param message
	 */
	private synchronized void show_message(String message) {
		if (message != null && message.trim().length() > 0) {
			if (toastinfo == null) {
				toastinfo = Toast.makeText(this, message, Toast.LENGTH_LONG);
				toastinfo.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toastinfo.cancel();// 取消上次 显示消息
				toastinfo.setText(message);// 设置显示消息
			}
			toastinfo.show();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			destoryResource();
		}
		return true;
	}

	/**
	 * rtsp 播放状态监听线程
	 * 
	 * @author guoyuanzhuang
	 * 
	 */
	class ListenerThread extends Thread {
		public boolean isRunning = true;

		@Override
		public void run() {
			super.run();
			while (isRunning) {
				int flag = -1;
				if (mLivePlayer != null) {
					flag = mLivePlayer.rtspInteractiveState();
				}
				if (flag == 1) {
					handler.sendEmptyMessage(MegaeyeFamilyApplication.ERROR01);
					isRunning = false;
				} else if (flag == 2) {
					handler.sendEmptyMessage(MegaeyeFamilyApplication.ERROR02);
					isRunning = false;
				} else if (flag == 3) {
					handler.sendEmptyMessage(MegaeyeFamilyApplication.ERROR03);
					isRunning = false;
				} else if (flag == 4) {
					handler.sendEmptyMessage(MegaeyeFamilyApplication.ERROR04);
					isRunning = false;
				} else if (flag == 5) {
					handler.sendEmptyMessage(MegaeyeFamilyApplication.ERROR05);
					isRunning = false;
				} else if (flag == 6) {
					handler.sendEmptyMessage(MegaeyeFamilyApplication.ERROR06);
					isRunning = false;
				}
				flag = -1;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 资源销毁
	void destoryResource() {
		exitLivePlayer(); // 关闭播放器
		if (listenerThread != null) { // 关闭监听线程
			listenerThread.isRunning = false;
		}
		httpRequestUtil = null;
		toastinfo = null;
		// megaeyeApplication = null;
		megaeyeAdapter = null;
		megaeyeInfoList = null;
		mPlayAddress = null;
		settingBean = null;
		dm = null;
		// 标题
		play_title_layout = null;
		title_exit_ib = null;
		title_set_ib = null;
		// 播放区域
		play_view_layout = null;
		fulllayoutPrms = null;
		nofulllayoutPrms = null;
		mySurfaceView = null;
		// 移动控制
		play_remove_rl = null;
		play_up_ib = null;
		play_down_ib = null;
		play_left_ib = null;
		play_right_ib = null;
		//
		play_pause_ib = null;
		play_camera_ib = null;
		play_videotape_ib = null;
		play_bright_ib = null;
		play_dark_ib = null;
		play_videoflow_ib = null;
		//
		mScrollView = null;
		play_point_gv = null;
		this.finish();
	}

}
