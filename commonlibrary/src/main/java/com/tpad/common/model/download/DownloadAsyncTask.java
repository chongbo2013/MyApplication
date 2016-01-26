package com.tpad.common.model.download;

import android.app.DownloadManager;
import android.content.Context;

import com.tpad.common.model.download.downloadmanager.DownloadConfig;
import com.tpad.common.model.download.downloadmanager.DownloadInfo;
import com.tpad.common.model.download.downloadmanager.DownloadManagerOperator;
import com.tpad.common.model.download.downloadmanager.DownloadUtil;
import com.tpad.common.model.download.httpconnection.HttpConnDownloadOperator;

import java.io.File;

/**
 * Created by jone.sun on 2015/10/8.
 */
public class DownloadAsyncTask extends BaseDownloadAsyncTask {
    public  DownloadAsyncTask(Context context, DownloadManagerOperator.DownloadListener downloadListener){
        super(context, downloadListener);
    }

    @Override
    protected String doInBackground(DownloadConfig... downloadConfigs) {
        if(DownloadManagerOperator.getInstance(getContext()).isCanDownload()){
            downloadByDownloadManager(downloadConfigs[0]);
        }else {
            downloadByHttpConnection(downloadConfigs[0]);
        }
        return null;
    }

    private void downloadByDownloadManager(DownloadConfig downloadConfig){
        String url = downloadConfig.getUrl();
        DownloadInfo downloadInfo = DownloadManagerOperator.getInstance(getContext()).getDownloadInfoByUrl(url);
        if(downloadInfo == null
                || downloadInfo.getStatus() == DownloadManager.STATUS_FAILED
                || !new File(downloadInfo.getLocal_filename()).exists()){
            startDownloadingByDownloadManager(downloadConfig);
        }else if(DownloadUtil.isDownloadingByStateId(downloadInfo.getStatus())){
            monitorDownloadingByDownloadManager(url);
        } else {
            checkDownloadDownFile(downloadInfo);
        }
    }

    private void startDownloadingByDownloadManager(DownloadConfig downloadConfig){
        try {
            DownloadManagerOperator.getInstance(getContext()).download(downloadConfig);
            monitorDownloadingByDownloadManager(downloadConfig.getUrl());
        } catch (DownloadManagerOperator.DownloadException e) {
            downloadByHttpConnection(downloadConfig);
        }
    }

    private void downloadByHttpConnection(final DownloadConfig downloadConfig){
        HttpConnDownloadOperator httpConnDownloadOperator = new HttpConnDownloadOperator();
        httpConnDownloadOperator.startDownload(getContext(),
                downloadConfig.getUrl(),
                downloadConfig.getSavePath(),
                downloadConfig.getSaveName(),
                3, new HttpConnDownloadOperator.DownloadListener() {
                    @Override
                    public void onDownloadFailed(String fileName, int downloadSize, int fileSize, String reason) {
                        if (fileName != null) {
                            File file = new File(fileName);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        DownloadInfo downloadInfo = new DownloadInfo();
                        downloadInfo.setUri(downloadConfig.getUrl());
                        downloadInfo.setStatus(DownloadManager.STATUS_FAILED);
                        downloadInfo.setBytes_so_far(downloadSize);
                        downloadInfo.setTotal_size(fileSize);
                        notifyDownloadFinish(false, downloadInfo, reason);
                    }

                    @Override
                    public void onDownloadComplete(String fileName, int downloadSize, int fileSize) {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        downloadInfo.setUri(downloadConfig.getUrl());
                        downloadInfo.setStatus(DownloadManager.STATUS_SUCCESSFUL);
                        downloadInfo.setBytes_so_far(downloadSize);
                        downloadInfo.setTotal_size(fileSize);
                        notifyDownloadFinish(true, downloadInfo, "");
                    }

                    @Override
                    public void onDownloading(int downloadSize, int fileSize) {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        downloadInfo.setUri(downloadConfig.getUrl());
                        downloadInfo.setStatus(DownloadManager.STATUS_RUNNING);
                        downloadInfo.setBytes_so_far(downloadSize);
                        downloadInfo.setTotal_size(fileSize);
                        publishProgress(downloadInfo);
                    }
                });
    }

}
