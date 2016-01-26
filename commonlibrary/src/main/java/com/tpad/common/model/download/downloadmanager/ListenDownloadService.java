package com.tpad.common.model.download.downloadmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ListenDownloadService extends Service {
    private DownloadManagerOperator downloadManagerOperator;
    public ListenDownloadService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManagerOperator = DownloadManagerOperator.getInstance(ListenDownloadService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.hasExtra("downloadId")){
            long downloadId = intent.getLongExtra("downloadId", -1);
            if(downloadId > -1){
                downloadManagerOperator.monitorDownloadStatus(ListenDownloadService.this,
                        downloadId, downloadListener);
            }
        }
        return super.onStartCommand(intent, flags, startId);
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
            if(downloadManagerOperator != null){
                downloadManagerOperator.unMonitorDownloadStatus();
            }
            stopSelf();
        }

        @Override
        public void onFailed(DownloadInfo downloadInfo, String reason) {
            if(downloadManagerOperator != null){
                downloadManagerOperator.unMonitorDownloadStatus();
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(downloadManagerOperator != null){
            downloadManagerOperator.unMonitorDownloadStatus();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
