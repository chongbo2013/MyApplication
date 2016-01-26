package com.tpad.common.model.processData;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 跨进程读取数据ContentProvider基类
 * Created by jone.sun on 2015/9/24.
 */
public abstract class BaseProcessDataContentProvider extends ContentProvider {
    private static final String TAG = BaseProcessDataContentProvider.class.getSimpleName();
    public final static int SP = 1;
    public final static int DB = 2;


    private UriMatcher URI_MATCHER;
    private String STRING_SP_URI, STRING_DB_URI;
    private String SP_NAME = "sp_process";

    private SharedPreferences sharedPreferences;

    public BaseProcessDataContentProvider(UriMatcher URI_MATCHER, String AUTHORITY) {
        this.URI_MATCHER = URI_MATCHER;
        STRING_SP_URI = "content://" + AUTHORITY + "/sp/";
        STRING_DB_URI = "content://" + AUTHORITY + "/db/";
        Log.e(TAG, "STRING_SP_URI: " + STRING_SP_URI + "\r\n"
                + "STRING_DB_URI: " + STRING_DB_URI + "\r\n"
                + "SP_NAME: " + SP_NAME);
    }

    public BaseProcessDataContentProvider(UriMatcher URI_MATCHER, String AUTHORITY, String SP_NAME) {
        this.URI_MATCHER = URI_MATCHER;
        this.SP_NAME = SP_NAME;
        STRING_SP_URI = "content://" + AUTHORITY + "/sp/";
        STRING_DB_URI = "content://" + AUTHORITY + "/db/";
        Log.e(TAG, "STRING_SP_URI: " + STRING_SP_URI + "\r\n"
                + "STRING_DB_URI: " + STRING_DB_URI + "\r\n"
                + "SP_NAME: " + SP_NAME);
    }

    @Override
    public boolean onCreate() {
        sharedPreferences = getContext().getSharedPreferences(SP_NAME, getContext().MODE_PRIVATE);
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int flag = URI_MATCHER.match(uri);
//        Log.e(TAG, "insert>>" + uri.toString() + " ," + flag);
        if(values == null){
            return null;
        }
        switch (flag){
            case SP:
                List<Uri> uriList = new ArrayList<>();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    for (Map.Entry<String, Object> item : values.valueSet()) {
                        String key = item.getKey(); // getting key
                        Object value = item.getValue(); // getting value
                        saveToSharedPreferences(editor, key, value);
                        uriList.add(Uri.parse(STRING_SP_URI + key));
                    }
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    for (String key : values.keySet()) {
                        saveToSharedPreferences(editor, key, values.get(key));
                        uriList.add(Uri.parse(STRING_SP_URI + key));
                    }
                }
                if(editor.commit()){
                    for(Uri u : uriList){
                        getContext().getContentResolver().notifyChange(u, null);
                    }
                }
                break;
            case DB:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    for (Map.Entry<String, Object> item : values.valueSet()) {
                        String key = item.getKey(); // getting key
                        Object value = item.getValue(); // getting value
                        if(LongTextStringDao.getInstance(getContext()).save(key, String.valueOf(value))){
                            getContext().getContentResolver().notifyChange(Uri.parse(STRING_DB_URI + key), null);
                        }
                    }
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    for (String key : values.keySet()) {
                        if(LongTextStringDao.getInstance(getContext()).save(key, String.valueOf(values.get(key)))){
                            getContext().getContentResolver().notifyChange(Uri.parse(STRING_DB_URI + key), null);
                        }
                    }
                }
                break;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int flag = URI_MATCHER.match(uri);
        // Log.e(TAG, "delete>>" + uri.toString() + " ," + flag);
        int result = 0;
        switch (flag){
            case SP:
                if(selection != null){
                    result = sharedPreferences.edit().remove(selection).commit() ? 1: 0;
                    getContext().getContentResolver().notifyChange(Uri.parse(STRING_SP_URI + selection), null);
                }
                break;
            case DB:
                if(selection != null){
                    result = LongTextStringDao.getInstance(getContext()).delValueByKey(selection);
                    getContext().getContentResolver().notifyChange(Uri.parse(STRING_DB_URI + selection), null);
                }
                break;
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        int flag = URI_MATCHER.match(uri);
        switch (flag) {
            case SP:
                return "vnd.android.cursor.item/com.change.unlock.common.sp";
            case DB:
                return "vnd.android.cursor.item/com.change.unlock.common.db";
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String defaultStr) {
        int flag = URI_MATCHER.match(uri);
        //Log.e(TAG, "query>>" + uri.toString() + " ," + flag);
        String[] names = new String[]{"result"};
        String[] values = new String[]{"null"};
        MatrixCursor cursor = new MatrixCursor(names);
        switch (flag){
            case SP:
                if(selection != null ){
                    if(selectionArgs != null && selectionArgs.length > 0){
                        String type = selectionArgs[0];
                        if(type.equals(String.class.getCanonicalName())){
                            values = new String[]{sharedPreferences.getString(selection, defaultStr)};
                        }else if(type.equals(Integer.class.getCanonicalName())){
                            values = new String[]{String.valueOf(sharedPreferences.getInt(selection, Integer.parseInt(defaultStr)))};
                        }else if(type.equals(Boolean.class.getCanonicalName())){
                            values = new String[]{String.valueOf(sharedPreferences.getBoolean(selection, Boolean.parseBoolean(defaultStr)))};
                        }else if(type.equals(Float.class.getCanonicalName())){
                            values = new String[]{String.valueOf(sharedPreferences.getFloat(selection, Float.parseFloat(defaultStr)))};
                        }else if(type.equals(Long.class.getCanonicalName())){
                            values = new String[]{String.valueOf(sharedPreferences.getLong(selection, Long.parseLong(defaultStr)))};
                        }
                    }else if(selectionArgs == null){//默认值为null时要单独处理
                        values = new String[]{sharedPreferences.getString(selection, defaultStr)};
                    }

                }
                cursor.addRow(values);
                break;
            case DB:
                if(selection != null){
                    String result = LongTextStringDao.getInstance(getContext()).getValueByKey(selection);
                    if(result != null){
                        values = new String[]{result};
                    }
                }
                cursor.addRow(values);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int flag = URI_MATCHER.match(uri);
        //Log.e(TAG, "update>>" + uri.toString() + " ," + flag);
        int result = -1;
        switch (flag){
            case SP:
                break;
            case DB:
                break;
        }
        return result;
    }

    private void saveToSharedPreferences(SharedPreferences.Editor editor, String key, Object value){
        if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if (value instanceof String){
            editor.putString(key, (String) value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if(value instanceof Long){
            editor.putLong(key, (Long) value);
        }else if(value instanceof Float){
            editor.putFloat(key, (Float) value);
        }
    }

}

