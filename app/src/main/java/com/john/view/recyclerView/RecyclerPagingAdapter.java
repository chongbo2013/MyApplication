package com.john.view.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiajun.jiang on 2015/10/19.
 */
public abstract class RecyclerPagingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> dataList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public T getItem(int position){
        return getDataList() != null && !getDataList().isEmpty() ? getDataList().get(position) : null;
    }


    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataList(List<T> dataList) {
//        Log.e("addDataList","getItemCount : " + getItemCount() + "dataList.size() : " + dataList.size());
        if (dataList != null && !dataList.isEmpty()){
//            this.dataList.addAll(dataList);
            notifyItemRangeChanged(getItemCount(),dataList.size());
        }
    }
}
