package com.tpad.common.model.net.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tpad.common.application.CommonApp;
import com.tpad.common.utils.GsonUtils;
import com.tpad.common.utils.logPrint.LogType;
import com.tpad.common.utils.logPrint.LogUtils;

import java.util.Map;

/**
 * @author jone.sun on 2015/3/24.
 */
public class VolleyCommon {
    public static final String TAG = VolleyCommon.class.getSimpleName();
    private static VolleyCommon instance = null;


    public static VolleyCommon getInstance(Context context){
        if(instance == null){
            instance = new VolleyCommon(context);
        }
        return instance;
    }

    private static final int MY_SOCKET_TIMEOUT_MS = 10 * 1000; //超时时间10s
    private RequestQueue mRequestQueue;
    private VolleyCommon(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        try {
//            req.getHeaders().put("User-Agent","tpadsz_bosslocker");
//        } catch (AuthFailureError authFailureError) {
//            authFailureError.printStackTrace();
//            Log.e(TAG,"Volly 设置头部信息失败");
//        }
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//重试失败的请求，自定义请求超时
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        try {
            Map<String,String> requestHeader = req.getHeaders();
            if (LogUtils.isLogSwitch()){
                LogUtils.getInstance(CommonApp.getInstance()).printf(LogUtils.FILE_TAG_LOGIN,
                        "volley", "requestHeader",
                        LogType.INFO,
                        "请求头：" + GsonUtils.toJson(requestHeader));
            }
        } catch (AuthFailureError authFailureError) {
//            authFailureError.printStackTrace();
        }
        mRequestQueue.add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void requestImg(String url, int maxWidth, int maxHeight, Bitmap.Config decodeConfig,
                           Response.Listener<Bitmap> successListener,
                           Response.ErrorListener errorListener){
        addToRequestQueue(new ImageRequest(url, successListener,
                maxWidth, maxHeight, decodeConfig, errorListener));
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }
}
