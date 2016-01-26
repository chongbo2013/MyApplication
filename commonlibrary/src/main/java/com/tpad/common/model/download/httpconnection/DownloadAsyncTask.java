package com.tpad.common.model.download.httpconnection;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * @author jone.sun on 2015/3/23.
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, String> {
    public static final int PROCESSING = 1;
    public static final int COMPLETE = 2;
    public static final int FAILURE = -1;
    private Context context;
    private String url;
    private String saveDir, saveName;
    private HttpConnDownloadOperator loader;
    private Handler handler;

    public DownloadAsyncTask(Context context,
                             String url,
                             String saveDir,
                             String saveName,
                             Handler handler) {
        this.context = context;
        this.url = url;
        this.saveDir = saveDir;
        this.saveName = saveName;
        this.handler = handler;
    }

    public void exit() {
        if (loader != null ){
            loader.exit(url);
        }
    }

    HttpConnDownloadOperator.DownloadListener downloadListener = new HttpConnDownloadOperator.DownloadListener() {

        @Override
        public void onDownloading(int downloadSize, int fileSize) {
            Message msg = new Message();
            msg.what = PROCESSING;
            msg.getData().putInt("downloadSize", downloadSize);
            msg.getData().putInt("fileSize", fileSize);
            handler.sendMessage(msg);
        }

        @Override
        public void onDownloadFailed(String name, int downloadSize, int fileSize, String reason) {
            Message msg = new Message();
            msg.what = FAILURE;
            msg.getData().putString("name", name);
            msg.getData().putInt("downloadSize", downloadSize);
            msg.getData().putInt("fileSize", fileSize);
            msg.getData().putString("reason", reason);
            handler.sendMessage(msg);
            exit();
        }

        @Override
        public void onDownloadComplete(String name, int downloadSize, int fileSize) {
            Message msg = new Message();
            msg.what = COMPLETE;
            msg.getData().putString("name", name);
            handler.sendMessage(msg);
        }
    };
    @Override
    protected String doInBackground(String... params) {
        try {
            loader = new HttpConnDownloadOperator();
            loader.startDownload(context, url, saveDir, saveName, 3, downloadListener);

        } catch (Exception e) {
            e.printStackTrace();
            handler.sendMessage(handler.obtainMessage(FAILURE));
        }
        return null;
    }
}
