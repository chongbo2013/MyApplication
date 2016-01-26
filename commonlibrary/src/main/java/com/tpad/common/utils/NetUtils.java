package com.tpad.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/*
 * Created by loang.chen on 2015/5/8.
 * 判断当前网络状态
 */
public class NetUtils {

    private final static String TAG = NetUtils.class.getSimpleName();
    private static NetUtils mNetUtils;
    private Context mContext;

    public NetUtils(Context c) {
        this.mContext = c;
    }

    public static NetUtils getInstance(Context c) {
        if (mNetUtils == null) {
            mNetUtils = new NetUtils(c);
        }
        return mNetUtils;
    }


    /**
     * Get phone curr WIFI state
     *
     * @return
     */
    public boolean isPhoneCurrWifiOpen() {
        WifiManager mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        try {
            wifiInfo = mWifiManager.getConnectionInfo();
        } catch (Exception e) {
            wifiInfo = null;
            Log.e(TAG, "isPhoneCurrWifiOpen", e);
        }
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            // WIFI is on
            return true;
        } else {
            // WIFI is off
            return false;
        }
    }

    /**
     * is Phone CurrNetwork Open(whether 3G net open or not)
     *
     * @return true or false
     */
    public boolean isPhoneCurrNetworkOpen() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null) {
                    return false;
                } else {
                    if (info.isAvailable()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasNetWork() {
        return isPhoneCurrNetworkOpen() || isPhoneCurrWifiOpen();
    }

    public String sendMessageToServerByPostFun(String destUrl,
                                               List<? extends NameValuePair> params)
            throws ClientProtocolException, IOException {
        String result = "error";
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpResponse response = null;
        HttpEntityEnclosingRequestBase httpRequest = new HttpPost(destUrl);
        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        Log.e(TAG, "params is : " + params);

        response = client.execute(httpRequest);
        if (response.getStatusLine().getStatusCode() == 200) {
            result = "success";
        } else {
            Log.e(TAG, "Error Response "
                    + response.getStatusLine().toString());
            result = "error";
        }
        return result;
    }

    /**
     * Post请求
     *
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String doPost(String url, String params) throws ClientProtocolException, IOException {
        String result = null;
        boolean isReconnect = false;
        Log.e(TAG, "post url is : " + url);
        //新建HttpClient对象
        HttpClient httpclient = new DefaultHttpClient();
        //创建POST连接
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/json");
        Log.e(TAG, "Md5Utils.getMD5String(params) is " + Md5Utils.getMD5String(params));
        httppost.setHeader("Content-Digest", Md5Utils.getMD5String(params));
        try {
            if (params != null) {
                StringEntity entity = new StringEntity(params);
                entity.setContentType("application/json");
                entity.setContentEncoding("utf-8");
                httppost.setEntity(entity);
            }
            HttpResponse response = httpclient.execute(httppost);
            int resultcode = response.getStatusLine().getStatusCode();
            Log.e(TAG, "response.getStatusLine().getStatusCode() is : " + resultcode);
            if (resultcode == 200) {
                result = EntityUtils.toString(response.getEntity());
                //result = "ok";
            } else if (resultcode == 202) {
                result = EntityUtils.toString(response.getEntity());
            } else if (resultcode == 406) {
                // md5 jiaoyan error  reconnect
                if (!isReconnect) {
                    doPost(url, params);
                    isReconnect = true;
                }
                result = "md5 error";
            } else {
                result = "error";
            }
        } finally {

        }
        Log.e(TAG, "result is : " + result);
        return result;
    }

}
