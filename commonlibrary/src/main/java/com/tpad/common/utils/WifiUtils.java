package com.tpad.common.utils;

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
    private static boolean isShowLog = true;
    private static int loadTime = 500;
    private static Context context;

    private static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case NETWORK_CONNECTION_NORMAL:
                    //setWIFIStatus(context, true);
                    wifiConnection.canConnection(true);
                    break;

                case NETWORK_CONNECTION_NOTNORMAL:
                    //setWIFIStatus(context, false);
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
//		if (wifiNameString.contains("CMCC")
//				|| wifiNameString.contains("ChinaNet")) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = null;
                boolean isCanConnection = false;
                String[] times = null;
                try {
                    String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题(也可以换成自己要连接的服务器地址)
                    Process p = Runtime.getRuntime().exec(
                            "ping -c 3 -w 5 " + ip);// ping3次
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
                    InputStream input = p.getInputStream();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(input));
                    StringBuffer stringBuffer = new StringBuffer();
                    String content = "";
                    while ((content = in.readLine()) != null) {
                        stringBuffer.append(content);
                    }
                    if (isShowLog) {
                        Log.e(TAG,
                                "result content : " + stringBuffer.toString());
                    }
                    String resultString = stringBuffer.toString();
                    if (isShowLog) {
                        Log.e(TAG, resultString);
                    }
                    if (resultString.contains(" = ")) {
                        String timesString = resultString.substring(resultString.lastIndexOf("=") + 1)
                                .trim();
                        if (isShowLog) {
                            Log.e(TAG, timesString);
                        }
                        times = timesString.split("/");
                        if (times != null && times.length > 0) {
                            for (int i = 0; i < times.length; i++) {
                                times[i] = times[i].substring(0, times[i].indexOf("."));
                            }
                            for (String string : times) {
                                if (isShowLog) Log.e(TAG, "time:" + string);
                            }
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
                    int avgTime = -1;
                    if (times != null && times.length > 2) {
                        String time = times[1];
                        if (time != null && !time.equals("")) {
                            avgTime = Integer.parseInt(time);
                        }
                    }
                    if (isShowLog) {
                        Log.e(TAG, "avgTime = " + avgTime);
                    }
                    if (isCanConnection && avgTime <= loadTime) {
                        handler.sendEmptyMessage(NETWORK_CONNECTION_NORMAL);
                    } else {
                        handler.sendEmptyMessage(NETWORK_CONNECTION_NOTNORMAL);
                    }
                }
            }
        }).start();
//		} else {
//			handler.sendEmptyMessage(NETWORK_CONNECTION_NORMAL);
//		}

    }

    public static void install() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment
                        .getExternalStorageDirectory().toString() + "/51人品.apk";
                Log.e("wifiUtil", path);
                try {
                    Process p = Runtime.getRuntime().exec("pm install -f " + path);
                    Log.e("wifiUtil", p.waitFor() + ";");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


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


//    private static void setWIFIStatus(Context context, boolean isGood){
//        TTApplication.getProcessDataSPOperator().putValue(context, "sp_key_wifi_is_good", isGood);
//        LogUtils.getInstance().printf(LogUtils.FILE_TAG_WIFI_AUTO,
//                TAG, "设置WIFI状态标示",
//                LogType.INFO,
//                "isGood: " + isGood);
//    }
//
//    public static boolean isWiFIGood(Context context){
//        return TTApplication.getProcessDataSPOperator().getValueByKey(context, "sp_key_wifi_is_good", false);
//    }

    public interface CanWifiConnection {
        void canConnection(boolean connect);
    }
}
