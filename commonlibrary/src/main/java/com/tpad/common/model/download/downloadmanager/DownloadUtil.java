package com.tpad.common.model.download.downloadmanager;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jone.sun on 2015/5/7.
 */
public class DownloadUtil {

    /**
     * 获取下载不能继续的状态
     * @param reasonId
     * @return
     */
    public static String getDownloadReason(int reasonId){
        String reason;
        switch (reasonId) {
            case DownloadManager.ERROR_CANNOT_RESUME: //1008
                reason = "不能够继续，由于一些其他原因";
                break;
            case DownloadManager.ERROR_DEVICE_NOT_FOUND: //1007
                reason = "外部存储设备没有找到，比如SD卡没有插入";
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS: //1009
                reason = "要下载的文件已经存在";
                break;
            case DownloadManager.ERROR_FILE_ERROR: //1001
                reason = "可能由于SD卡原因导致了文件错误";
                break;
            case DownloadManager.ERROR_HTTP_DATA_ERROR: //1004
                reason = "在Http传输过程中出现了问题";
                break;
            case DownloadManager.ERROR_INSUFFICIENT_SPACE: //1006
                reason = "由于SD卡空间不足造成的";
                break;
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS: //1005
                reason = "这个Http有太多的重定向，导致无法正常下载";
                break;
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE: //1002
                reason = "无法获取http出错的原因，比如说远程服务器没有响应";
                break;
            case DownloadManager.ERROR_UNKNOWN: //1000
                reason = "未知的错误类型";
                break;
            case DownloadManager.PAUSED_QUEUED_FOR_WIFI: //3
                reason = "由于移动网络数据问题，等待WiFi连接能用后再重新进入下载队列";
                break;
            case DownloadManager.PAUSED_UNKNOWN: //4
                reason = "未知原因导致了任务下载的暂停";
                break;
            case DownloadManager.PAUSED_WAITING_FOR_NETWORK: //2
                reason = "可能由于没有网络连接而无法下载，等待有可用的网络连接恢复";
                break;
            case DownloadManager.PAUSED_WAITING_TO_RETRY: //1
                reason = "由于重重原因导致下载暂停，等待重试";
                break;
            case 0:
                reason = "下载完成";
                break;
            default:
                reason = "未知原因: " + reasonId;
                break;
        }
        return reason;
    }

    /**
     * 获取下载的状态
     * @param stateId
     * @return
     */
    public static String getDownloadState(int stateId){
        String state;
        switch (stateId) {
            case DownloadManager.STATUS_FAILED: //16
                state = "失败";
                break;
            case DownloadManager.STATUS_PAUSED: //4
                state = "暂停";
                break;
            case DownloadManager.STATUS_PENDING: //1
                state = "等待将开始";
                break;
            case DownloadManager.STATUS_RUNNING: //2
                state = "正在下载中...";
                break;
            case DownloadManager.STATUS_SUCCESSFUL: //8
                state = "已经下载成功";
                break;
            case 0:
                state = "下载完成";
                break;
            default:
                state = "未知状态: " + stateId;
                break;
        }
        return state;
    }

    public static boolean isDownloadingByStateId(int stateId){
        boolean isDownloading = false;
        switch (stateId) {
            case DownloadManager.STATUS_PAUSED: //4
            case DownloadManager.STATUS_PENDING: //1
            case DownloadManager.STATUS_RUNNING: //2
                isDownloading = true;
                break;
        }
        return isDownloading;
    }

    /***
     * 根据ID获取下载的信息
     * @param downloadManager
     * @param downloadId
     * @return
     */
    @SuppressWarnings("finally")
    public static DownloadInfo getDownloadInfoById(DownloadManager downloadManager, long downloadId){
        List<DownloadInfo> downloadInfoList = query(downloadManager, new DownloadManager.Query().setFilterById(downloadId));
        if(downloadInfoList != null && downloadInfoList.size() > 0){
            return downloadInfoList.get(0);
        }
        return null;
    }

    /***
     *
     * 根据下载状态获取下载信息
     * @param downloadManager
     * @param status
     * @return
     */
    public static List<DownloadInfo> getDownloadInfosByStatus(DownloadManager downloadManager, int status){
        return query(downloadManager, new DownloadManager.Query().setFilterByStatus(status));
    }

    private static List<DownloadInfo> query(DownloadManager downloadManager, DownloadManager.Query query){
        List<DownloadInfo> downloadInfoList = new ArrayList<>();
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null) {
                while (c.moveToNext()){
                    int bytes_so_far = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));//API level 9
                    String description = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_DESCRIPTION));//API level 9
                    long _id = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));//API level 9
                    long last_modified_timestamp = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP));//API level 9
                    String local_uri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));//API level 9
                    String mediaprovider_uri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_MEDIAPROVIDER_URI));//API level 9
                    String media_type = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_MEDIA_TYPE));//API level 9
                    int reason = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON));//API level 9
                    int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));//API level 9
                    String title = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE));//API level 9
                    int total_size = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));//API level 9
                    String uri = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_URI));//API level 9
                    String local_filename;
                    if(Build.VERSION.SDK_INT >= 11){
                        local_filename = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME));
                    }else {
                        local_filename = Uri.parse(local_uri).getPath();
                    }
                    downloadInfoList.add(new DownloadInfo(_id, bytes_so_far, description, last_modified_timestamp, local_filename, local_uri, mediaprovider_uri, media_type, reason, status, title, total_size, uri));
                }

            }
        } catch(Exception e){
            Log.e("DownloadUtil", "query异常", e);
        }finally {
            if (c != null) {
                c.close();
            }
            return downloadInfoList;
        }
    }
}
