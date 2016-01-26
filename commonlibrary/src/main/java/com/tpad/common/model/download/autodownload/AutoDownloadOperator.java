package com.tpad.common.model.download.autodownload;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpad.common.model.download.downloadmanager.DownloadConfig;
import com.tpad.common.model.download.downloadmanager.DownloadInfo;
import com.tpad.common.model.download.downloadmanager.DownloadManagerOperator;
import com.tpad.common.model.download.downloadmanager.DownloadMd5Util;
import com.tpad.common.model.download.httpconnection.HttpConnDownloadOperator;
import com.tpad.common.utils.FileUtils;
import com.tpad.common.utils.GsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动下载的操作者
 * @author jone.sun on 2015/5/8.
 */
public class AutoDownloadOperator {
    private static final String TAG = AutoDownloadOperator.class.getSimpleName();
    private int count = 0;
    private int index = 0;
    private int redownloadCount = 0; //重试的次数
    private List<AutoDownloadData> autoDownloadBeanList;
    private Context context;

    private DownloadManagerOperator downloadManagerOperator;

    private AutoDownloadData currentAutoDownloadData = null;
    private ResponseListener responseListener = null; //此回调为外部传入(全部下载完成或者下载遇到问题需退出)
    private long allowSDSize = 50 * 1024 * 1024;

    public AutoDownloadOperator(Context context,
                                List<AutoDownloadData> autoDownloadBeanList){
        this.context = context;
        this.autoDownloadBeanList = autoDownloadBeanList;
        init();
    }

    public AutoDownloadOperator(Context context,
                                List<AutoDownloadData> autoDownloadBeanList,
                                long allowSDSize){
        this.context = context;
        this.autoDownloadBeanList = autoDownloadBeanList;
        this.allowSDSize = allowSDSize;
        init();
    }

    private void init(){
        if(autoDownloadBeanList != null && autoDownloadBeanList.size() > 0){
            count = autoDownloadBeanList.size();
            AutoDownloadData tmpDownloadRecord = autoDownloadBeanList.get(0);
            if(tmpDownloadRecord.getDownloadType().equals(AutoDownloadData.SAVE_TYPE_OF_APK)){
                downloadManagerOperator = DownloadManagerOperator.getInstance(context);
            }
        }
    }

    public void startDownload(ResponseListener listener){
        if(listener != null){
            responseListener = listener;
        }
        if(redownloadCount > 1){ //如果重新下载的次数超过一次，则下载下个任务
            AutoDownloadDataDao.getInstance(context).delete(currentAutoDownloadData);
            deleteFailFile();
            index += 1;
            redownloadCount = 0;
        }
        if (index > -1 && index < count) {
            currentAutoDownloadData = autoDownloadBeanList.get(index);
            if(currentAutoDownloadData == null){
                responseListener.onSuccess("autoDownloadBean is null"); //防止计数器出现问题
                return;
            }
            //todo 判断当前是不是WIFI情况

            long sdFreeSize = FileUtils.getSDFreeSize();
            if(sdFreeSize <= allowSDSize + currentAutoDownloadData.getSize()){
                //轮询下一个
                responseForDownloadListener.onFailure("SD卡空闲空间小于要求的空间:"
                        + allowSDSize
                        + " , currentAutoDownloadData.getSize: " + currentAutoDownloadData.getSize()
                        + ", sdFreeSize: " + sdFreeSize);
                return;
            }
            download();
        } else {
            responseListener.onSuccess("");
        }
    }

    //此回调为内部调用(每个下载任务完成的回调)
    private ResponseListener responseForDownloadListener = new ResponseListener() {
        @Override
        public void onSuccess(String data) {
            Log.e(TAG, "onSuccess: " + data);
            AutoDownloadDataDao.getInstance(context).delete(currentAutoDownloadData);
            index += 1;
            startDownload(null);
        }

        @Override
        public void onFailure(String error) {
            Log.e(TAG, error + "");
            redownloadCount += 1;//下载失败后再尝试下载一遍
            startDownload(null);
        }
    };

