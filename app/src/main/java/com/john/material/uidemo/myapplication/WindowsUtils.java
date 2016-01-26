package com.john.material.uidemo.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class WindowsUtils {
    private static final WindowManager.LayoutParams a(int paramInt1, int paramInt2){
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.systemUiVisibility = 1;
        localLayoutParams.gravity = 48;
        localLayoutParams.format = 1;
//        localLayoutParams.format = PixelFormat.TRANSLUCENT;
        localLayoutParams.flags = 264072;
//        localLayoutParams.flags = 1280;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.y = 0;
        localLayoutParams.type = 2003;
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
////        wmParams.format = PixelFormat.RGBA_8888;
//        localLayoutParams.format = PixelFormat.TRANSLUCENT;
//        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        localLayoutParams.flags = 1280;
        return localLayoutParams;
    }

    public static final void a(WindowManager paramWindowManager, View paramView) {
        a(paramWindowManager, paramView, a(-1, 0));
    }

    public static final void a(WindowManager paramWindowManager, View paramView, int paramInt) {
        b(paramWindowManager, paramView, paramInt, 0);
    }

    public static final void a(WindowManager paramWindowManager, View paramView, int paramInt1, int paramInt2) {
        WindowManager.LayoutParams localLayoutParams = a(paramInt1, paramInt2);
        localLayoutParams.flags = 1464;
        a(paramWindowManager, paramView, localLayoutParams);
    }

    private static final void a(WindowManager paramWindowManager, View paramView, WindowManager.LayoutParams paramLayoutParams) {
            try{
                paramWindowManager.addView(paramView, paramLayoutParams);
            }catch(Exception e){
            }

    }

    public static final void b(WindowManager paramWindowManager, View paramView) {
        if (paramView != null);
        try{
            paramWindowManager.removeView(paramView);
        }
        catch (Exception localException)
        {
        }
    }

    public static final void b(WindowManager paramWindowManager, View paramView, int paramInt1, int paramInt2) {
        a(paramWindowManager, paramView, a(paramInt1, paramInt2));
    }
}