package com.gyz.myandroidframe.httprequest.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import android.content.Context;

import com.gyz.myandroidframe.app.AppLog;

public class DataDiskCache implements BaseDataCache {
	private final String tag = this.getClass().getName();
	private static final String CHARSET = "UTF-8";
	private static final int CACHE_TIME = 60000;// 缓存失效时间60 * 60000

	private Context mContext;

	public DataDiskCache(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param mContext
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(Context mContext, String cachefile) {
		boolean exist = false;
		File data = mContext.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 判断缓存是否失效
	 * 
	 * @param cachefile
	 * @return
	 */
	public boolean isCacheDataFailure(Context mContext, String fileName) {
		boolean failure = false;
		File data = mContext.getFileStreamPath(fileName);
		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if (!data.exists())
			failure = true;
		return failure;
	}

	@Override
	public void put(String key, String data) {
		// TODO Auto-generated method stub
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = mContext.openFileOutput(key, mContext.MODE_PRIVATE);
			osw = new OutputStreamWriter(fos, CHARSET);
			bw = new BufferedWriter(osw);
			bw.write(data);
			AppLog.e("saveText2File", data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppLog.e(tag, "saveObject error : " + e.getStackTrace());
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (osw != null) {
					osw.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (Exception exc) {
				// TODO: handle exception
				AppLog.e(tag, "saveText2File error : " + exc.getStackTrace());
			}
		}
	}

	@Override
	public String getString(String key) {
		// TODO Auto-generated method stub
		if (!isExistDataCache(mContext, key))
			return null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String results = "";
		try {
			fis = mContext.openFileInput(key);
			isr = new InputStreamReader(fis, CHARSET);
			br = new BufferedReader(isr);
			while (br.readLine() != null) {
				results += br.readLine();
			}
			AppLog.e("readFile2Text", results);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (Exception exc) {
				AppLog.e(tag, "saveObject error : " + exc.getStackTrace());
			}
		}
		return null;
	}

	@Override
	public void evictAll() {
		// TODO Auto-generated method stub
		clearCacheFolder(mContext.getFilesDir(), System.currentTimeMillis());
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 清除缓存目录
	 * 
	 * @param dir
	 *            目录
	 * @param numDays
	 *            当前系统时间
	 * @return 缓存文件数
	 */
	private void clearCacheFolder(File dir, long curTime) {
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						child.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取缓存大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public long getFileSize(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	private boolean saveObject2File(Context mContex, Serializable ser,
			String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = mContex.openFileOutput(file, mContex.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (fos != null)
					fos.close();
			} catch (Exception exc) {
				AppLog.e(tag, "saveObject2File error : " + exc.getStackTrace());
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private Serializable readFile2Object(Context mContext, String file) {
		if (!isExistDataCache(mContext, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = mContext.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = mContext.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (Exception exc) {
				AppLog.e(tag, "readFile2Object error : " + exc.getStackTrace());
			}
		}
		return null;
	}

	@Override
	public boolean put(String key, Serializable data) {
		// TODO Auto-generated method stub
		return saveObject2File(mContext, data, key);
	}

	@Override
	public Serializable getObject(String key) {
		// TODO Auto-generated method stub
		return readFile2Object(mContext, key);
	}
}
