package com.tpad.common.model.processData;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

/**
 * 跨进程读写到SQLite
 * 需在XML的注册ProcessDataContentProvider
 * Created by jone.sun on 2015/3/24.
 */
public class ProcessDBDataOperator implements ProcessDataOperator {
    private static ProcessDBDataOperator instance;
    private Context context;
    private ContentResolver contentResolver;
    private static Uri URL;
    private static String STRING_URI;
    public static ProcessDBDataOperator getInstance(Context context){
        if(instance == null){
            instance = new ProcessDBDataOperator(context);
        }
        return instance;
    }

    private ProcessDBDataOperator(Context context){
        this.context = context;
        contentResolver = context.getContentResolver();
        AppProcessConfigInterface appProcessInterface = (AppProcessConfigInterface) context.getApplicationContext();
        URL = appProcessInterface.getProcessDataConfig().getDb_uri();
        STRING_URI = appProcessInterface.getProcessDataConfig().getString_db_uri();
    }

    @Override
    public <T> void putValue(String key, T value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, String.valueOf(value));
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public void batchPutValue(ContentValues contentValues) {
        contentResolver.insert(URL, contentValues);
    }

    @Override
    public int getValueByKey(String key, int defaultValue) {
        try{
            String result = getResult(context, key);
            if(result == null){
                return defaultValue;
            }
            return Integer.parseInt(result);
        }catch (Exception e){
            return defaultValue;
        }
    }

    @Override
    public float getValueByKey(String key, float defaultValue) {
        try{
            String result = getResult(context, key);
            if(result == null){
                return defaultValue;
            }
            return Float.parseFloat(result);
        }catch (Exception e){
            return defaultValue;
        }
    }

    @Override
    public long getValueByKey(String key, long defaultValue) {
        try{
            String result = getResult(context, key);
            if(result == null){
                return defaultValue;
            }
            return Long.parseLong(result);
        }catch (Exception e){
            return defaultValue;
        }
    }

    @Override
    public boolean getValueByKey(String key, boolean defaultValue) {
        try{
            String result = getResult(context, key);
            if(result == null){
                return defaultValue;
            }
            return Boolean.parseBoolean(result);
        }catch (Exception e){
            return defaultValue;
        }
    }

    @Override
    public String getValueByKey(String key, String defaultValue) {
        String result = getResult(context, key);
        if(result == null){
            return defaultValue;
        }
        return result;
    }

    @Override
    public boolean delValueByKey(String key) {
        contentResolver.delete(URL, key, null);
        return true;
    }

    @Override
    public void register(ContentObserver contentObserver, String key) {
        contentResolver.registerContentObserver(Uri.parse(STRING_URI +
                        key),
                true, contentObserver);
    }

    @Override
    public void unregister(ContentObserver contentObserver) {
        contentResolver.unregisterContentObserver(contentObserver);
    }

    private static String getResult(Context context, String key){
        String result = null;
        Cursor cursor = context.getContentResolver().query(URL,
                null,
                key,
                null,
                null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getString(0);

            cursor.close();
        }
        if(result == null || result.equals("null")){
            result = null;
        }
        return result;
    }
}
