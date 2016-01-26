package com.tpad.common.model.download.downloadmanager;

import android.content.Context;

import com.tpad.common.model.download.BaseDownloadAsyncTask;

/**
 * Created by jone.sun on 2015/10/9.
 */
public class MonitorDownloadStatusDownloadAsyncTask extends BaseDownloadAsyncTask {
    public MonitorDownloadStatusDownloadAsyncTask(Context context, DownloadManagerOperator.DownloadListener downloadListener){
        super(context, downloadListener);
    }
    @Override
    protected String doInBackground(DownloadConfig... downloadConfigs) {
        monitorDownloadingByDownloadManager(downloadConfigs[0].getUrl());
        return null;
    }

}
