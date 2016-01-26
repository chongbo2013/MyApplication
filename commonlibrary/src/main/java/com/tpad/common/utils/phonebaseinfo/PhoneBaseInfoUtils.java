package com.tpad.common.utils.phonebaseinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.tpad.common.utils.Md5Utils;

/*
 * Created by loang.chen on 2015/4/23.
 * 手机相关的基本信息获取
 */
public class PhoneBaseInfoUtils {

    private final static String TAG = PhoneBaseInfoUtils.class.getSimpleName();
    private static PhoneBaseInfoUtils mPhoneBaseInfoUtils;
    private Context mContext;

    public PhoneBaseInfoUtils(Context c) {
        this.mContext = c;
    }

    public static PhoneBaseInfoUtils getInstance(Context c) {
        if (mPhoneBaseInfoUtils == null) {
            mPhoneBaseInfoUtils = new PhoneBaseInfoUtils(c);
        }
        return mPhoneBaseInfoUtils;
    }

    public Firmware getFirmware() {
        Firmware firmware = new Firmware();
        firmware.setClientVersion(getPhoneAppVersion(mContext
                .getPackageName()));
//        String fm = FileUtils.getFm(mContext, Environment
//                .getExternalStorageDirectory().toString() + "/BossUnlock/config/fm.txt");
        firmware.setFm(getMetaValueFromAndroidManifest("UMENG_CHANNEL"));
        firmware.setImei(getPhoneImeiNum());
        firmware.setImsi(getPhoneImsiNum());
        String phoneModel = Build.MODEL;
        if (phoneModel.contains(" ")) {
            String[] model = phoneModel.split(" ");
            phoneModel = model[0] + "_" + model[1];
        }
        firmware.setModel(phoneModel);
        firmware.setNetEnv(getNetType());
        firmware.setOperators(getPhoneUseMobileType());
        firmware.setOs("android-" + Build.VERSION.RELEASE);
        firmware.setPkg(getAndroidManifestInfo().packageName);
        firmware.setResolution(getPhoneDistinGuishability());
        firmware.setAndroid_id(getAndroidId());
        firmware.setMac(getMac());
        firmware.setDevice_id(getDeviceId());
        firmware.setBrand(Build.BRAND.toString());

        return firmware;
    }

    /**
     * 获取渠道名
     *
     * @return 如果没有获取成功，那么返回值为空
     */
    public String getMetaValueFromAndroidManifest(String key) {
        String channelName = "";
        try {
            PackageManager packageManager = mContext.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    public String getDeviceId() {
        StringBuffer sb = new StringBuffer();
        sb.append(getPhoneImeiNum()).append("-").append(getMac()).append("-").append(getAndroidId());
        return Md5Utils.getMD5String(sb.toString());
    }

    public String getAndroidId() {
        String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public String getMac() {
        try {
            WifiManager localWifiManager = (WifiManager) mContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
            return localWifiInfo.getMacAddress();
        } catch (Exception localException) {
            Log.e(TAG, "Could not get mac address." + localException.toString());
        }
        return "";
    }

    public boolean isPhoneCurrWifiOpen() {
        WifiManager mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        try {
            wifiInfo = mWifiManager.getConnectionInfo();
        } catch (Exception e) {
            wifiInfo = null;
        }
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        // WIFI is on
// WIFI is off
        return mWifiManager.isWifiEnabled() && ipAddress != 0;
    }

    public String getPhoneAppVersion(String packageName) {
        if (mContext == null) {
            return null;
        }
        if (packageName == null || packageName.equals(""))
            return null;
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPhoneAppVersion() {
        if (mContext == null) {
            return null;
        }
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPhoneImeiNum() {
        return ((TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public String getPhoneUseLanguage() {
        return mContext.getResources().getConfiguration().locale.getLanguage();
    }

    public String getPhoneImsiNum() {
        return ((TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    public String getPhoneUseMobileType() {
        String mobileType = null;
        TelephonyManager iPhoneManager = null;
        String iNumeric = null;
        iPhoneManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (iPhoneManager == null) {
            return "UNKNOWN";
        }
        iNumeric = iPhoneManager.getSimOperator();
        if (iNumeric != null && iNumeric.length() > 0) {
            if (iNumeric.equals("46000") || iNumeric.equals("46002")) {
                // YD
                mobileType = "YD";
            } else if (iNumeric.equals("46001")) {
                // LT
                mobileType = "LT";
            } else if (iNumeric.equals("46003")) {
                // DX
                mobileType = "DX";
            }
        }
        return mobileType;
    }

    public DisplayMetrics getPhoneScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }


    public String getPhoneDistinGuishability() {
        return new StringBuffer().append(getPhoneScreen().widthPixels)
                .append("*").append(getPhoneScreen().heightPixels).toString();
    }

    public PackageInfo getAndroidManifestInfo() {
        PackageInfo pi = new PackageInfo();
        try {
            pi = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public String getNetType() {
        if (isPhoneCurrWifiOpen()) {
            return "WIFI";
        } else {
            return getPhoneUseNetWorkType();
        }
    }

    public String getPhoneUseNetWorkType() {
        String type = null;
        TelephonyManager telMgr = null;
        telMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);

        switch (telMgr.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
                type = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                type = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                type = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                type = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                type = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                type = "IDEN";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                type = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                type = "UNKNOW";
                break;
        }
        return type;
    }

}
