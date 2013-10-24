package com.mmg.mliveplayer.jni;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;
import android.view.SurfaceHolder;

import com.gyz.megaeyefamily.ui.MegaeyePlayerActivity;

public class LivePlayer {

	public static boolean isPause = false;

	static {
		System.loadLibrary("livePlayer");
		native_init();
	}

	private int in_width, in_height, out_width, out_height;

	private static final String TAG = "mlive";
	private boolean livecanBack = false;// 20120821-hll
	private String rtspAddress;
	private int rtp_over_tcp;
	Bitmap mBitmap;

	SurfaceHolder mVideoHolder;
	// AudioTrack mAudioTrack;

	private Matrix matrix;
	private boolean to_rotate;
	private float rotate_degrees;
	private int origin_width;
	private int origin_height;
	private MegaeyePlayerActivity gePlay;

	// private SettingBean setBean;

	public LivePlayer() {
		Log.d(TAG, "creating LivePlayer");
		native_setup();
		// Create an identity matrix
		matrix = new Matrix();
	}

	public LivePlayer(MegaeyePlayerActivity gePlay) {
		native_setup();
		livecanBack = false;// 20120821-hll
		// Create an identity matrix
		matrix = new Matrix();
		this.gePlay = gePlay;
	}

	public void rotate(float degrees) {
		if (degrees == 0) {
			to_rotate = false;
		} else {
			to_rotate = true;
		}
		rotate_degrees = degrees;
	}

	public void setSurfaceHolder(SurfaceHolder holder) {
		this.mVideoHolder = holder;
	}

	public void setRtpOverTcp(int rtp_over_tcp) {
		this.rtp_over_tcp = rtp_over_tcp;
	}

	public void setSource(String rtsp_address) {
		this.rtspAddress = rtsp_address;
	}

	public void prepare() {
		_setSource(rtspAddress, rtp_over_tcp);
		// _setSource(rtspAddress , rtp_over_tcp,streamType);
		// Since AudioTrack cannot be opened in the native thread, we open it
		// here.
		createAudioTrack2(8000, 1, 16);
		_prepare();
	}

	public int start() {

		// if (mAudioTrack != null) {
		// mAudioTrack.play();
		// }

		return _start();
	}

	public void stop() {
		// if(mAudioTrack!=null){
		// //停止声音
		// mAudioTrack.stop();
		// mAudioTrack.release();
		// mAudioTrack=null;
		// }
		_stop();
	}

	public void release() {

		// 20120719-hll start
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();// 销毁图片
			mBitmap = null;
			System.gc();
		}
		_release();

