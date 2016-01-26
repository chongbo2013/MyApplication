package com.tpad.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

/*
 * Created by loang.chen on 2015/5/19.
 * apk文件的安装
 */
public class ApkInstall extends Activity {

    private final static String TAG = ApkInstall.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String installPath = getIntent().getStringExtra("path");
        if (installPath != null) {
            if (!installPath.equals("")) {
                install(installPath);
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * apk的安装
     *
     * @param path
     */
    public void install(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
