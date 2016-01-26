package com.john.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by jiajun.jiang on 2015/12/11.
 */
public class NotificationListener extends AccessibilityService {

    private final AccessibilityServiceInfo a = new AccessibilityServiceInfo();

    public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent){
        Log.e("kevint", "onAccessibilityEvent===========");
    }

    public void onCreate(){
        Log.e("kevint", "AccessibilityService===========onCreate====");
    }

    public void onDestroy(){
        Log.e("kevint", "onDestroy===========");
        super.onDestroy();
    }

    public void onInterrupt(){
        Log.e("kevint", "onInterrupt===========");
    }

    public void onServiceConnected()
    {
        Log.e("kevint", "onServiceConnected===========");
        this.a.eventTypes = 64;
        if (Build.VERSION.SDK_INT >= 14);
        for (this.a.feedbackType = -1; ; this.a.feedbackType = 16){
            this.a.notificationTimeout = 100L;
            setServiceInfo(this.a);
//            return;
        }
    }
}
