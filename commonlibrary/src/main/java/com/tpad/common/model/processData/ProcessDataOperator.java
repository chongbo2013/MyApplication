package com.tpad.common.model.processData;

import android.content.ContentValues;
import android.database.ContentObserver;

/**
 * @author jone.sun on 2015/3/24.
 */
public interface ProcessDataOperator {
    <T>void putValue(String key, T value);

    void batchPutValue(ContentValues contentValues);

    int getValueByKey(String key, int defaultValue);

    float getValueByKey(String key, float defaultValue);

    long getValueByKey(String key, long defaultValue);

    boolean getValueByKey(String key, boolean defaultValue);

    String getValueByKey(String key, String defaultValue);

    boolean delValueByKey(String key);

    public void register(ContentObserver contentObserver, String key);

    public void unregister(ContentObserver contentObserver);

}
