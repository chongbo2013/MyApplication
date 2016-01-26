package com.tpad.common.model.download.autodownload;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.change.unlock.common.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpad.common.model.processData.ProcessSpDataOperator;
import com.tpad.common.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动下载的服务
 * @author jone.sun on 2015/5/8.
 */
public class AutoDownloadService extends Service {
    private static final String TAG = AutoDownloadService.class.getSimpleName();
    public static final String ACTION_AUTO_DOWNLOAD = "action.service.AUTO_DOWNLOAD";
    public static final String SP_KEY_IS_ALLOW_AUTO_DOWNLOAD = "is_allow_auto_download";
    private AutoDownloadOperator autoDownloadImageOperator, autoDownloadHtmlOperator, autoDownloadApkOperator;

    @Override
    public void onCreate() {
        super.onCreate();
        if(!isCanAutoDownload()){
            stopSelf();
            return;
        }
        try{
            initData();
        }catch (Exception e){
            stopSelf();
        }
    }

    private boolean isCanAutoDownload(){
        //todo 网络允许、开关开启、任务不为空
        return ProcessSpDataOperator.getInstance(AutoDownloadService.this).
                getValueByKey(SP_KEY_IS_ALLOW_AUTO_DOWNLOAD, true);
    }

    private void initData(){
        List<AutoDownloadData> autoDownloadImageBeans = getAutoDownloadBeans(AutoDownloadData.SAVE_TYPE_OF_IMAGE);
        List<AutoDownloadData> autoDownloadHtmlBeans = getAutoDownloadBeans(AutoDownloadData.SAVE_TYPE_OF_HTML);
        List<AutoDownloadData> autoDownloadApkBeans = getAutoDownloadBeans(AutoDownloadData.SAVE_TYPE_OF_APK);
        autoDownloadImageOperator = new AutoDownloadOperator(AutoDownloadService.this, autoDownloadImageBeans);
        autoDownloadHtmlOperator = new AutoDownloadOperator(AutoDownloadService.this, autoDownloadHtmlBeans);
        autoDownloadApkOperator = new AutoDownloadOperator(AutoDownloadService.this, autoDownloadApkBeans);
        if(autoDownloadImageBeans != null && autoDownloadImageBeans.size() > 0){
            startDownloadIcons();
        }else if(autoDownloadHtmlBeans != null && autoDownloadHtmlBeans.size() > 0){
            startDownloadHtmls();
        }else if(autoDownloadApkBeans != null && autoDownloadApkBeans.size() > 0){
            startDownloadApks();
        }else {
            //没有下载列表了，设置标志
            ProcessSpDataOperator.getInstance(AutoDownloadService.this)
                    .putValue(SP_KEY_IS_ALLOW_AUTO_DOWNLOAD, false);
            stopSelf();
        }
    }

    private void startDownloadIcons(){
        autoDownloadImageOperator.startDownload(new AutoDownloadOperator.ResponseListener() {
            @Override
            public void onSuccess(String data) {
                //Log.e(TAG, "图片下载完成");
                startDownloadHtmls();
            }

            @Override
            public void onFailure(String error) {
                //Log.e(TAG, "图片下载失败: " + error);
                stopSelf();
            }
        });
    }

    private void startDownloadHtmls(){
        autoDownloadHtmlOperator.startDownload(new AutoDownloadOperator.ResponseListener() {
            @Override
            public void onSuccess(String data) {
                //Log.e(TAG, "CAP网页下载完成");
                startDownloadApks();
            }

            @Override
            public void onFailure(String error) {
                //Log.e(TAG, "CAP网页下载失败: " + error);
                stopSelf();
            }
        });
    }

    private void startDownloadApks(){
        autoDownloadApkOperator.startDownload(new AutoDownloadOperator.ResponseListener() {
            @Override
            public void onSuccess(String data) {
                //Log.e(TAG, "CAP apk下载完成");
                stopSelf();
            }
            @Override
            public void onFailure(String error) {
                //Log.e(TAG, "CAP apk下载失败: " + error);
                stopSelf();
            }
        });
    }

    private List<AutoDownloadData> getAutoDownloadBeans(String downloadType){
        return AutoDownloadDataDao.getInstance(AutoDownloadService.this).getAutoDownloadDatasByDownloadType(downloadType);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(autoDownloadImageOperator != null){
            autoDownloadImageOperator = null;
        }
        if(autoDownloadHtmlOperator != null){
            autoDownloadHtmlOperator = null;
        }
        //todo apk下载暂时需手动停止
        if(autoDownloadApkOperator != null){
            autoDownloadApkOperator.stopDownload();
            autoDownloadApkOperator = null;
        }
        //Log.e(TAG, "******自动下载服务已关闭******");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void startDownloadTaskService(Context context){
        Intent intent = new Intent(context, AutoDownloadService.class);
        intent.setAction(AutoDownloadService.ACTION_AUTO_DOWNLOAD);
        context.startService(new Intent(intent));
    }

    public static void stopDownloadTaskService(Context context, String reason){
        try{
            context.stopService(new Intent(context, AutoDownloadService.class));
            if(BuildConfig.DEBUG){
                Log.e(TAG, "关闭下载服务>>原因: " + reason);
            }
        }catch (Exception e){
            Log.e(TAG, "关闭服务异常", e);
        }
    }
}
