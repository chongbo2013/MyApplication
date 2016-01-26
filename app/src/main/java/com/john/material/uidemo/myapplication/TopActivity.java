package com.john.material.uidemo.myapplication;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tpad.common.model.guidemodel.GuideUtils;

/**
 * Created by jiajun.jiang on 2015/12/8.
 */
public class TopActivity extends Activity {
    public Activity activity = null;
    private Button button;
    private LinearLayout base_ll;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
//        setContentView(R.layout.activity_first_layout);
//        button = (Button)findViewById(R.id.button);
//        base_ll = (LinearLayout) findViewById(R.id.base_ll);
//        button.setText("这是服务开启后的activity");
//        base_ll.setBackgroundColor(Color.TRANSPARENT);
        view = getLayoutInflater().inflate(R.layout.layout_top_view,null);
        Button button4 = (Button)view.findViewById(R.id.button4);
        Button button5 = (Button)view.findViewById(R.id.button5);
        Button button6 = (Button)view.findViewById(R.id.button6);
        button4.setText("关闭窗口");
        button5.setText("一键设置");
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                WindowsUtils.b((WindowManager) getApplication().getSystemService("window"), TopActivity.this.view);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuideUtils.openSetting(TopActivity.this, false);
            }
        });
        WindowsUtils.a((WindowManager) getApplication().getSystemService("window"), view);
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }
}
