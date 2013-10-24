package com.gyz.megaeyefamily.remote;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.gyz.megaeyefamily.ui.RecordPlayer;

public class PlayRecordThread extends Thread {

	public static boolean videoIsPause = false;
	private RecordPlayer recordPlayer;
	private String filePath;

	private int VIDEOBUF_SIZE = 204800;
	public int playStopOrStart = 1;// 0录像表示播放 1录像表示停止
	byte[] sPs = { 0x00, 0x00, 0x00, 0x01, 0x67, 0x4D, 0x00, 0x0B, (byte) 0x88,
			(byte) 0x8B, 0x71, 0x62, 0x72, 0x00, 0x00, 0x00, 0x01, 0x68,
			(byte) 0xCE, 0x38, (byte) 0x80 };// DX-20090208-1
	private RandomAccessFile ranFile;

	int n = 1;

	public PlayRecordThread(RecordPlayer recordPlayer, String filePath) {
		super();
		this.recordPlayer = recordPlayer;
		this.filePath = filePath;
	}

	public void setVideoPath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isRunning = false;

	public void run() {
		playStopOrStart = 0;
		try {
			PlayKinescope(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int width = 0, height = 0;// 录像的分辨率 播放的时候用到
	private void PlayKinescope(String pathName) throws IOException {

		byte[] videoBuff = new byte[VIDEOBUF_SIZE];
		byte[] temp = new byte[4];
		int resolutionFlag = 0;// 分辨率标识

		int eachFrameLen = 0;// 每帧的长度
		ranFile = new RandomAccessFile(pathName, "rw");
		// 总的长度
		ranFile.readFully(temp); // 41 - 44
		resolutionFlag = ByteToInt(temp, 4);
		switch (resolutionFlag) {
		case 0:
			width = 176;
			height = 144;
			break;
		case 1:
			width = 320;
			height = 240;
			break;
		case 2:
			width = 352;
			height = 288;
			break;
		case 3:
			width = 640;
			height = 480;
			break;
		case 4:
			width = 704;
			height = 576;
			break;
		}
		// read sps len
		while (true) {
			try {
				if (!videoIsPause) {
					ranFile.readFully(temp);
					eachFrameLen = ByteToInt(temp, 4);
					ranFile.readFully(videoBuff, 0, eachFrameLen);
					if (playStopOrStart == 1) {
						break;
					}
					recordPlayer.decodeframe(videoBuff, eachFrameLen);
					try {
						Thread.sleep(40);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (playStopOrStart == 1) {
						break;
					}
					try {
						Thread.sleep(1000);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (EOFException e) {
				break;
			}
		}
		ranFile.close();
		playStopOrStart = 1;
	}

	private int ByteToInt(byte[] b, int len) {
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < len; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
}
