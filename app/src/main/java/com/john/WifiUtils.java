package com.john;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 判断wifi是否能够正常使用的工具类 使用时调用ping方法，实现CanWifiConnection接口
 *
 * @author john.jiang 2014/12/22
 */

public class WifiUtils {

    private final static int NETWORK_CONNECTION_NORMAL = 1;
    private final static int NETWORK_CONNECTION_NOTNORMAL = 2;
    private final static String TAG = WifiUtils.class.getSimpleName();
    private static CanWifiConnection wifiConnection;
    private static boolean isShowLog = false;
    private static int loadTime = 500;
    private static Context context;
    private static boolean canRemove = true;

    private static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case NETWORK_CONNECTION_NORMAL:
                    wifiConnection.canConnection(true);
                    break;

                case NETWORK_CONNECTION_NOTNORMAL:
                    wifiConnection.canConnection(false);
                    break;

                default:
                    break;
            }
        }

    };

    public static void ShowLog() {
        isShowLog = true;
    }

    public static void CloseLog() {
        isShowLog = false;
    }

    public static void setLoadTime(int time) {
        loadTime = time;
    }

    /**
     * 通过ping外网判断网络是否可用
     *
     * @return 是否能ping通外网
     */
    public static void ping(Context c,
                            final CanWifiConnection canWifiConnection) {
        if (canWifiConnection == null) {
            return;
        }
        context = c;
        wifiConnection = canWifiConnection;
        if (context == null) {
            wifiConnection.canConnection(false);
            return;
        }
        String wifiNameString = getWifiName(context);
        if (wifiNameString == null) {
//            setWIFIStatus(context, false);
            wifiConnection.canConnection(false);
            return;
        }
        handler.post(pingRunnable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canRemove){
                    handler.removeCallbacks(pingRunnable);
                    handler.sendEmptyMessage(NETWORK_CONNECTION_NOTNORMAL);
                }
            }
        },1500);
    }

    private static Runnable pingRunnable = new Runnable() {
        @Override
        public void run() {
            String result = null;
            boolean isCanConnection = false;
            try {
                String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题(也可以换成自己要连接的服务器地址)
                Process p = Runtime.getRuntime().exec(
                        "ping -c 1 -w 5 " + ip);// ping3次
                // PING的状态
                int status = p.waitFor();
                if (isShowLog) {
                    Log.e(TAG, "p.waitFor():" + status);
                }
                if (status == 0) {
                    result = "successful~";
                    isCanConnection = true;
                } else {
                    ip = "www.taobao.com";// 除非淘宝挂了，否则用这个应该没问题(也可以换成自己要连接的服务器地址)
                    p = Runtime.getRuntime().exec(
                            "ping -c 3 -w 5 " + ip);// ping3次
                    // 读取ping的内容，可不加。
                    status = p.waitFor();
                    if (status == 0) {
                        result = "successful~";
                        isCanConnection = true;
                    } else {
                        result = "failed~ cannot reach the IP address";
                        isCanConnection = false;
                    }
                }

            } catch (IOException e) {
                result = "failed~ IOException";
                isCanConnection = false;
            } catch (InterruptedException e) {
                result = "failed~ InterruptedException";
                isCanConnection = false;
            } finally {
                if (isShowLog) {
                    Log.e(TAG, "result = " + result);
                }
                canRemove = false;
                if (isCanConnection) {
                    handler.sendEmptyMessage(NETWORK_CONNECTION_NORMAL);
                } else {
                    handler.sendEmptyMessage(NETWORK_CONNECTION_NOTNORMAL);
                }
            }
        }
    };

    private static String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (isShowLog && wifiInfo != null) {
            long ll = System.currentTimeMillis();
            Log.e(TAG, "\n获取BSSID属性（所连接的WIFI设备的MAC地址）：" + wifiInfo.getBSSID());
            Log.e(TAG, "\n\n获取SSID 是否被隐藏：" + wifiInfo.getHiddenSSID());
            Log.e(TAG, "\n\n获取IP 地址：" + wifiInfo.getIpAddress());
            Log.e(TAG, "\n\n获取连接的速度：" + wifiInfo.getLinkSpeed());
            Log.e(TAG, "\n\n获取802.11n 网络的信号：" + wifiInfo.getRssi());
            Log.e(TAG, "\n\n获取SSID（所连接的WIFI的网络名称）：" + wifiInfo.getSSID());
            Log.e(TAG, "\n\n获取具体客户端状态的信息：" + wifiInfo.getSupplicantState());
            Log.e(TAG, "所花时间：" + (System.currentTimeMillis() - ll) / 1 + "s");
        }
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (wifiManager.isWifiEnabled() && ipAddress != 0) {
            return wifiInfo.getSSID();
        }
        return null;
    }

    public interface CanWifiConnection {
        void canConnection(boolean connect);
    }
}
