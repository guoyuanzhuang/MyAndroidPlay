package com.gyz.myandroidframe.bean;

/**
 * 下载相关信息
 * 
 * @author guoyuanzhuang
 * 
 */
public class DownloadBean extends BaseEntity {
	private String downloadId_key = "download_id";// 下载文件的downloadId
	private String downloadUrl; // 下载链接
	private int imgResourseId; // 下载图标
	//
	private String fileName; // 下载文件名称
	private String downloadDir; // 文件保存地址

	public DownloadBean(String downloadUrl, String downloadName,
			String downloadPath) {
		this.downloadUrl = downloadUrl;
		this.fileName = downloadName;
		this.downloadDir = downloadPath;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public int getImgResourseId() {
		return imgResourseId;
	}

	public void setImgResourseId(int imgResourseId) {
		this.imgResourseId = imgResourseId;
	}

	public String getDownloadId_key() {
		return downloadId_key;
	}

	public void setDownloadId_key(String downloadId_key) {
		this.downloadId_key = downloadId_key;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

}
