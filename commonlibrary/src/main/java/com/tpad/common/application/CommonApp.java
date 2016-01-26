package com.tpad.common.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tpad.common.model.db.ormlite.OrmLiteBaseDao;
import com.tpad.common.model.processData.ProcessDBDataOperator;

/**
 * @author jone.sun on 2015/3/24.
 */
public class CommonApp extends Application {
    public static final int WHAT_SHOW_TOAST = 1010101;
    private static CommonApp instance;
    private static Handler handler = new Handler();

    public static CommonApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        handler = new ShowToastHandler(instance);
    }

    static class ShowToastHandler extends Handler {
        private Context context;

        public ShowToastHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_SHOW_TOAST:
                    if (msg.obj != null) {
                        String info = msg.obj.toString();
                        if (msg.arg1 == Toast.LENGTH_SHORT) {
                            Toast.makeText(context, info, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(context, info, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void showToast(String info) {
        showToast(info, Toast.LENGTH_SHORT);
    }

    public static void showToast(String info, int toastTime) {
        handler.sendMessage(handler.obtainMessage(WHAT_SHOW_TOAST, toastTime, 0, info));
    }
}