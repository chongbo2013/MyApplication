package com.tpad.common.model.net;

/**
 * Created by jone.sun on 2015/3/25.
 */
public interface NetOperator<E, T> {
    public void request(String url, com.tpad.common.model.net.NetResponseCallback<T> responseCallback);
    public void request(String url, E params, com.tpad.common.model.net.NetResponseCallback<T> responseCallback);
    public void request(int method, String url, E params, com.tpad.common.model.net.NetResponseCallback<T> responseCallback);
    public void cancelAll();
}