    private void download(){
        if(currentAutoDownloadData == null
                || currentAutoDownloadData.getUrl() == null
                || currentAutoDownloadData.getDownloadType() == null
                || currentAutoDownloadData.getSaveName() == null
                || currentAutoDownloadData.getSavePath() == null){
            responseListener.onFailure("参数传递错误"); //告知外部下载失败
            return;
        }
        Log.e(TAG, "准备下载: " + currentAutoDownloadData.getUrl());
        switch (currentAutoDownloadData.getDownloadType()){
            case AutoDownloadData.SAVE_TYPE_OF_APK:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadCpaAPk();
                    }
                }).start();
                break;
            case AutoDownloadData.SAVE_TYPE_OF_HTML:
                downloadHtml();
                break;
            case AutoDownloadData.SAVE_TYPE_OF_IMAGE:
                downloadImage();
                break;
            default:
                responseListener.onFailure("参数传递错误: " + currentAutoDownloadData.getDownloadType());//告知外部下载失败
                break;
        }
    }

    private void downloadImage() {
        File file = new File(currentAutoDownloadData.getSavePath() + File.separator + currentAutoDownloadData.getSaveName());
        if(file.exists() && file.canRead() && file.length() > 0){
            Log.e(TAG, "本地存在图片，不需要下载: " + currentAutoDownloadData.getUrl() + ", 保存到缓存的名称为: " + currentAutoDownloadData.getSaveName());
            responseForDownloadListener.onSuccess("本地存在图片，不需要下载: " + currentAutoDownloadData.getUrl() + ", 路径为: " + file.getPath() + ", 文件大小: " + file.length());
        }else {
            HttpConnDownloadOperator loader = new HttpConnDownloadOperator();
            loader.startDownload(context,
                    currentAutoDownloadData.getUrl(),
                    currentAutoDownloadData.getSavePath(),
                    currentAutoDownloadData.getSaveName(),
                    3,
                    new HttpConnDownloadOperator.DownloadListener() {
                        @Override
                        public void onDownloadFailed(String fileName, int downloadSize, int fileSize, String reason) {
                            responseForDownloadListener.onFailure("下载失败: " + currentAutoDownloadData.getUrl());
                        }

                        @Override
                        public void onDownloadComplete(String fileName, int downloadSize, int fileSize) {
                            responseForDownloadListener.onSuccess("下载成功: " + fileName);
                        }

                        @Override
                        public void onDownloading(int downloadSize, int fileSize) {

                        }
                    });
        }
    }

    private void downloadHtml() {
        if(!currentAutoDownloadData.getSaveName().endsWith(".html")){
            currentAutoDownloadData.setSaveName(currentAutoDownloadData.getSaveName() + ".html");
        }
        //todo 判断本地文件是否存在以及是否最新
        File file = new File(currentAutoDownloadData.getSavePath() + File.separator + currentAutoDownloadData.getSaveName());
        if(file.exists()){
            responseForDownloadListener.onSuccess("cap网页" + currentAutoDownloadData.getSaveName() + "已经存在");
        }else {
            try {
                if (DownloadHtmlFileUtil.getInstance().save(
                        DownloadHtmlFileUtil.getInstance().getHtmlCode(currentAutoDownloadData.getUrl()),
                        currentAutoDownloadData.getSavePath(),
                        currentAutoDownloadData.getSaveName())) {
                    responseForDownloadListener.onSuccess("cap网页" + currentAutoDownloadData.getSaveName() + "下载成功。");
                } else {
                    responseForDownloadListener.onFailure("cap网页" + currentAutoDownloadData.getSaveName() + "下载成功,保存失败。");
                }
            } catch (Exception e) {
                responseForDownloadListener.onFailure("保存错误");
            }
        }

    }

    private void downloadCpaAPk() {
        String url = currentAutoDownloadData.getUrl();
        if(!currentAutoDownloadData.getSaveName().endsWith(".apk")){
            currentAutoDownloadData.setSaveName(currentAutoDownloadData.getSaveName() + ".apk");
        }
        File currentFilePath = new File(currentAutoDownloadData.getSavePath() + File.separator + currentAutoDownloadData.getSaveName());
        if(DownloadMd5Util.getInstance().isCompleteFile(url, currentFilePath.getPath())){
            responseForDownloadListener.onSuccess("本地存在,不需要下载: " + currentFilePath.getPath());
        }else {
            new File(currentAutoDownloadData.getSavePath()).mkdirs();
            if(downloadManagerOperator.isCanDownload()){
                DownloadInfo downloadInfo = downloadManagerOperator.getDownloadingInfoByUrl(currentAutoDownloadData.getUrl());
                if(downloadInfo != null){
                    //当前正在下载
                    downloadManagerOperator.monitorDownloadStatus(context,
                            downloadInfo.get_id(),
                            downloadListener);
                }else {
                    try {
                        DownloadConfig downloadConfig = new DownloadConfig(
                                currentAutoDownloadData.getUrl(),
                                currentAutoDownloadData.getSavePath(),
                                currentAutoDownloadData.getSaveName(),
                                false,
                                currentAutoDownloadData.getIsCanMobile());
                        downloadManagerOperator.monitorDownloadStatus(context,
                                downloadManagerOperator.download(downloadConfig),
                                downloadListener);
                    } catch (DownloadManagerOperator.DownloadException e) {
                        responseForDownloadListener.onFailure("下载失败>>url: " + url + " ,原因: " + e.getMessage());
                    }
                }
            }else {
                responseForDownloadListener.onFailure("下载失败>>当前环境不允许下载" + url);
            }
        }
    }

    private DownloadManagerOperator.DownloadListener downloadListener = new DownloadManagerOperator.DownloadListener() {
        @Override
        public void onReady(DownloadInfo downloadInfo) {

        }

        @Override
        public void onDownloading(DownloadInfo downloadInfo, int downloadSize, int countSize) {

        }

        @Override
        public void onPause(DownloadInfo downloadInfo) {

        }

        @Override
        public void onComplete(DownloadInfo downloadInfo, String filePath) {
            responseForDownloadListener.onSuccess(filePath);
            downloadManagerOperator.unMonitorDownloadStatus();
        }

        @Override
        public void onFailed(DownloadInfo downloadInfo, String reason) {
            responseForDownloadListener.onFailure("下载失败>>url: " + currentAutoDownloadData.getUrl() + " ,原因: " + reason);
            downloadManagerOperator.unMonitorDownloadStatus();
        }
    };

    private void deleteFailFile(){
        File file = new File(currentAutoDownloadData.getSavePath() + File.separator + currentAutoDownloadData.getSaveName());
        if(file.exists()){
            file.delete();
        }
    }

    public void stopDownload(){
        try {
            if(downloadManagerOperator != null){
                downloadManagerOperator.unMonitorDownloadStatus();
                DownloadInfo downloadInfo = downloadManagerOperator.getDownloadingInfoByUrl(currentAutoDownloadData.getUrl());
                if(downloadInfo != null){
                    downloadManagerOperator.removeDownload(downloadInfo.get_id());
                    new File(downloadInfo.getLocal_filename()).delete();
                }
            }
            currentAutoDownloadData = null;
            responseForDownloadListener = null;
        } catch (Exception e) {
            //Log.e(TAG, "stopService", e);
        }
    }
    public interface ResponseListener {
        void onSuccess(String data);

        void onFailure(String error);
    }
}
