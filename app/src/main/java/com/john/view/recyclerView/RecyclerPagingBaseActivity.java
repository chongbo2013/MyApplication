package com.john.view.recyclerView;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jiajun.jiang on 2015/10/19.
 */
public abstract class RecyclerPagingBaseActivity<T> extends Activity{
    private RecyclerPagingBase<T> recyclerPagingBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerPagingBase = initRecyclerPagingBase();
        if (recyclerPagingBase!=null){
            setContentView(recyclerPagingBase.getBaseView());
        }
    }

    public RecyclerPagingBase<T> getRecyclerPagingBase() {
        return recyclerPagingBase;
    }

    public abstract RecyclerPagingBase<T> initRecyclerPagingBase();

}
