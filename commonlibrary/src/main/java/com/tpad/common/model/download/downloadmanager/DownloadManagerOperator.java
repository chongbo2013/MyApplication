package com.tpad.common.model.download.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DownloadManager下载
 *
 * @author jone.sun on 2015/5/5.
 */
public class DownloadManagerOperator {
    private static DownloadManagerOperator instance;

    public static DownloadManagerOperator getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadManagerOperator(context);
        }
        return instance;
    }

    public DownloadManagerOperator(Context context) {
        this.context = context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private DownloadManagerOperator() {
    }

    private static final String TAG = DownloadManagerOperator.class.getSimpleName();
    public static final String DOWNLOAD_FILE_TMP_SUFFIX = "dmTmp";
    private long downloadId = -1;

    public static final String ACTION_DOWNLOAD_COMPLETE = "com.tt.commit.DOWNLOAD_COMPLETE";

    protected static final int HANDLE_WHAT_DOWNLOAD_STATE = 101007;
    private Context context;
    private DownloadManager downloadManager;
    protected static final Uri CONTENT_URI = Uri
            .parse("content://downloads/my_downloads");

    /**
     * 是否可以下载
     * 主要判断DownloadManager是否可用、网络和SD卡状态等
     *
     * @return
     */
    public boolean isCanDownload() {
        return isDownloadManagerAvailable(); //todo 后续加上网络和SD卡状态判断
    }

    /**
     * 下载
     *
     * @param downloadConfig
     * @throws DownloadException
     */
    public long download(DownloadConfig downloadConfig) throws DownloadException {
        if (downloadConfig == null) {
            throw new DownloadException("downloadConfig is null!!!");
        }
        DownloadManager.Request request;
        try {
            String url = downloadConfig.getUrl();
            if (url == null) {
                throw new DownloadException("url is null!!!");
            }
            request = new DownloadManager.Request(Uri.parse(url));
            setRequestConfig(request, downloadConfig);
            downloadId = downloadManager.enqueue(request);
        } catch (Exception e) {
            throw new DownloadException(e.getMessage());
        }
        return downloadId;
    }

    /**
     * 根据downloadId监听下载进度(供退出应用后使用)
     *
     * @param downloadId
     * @param downloadListener
     */
    public void monitorDownloadStatus(Context context, long downloadId, DownloadListener downloadListener) {
        this.downloadId = downloadId;
        this.downloadListenerOfOut = downloadListener;
        this.context = context;
        context.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadsChangeObserver);
        context.registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 取消监听下载状态
     */
    public void unMonitorDownloadStatus() {
        try {
            context.getContentResolver().unregisterContentObserver(downloadsChangeObserver);
            context.unregisterReceiver(downloadCompleteReceiver);
        } catch (Exception e) {
        }
    }

    /**
     * 根据url获取下载信息
     *
     * @param url
     * @return
     */
    public DownloadInfo getDownloadInfoByUrl(String url) {
        int[] states = new int[]{
                DownloadManager.STATUS_RUNNING,
                DownloadManager.STATUS_PAUSED,
                DownloadManager.STATUS_PENDING,
                DownloadManager.STATUS_FAILED,
                DownloadManager.STATUS_SUCCESSFUL}; //todo 如果是下载完成的可能会查询慢
        return getDownloadInfoByUrlWithStatus(url, states);
    }

    /**
     * 根据url获取正在下载的信息
     *
     * @param url
     * @return
     */
    public DownloadInfo getDownloadingInfoByUrl(String url) {
        int[] states = new int[]{
                DownloadManager.STATUS_RUNNING,
                DownloadManager.STATUS_PAUSED,
                DownloadManager.STATUS_PENDING,};
        return getDownloadInfoByUrlWithStatus(url, states);
    }

    private DownloadInfo getDownloadInfoByUrlWithStatus(String url, int[] states) {
        DownloadInfo downloadInfo = null;
        for (int status : states) {
            downloadInfo = getDownloadInfoByUrlWithStatus(url, status);
            if (downloadInfo != null) {
                break;
            }
        }
        return downloadInfo;
    }

    /**
     * 根据downloadId取消下载
     *
     * @param downloadId
     */
    public void removeDownload(long... downloadId) {
        try {
            if (downloadId != null && downloadId.length > -1) {
                downloadManager.remove(downloadId);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 根据url取消下载
     *
     * @param url
     */
    public void removeDownloadByUrl(String url) {
        DownloadInfo downloadInfo = getDownloadInfoByUrl(url);
        if (downloadInfo != null) {
            removeDownload(downloadInfo.get_id());
        }
    }

    private DownloadListener downloadListenerOfOut = null;
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onReady(DownloadInfo downloadInfo) {
            if (downloadListenerOfOut != null) {
                downloadListenerOfOut.onReady(downloadInfo);
            }
        }

        @Override
        public void onDownloading(DownloadInfo downloadInfo, int downloadSize, int countSize) {
            if (downloadListenerOfOut != null) {
                downloadListenerOfOut.onDownloading(downloadInfo, downloadSize, countSize);
            }
        }

        @Override
        public void onPause(DownloadInfo downloadInfo) {
            if (downloadListenerOfOut != null) {
                downloadListenerOfOut.onPause(downloadInfo);
            }
        }

        @Override
        public void onComplete(final DownloadInfo downloadInfo, String filePath) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isComplete = false;
                    if (downloadInfo != null && downloadInfo.getStatus() == DownloadManager.STATUS_SUCCESSFUL) {
                        File file = new File(downloadInfo.getLocal_filename());
                        //进行MD5校验
                        String path = file.getPath();
                        if (path!=null){
                            Log.e(TAG,"文件地址 ：" + path.toString());
                        }else{
                            Log.e(TAG,"文件不存在");
                        }

                        if (DownloadMd5Util.getInstance().isCompleteFile(downloadInfo.getUri(), path)) {
                            String lastFileName = path.replace(DOWNLOAD_FILE_TMP_SUFFIX, "");
                            isComplete = file.renameTo(new File(lastFileName));
                            if (isComplete) {
                                sendBroadcast(true, downloadInfo);
                                if (downloadListenerOfOut != null) {
                                    downloadListenerOfOut.onComplete(downloadInfo, lastFileName);
                                }
                            }
                        }
                    }
                    if (!isComplete) {
                        onFailed(downloadInfo, "非完整文件");
                    }
                }
            }).start();
        }

        @Override
        public void onFailed(DownloadInfo downloadInfo, String reason) {
            if (downloadInfo != null) {
                removeDownload(downloadInfo.get_id());
            }
            sendBroadcast(false, downloadInfo);
            if (downloadListenerOfOut != null) {
                downloadListenerOfOut.onFailed(downloadInfo, reason);
            }
        }
    };

    public boolean isDownloadManagerAvailable() {
        Cursor cursor = null;
        boolean isAvailable = false;
        try {
            cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                isAvailable = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "isDownloadManagerAvailable", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return isAvailable;
        }
    }

    private void setRequestConfig(DownloadManager.Request request,
                                  DownloadConfig downloadConfig) throws Exception {
        String savePath = downloadConfig.getSavePath() == null ? null
                : downloadConfig.getSavePath();
        String saveName = downloadConfig.getSaveName() == null ? downloadConfig.getUrl().substring(downloadConfig.getUrl().lastIndexOf('/') + 1)
                : downloadConfig.getSaveName();
        saveName = saveName + DOWNLOAD_FILE_TMP_SUFFIX;
        if (savePath == null) {
            try {
                savePath = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                savePath = Environment.DIRECTORY_DOWNLOADS;
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        saveName);
            } catch (Exception e) {
                // todo 在一些手机中getExternalStoragePublicDirectory()失败时
                Log.e(TAG, "", e);
                try {
                    request.setDestinationInExternalFilesDir(context, null,
                            saveName);
                } catch (Exception ex) {
                    throw new DownloadException(ex.getMessage());
                }
            }
        } else {
            if (savePath.lastIndexOf(File.separator) == (savePath
                    .length() - 1)) {
                savePath = savePath.substring(0,
                        savePath.lastIndexOf(File.separator));
            }
            savePath = savePath.replace(Environment.getExternalStorageDirectory()
                    .toString(), "");
            request.setDestinationInExternalPublicDir(savePath,
                    saveName);
        }
        if (savePath != null) {
            File savePathFile = new File(savePath);
            if (!savePathFile.exists()) {
                savePathFile.mkdirs();
            }
            File file = new File(savePathFile.getPath() + saveName);
            if (file.exists()) {
                file.delete(); // API 11以下需删除存在的文件
            }
            Log.e(TAG, "download>>saveFile: " + file.getPath());
        }

        Boolean canDownloadByNetMobile = downloadConfig.getCanDownloadByNetMobile();
        if (canDownloadByNetMobile != null && !canDownloadByNetMobile) {
            // 允许的网络类型
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        } else {
            // 允许的网络类型
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);
        }

        if (downloadConfig.getShowInNotification() != null
                && !downloadConfig.getShowInNotification()) {
            if (Build.VERSION.SDK_INT >= 11) {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            } else {
                //request.setShowRunningNotification(false);
                Log.e("DownloadManagerUtil",
                        "setNotificationVisibility此方法最低支持API 11.");
            }
        }
        if (downloadConfig.getMimeType() != null) {
            request.setMimeType(downloadConfig.getMimeType());
        }
        Map<String, String> headerMap = downloadConfig.getHeaderMap();
        if (headerMap != null && headerMap.size() > 0) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                request.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private DownloadInfo getDownloadInfoByUrlWithStatus(String url, int status) {
        DownloadInfo downloadInfo = null;
        List<DownloadInfo> downloadInfoList = DownloadUtil.getDownloadInfosByStatus(downloadManager, status);
        if (downloadInfoList != null && downloadInfoList.size() > 0) {
            for (DownloadInfo d : downloadInfoList) {
                if (d.getUri() != null && d.getUri().equals(url)) {
                    downloadInfo = d;
                }
            }
        }
        return downloadInfo;
    }

    public List<DownloadInfo> getDownloadingInfoList() {
        int[] states = new int[]{
                DownloadManager.STATUS_RUNNING,
                DownloadManager.STATUS_PAUSED,
                DownloadManager.STATUS_PENDING,};
        List<DownloadInfo> downloadInfoList = new ArrayList<>();
        for (int status : states) {
            downloadInfoList.addAll(DownloadUtil.getDownloadInfosByStatus(downloadManager, status));
        }
        return downloadInfoList;
    }

    private Handler downloadsChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_WHAT_DOWNLOAD_STATE:
                    if (msg.obj != null) {
                        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
                        int status = downloadInfo.getStatus();
                        switch (status) {
                            case DownloadManager.STATUS_FAILED: // 16"失败"
                                if (downloadListener != null) {
                                    downloadListener.onFailed(downloadInfo, DownloadUtil.getDownloadReason(downloadInfo.getReason()));
                                }
                                break;
                            case DownloadManager.STATUS_PENDING: // 等待即将开始
                                if (downloadListener != null) {
                                    downloadListener.onReady(downloadInfo);
                                }
                                break;
                            case DownloadManager.STATUS_PAUSED: // 4"暂停"
                                if (downloadListener != null) {
                                    downloadListener.onPause(downloadInfo);
                                }
                                break;
                            case DownloadManager.STATUS_RUNNING: // 2"正在下载中..."
                                if (downloadInfo.getBytes_so_far() > 0 && downloadInfo.getTotal_size() == 0) {
                                    if (downloadListener != null) {
                                        downloadListener.onFailed(downloadInfo, DownloadUtil.getDownloadReason(downloadInfo.getReason()));
                                    }
                                } else {
                                    if (downloadListener != null) {
                                        downloadListener.onDownloading(downloadInfo, downloadInfo.getBytes_so_far(), downloadInfo.getTotal_size());
                                    }
                                }
                                break;
                            default: // "未知状态"
                                break;
                        }
                        break;
                    }
                default:
                    break;
            }
        }
    };

    private ContentObserver downloadsChangeObserver = new ContentObserver(downloadsChangeHandler) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (downloadsChangeHandler != null && downloadId > -1 && downloadManager != null) {
                DownloadInfo downloadInfo = DownloadUtil.getDownloadInfoById(downloadManager, downloadId);
                downloadsChangeHandler.sendMessage(downloadsChangeHandler.obtainMessage(HANDLE_WHAT_DOWNLOAD_STATE, downloadInfo));
            }
        }
    };

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadListener != null && completeDownloadId == downloadId) {
                    DownloadInfo downloadInfo = DownloadUtil.getDownloadInfoById(downloadManager, downloadId);
                    downloadListener.onComplete(downloadInfo, downloadInfo != null ? downloadInfo.getLocal_filename() : null);
                    context.getContentResolver().unregisterContentObserver(downloadsChangeObserver);
                    try {
                        context.unregisterReceiver(this);
                    } catch (Exception e) {
                    }
                }
            }
        }
    };

    private void sendBroadcast(boolean isComplete, DownloadInfo downloadInfo) {
        Intent intent = new Intent(ACTION_DOWNLOAD_COMPLETE);
        intent.putExtra("isComplete", isComplete);
        intent.putExtra("downloadInfo", downloadInfo);
        context.sendBroadcast(intent);
    }

    public class DownloadException extends Exception {
        public DownloadException(String message) {
            super(message);
        }
    }

    public interface DownloadListener {
        public void onReady(DownloadInfo downloadInfo);

        public void onDownloading(DownloadInfo downloadInfo, int downloadSize, int countSize);

        public void onPause(DownloadInfo downloadInfo);

        public void onComplete(DownloadInfo downloadInfo, String filePath);

        public void onFailed(DownloadInfo downloadInfo, String reason);
    }
}
