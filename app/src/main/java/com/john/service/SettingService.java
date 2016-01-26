package com.john.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.john.material.uidemo.myapplication.R;
import com.john.material.uidemo.myapplication.TopActivity;
import com.tpad.common.model.guidemodel.PhoneRelaxInfo;
import java.util.List;
import java.util.Map;

/**
 * Created by jiajun.jiang on 2015/12/7.
 */
public class SettingService extends AccessibilityService {
    private final static String TAG = SettingService.class.getSimpleName();
    private static Map a = null;
    private static Map<String,List> b = null;
    private static SettingService c = null;

    private final AccessibilityNodeInfo a(AccessibilityNodeInfo paramAccessibilityNodeInfo, List paramList)
    {
//        AccessibilityNodeInfo localObject1 = null;
//        int i = 0;
//        if ((localObject1 != null) || (i >= paramAccessibilityNodeInfo.getChildCount()))
//            return null;
//        Object localObject2 = paramAccessibilityNodeInfo.getChild(i);
//        if (localObject2 != null)
//            if ((((AccessibilityNodeInfo)localObject2).getText() != null) && (paramList.contains(((AccessibilityNodeInfo)localObject2).getText().toString())))
//            {
//                super.getClass().getSimpleName();
//                ((AccessibilityNodeInfo)localObject2).getClassName();
//            }
//        while (true)
//        {
//            ++i;
//            localObject1 = localObject2;
//            break;
//            localObject2 = a((AccessibilityNodeInfo)localObject2, paramList);
//            continue;
//            localObject2 = localObject1;
//        }
        return null;
    }




//    private static final String c(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
//        String str = "";
//        if (paramAccessibilityNodeInfo.getText() != null)
//            str = paramAccessibilityNodeInfo.getText().toString();
//        return str;
//    }



    @SuppressLint("NewApi")
    public final void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent){
        AccessibilityNodeInfo localAccessibilityNodeInfo = getRootInActiveWindow();
        if (localAccessibilityNodeInfo != null){

            Log.e(TAG, "getPackageName :" + localAccessibilityNodeInfo.getPackageName());
//            Log.e(TAG, "getClassName :" + localAccessibilityNodeInfo.getClassName());
            recycle(localAccessibilityNodeInfo);

        }
    }

    @SuppressLint("NewApi")
    public void recycle(AccessibilityNodeInfo info) {
//        showToastInit(info);
        if (info.getChildCount() == 0) {
            Log.e(TAG, "child widget----------------------------" + info.getClassName());
            Log.e(TAG, "showDialog:" + info.canOpenPopup());
            Log.e(TAG, "Text：" + info.getText());
            Log.e(TAG, "windowId:" + info.getWindowId());
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if(info.getChild(i) != null){
                    recycle(info.getChild(i));
//                    Log.e(TAG, "info.getChild widget----------------------------" + info.getChild(i).getClassName());
//                    Log.e(TAG, "info.getChild showDialog:" + info.getChild(i).canOpenPopup());
//                    Log.e(TAG, "info.getChild Text：" + info.getChild(i).getText());
//                    Log.e(TAG, "info.getChild windowId:" + info.getChild(i).getWindowId());
                }
            }
        }
    }


    @Override
    public void onInterrupt() {
        Log.e(TAG, "onInterrupt()");

    }

    @SuppressLint("NewApi")
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG, "onServiceConnected() : 服务已开启");
        Intent intent = new Intent(this, TopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        openAccessibilityServiceSetting(this);
//        performGlobalAction(GLOBAL_ACTION_BACK);
}

    @SuppressLint("NewApi")
    private void showToastInit(AccessibilityNodeInfo info){
        if (info != null && info.getText() != null) {
//            boolean flag = false;
            if (info.getText().toString().equals("显示通知")){
                if (info.isChecked()){
//                    flag = true;
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
            if (info.getText().toString().equals("确定")){
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void InitOPPO_OS2(){
        String stepString = "悬浮窗管理#开启悬浮窗";
    }

    public static void openAccessibilityServiceSetting(Context paramContext){
        Intent localIntent;
        String str;
        Bundle localBundle;
        if (new PhoneRelaxInfo().getCurrPhoneBrandModel().equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI) ){
            //判断是否是小米的
            localIntent = new Intent("android.intent.action.MAIN");
            str = paramContext.getPackageName();
            localIntent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
            localIntent.putExtra(":android:show_fragment_short_title", 0);
            localIntent.putExtra(":android:show_fragment_args", 0);
            localIntent.putExtra(":android:show_fragment_title", 0);
            localIntent.putExtra(":android:no_headers", true);
            localIntent.putExtra("setting:ui_options", 1);
            localBundle = new Bundle();
            localBundle.putString("summary", paramContext.getString(R.string.app_name));
            localBundle.putString("title", paramContext.getString(R.string.testaccessibility));
            localBundle.putString("preference_key", str + "/" + SettingService.class.getName());
            localBundle.putParcelable("component_name", new ComponentName(str, SettingService.class.getName()));
            localBundle.putBoolean("checked", false);
            if ((Build.VERSION.SDK_INT >= 19) && (c.hasClassInApk(paramContext, "com.android.settings", "com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment")))
            {
                localIntent.putExtra(":android:show_fragment", "com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment");
                localIntent.putExtra(":android:show_fragment_args", localBundle);
                localIntent.addFlags(268468224);
            }
            if (!a(paramContext, localIntent)){
                e(paramContext);
            }
        }else{
            e(paramContext);
        }

    }
    private static final boolean a(Context paramContext, Intent paramIntent)
    {
        boolean i = false;
        try
        {
            paramIntent.addFlags(268468224);
            paramContext.startActivity(paramIntent);
            i = true;
            return i;
        }
        catch (Exception localException)
        {
        }
        return false;
    }

    private static final boolean e(Context paramContext){
        return a(paramContext, new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
    }


    public static final boolean hasClassInApk(Context paramContext, String paramString1, String paramString2)
    {
        boolean i = false;
        try
        {
            Context localContext = paramContext.createPackageContext(paramString1, 3);
            if (localContext != null)
            {
                Class localClass = localContext.getClassLoader().loadClass(paramString2);
                if (localClass != null)
                    i = true;
                localClass.getName();
            }
            return i;
        }
        catch (Exception localException){
            return i;
        }
    }

}
