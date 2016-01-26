package com.tpad.common.model.net;

import android.content.Context;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * 异步网络请求（get/put/post）
 *
 * @author loang
 * @date 20141027
 */
public class AsyncHttpResponseUtils {

    private final static String TAG = AsyncHttpResponseUtils.class
            .getSimpleName();
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static AsyncHttpClient SyncHttpClient = new SyncHttpClient();

    static {
        client.setMaxRetriesAndTimeout(1, 10 * 1000);
        //client.setUserAgent(TTApplication.getPhoneUtils().getPhoneAppVersion());
        //client.addHeader("client_version", TTApplication.getPhoneUtils().getPhoneAppVersion());
        SyncHttpClient.setMaxRetriesAndTimeout(1, 10 * 1000);
        //SyncHttpClient.setUserAgent(TTApplication.getPhoneUtils().getPhoneAppVersion());
        //SyncHttpClient.addHeader("client_version", TTApplication.getPhoneUtils().getPhoneAppVersion());
    }

    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return SyncHttpClient;
        return client;
    }

    /**
     * POST提交本地信息到服务器
     *
     * @param url
     * @param params
     * @param handler
     */
    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        getClient().post(url, params, handler);
    }

    /**
     * PUT提交本地信息到服务器
     *
     * @param url
     * @param params
     * @param handler
     */
    public static void put(String url, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        getClient().put(url, params, handler);
    }

    /**
     * GET获取服务器返回数据
     *
     * @param url
     * @param params
     * @param handler
     */
    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        getClient().get(url, params, handler);
    }

    /**
     * 取消請求
     *
     * @param mContext
     */
    public static void cancelRequests(Context mContext) {
        getClient().cancelRequests(mContext, true);
    }

}
