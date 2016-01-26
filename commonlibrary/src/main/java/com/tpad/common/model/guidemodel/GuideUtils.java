package com.tpad.common.model.guidemodel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.change.unlock.common.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiajun.jiang on 2015/4/17.
 */
public class GuideUtils {
    private String model = "";
    private Context context;
    private final static String TAG = GuideUtils.class.getSimpleName();
    private String appName;

    public GuideUtils(Context context,String appName){
        this.context = context;
        this.appName = appName;
        model = new PhoneRelaxInfo().getCurrPhoneBrandModel();
    }

    public String getModel(){
        return model;
    }

    public void settingShowToast(){
        if (model.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU)) {
            if(isFlymeOS3x()){
                openSetting(context);
            }else if (isFlymeOS4x()){
                try {
                    Intent localIntent = new Intent();
                    localIntent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                    localIntent.putExtra("packageName", context.getPackageName());
                    context.startActivity(localIntent);
                    ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    startSelfForGuid(context, 2);
                }catch (Exception e){
                    Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (model.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_X9007)){
            try {
                Intent localIntent = new Intent("com.oppo.notification.center.app.detail");
                localIntent.putExtra("pkg_name",context.getPackageName());
                localIntent.putExtra("app_name",appName);
                context.startActivity(localIntent);
                ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                startSelfForGuid(context, 2);
            }catch (Exception e){
                Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
            }
        }else{
            openSetting(context);
        }

    }

    public void settingAutoStart(){
        switch (model){
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI:
                PackageManager pm = context.getPackageManager();
                PackageInfo info = null;
                try {
                    info = pm.getPackageInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
                i.setClassName("com.android.settings",
                        "com.miui.securitycenter.permission.AppPermissionsEditor");
                i.putExtra("extra_package_uid", info.applicationInfo.uid);
                try {
                    context.startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    startSelfForGuid(context, 1);
                } catch (Exception e) {
                    Toast.makeText(context, "该MIUI需手动设置哦", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage(), e);
                }
                break;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI:
                try{
                    context.startActivity(new Intent("huawei.intent.action.HSM_BOOTAPP_MANAGER")); //开机自动启动
                    ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    startSelfForGuid(context, 1);
                }catch (Exception e){
                    try{
                        context.startActivity(new Intent("huawei.intent.action.HSM_PROTECTED_APPS")); //受保护的后台应用
                        ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                                R.anim.out_to_left);
                        startSelfForGuid(context, 1);
                    }catch (Exception e1){
                        Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU:
                if(isFlymeOS3x()){ //魅族应用控制(Flyme OS 3.x)
                    try{
                        context.startActivity(new Intent("android.settings.APP_OPS_SETTINGS"));
                        ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                                R.anim.out_to_left);
//                        startSelfForGuid(context, 1);
                    }catch (Exception e1){
                        Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
                    }
                }else if(isFlymeOS4x()){
                    try{
                        Intent localIntent = new Intent();
                        localIntent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                        localIntent.putExtra("packageName", context.getPackageName());
                        context.startActivity(localIntent);
                        ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                                R.anim.out_to_left);
//                        startSelfForGuid(context, 1);
                    }catch (Exception e1){
                        Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void closeSystemLock(){
        if (model.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI)) {
            Intent closesystemlock = new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
            context.startActivity(closesystemlock);
            ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
            startSelfForGuid(context, 3);
        } else if (model.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_JINLI)) {
            Toast.makeText(context, "当前设置项不支持你的手机",Toast.LENGTH_SHORT).show();
        } else {
            Intent localIntent = new Intent("android.app.action.SET_NEW_PASSWORD");
            localIntent.addCategory("android.intent.category.DEFAULT");
            context.startActivity(localIntent);
            ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
            startSelfForGuid(context, 3);
        }
    }

    public static void startSelfForGuid(final Context paramContext, final int paramInt) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent localIntent = new Intent(paramContext, GuideWindowActivity.class);
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.putExtra("from_where", paramInt);
                paramContext.startActivity(localIntent);
            }
        }, 600L);
    }
    public static boolean isFlymeOS3x(){
        String display = Build.DISPLAY;
        display = display.replace("_", " ");
        if(display.contains("Flyme OS 3.")){
            return true;
        }
        return false;
    }

    public static boolean isFlymeOS4x(){
        String display = Build.DISPLAY;
        display = display.replace("_", " ");
        if(display.contains("Flyme OS 4.")){
            return true;
        }
        return false;
    }


    public static void openSetting(Context context){
        try {
            Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            String pkg = "com.android.settings";
            String cls = "com.android.settings.applications.InstalledAppDetails";
            i.setComponent(new ComponentName(pkg, cls));
            i.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(i);
            ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
            startSelfForGuid(context, 2);
        }catch (Exception e){
            Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
        }
    }
    public static void openSetting(Context context,boolean flag){
        try {
            Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            String pkg = "com.android.settings";
            String cls = "com.android.settings.applications.InstalledAppDetails";
            i.setComponent(new ComponentName(pkg, cls));
            i.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(i);
            ((Activity)context).overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
            if (flag){
                startSelfForGuid(context, 2);
            }
        }catch (Exception e){
            Toast.makeText(context, "该机型还没适配", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean canShowApplication(String model){
        switch (model) {
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_LENOVO:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_BBK:
                return true;
            default:
                return false;
        }
    }

    public static boolean canShowAutoStart(String model){
        switch (model) {
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI:
                return true;
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU:
                return true;
            default:
                return false;
        }
    }

    public boolean canShowMobileManger(String model){

        for(String pkg : PhoneRelaxInfo.ALREADY_INSTALL_MOBILE_MANGER ){
            if(isExistsAppByPkgName(pkg)){
                return true;
            };
        }
        return false;
    }


    /**
     * 兼容MIUI5和MIUI6的开启悬浮窗设置界面
     * @param context
     */
    public static void openMiuiPermissionActivity(Context context) {
        try {
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }finally {
            startSelfForGuid(context, 4);
        }
    }

    public static boolean canShowOpenMiuiPermission(String model){
        switch (model) {
            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI:
                return true;
            default:
                return false;
        }
    }
//    public List<MobileManger> getShowMobileManger(Context context){
//        List<MobileManger> mobileMangers = new ArrayList<>();
//
//        PackageManager pm = context.getPackageManager();
//        for(String pkg : PhoneRelaxInfo.ALREADY_INSTALL_MOBILE_MANGER ){
//            if(isExistsAppByPkgName(pkg)){
//                ApplicationInfo appInfo = null;
//                try {
//                    appInfo = pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
//                } catch (PackageManager.NameNotFoundException e) {
//                    Log.e(TAG,"没有获取到icon 包名是：" + pkg + ";" + e.getMessage());
//                    continue;
//                }
//                MobileManger mobileManger = new MobileManger();
//                mobileManger.setName(pm.getApplicationLabel(appInfo).toString());
//                mobileManger.setIcon(pm.getApplicationIcon(appInfo));
//                mobileManger.setLinkUrl(getMangerLinkUrl(pkg));
//                mobileMangers.add(mobileManger);
//            }
//        }
//        return mobileMangers;
//    }


    public List<MobileManger> getShowMobileManger(Context context,Map<String,String> settingUrls){
        List<MobileManger> mobileMangers = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        for(String pkg : PhoneRelaxInfo.ALREADY_INSTALL_MOBILE_MANGER ){
            if(isExistsAppByPkgName(pkg)){
                ApplicationInfo appInfo = null;
                try {
                    appInfo = pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG,"没有获取到icon 包名是：" + pkg + ";" + e.getMessage());
                    continue;
                }
                MobileManger mobileManger = new MobileManger();
                mobileManger.setName(pm.getApplicationLabel(appInfo).toString());
                mobileManger.setIcon(pm.getApplicationIcon(appInfo));
                mobileManger.setLinkUrl(getMangerLinkUrl(settingUrls,pkg));
                mobileMangers.add(mobileManger);
            }
        }
        return mobileMangers;
    }

//    public static String getMangerLinkUrl(String pkg){
//        switch (pkg){
//            case "com.qihoo360.mobilesafe":     //360手机卫士
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205777314&idx=1&sn=0879922bf8d36dae7caca95b8aecc27b#rd";
//            case "com.lbe.security":            //LBE安全大师
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205774316&idx=1&sn=62e656c0b3232137f81c2c9ad19ef41b#rd";
//            case "com.tencent.qqpimsecure":     //手机管家
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205777314&idx=4&sn=d0aef4ceb84acaabbbdace6404e1e1a1#rd";
//            case "net.hidroid.himanager":        // 海卓手机管家
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205774316&idx=2&sn=db2db24dcc6ff2b8972a5422b144f71b#rd";
//            case "com.cleanmaster.mguard_cn":    //猎豹清理大师
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205774316&idx=3&sn=0fb79ba16e81fc5c3ea6c89a6d2f6b4e#rd";
//            case "cn.opda.a.phonoalbumshoushou": //百度手机卫士
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205777314&idx=2&sn=cef4f2616acdfd419422f8d627d392f7#rd";
//            case "com.ijinshan.mguard":          //金山手机卫士
//                return "http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205777314&idx=3&sn=582dca31930c161bd451deb0aadf869e#rd";
//        }
//        return null;
//    }

    public static String getMangerLinkUrl(Map<String,String> mangerUrls,String pkg){
        if (mangerUrls != null && mangerUrls.containsKey(pkg)){
            return mangerUrls.get(pkg);
        }
        return null;
    }

    public void settingApplication(Map<String,String> settingUrls,String model){
        if (settingUrls != null && settingUrls.containsKey(model)){
            startWithACT_DATA(context, settingUrls.get(model));
        }else{
            Toast.makeText(context,"该机型还没适配",Toast.LENGTH_SHORT).show();
        }
    }

//    public void settingApplication(String model){
//        switch (model){
//            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO:
//                startWithACT_DATA(context,"http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205774493&idx=1&sn=1c2d3a1968f577c86a54a662a59ef8a0#rd");
//                break;
//            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI:
//                startWithACT_DATA(context,"http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205772685&idx=2&sn=a32091a6eb41cdfe9928822ca71179af#rd");
//                break;
//            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI:
//                startWithACT_DATA(context,"http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205774493&idx=4&sn=37087a20b8c10dda4127836865c9be19#rd");
//                break;
//            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU:
//                startWithACT_DATA(context,"http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205774493&idx=3&sn=a7a259138efb9f593944d66dd977e274#rd");
//                break;
//            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_LENOVO:
//                startWithACT_DATA(context,"http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205772685&idx=3&sn=04e27d1e75d3861df6235979657e250a#rd");
//                break;
//            case PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_BBK:
//                startWithACT_DATA(context,"http://mp.weixin.qq.com/s?__biz=MzAxODU1OTM3Mw==&mid=205772685&idx=1&sn=9152e438505861ae2f36c3f2a1757717#rd");
//                break;
//            default:
//                Toast.makeText(context,"该机型还没适配",Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }


    public static void startWithACT_DATA(Context context,String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * is Exists App By PkgName
     *
     * @param pkgName
     * @return true or false
     */
    public boolean isExistsAppByPkgName(String pkgName) {
        boolean result = false;
        if (pkgName == null)
            return result;
        try {
            ApplicationInfo api = context.getPackageManager()
                    .getApplicationInfo(pkgName, 0);
            result = true;
        } catch (PackageManager.NameNotFoundException e) {
            return result;
        }
        return result;
    }
}
