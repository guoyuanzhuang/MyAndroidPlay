package com.gyz.megaeyefamily.ui;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyz.megaeyefamily.app.MegaeyeFamilyApplication;
import com.gyz.megaeyefamily.bean.LoginResult;
import com.gyz.megaeyefamily.bean.SettingInfo;
import com.gyz.megaeyefamily.bean.WeatherInfoBean;
import com.gyz.megaeyefamily.httprequest.HttpRequestUtil;
import com.gyz.megaeyefamily.util.SharedPreferSave;

public class MegaeyeFamilyActivity extends Activity {
	private final String tag = this.getClass().getName();
	private Toast toastinfo = null;

	private List<WeatherInfoBean> weatherInfoList = null;
	private LoginResult mLoginResult = null;
	public HttpRequestUtil httpRequestUtil = null;
	public String simNumber = "";
	// 标题
	private RelativeLayout megaeyefamily_title_layout;
	private ImageButton title_exit_ib;
	private ImageButton title_set_ib;
	// 天气
	private TextView megaeyefamily_temp_tv;
	private TextView megaeyefamily_weather_tv;
	private TextView megaeyefamily_wind_tv;
	private ImageView megaeyefamily_photo_iv;
	//
	private ImageButton megaeyefamily_player_ib;
	private ImageView megaeye_loading_iv; // 登录加载
	// 登录
	private Dialog mEnterDialog = null; // 违章查询dialog
	private EditText username = null; // 账户编辑框
	private EditText password = null; // 密码编辑框
	private Button accountLogin = null; // 账户登录
	private CheckBox keyLogin = null; // 记住
	private CheckBox remember_pwd = null; // 记住密码
	private String uname = null; // 用户名
	private String pwd = null; // 密码
	private SettingInfo settingInfo; // 用户信息

	public final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HttpRequestUtil.SYNC_ISNINGBO:
				mLoginResult = httpRequestUtil.mLoginResult;
				if (mLoginResult != null && mLoginResult.code == 0) {
					// 请求天气
					httpRequestUtil.asynRequestStr(
							httpRequestUtil.PATH_WEANTHER,
							MegaeyeFamilyActivity.this, handler,
							HttpRequestUtil.SYNC_WEATHERINFO);
				} else {
					message = "非电信用户无法使用该应用";
					showDialog(ERRORDIALOG);
				}
				break;
			case HttpRequestUtil.SYNC_WEATHERINFO:
				weatherInfoList = httpRequestUtil.weatherInfoList;
				WeatherInfoBean weatherInfo = weatherInfoList.get(0);
				if (weatherInfo != null) {
					megaeyefamily_temp_tv.setText(weatherInfo.temperature);
					megaeyefamily_wind_tv.setText(weatherInfo.date + "\n"
							+ weatherInfoList.get(0).wind);
					int image_index = 32;
					if (weatherInfo.weatherP1 != null
							&& weatherInfo.weatherP1.indexOf(".") > 0) {
						image_index = Integer.valueOf(weatherInfo.weatherP1
								.substring(0,
										weatherInfo.weatherP1.indexOf(".")));
					}
					if (image_index >= 0 && image_index <= 31) {
						megaeyefamily_photo_iv
								.setBackgroundResource(MegaeyeFamilyApplication.weathers_png[image_index]);
					} else {
						megaeyefamily_photo_iv
								.setBackgroundResource(MegaeyeFamilyApplication.weathers_png[image_index]);
					}
				} else {
					show_message("加载天气信息失败!");
				}
				if (loadDialog.isShowing()) {
					loadDialog.cancel(); // 取消加载
					removeDialog(LOADINGDIALOG);
				}
				
				break;
			case HttpRequestUtil.SYNC_LOGIN: // 登录结果
				mLoginResult = httpRequestUtil.mLoginResult;
				if (mLoginResult != null && mLoginResult.code == 1) {
					if (mEnterDialog != null && mEnterDialog.isShowing()) {
						mEnterDialog.cancel(); // 取消
					}
					Intent intent = new Intent(MegaeyeFamilyActivity.this,
							MegaeyePlayerActivity.class);
					startActivity(intent);
				} else {
					show_message("登录失败");
				}
				if (loadDialog.isShowing()) {
					loadDialog.cancel(); // 取消加载
					removeDialog(LOADINGDIALOG);
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
		setContentView(R.layout.megaeyefamily);
		if(!CheckNetwork()){
			message = "没有检测到可用网络,请检查后重试!";
			showDialog(ERRORDIALOG);
			return;
		}
		init_parmars();
		init_view(); // 初始化控件
//		handler.sendEmptyMessageDelayed(100, 3000);
	}