		this.gePlay = null;// 20120820-hll
	}

	// Following methods are only called by native code
	// FIXME: imply RGB_565 format
	private void createBitmap(int width, int height) {
		// Log.d(TAG, "create bitmap in Java: " + width + "x" + height);
		if (width > 0 && height > 0) {
			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		}
	}

	private void createAudioTrack(int sampleRate, int channels, int sampleFmt) {
		// createAudioTrack2(sampleRate, channels, sampleFmt);
		// AudioTrack cannot be opened by the native thread
	}

	private void createAudioTrack2(int sampleRate, int channels, int sampleFmt) {
		int channelConfig, audioFormat;
		int bufferSize;

		// Log.d(TAG, "create AudioTrack in Java: " + sampleRate
		// + "Hz, channels: " + channels + " bit width: " + sampleFmt);

		channelConfig = channels == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO
				: AudioFormat.CHANNEL_CONFIGURATION_STEREO;

		audioFormat = sampleFmt == 8 ? AudioFormat.ENCODING_PCM_8BIT
				: AudioFormat.ENCODING_PCM_16BIT;

		bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig,
				audioFormat);
		// bufferSize = 3200;
		try {
			// mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
			// sampleRate,channelConfig, audioFormat, bufferSize,
			// AudioTrack.MODE_STREAM);
		} catch (IllegalArgumentException e) {
			System.out.println("new AudioTrack Exceeption:" + e.toString());
			e.printStackTrace();
		}
	}

	private void drawVideoFrame() {
		Canvas canvas = mVideoHolder.lockCanvas();
		if (canvas != null) {
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				if (rotate_degrees != 0) {
					float a = (float) canvas.getHeight()
							/ (float) mBitmap.getWidth();
					float b = (float) canvas.getWidth()
							/ (float) mBitmap.getHeight();

					matrix.setScale(b, a);

					matrix.preRotate(rotate_degrees,
							(float) mBitmap.getWidth() / 2,
							(float) mBitmap.getHeight() / 2);

					Bitmap newBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
							mBitmap.getWidth(), mBitmap.getHeight(), matrix,
							false);

					if (!isPause) {
						canvas.drawBitmap(newBitmap, 0, 0, null);
					}

					// 20120719-hll start
					if (newBitmap != null && !newBitmap.isRecycled()) {
						newBitmap.recycle();// 销毁图片
						newBitmap = null;
						System.gc();
					}

				} else {
					float a = (float) canvas.getWidth() / mBitmap.getWidth();
					float b = (float) canvas.getHeight() / mBitmap.getHeight();

					matrix.setScale(a, b);

					if (!isPause) {

						canvas.drawBitmap(mBitmap, matrix, null);
					}

				}

				if (gePlay != null && !livecanBack) {
					livecanBack = true;// 20120821-hll
					// gePlay.handler.sendEmptyMessage(gePlay.CNA_BACK_HANDLER);
				}

				// GEPlayerActivity.isPlaying = true;
				mVideoHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	// 照相的接口
	public void saveImage(String imgPath, String puName4File,
			String getTimestamp, String getRandomNum3bit) {
		try {
			if (mBitmap != null) {
				File file = new File(imgPath);
				if (!file.exists())
					file.mkdir();
				StringBuilder picName = new StringBuilder();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				String date = dateFormat.format(new Date());
				picName.append(imgPath).append(java.io.File.separator)
						.append(puName4File)
						// .append(date)
						.append(getTimestamp).append(getRandomNum3bit)
						.append(".jpg");
				File myCaptureFile = new File(picName.toString());
				BufferedOutputStream bos;
				bos = new BufferedOutputStream(new FileOutputStream(
						myCaptureFile));
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 照相的接口
	public Bitmap saveImage(String imgPath, String puName4File) {
		try {
			if (mBitmap != null) {
				File file = new File(imgPath);
				if (!file.exists())
					file.mkdirs();
				File myCaptureFile = new File(imgPath + puName4File);
				Log.e("截图路径和名称", imgPath + "-------" + puName4File);
				BufferedOutputStream bos;
				bos = new BufferedOutputStream(new FileOutputStream(
						myCaptureFile));
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BitmapFactory.decodeFile(imgPath + puName4File);
	}

	// 20120626 start 录像文件
	public void setSourceFile(int in_width, int in_height, int out_width,
			int out_height) {

		this.in_width = in_width;
		this.in_height = in_height;
		this.out_width = out_width;
		this.out_height = out_height;

		_setSourceFile(in_width, in_height, out_width, out_height);

	}

	public void setDecoderFormat(int in_width, int in_height, int out_width,
			int out_height, int pix_fmt_type) {
		_setDecoderFormat(in_width, in_height, out_width, out_height,
				pix_fmt_type);
	}

	public void prepareFile() {
		_prepareFile();
	}

	public int decodeFileVideoFrame(byte[] in_buf, int in_size, byte[] out_buf) {
		return _decodeFileVideoFrame(in_buf, in_size, out_buf);
	}

	public int decodeFileAudioFrame(byte[] in_buf, int in_size, byte[] out_buf) {
		return _decodeFileAudioFrame(in_buf, in_size, out_buf);
	}

	// 20120903-hll start
	public void decodesps(byte[] in_buf, int len) {
		_decodesps(in_buf, len);
	}

	public int getWidth()// 获取寛
	{
		return _getWidth();
	}

	public int getHeight()// 获取高
	{
		return _getHeight();
	}

	// 20120903-hll end
	public void stopFile() {
		_stopFile();
	}

	public void releaseFile() {
		_releaseFile();
	}

	public void startRecord(String filename) // 开始录像
	{
		_startRecord(filename, 1);
	}

	public void endRecord() // 停止录像
	{
		_endRecord();
	}

	public void stopHearts() // 停止心跳
	{
		_stopHearts();
	}

	public int returnTrafficStatistic() // 视频流量统计
	{
		return _returnTrafficStatistic();
	}

	/**
	 * @return 1: 表示 options失败 2: 表示description失败 3: setup 失败 4: play 失败 5:
	 *         没有收到包 6: 与服务器连接失败
	 * */
	public int rtspInteractiveState() {
		return _rtspInteractiveState();
	}

	// 20120626 end
	// TODO: maybe we can use a persistent byteArray to
	// hold the audio data, rather than copy from native
	private void writeAudioFrame(byte[] buf, int size) {
		// mAudioTrack.write(buf, 0, size);
	}

	private static native final void native_init();

	private native void native_setup();

	private native void _setSource(String filename, int tcp_mode);

	// private native void _setSource(String filename, int tcp_mode,int
	// streamType);

	private native void _prepare();

	private native int _start();

	private native void _stop();

	private native void _release();

	// 20120625 start
	private native void _startRecord(String filename, int a);// 开始录像

	private native void _endRecord();// 结束录像

	private native int _returnTrafficStatistic();// 流量统计

	private native int _rtspInteractiveState();// 信令交互状态
	// private native void nativeFile_init();//录像初始化
	// private native void nativeFile_setup();

	private native void _setSourceFile(int in_width, int in_height,
			int out_width, int out_height);// 设置播放录像的分辨率的大小

	private native void _setDecoderFormat(int in_width, int in_height,
			int out_width, int out_height, int pix_fmt_type);

	private native void _prepareFile();

	private native int _decodeFileVideoFrame(byte[] in_buf, int in_size,
			byte[] out_buf);// 视频解码函数

	private native int _decodeFileAudioFrame(byte[] in_buf, int in_size,
			byte[] out_buf);// 音频解码函数

	private native void _stopFile();

	private native void _releaseFile();

	private native void _stopHearts();// 停止心跳
	// 20120625 end
	// 20120903-hll start

	private native void _decodesps(byte[] in_buf, int len);// 解析sps中的寬高

	private native int _getWidth();// 获取寛

	private native int _getHeight();// 获取高
	// 20120903-hll end
}
