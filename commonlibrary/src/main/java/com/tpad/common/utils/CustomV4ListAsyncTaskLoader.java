package com.tpad.common.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author jone.sun on 2015/3/24.
 */
public class CustomV4ListAsyncTaskLoader extends AsyncTaskLoader<List> {
    private List list;
    private LoadListener listener;
    public CustomV4ListAsyncTaskLoader(Context context, LoadListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onStartLoading() {
        if (list != null){
            deliverResult(list);
        }

        if(takeContentChanged() || list == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        list = null;
    }

    @Override
    public List loadInBackground() {
        list = Collections.unmodifiableList(listener.loading());
        return list;
    }

    public interface LoadListener {
        List loading();
    }
}


