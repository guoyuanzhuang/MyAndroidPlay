package com.gyz.megaeyefamily.remote;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.content.Context;
import android.util.Log;

import com.gyz.megaeyefamily.ui.R;

/**
 * 
 * @author
 * 
 */
public class PtzUdpClient {

	private Context context;
	public String serverIp;
	public int serverPort;
	public String userName;// 用户名
	public String destID;// 目标监控点

	public int step;// 最终操作步长
	private int ptStep;// 云台步长
	private int zStep;// 镜头步长

	/**
	 * 构造函数 需要服务器ip，服务器端口，用户名，监控点id，云台步长，镜头步长
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @param userName
	 * @param destID
	 * @param ptStep
	 * @param zStep
	 */
	public PtzUdpClient(Context context, String serverIp, int serverPort,
			String userName, String destID, int ptStep, int zStep) {
		super();
		this.context = context;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.userName = userName;
		this.destID = destID;
		this.ptStep = ptStep;
		this.zStep = zStep;
	}

	/**
	 * 读云镜文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public String readPtzCfg(int fileName) {
		String content = "";
		String line = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(context
					.getResources().openRawResource(fileName)));
			while ((line = reader.readLine()) != null) {
				content += line;
				content += "\r\n";

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	/**
	 * 控制云台方法
	 * 
	 * @param opType
	 */
	public void ptzAction(int opType) {
		
		String ptzBody = readPtzCfg(R.raw.ptz_body);
		// PrintRuningLog.printLog("加载R.raw.ptz_body文件中的内容:"+ptzBody);
		String newstr = ptzBody.replaceFirst("%s", String.valueOf(userName));
		// PrintRuningLog.printLog("给userName赋值:"+newstr);
		newstr = newstr.replaceFirst("%s", String.valueOf(destID));
		// PrintRuningLog.printLog("给destID赋值:"+newstr);
		newstr = newstr.replaceFirst("%s", String.valueOf(opType));
		// PrintRuningLog.printLog("给OpId赋值:"+newstr);
		newstr = newstr.replaceFirst("%s", String.valueOf(step));
		// PrintRuningLog.printLog("给step赋值:"+ptzBody);
		int bodyLength = newstr.length() - 1;
		// PrintRuningLog.printLog("Content-Length:"+bodyLength);
		String ptzHeader = readPtzCfg(R.raw.ptz_head);
		String newstr1 = ptzHeader.replaceFirst("%d",
				String.valueOf(bodyLength));
		// PrintRuningLog.printLog("R.raw.ptz_head文件中ontent-Length赋值以后的内容:"+ptzHeader);
		// System.out.println("--------------------------------");
		// System.out.println(headlen);
		// String count = ptzHeader + "\n" + ptzBody;

		StringBuffer temp = new StringBuffer(newstr1 + newstr);
		temp.append("\r\n");
		String count = temp.toString();

		Log.v("sendString", count);

		byte[] sendBuff = count.getBytes();
		try {
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dg = new DatagramPacket(sendBuff, sendBuff.length,
					InetAddress.getByName(serverIp), serverPort);
			ds.send(dg);
			Thread.sleep(50);
			ds.close();
			String newstrstop = ptzBody.replaceFirst("%s",
					String.valueOf(userName));
			// PrintRuningLog.printLog("给userName赋值:"+newstr);
			newstrstop = newstrstop.replaceFirst("%s", String.valueOf(destID));
			// PrintRuningLog.printLog("给destID赋值:"+newstr);
			newstrstop = newstrstop.replaceFirst("%s", String.valueOf(15));
			// PrintRuningLog.printLog("给OpId赋值:"+newstr);
			newstrstop = newstrstop.replaceFirst("%s", String.valueOf(step));
			// PrintRuningLog.printLog("给step赋值:"+ptzBody);
			bodyLength = newstrstop.length() - 1;
			String newstrstop1 = ptzHeader.replaceFirst("%d",
					String.valueOf(bodyLength));
			StringBuffer temp1 = new StringBuffer(newstrstop1 + newstrstop);
			temp1.append("\r\n");
			String count1 = temp1.toString();
			byte[] sendBuffStop = count1.getBytes();
			Thread.sleep(300);
			DatagramSocket ds1 = new DatagramSocket();
			DatagramPacket dg1 = new DatagramPacket(sendBuffStop,
					sendBuffStop.length, InetAddress.getByName(serverIp),
					serverPort);
			ds1.send(dg1);
			Thread.sleep(50);
			ds1.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
