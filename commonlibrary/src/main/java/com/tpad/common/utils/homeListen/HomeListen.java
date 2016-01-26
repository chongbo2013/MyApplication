package com.tpad.common.utils.homeListen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by loang.chen on 2015/5/26.
 */
public class HomeListen {

    private Context mContext = null;
    private IntentFilter mHomeBtnIntentFilter = null;
    private OnHomeBtnPressLitener mOnHomeBtnPressListener = null;
    private HomeBtnReceiver mHomeBtnReceiver = null;

    public HomeListen(Context context) {
        mContext = context;
        mHomeBtnReceiver = new HomeBtnReceiver();
        mHomeBtnIntentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomeBtnPressListener(OnHomeBtnPressLitener onHomeBtnPressListener) {
        mOnHomeBtnPressListener = onHomeBtnPressListener;
    }

    public void start() {
        mContext.registerReceiver(mHomeBtnReceiver, mHomeBtnIntentFilter);
    }

    public void stop() {
        mContext.unregisterReceiver(mHomeBtnReceiver);
    }

    class HomeBtnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive(context, intent);
        }
    }

    private void receive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("", "action is : " + action);
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra("reason");
            Log.e("", "reason is : " + reason);
            if (reason != null) {
                if (null != mOnHomeBtnPressListener) {
                    if (reason.equals("homekey")) {
                        // 短按
                        mOnHomeBtnPressListener.onHomeBtnPress();
                    } else if (reason.equals("recentapps")) {
                        // 长按
                        mOnHomeBtnPressListener.onHomeBtnLongPress();
                    }
                }
            }
        }
    }
}
