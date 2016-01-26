package com.tpad.common.model.download.httpconnection;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
	private static final String TAG = DownloadThread.class.getSimpleName();
	private File saveFile;
	private URL downUrl;
	private int block;
	/* 下载开始位置 */
	private int threadId = -1;
	private int downLength;
	private boolean finish = false;
	private HttpConnDownloadOperator downloader;
	int threadSize;

	public DownloadThread(HttpConnDownloadOperator downloader, URL downUrl,
			File saveFile, int block, int downLength, int threadId) {
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block = block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downLength = downLength;
		this.threadSize = this.downloader.getThreadSize();
	}

	@Override
	public void run() {
		RandomAccessFile threadfile = null;
		InputStream inStream = null;
		if (downLength < block) {// 未下载完成
			try {
				
				HttpURLConnection http = null;
				
				http = (HttpURLConnection) downUrl.openConnection();
				http.setConnectTimeout(5 * 1000); // 设置连接超时
				int startPos = block * (threadId - 1) + downLength;// 开始位置
				int endPos = block * threadId - 1;// 结束位置
                http.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);// 设置获取实体数据的范围

				inStream = http.getInputStream();
				byte[] buffer = new byte[1024];
				int offset = 0;
				threadfile = new RandomAccessFile(
						this.saveFile, "rwd");
				threadfile.seek(startPos);
				while (!downloader.getExit() && (offset = inStream.read(buffer, 0, 1024)) != -1 ) {
					threadfile.write(buffer, 0, offset);
					downLength += offset; // 累加下载的大小
					downloader.update(this.threadId, downLength); // 更新指定线程下载最后的位置
					downloader.append(offset); // 累加已下载大小
				}
				this.finish = true;
			} catch (Exception e) {
				this.downLength = -1;
			}finally{
				try {
					if (threadfile!=null) {
						threadfile.close();
					}if (inStream!=null) {
						inStream.close();
					}
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
			}
		}
	}


	/**
	 * 下载是否完成
	 * 
	 * @return
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * 已经下载的内容大小
	 * 
	 * @return 如果返回值为-1,代表下载失败
	 */
	public long getDownLength() {
		return downLength;
	}
}