	private void init_parmars() {
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		simNumber = mTelephonyMgr.getSubscriberId();
		if (simNumber == null) {
			// simNumber = "123456";
			message = "没有检测到手机卡,请安装后重试!";
			showDialog(ERRORDIALOG);
			return;
		}
		httpRequestUtil = HttpRequestUtil.getHttpInstance();
		message = "正在加载...";
		showDialog(LOADINGDIALOG); //
		NameValuePair parm0 = new BasicNameValuePair("LoginIdType", "0");
		NameValuePair parm1 = new BasicNameValuePair("LoginId", simNumber);
		NameValuePair parm2 = new BasicNameValuePair("PhoneSystem", "0");
		NameValuePair parm3 = new BasicNameValuePair("CurrentVerson", "v1.14");
		httpRequestUtil.asynRequestStr(httpRequestUtil.PATH_ISNINGBO, this,
				handler, HttpRequestUtil.SYNC_ISNINGBO, new NameValuePair[] {
						parm0, parm1, parm2, parm3 });
		settingInfo = SharedPreferSave.getProperties(this);
	}
	
	private boolean CheckNetwork() {
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = cwjManager.getActiveNetworkInfo();   
		if (activeNetInfo != null){
			return cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	private void init_view() {
		megaeyefamily_title_layout = (RelativeLayout) findViewById(R.id.megaeyefamily_title_layout);
		title_exit_ib = (ImageButton) megaeyefamily_title_layout
				.findViewById(R.id.title_exit_ib);
		title_set_ib = (ImageButton) megaeyefamily_title_layout
				.findViewById(R.id.title_set_ib);
		title_exit_ib.setOnClickListener(listener);
		title_set_ib.setOnClickListener(listener);
		//
		megaeye_loading_iv = (ImageView) findViewById(R.id.megaeye_loading_iv);
		megaeye_loading_iv.setVisibility(View.GONE);
		megaeyefamily_temp_tv = (TextView) findViewById(R.id.megaeyefamily_temp_tv);
		megaeyefamily_weather_tv = (TextView) findViewById(R.id.megaeyefamily_weather_tv);
		megaeyefamily_wind_tv = (TextView) findViewById(R.id.megaeyefamily_wind_tv);
		megaeyefamily_photo_iv = (ImageView) findViewById(R.id.megaeyefamily_photo_iv);
		//
		megaeyefamily_player_ib = (ImageButton) findViewById(R.id.megaeyefamily_player_ib);
		megaeyefamily_player_ib.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.title_exit_ib: // 退出
				// destoryResource();
				showDialog(ExitDialog);
				break;
			case R.id.title_set_ib: // 个人中心
				show_message("正在建设中...");
				break;
			case R.id.megaeyefamily_player_ib: // 监控点
//				showDialog(ENTER_DLOG);
				message = "正在登录...";
				showDialog(LOADINGDIALOG); //
				NameValuePair parm0 = new BasicNameValuePair("Account",
						"shjz@tykjt.nb.zj.ge");
				NameValuePair parm1 = new BasicNameValuePair("Password", "shjz123");
				httpRequestUtil.asynRequestStr(httpRequestUtil.PATH_LOGIN,
						MegaeyeFamilyActivity.this, handler,
						HttpRequestUtil.SYNC_LOGIN, new NameValuePair[] {
								parm0, parm1 });
				break;
			default:
				break;
			}
		}
	};
	private final int LOADINGDIALOG = 1; // 加载框
	private final int ENTER_DLOG = 2; // 登录框
	private final int ExitDialog = 3; // 退出
	private final int ERRORDIALOG = 4; //
	private ProgressDialog loadDialog = null;
	private String message = "";

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LOADINGDIALOG:
			loadDialog = new ProgressDialog(MegaeyeFamilyActivity.this);
			loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			loadDialog.setTitle("进度提示");
			loadDialog.setMessage(message);
			// m_pDialog.setIcon(R.drawable.img1);
			loadDialog.setIndeterminate(false);
			loadDialog.setCancelable(true);
			return loadDialog;
		case ExitDialog:
			return new AlertDialog.Builder(this)
					.setTitle("退出提示")
					// .setIcon(R.drawable.logo)
					.setMessage("您确定要退出程序吗?")
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									destoryResource();
									dismissDialog(ExitDialog);
									removeDialog(ExitDialog);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dismissDialog(ExitDialog);
									removeDialog(ExitDialog);
								}
							}).setCancelable(false).create();
		case ENTER_DLOG:
			// 自定义dialog
			mEnterDialog = new Dialog(MegaeyeFamilyActivity.this,
					R.style.MyDialog);//
			mEnterDialog.setCanceledOnTouchOutside(false);
			// mEnterDialog.setTitle("登录提示");
			mEnterDialog.setContentView(R.layout.megaeyelogin_dialog);
			username = (EditText) mEnterDialog.findViewById(R.id.username);
			username.setText(settingInfo.username);
			// username.setText("shjz@tykjt.nb.zj.ge");
			password = (EditText) mEnterDialog.findViewById(R.id.password);
			password.setText(settingInfo.password);
			// password.setText("shjz123");
			accountLogin = (Button) mEnterDialog
					.findViewById(R.id.accountlogin);
			keyLogin = (CheckBox) mEnterDialog.findViewById(R.id.keylogin);
			remember_pwd = (CheckBox) mEnterDialog
					.findViewById(R.id.remember_pwd);
			keyLogin.setChecked(settingInfo.isChecked);
			remember_pwd.setChecked(settingInfo.isPassword);
			// 账户登录
			accountLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					uname = username.getText().toString();
					pwd = password.getText().toString();
					System.out.println("用户名<" + uname + ">");
					if (uname.trim() != null && !uname.trim().equals("")) {
						if (pwd.trim() != null && !pwd.trim().equals("")) {
							message = "正在登录...";
							showDialog(LOADINGDIALOG); //
							NameValuePair parm0 = new BasicNameValuePair(
									"Account", uname);
							NameValuePair parm1 = new BasicNameValuePair(
									"Password", pwd);
							httpRequestUtil.asynRequestStr(
									httpRequestUtil.PATH_LOGIN,
									MegaeyeFamilyActivity.this, handler,
									HttpRequestUtil.SYNC_LOGIN,
									new NameValuePair[] { parm0, parm1 });
							if (keyLogin.isChecked()
									&& remember_pwd.isChecked()) {
								settingInfo.username = uname;
								settingInfo.password = pwd;
								settingInfo.isChecked = true;
								settingInfo.isPassword = true;
								SharedPreferSave
										.setProperties(
												MegaeyeFamilyActivity.this,
												settingInfo);
							} else if (keyLogin.isChecked() == false
									&& remember_pwd.isChecked()) {
								settingInfo.username = "";
								settingInfo.password = pwd;
								settingInfo.isChecked = false;
								settingInfo.isPassword = true;
								SharedPreferSave
										.setProperties(
												MegaeyeFamilyActivity.this,
												settingInfo);
							} else if (keyLogin.isChecked()
									&& remember_pwd.isChecked() == false) {
								settingInfo.username = uname;
								settingInfo.password = "";
								settingInfo.isChecked = true;
								settingInfo.isPassword = false;
								SharedPreferSave
										.setProperties(
												MegaeyeFamilyActivity.this,
												settingInfo);
							} else if (keyLogin.isChecked() == false
									&& remember_pwd.isChecked() == false) {
								SharedPreferSave
										.delProperties(MegaeyeFamilyActivity.this);
							}
						} else {
							show_message("请输入密码!");
						}
					} else {
						show_message("请输入用户名!");
					}
				}
			});
			return mEnterDialog;
		case ERRORDIALOG:
			return new Builder(MegaeyeFamilyActivity.this)
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
									destoryResource();
								}
							}).setCancelable(false).create();
		default:
			return null;
		}
	};

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(ExitDialog);
			// destoryResource();
		}
		return true;
	}

	// 资源销毁
	void destoryResource() {
		toastinfo = null;
		weatherInfoList = null;
		mLoginResult = null;
		httpRequestUtil = null;
		// megaeyeApplication = null;
		// 标题
		megaeyefamily_title_layout = null;
		title_exit_ib = null;
		title_set_ib = null;
		// 天气
		megaeyefamily_temp_tv = null;
		megaeyefamily_weather_tv = null;
		megaeyefamily_wind_tv = null;
		megaeyefamily_photo_iv = null;
		//
		megaeyefamily_player_ib = null;
		// 登录
		mEnterDialog = null; // 违章查询dialog
		username = null; // 账户编辑框
		password = null; // 密码编辑框
		accountLogin = null; // 账户登录
		keyLogin = null; // 一键登录
		uname = null; // 用户名
		pwd = null; // 密码
		this.finish();
	}
}
