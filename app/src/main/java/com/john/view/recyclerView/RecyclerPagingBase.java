package com.john.view.recyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.john.material.uidemo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.jiang on 2015/10/19.
 */
public abstract class RecyclerPagingBase<T> extends RecyclerView.OnScrollListener implements RecyclerPagingBaseCallBack<T>{
    private Context context;
    private View pagingView;
    private List<T> dataList = new ArrayList<>();
    private int currentPage = 1;
    private boolean isLoadingData = false; // 是否正在加载数据
    private boolean isHaveMore = false;
    private static final int WHAT_RESPONSE_SUCCESS = 10001;
    private static final int WHAT_RESPONSE_FAIL = 10002;
    private List<T> loadData = new ArrayList<>();
    private String nextPage = null;
    private SwipeRefreshLayout paging_base_refresh;
    private RecyclerView paging_base_recycler;
    private RecyclerView.LayoutManager gridLayoutManager;
    private RecyclerPagingAdapter<T> adapter;
    private DividerGridItemDecoration dividerGridItemDecoration;
    private int lastVisibleItem;
    private boolean canLoadMore = true;

    public RecyclerPagingBase(Context context){
        this.context = context;
    }

    public View getBaseView() {
        if (context == null) {
            return null;
        }
        pagingView = View.inflate(context, R.layout.recycler_base_paging_layout, null);
        findViews();
        initViews();
        bindListen();
        return pagingView;
    }

    private void bindListen() {

    }

    private void initViews() {

        gridLayoutManager = initGridLayoutManager();
        adapter = initBaseAdapter();
        dividerGridItemDecoration = initDividerGridItemDecoration();

        paging_base_recycler.setLayoutManager(gridLayoutManager);
        paging_base_recycler.setAdapter(adapter);
        if (dividerGridItemDecoration!=null) {
            paging_base_recycler.addItemDecoration(dividerGridItemDecoration);
        }
        paging_base_recycler.setOnScrollListener(this);
        paging_base_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(200);
            }
        });
    }

    private void findViews() {
        paging_base_refresh = (SwipeRefreshLayout)pagingView.findViewById(R.id.paging_base_refresh);
        paging_base_recycler = (RecyclerView)pagingView.findViewById(R.id.paging_base_recycler);
    }

    public abstract RecyclerPagingAdapter<T> initBaseAdapter();

    public abstract RecyclerView.LayoutManager initGridLayoutManager();

    public abstract DividerGridItemDecoration initDividerGridItemDecoration();

    public abstract List<T> getListData(String result, List<T> data);

    public abstract void setCallBack(RecyclerPagingBaseCallBack<T> callBack);

    @Override
    public void onFailed(String errorMessage) {
        handler.sendEmptyMessage(WHAT_RESPONSE_FAIL);
    }

    @Override
    public void onSuccess(String result, List<T> data) {
        Log.e("loadData :" ,"" +  data.size());
        loadData = getListData(result,data);
        if (loadData != null) {
            handler.sendEmptyMessage(WHAT_RESPONSE_SUCCESS);
        } else {
            handler.sendEmptyMessage(WHAT_RESPONSE_FAIL);
        }
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_RESPONSE_SUCCESS:
                    showData(loadData);
                    loadData.clear();
                    loadData = null;
                    paging_base_refresh.setRefreshing(false);
                    isLoadingData = false;
                    canLoadMore = true;
                    break;
                case WHAT_RESPONSE_FAIL:
                    paging_base_refresh.setRefreshing(false);
                    if (currentPage > 1) {
                        currentPage -= 1;
                    } else {

                    }
                    isLoadingData = false;
                    canLoadMore = true;
                    break;
                case 100:
                    addFootView();

                    break;

                case 200:
                    RefreshView();

                    break;
                default:
                    break;
            }

        }

    };



    private void showData(List list) {
        dataList.addAll(list);
        if (currentPage == 1){
            adapter.setDataList(dataList);
        }else{
            adapter.addDataList(list);
        }

    }

    public void loadData() {
        isLoadingData = true;
        setCallBack(this);
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        lastVisibleItem = ((LinearLayoutManager)gridLayoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if ( canLoadMore && newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItem + 1 == adapter.getItemCount()) {
            canLoadMore = false;
            handler.sendEmptyMessage(100);
        }
//        Log.e("onScrollStateChanged","lastVisibleItem : " + lastVisibleItem +  " newState : " + newState);
//        if (newState == RecyclerView.SCROLL_STATE_IDLE
//                && lastVisibleItem == 0){
//            handler.sendEmptyMessage(200);
//        }
    }

    private void addFootView() {
        paging_base_refresh.setRefreshing(true);
        handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               currentPage += 1;
               loadData();
           }
        },1500);
    }

    private void RefreshView() {
        paging_base_refresh.setRefreshing(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                loadData();
            }
        },1500);
    }
}
