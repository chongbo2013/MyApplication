package com.tpad.common.model.guidemodel;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.change.unlock.common.R;
import com.tpad.common.utils.PhoneUtils;


/**
 * @author Created by loang.chen on 2015/3/3.
 */
public class GuideWindowActivity extends Activity implements View.OnClickListener {

    private final static String TAG = GuideWindowActivity.class.getSimpleName();
    private int opentype = 0;// 表示标准
    private PhoneUtils mPhoneUtils;
    private String currBrandModel = "";//当前品牌机型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoneUtils = PhoneUtils.getInstance(this);

        Log.e(TAG, "BRAND IS : " + Build.BRAND + "==MANUFACTURER IS : " + Build.MANUFACTURER
                + "==MODEL IS : " + Build.MODEL + "==PRODUCT IS : " + Build.PRODUCT + "== DEVICE IS : " + Build.DEVICE);

        currBrandModel = new PhoneRelaxInfo().getCurrPhoneBrandModel();

        Log.e(TAG, "currBrandModel is :" + currBrandModel);


        opentype = getIntent().getIntExtra("from_where", 0);
        switch (opentype) {
            case 1:
                if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI) ||
                        currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_U750T)) {
                    setLayoutPadding(26,26);
                } else {
                    // 普通手机

                }
                if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI)) {
                    setContentView(R.layout.guide_setting_window_auto_launch_view);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI)) {
                    setContentView(R.layout.guide_setting_window_auto_launch_view_huawei);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if(currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU)){
                    if(PhoneRelaxInfo.isFlymeOS3x()){
                        setContentView(R.layout.guide_setting_window_auto_launch_view_meizu_3x);
                        findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                    }else if(PhoneRelaxInfo.isFlymeOS4x()){
                        setContentView(R.layout.guide_setting_window_auto_launch_view_meizu_4x);
                        findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                    }
                }
                break;
            case 2:
                //开启消息通知
                if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI)){
                    setContentView(R.layout.guide_setting_windows_showtoast_miui);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_X9007)){
                    setLayoutPadding(15,15);
                    setContentView(R.layout.guide_setting_windows_showtoast_oppo_display2);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                }else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU)){
                    setLayoutPadding(15,15);
                    if(PhoneRelaxInfo.isFlymeOS3x()){
                        setContentView(R.layout.guide_setting_windows_showtoast_meizu_3x);
                        findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                    }else if(PhoneRelaxInfo.isFlymeOS4x()){
                        setContentView(R.layout.guide_setting_windows_showtoast_meizu_4x);
                        findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                    }
                }else{
                    setContentView(R.layout.guide_setting_window_showtoast_view_normal);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                }

                break;
            case 3:
                if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI) ||
                        currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_U750T)) {
                    setLayoutPadding(26,26);
                } else {
                    // 普通手机

                }
                if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI)) {
                    if (Build.MODEL.equals("MI 1S") && Build.DEVICE.equals("mione_plus") && Build.PRODUCT.equals("mione_plus")) {
                        setContentView(R.layout.guide_setting_window_disable_sys_locker_miui_1s_view);
                    } else {
                        setContentView(R.layout.guide_setting_window_disable_sys_locker_miui_view);
                    }
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI)) {
                    setContentView(R.layout.guide_setting_window_disable_sys_locker_huawei_view);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU)) {
                    setContentView(R.layout.guide_setting_window_disable_sys_locker_meizu_view);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_U750T)) {
                    setContentView(R.layout.guide_setting_window_disable_sys_locker_oppou750t_view);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else if (currBrandModel.equals(PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_X9007)) {
                    setContentView(R.layout.guide_setting_window_disable_sys_locker_oppo_x9007_view);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                } else {
                    setContentView(R.layout.guide_setting_window_disable_sys_locker_view);
                    findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                }

                break;
            case 4: //MIUI悬浮窗
                setContentView(R.layout.guide_setting_window_manager);
                findViewById(R.id.btn_guide_confirm).setOnClickListener(this);
                break;
        }

    }

    private void setLayoutPadding(int left,int right){
        Window dialogWindow = this.getWindow();
        dialogWindow.getDecorView().setPadding((int) (left * mPhoneUtils.getWScale(720)), 0, (int) (right * mPhoneUtils.getWScale(720)), 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View view) {
       if(view.getId()==R.id.btn_guide_confirm) {
                this.finish();
        }
    }




}
