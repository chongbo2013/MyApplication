package com.john.view.recyclerView;

import java.util.List;

/**
 * Created by jiajun.jiang on 2015/10/19.
 */
public interface RecyclerPagingBaseCallBack<T> {
    void onSuccess(String result,List<T> data);
    void onFailed(String errorMessage);
}
