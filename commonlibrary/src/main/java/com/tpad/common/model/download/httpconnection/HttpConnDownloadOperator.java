package com.tpad.common.model.download.httpconnection;

import android.content.Context;
import android.util.Log;

import com.tpad.common.utils.Md5Utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件下载器
 * 
 */
public class HttpConnDownloadOperator {
	private static final String TAG = "FileDownloader";
    public static final String fileTmpSuffix = ".ttmp";
	private Context context;
	private DownloadEntityDao downloadEntityDao;
	/* 停止下载 */
	private boolean exit;
	/* 已下载文件长度 */
	private int downloadSize = 0;
	/* 线程数 */
	private DownloadThread[] threads;
	/* 缓存各线程下载的长度 */
	private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
	/* 每条线程下载的长度 */
	private int block;
	/*获得网络连接*/
	private GetHttpConnection httpConnection;
    private String contentMd5 = "";

    private void init(){
        downloadEntityDao = null;
        exit = false;
        downloadSize = 0;
        threads = null;
        data = new ConcurrentHashMap<>();
        block = 0;
        httpConnection = null;
        contentMd5 = "";
    }
	/**
	 * 累计已下载大小
	 * 
	 * @param size
	 */
	protected synchronized void append(int size) {
		downloadSize += size;
	}

    /***
     * 更新指定线程最后下载的位置
     * @param threadId 线程id
     * @param pos 最后下载的位置
     */
	protected synchronized void update(int threadId, int pos) {
		this.data.put(threadId, pos);
	}

