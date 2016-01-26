package com.john.material.uidemo.myapplication;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.john.service.SettingService;
import com.tpad.common.model.guidemodel.GuideUtils;

/**
 * Created by jiajun.jiang on 2015/12/1.
 */
public class FirstActivity extends Activity {
    private Button button;
    private Button OPPO;
    private Button Vivo;
    private Button white_list;
    private int index = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_layout);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingService.openAccessibilityServiceSetting(FirstActivity.this);
            }
        });

        OPPO = (Button)findViewById(R.id.OPPO);
        white_list = (Button)findViewById(R.id.white_list);
        white_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
//                Log.e("FirstActivity",getApplication().getPackageName()+"");
//                intent.putExtra("packageName","com.change.unlock.boss");
                startActivity(intent);
//                new GuideUtils(FirstActivity.this,FirstActivity.this.getPackageName()).settingAutoStart();
            }
        });

        OPPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("com.meizu.safe.security.SHOW_SECAPPLIST");
//                Log.e("FirstActivity",getApplication().getPackageName()+"");
                intent.putExtra("id", 268435456);
                startActivity(intent);
                Toast.makeText(FirstActivity.this,index + "",Toast.LENGTH_SHORT).show();
                index ++;
            }
        });

        Vivo = (Button)findViewById(R.id.Vivo);
        Vivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
//                intent.putExtra("android.intent.extra.REPLACING",true);
//                sendBroadcast(intent);
                try {
                    Context context = createPackageContext("com.meizu.safe", 0);
                    intent.setClassName("com.meizu.safe", "com.meizu.safe.security.HomeActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("extra_pkgname",getApplication().getPackageName());
//                    intent.setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity"));
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    Context context = createPackageContext("com.oppo.safe", 0);
//                    intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity"));
//                    intent.addCategory("android.intent.category.DEFAULT");
                    context.startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("PackageManager","onClick", e);
                    e.printStackTrace();
                }
            }
        });
    }

}
