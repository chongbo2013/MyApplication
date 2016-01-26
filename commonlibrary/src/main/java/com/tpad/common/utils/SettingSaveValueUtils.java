package com.tpad.common.utils;

import android.content.ContentResolver;
import android.content.Context;

import com.tpad.common.model.processData.ProcessSpDataOperator;

/*
 * File And Sp operation related 
 */
public class SettingSaveValueUtils {

    private final static String TAG = SettingSaveValueUtils.class.getSimpleName();
    private Context context;
    private ContentResolver resolver;
    private static SettingSaveValueUtils mFileSpUtils;
    private ProcessSpDataOperator mProcessSpDataOperator;


    public SettingSaveValueUtils(Context c) {
        this.context = c;
        resolver = context.getContentResolver();
        mProcessSpDataOperator = ProcessSpDataOperator.getInstance(c);
    }

    public static SettingSaveValueUtils getInstance(Context c) {
        if (mFileSpUtils == null) {
            mFileSpUtils = new SettingSaveValueUtils(c);
        }
        return mFileSpUtils;
    }

    public String getKeyFromSetting(String key) {
        String result = "";
        result = android.provider.Settings.System.getString(resolver, key);
        if (result == null || (result != null && result.equals(""))) {
            if (mProcessSpDataOperator != null) {
                result = mProcessSpDataOperator.getValueByKey(key, "");
            }
        }
        return result;
    }

    public void setKeyToSetting(String key, String value) {
        android.provider.Settings.System.putString(resolver, key, value);
        if (mProcessSpDataOperator != null) {
            mProcessSpDataOperator.putValue(key, value);
        }
    }

    public void setKeyToSettingForBoolean(String key, boolean flag) {
        int value = 0;
        if (flag) {
            value = 1;
        } else {
            value = 0;
        }
        android.provider.Settings.System.putInt(resolver, key, value);
        if (mProcessSpDataOperator != null) {
            mProcessSpDataOperator.putValue(key, value);
        }
    }

    public boolean getKeyFromSettingForBoolean(String key) {
        return 1 == android.provider.Settings.System.getInt(resolver, key, 0) ? true
                : false;
    }

}
