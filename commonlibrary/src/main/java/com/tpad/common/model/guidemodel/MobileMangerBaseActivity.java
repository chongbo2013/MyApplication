package com.tpad.common.model.guidemodel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.change.unlock.common.R;
import com.tpad.common.utils.PhoneUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by jiajun.jiang on 2015/3/16.
 */
public abstract class MobileMangerBaseActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listview;
    private List<MobileManger> mobileMangerLists;
    private LinearLayout view;
    private View topView;
    private PhoneUtils phoneUtils;
    private GuideUtils guideUtils;
    private String APPName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_mobile_manger_layout);
        phoneUtils = PhoneUtils.getInstance(this);
        guideUtils = new GuideUtils(this,APPName);
        view = (LinearLayout) findViewById(R.id.top_view);
        topView = getTopView();
        APPName = getAppName();
        initData();
        if (topView != null){
            view.addView(topView);
        }
        listview = (ListView)findViewById(R.id.setting_mobile_manger_listView);
        mobileMangerLists = guideUtils.getShowMobileManger(this,initMangerLinkUrls());
        listview.setAdapter(new myAdapter(mobileMangerLists));
        listview.setOnItemClickListener(this);
    }

    protected abstract void initData();

    protected abstract View getTopView();

    protected abstract String getAppName();

    public abstract Map<String,String> initMangerLinkUrls();

    public PhoneUtils getPhoneUtils(){
        return phoneUtils;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         guideUtils.startWithACT_DATA(MobileMangerBaseActivity.this,mobileMangerLists.get(i).getLinkUrl());
    }

    class myAdapter extends BaseAdapter{
        private List<MobileManger> mobileMangers;

        public myAdapter(List<MobileManger> lists){
            this.mobileMangers = lists;

        }

        @Override
        public int getCount() {
            return mobileMangers == null ? 0 : mobileMangers.size();
        }

        @Override
        public Object getItem(int i) {
            return mobileMangers == null ? null : mobileMangers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            View contentView = view;
            if(contentView == null){
                contentView = MobileMangerBaseActivity.this.getLayoutInflater()
                        .inflate(R.layout.guidemodel_mobilemanger_item, viewGroup, false);
                holder = new ViewHolder();
                assert contentView != null;
                holder.icon = (ImageView)contentView.findViewById(R.id.setting_item_mobilemanger_icon);
                holder.name = (TextView) contentView.findViewById(R.id.setting_item_mobilemanger_name);
                RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
                        (int) (96 * phoneUtils.getWScale(720)), (int) (96 * phoneUtils.getWScale(720))
                 );
                iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
                holder.icon.setLayoutParams(iconParams);
                holder.name.setTextSize(phoneUtils.px2sp(30.0f * phoneUtils.getWScale(720)));
                contentView.setTag(holder);

            }else{
                holder = (ViewHolder) contentView.getTag();
            }
            if (mobileMangers.size()==0){
                return contentView;
            }
            holder.icon.setImageDrawable(mobileMangers.get(i).getIcon());
            holder.name.setText(mobileMangers.get(i).getName());
            return contentView;
        }

        class ViewHolder {
            TextView name;
            ImageView icon;
        }
    }

//    public static void StartMobileMangerActivity(Activity activity){
//        activity.startActivity(new Intent(activity,MobileMangerBaseActivity.class));
//        activity.overridePendingTransition(R.anim.in_from_right,
//                R.anim.out_to_left);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
