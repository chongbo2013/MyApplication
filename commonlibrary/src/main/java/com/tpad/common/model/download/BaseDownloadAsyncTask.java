package com.tpad.common.model.download;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.tpad.common.model.download.downloadmanager.DownloadConfig;
import com.tpad.common.model.download.downloadmanager.DownloadInfo;
import com.tpad.common.model.download.downloadmanager.DownloadManagerOperator;
import com.tpad.common.model.download.downloadmanager.DownloadMd5Util;
import com.tpad.common.model.download.downloadmanager.DownloadUtil;

import java.io.File;

/**
 * Created by jone.sun on 2015/10/9.
 */
public abstract class BaseDownloadAsyncTask extends AsyncTask<DownloadConfig, DownloadInfo, String> {
    private static final String TAG = BaseDownloadAsyncTask.class.getSimpleName();
    private Context context;
    private DownloadManagerOperator downloadManagerOperator;
    private DownloadManagerOperator.DownloadListener downloadListener;
    private boolean isCancel = false;
    public BaseDownloadAsyncTask(Context context, DownloadManagerOperator.DownloadListener downloadListener){
        this.context = context;
        downloadManagerOperator = DownloadManagerOperator.getInstance(context);
        this.downloadListener = downloadListener;
    }

    @Override
    protected void onCancelled() {
        isCancel = true;
        super.onCancelled();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCancelled(String s) {
        isCancel = true;
        super.onCancelled(s);
    }

    @Override
    protected void onProgressUpdate(DownloadInfo... values) {
        super.onProgressUpdate(values);
        DownloadInfo downloadInfo = values[0];
        switch (downloadInfo.getStatus()) {
            case DownloadManager.STATUS_PAUSED:
                downloadListener.onPause(downloadInfo);
                break;
            case DownloadManager.STATUS_PENDING:
                downloadListener.onReady(downloadInfo);
                break;
            case DownloadManager.STATUS_RUNNING:
                downloadListener.onDownloading(downloadInfo, downloadInfo.getBytes_so_far(), downloadInfo.getTotal_size());
                break;
        }
    }

    public void monitorDownloadingByDownloadManager(String url){
        DownloadInfo downloadInfo = null;
        if(downloadListener != null){
            boolean isDownloading = true;
            while (isDownloading && !isCancel){
                try {
                    Thread.sleep(500);
                    DownloadInfo downloadingInfo = downloadManagerOperator.getDownloadingInfoByUrl(url);
                    if(downloadingInfo != null){
                        downloadInfo = downloadingInfo;
                        switch (downloadInfo.getStatus()) {
                            case DownloadManager.STATUS_PAUSED:
                                isDownloading = true;
                                publishProgress(downloadInfo);
                                break;
                            case DownloadManager.STATUS_PENDING:
                                isDownloading = true;
                                publishProgress(downloadInfo);
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                if (downloadInfo.getBytes_so_far() > 0 && downloadInfo.getTotal_size() == 0) {
                                    isDownloading = false;
                                } else {
                                    isDownloading = true;
                                    publishProgress(downloadInfo);
                                }
                                break;
                        }
                    }else {
                        isDownloading = false;
                    }
                }catch (Exception e){
                    isDownloading = false;
                    isCancel = true;
                    Log.e(TAG, "monitorDownloadingByDownloadManager", e);
                }
            }
            if(downloadInfo != null){
                Log.e("下载", "下载完成: " + downloadInfo.getStatus());
                if(downloadInfo.getStatus() == DownloadManager.STATUS_FAILED){
                    notifyDownloadFinish(false, downloadInfo,
                            DownloadUtil.getDownloadReason(downloadInfo.getReason()));
                } else if(!isCancel){
                    checkDownloadDownFile(downloadInfo);
                } else {
                    Log.e(TAG, "取消监听下载");
                }
            } else {
                notifyDownloadFinish(false, null, "downloadInfo is null");
            }
        }
    }

    public void checkDownloadDownFile(DownloadInfo downloadInfo){
        Log.e("下载", "校验文件" + downloadInfo.getTitle());
        boolean completeFile = DownloadMd5Util.getInstance().
                isCompleteFile(downloadInfo.getUri(), downloadInfo.getLocal_filename());
        Log.e("下载", "校验文件完成: " + downloadInfo.getTitle() + " " + completeFile);
        File file = new File(downloadInfo.getLocal_filename());
        if(completeFile){
            String path = file.getPath();
            String lastFileName = path.replace(DownloadManagerOperator.DOWNLOAD_FILE_TMP_SUFFIX, "");
            completeFile = file.renameTo(new File(lastFileName));
            downloadInfo.setLocal_filename(lastFileName);
            if (completeFile) {
                notifyDownloadFinish(true, downloadInfo, "");
            }else {
                notifyDownloadFinish(false, downloadInfo, "重命名失败");
            }
        }else {
            file.delete();
            Log.e(TAG, "删除非完整文件: " + file.getPath());
            notifyDownloadFinish(false, downloadInfo, "md5校验为非完整文件");
        }
    }

    public void notifyDownloadFinish(boolean isComplete, DownloadInfo downloadInfo, String reason) {
        if(isComplete){
            downloadListener.onComplete(downloadInfo, downloadInfo.getLocal_filename());
        }else{
            downloadListener.onFailed(downloadInfo, reason);
        }
        Intent intent = new Intent(DownloadManagerOperator.ACTION_DOWNLOAD_COMPLETE);
        intent.putExtra("isComplete", isComplete);
        intent.putExtra("downloadInfo", downloadInfo);
        intent.putExtra("reason", reason);
        context.sendBroadcast(intent);
    }

    public Context getContext() {
        return context;
    }
}
