package com.john.material.uidemo.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.john.WifiUtils;
import com.john.view.recyclerView.DividerGridItemDecoration;
import com.john.view.recyclerView.RecyclerPagingAdapter;
import com.john.view.recyclerView.RecyclerPagingBase;
import com.john.view.recyclerView.RecyclerPagingBaseActivity;
import com.john.view.recyclerView.RecyclerPagingBaseCallBack;
import com.tpad.common.model.net.NetImageOperator;
import com.tpad.common.utils.CtrDateUtils;
import com.tpad.common.utils.PhoneUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends RecyclerPagingBaseActivity<String> {
    private int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRecyclerPagingBase().loadData();
        final long startTime = System.currentTimeMillis();
        Log.e("times ","开始时间 >>>>>> " + CtrDateUtils.ConverLongToString(startTime));
        WifiUtils.ping(this, new WifiUtils.CanWifiConnection() {
            @Override
            public void canConnection(boolean connect) {
                long endTime = System.currentTimeMillis();
                Log.e("times ","结束时间 >>>>>> " + CtrDateUtils.ConverLongToString(endTime));
                Log.e("times ", "相差毫秒数 >>>> " + CtrDateUtils.getGapHaoMiaoCount(new Date(startTime),new Date(endTime)));
                Log.e("times ","connect >>>>> " + connect);
            }
        });
    }

    @Override
    public RecyclerPagingBase<String> initRecyclerPagingBase() {
        return new RecyclerPagingBase<String>(MainActivity.this) {
            @Override
            public RecyclerPagingAdapter<String> initBaseAdapter() {
                return new MyRecyclerPagingAdapter(MainActivity.this);
            }

            @Override
            public RecyclerView.LayoutManager initGridLayoutManager() {
//                return new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
                return new GridLayoutManager(MainActivity.this,2,GridLayoutManager.VERTICAL, false);
            }

            @Override
            public DividerGridItemDecoration initDividerGridItemDecoration() {
//                return null;
                return new DividerGridItemDecoration(MainActivity.this,10);
            }

            @Override
            public List<String> getListData(String result, List<String> data) {
                return data;
            }

            @Override
            public void setCallBack(RecyclerPagingBaseCallBack<String> callBack) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 15 ; i++){
                    count ++;
                    list.add("http://e.hiphotos.baidu.com/image/pic/item/9d82d158ccbf6c819c2095e0be3eb13533fa40a6.jpg");
                }
                callBack.onSuccess(null,list);
            }
        };
    }

    class MyRecyclerPagingAdapter extends RecyclerPagingAdapter<String>{
        private Context context;

        public MyRecyclerPagingAdapter(Context context){
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = View.inflate(context, R.layout.cardview_item_layout, null);
            CardViewHolder viewHolder =  new CardViewHolder(context,view);
            viewHolder.setIsRecyclable(true);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            CardViewHolder cardViewHolder =  (CardViewHolder)viewHolder;
//            cardViewHolder.imageView.setBackgroundResource(R.mipmap.icon_test);
//            cardViewHolder.cardview.setCardBackgroundColor(Color.YELLOW);
            cardViewHolder.textView.setText(i + "");
//            cardViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            NetImageOperator.getInstance(context).showToImageView(getItem(i), cardViewHolder.imageView,0,0);
            cardViewHolder.imageView.setImageUrl(getItem(i),NetImageOperator.getInstance(context).getImageLoader());
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        public CardView cardview;
        public TextView textView;
        public NetworkImageView imageView;


        public CardViewHolder(Context context,View view) {
            super(view);
            cardview = (CardView) view.findViewById(R.id.cardview);
            textView = (TextView) view.findViewById(R.id.tv);
            imageView = (NetworkImageView)view.findViewById(R.id.image);
            cardview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PhoneUtils.getInstance(context).get720WScale(500)));
        }
    }

}