    public void startDownload(Context context, String downloadUrl ,String saveDir, String saveName, int threadNum, DownloadListener downloadListener){
        init();
        if(context == null || downloadUrl == null || saveDir == null){
            if(downloadListener != null){
                downloadListener.onDownloadFailed(null, -1, -1,"参数传递有误");
            }
        }else {
            try {
                httpConnection = new GetHttpConnection();
                HttpURLConnection conn = httpConnection.getConnection(downloadUrl);
                int responseCode = conn.getResponseCode();
                if(responseCode == 200){
                    int fileSize = conn.getContentLength();
                    if(fileSize <= 0){
                        if(downloadListener != null){
                            downloadListener.onDownloadFailed(null, -1, -1,"contentLength <= 0");
                        }
                    }else {
                        String s = getFileName(downloadUrl, conn); //通过conn获取fileName,并且得到MD5
                        if(saveName == null){
                            saveName = s;
                        }
                        saveName = saveName + fileTmpSuffix;
                        File saveFile;
                        if(saveDir.lastIndexOf(File.separator) == saveDir.length() - 1){
                            saveFile = new File(saveDir + saveName);
                        }else {
                            saveFile = new File(saveDir + File.separator + saveName);
                        }
                        File parentFile = saveFile.getParentFile();
                        if (parentFile != null){
                            if(!parentFile.exists()){
                                parentFile.mkdirs();
                            }
                            if(threadNum <= 0){
                                threadNum = 1;
                            }
                            threads = new DownloadThread[threadNum];
                            downloadEntityDao = DownloadEntityDao.getInstance(context);
                            Map<Integer, Integer> map = downloadEntityDao.getData(downloadUrl);
                            if(saveFile.exists()){
                                if (map != null && map.size() > 0 ) {// 如果存在下载记录
                                    for (Map.Entry<Integer, Integer> entry : map.entrySet()){
                                        data.put(entry.getKey(), entry.getValue());
                                    }
                                }else {
                                    saveFile.delete();
                                }
                            }

                            if (data.size() == threads.length) {// 下面计算所有线程已经下载的数据总长度
                                for (int i = 0; i < threads.length; i++) {
                                    downloadSize += data.get(i + 1);
                                }
                            }
                            // 计算每条线程下载的数据长度
                            block = (fileSize % this.threads.length) == 0 ? fileSize
                                    / this.threads.length
                                    : fileSize / this.threads.length + 1;
                            downloadFile(downloadUrl, saveFile, fileSize, downloadListener);
                        }else {
                            if(downloadListener != null){
                                downloadListener.onDownloadFailed(null, -1, -1,"parentFile is null");
                            }
                        }
                    }
                }else {
                    if(downloadListener != null){
                        downloadListener.onDownloadFailed(null, -1, -1,"server responseCode is " + responseCode);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
                if(downloadListener != null){
                    downloadListener.onDownloadFailed(null, -1, -1, e.getMessage());
                }
            }finally {
                if(httpConnection != null){
                    httpConnection.closeConnection();
                }
            }
        }
    }

    private void downloadFile(String downloadUrl,File saveFile, int fileSize, DownloadListener downloadListener) throws Exception {
        try {
            RandomAccessFile randOut = new RandomAccessFile(saveFile, "rw");
            if (fileSize > 0){
                randOut.setLength(fileSize); // 预分配fileSize大小
            }
            randOut.close();
            URL url = new URL(downloadUrl);
            if (data.size() != threads.length) {// 如果原先未曾下载或者原先的下载线程数与现在的线程数不一致
                data.clear();
                for (int i = 0; i < threads.length; i++) {
                    this.data.put(i + 1, 0);// 初始化每条线程已经下载的数据长度为0
                }
                downloadSize = 0;
            }
            boolean flag = true;
            for (int i = 0; i < threads.length; i++) {// 开启线程进行下载
                int downLength = data.get(i + 1);
                if (downLength < block && downloadSize < fileSize && flag) {// 判断线程是否已经完成下载,否则继续下载
                    threads[i] = new DownloadThread(this, url, saveFile, block,
                            data.get(i + 1), i + 1);
                    threads[i].setPriority(7); // 设置线程优先级
                    threads[i].start();
                    flag = threads[i].isAlive();
                } else {
                    threads[i] = null;
                }
            }
            downloadEntityDao.delete(downloadUrl);// 如果存在下载记录，删除它们，然后重新添加
            downloadEntityDao.save(downloadUrl, data);
            boolean notFinish = true;// 下载未完成
            int count = 0;
            while (notFinish && flag) {// 循环判断所有线程是否完成下载
                try {
                    Thread.sleep(300);
                }catch (Exception e){}
                notFinish = false;// 假定全部线程下载完成
                for (int i = 0; i < threads.length; i++) {
                    if (threads[i] != null && !threads[i].isFinish()) {// 如果发现线程未完成下载
                        notFinish = true;// 设置标志为下载没有完成
                        flag = threads[i].isAlive();
                        if (threads[i].getDownLength() == -1) {// 如果下载失败,再重新下载
                            threads[i] = new DownloadThread(this, url, saveFile, this.block,
                                    data.get(i + 1), i + 1);
                            threads[i].setPriority(7);
                            threads[i].start();
                            count++;
                            if (count > 5) {
                                if (downloadListener!=null){
                                    downloadListener.onDownloadFailed(saveFile.toString(), downloadSize, fileSize,"count > 5");
                                }
                            }
                        }
                    }
                }
                if (downloadListener != null){
                    downloadListener.onDownloading(downloadSize, fileSize);// 通知目前已经下载完成的数据长度
                }
            }
            if (downloadSize == fileSize){
                // 下载完成删除记录
                downloadEntityDao.delete(downloadUrl);
                Log.e(TAG, "contentMd5: " + contentMd5 + "local: " + Md5Utils.getFileMD5StringForApache(saveFile));
                if(contentMd5 == null || contentMd5.length() == 0
                        || contentMd5.equals(Md5Utils.getFileMD5StringForApache(saveFile))){
                    String saveFileName = saveFile.getName().replace(fileTmpSuffix, "");
                    String savePath = saveFile.getParent().concat(File.separator).concat(saveFileName);
                    if(saveFile.renameTo(new File(savePath))){
                        if (downloadListener != null){
                            downloadListener.onDownloadComplete(savePath, downloadSize, fileSize);
                        }
                    }else {
                        if (downloadListener != null){
                            downloadListener.onDownloadFailed(saveFile.toString(), downloadSize, fileSize, "文件重命名失败");
                        }
                    }
                }else {
                    if (downloadListener != null){
                        downloadListener.onDownloadFailed(saveFile.toString(), downloadSize, fileSize, "文件不完整");
                    }
                }
                downloadEntityDao.delete(downloadUrl);
            }else {
                if (!getExit()) {
                    downloadEntityDao.delete(downloadUrl);
                    if(contentMd5.equals(Md5Utils.getFileMD5StringForApache(saveFile))){
                        String saveFileName = saveFile.getName().replace(fileTmpSuffix, "");
                        saveFile.renameTo(new File(saveFile.getParent().concat(File.separator)
                                .concat(saveFileName)));
                        if (downloadListener != null){
                            downloadListener.onDownloadComplete(saveFile.toString(), downloadSize, fileSize);
                        }
                    }else {
                        if (downloadListener != null){
                            downloadListener.onDownloadFailed(saveFile.toString(), downloadSize, fileSize,
                                    "文件不完整&文件大小不一致"
                                    + "downloadSize: " + downloadSize + ", fileSize: " + fileSize);
                        }
//                        saveFile.renameTo(new File(saveFile.getParentFile().toString()
//                                + File.separator + System.currentTimeMillis() + ".apk"));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "downloadFile", e);
            if (!getExit()) {
                downloadEntityDao.delete(downloadUrl);
                if (downloadListener != null){
                    downloadListener.onDownloadFailed(saveFile.toString(), downloadSize, fileSize, e.getMessage());
                }
            }
        }
    }



	/**
	 * 获取文件名
	 */
	private String getFileName(String downloadUrl, HttpURLConnection conn) {
		String filename = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
		try {
			filename = URLDecoder.decode(filename, "utf-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "", e);
		}
        for (int i = 0;; i++) {
            String key = conn.getHeaderFieldKey(i);
            String mine = conn.getHeaderField(i);
            if (mine == null){
                break;
            }
            if(key != null && key.toLowerCase().equals("content-md5")){
                contentMd5 = mine;
            }
            if(filename == null || "".equals(filename.trim())){
                if ("content-disposition".equals(key)) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(
                            mine.toLowerCase());
                    if (m.find()){
                        filename = m.group(1);
                    }
                }else {
                    filename = UUID.randomUUID() + ".tmp";// 默认取一个文件名
                }
            }
        }
		return filename;
	}

    /**
     * 获取线程数
     */
    public int getThreadSize() {
        return threads.length;
    }

    /**
     * 退出下载
     */
    public void exit(String downloadUrl) {
        exit = true;
        for (int i = 1; i < data.size()+1; i++) {
            downloadEntityDao.update(downloadUrl, i, data.get(i));
        }
    }

    public boolean getExit() {
        return exit;
    }

    public interface DownloadListener {

        public void onDownloadFailed(String fileName, int downloadSize, int fileSize, String reason);

        public void onDownloadComplete(String fileName, int downloadSize, int fileSize);

        public void onDownloading(int downloadSize, int fileSize);

    }
}
