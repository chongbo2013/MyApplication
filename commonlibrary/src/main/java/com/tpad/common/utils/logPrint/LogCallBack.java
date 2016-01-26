package com.tpad.common.utils.logPrint;

/**
 * Created by loang.chen on 2015/4/29.
 */
public interface LogCallBack {

    void onSuccess(String jsonData);

    void onFailure(String error);

}
