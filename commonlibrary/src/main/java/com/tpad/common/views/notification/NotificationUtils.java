package com.tpad.common.views.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tpad.common.model.processData.ProcessSpDataOperator;

/*
 * Created by loang on 2015.05.12
 */
public class NotificationUtils {

    private Context context;
    private final static String TAG = NotificationUtils.class.getSimpleName();
    private android.app.NotificationManager notificationManager;
    private Notification notification;
    private PendingIntent contentIntent;
    private int icon = -1;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    public NotificationUtils(Context context, int icon) {
        this.context = context;
        this.icon = icon;
    }

    public void showNotification(NotiType showType, String title, String info, NotiCallback callback) {
        initNotification(callback);
        notification = new Notification();
        if (icon == -1) {
            notification.icon = android.R.drawable.ic_notification_overlay;
        } else {
            notification.icon = icon;
        }
        notification.defaults = Notification.DEFAULT_SOUND;// 采用默认声音
        Log.e(TAG, "Build.VERSION.SDK_INT is :" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 16) {
            notification = new Notification.Builder(context)
                    .setAutoCancel(false)
                    .setSmallIcon(notification.icon).setTicker(info)
                    .setContentTitle(title).setContentText(info)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent).build();
            if (showType == NotiType.TYPE_SHOW_L) {
                notification.flags |= Notification.FLAG_ONGOING_EVENT;
            } else {
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
            }
        } else {
            if (showType == NotiType.TYPE_SHOW_L) {
                notification.flags |= Notification.FLAG_ONGOING_EVENT;
            } else {
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
            }
            notification.setLatestEventInfo(context, title, info,
                    contentIntent);
        }

        try {
            notification.contentIntent = contentIntent;
            if (notificationManager == null) {
                notificationManager = (android.app.NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (showType == NotiType.TYPE_SHOW_L) {
                notificationManager.notify(0, notification);
            } else if (showType == NotiType.TYPE_SHOW_S_FIXED_UPDATE_VERSION) {
                notificationManager.cancel(0);
                notificationManager.notify(0, notification);
            } else if (showType == NotiType.TYPE_SHOW_S_FIXED) {
                notificationManager.notify(0, notification);
            } else {
                int showIndex = ProcessSpDataOperator.getInstance(context).getValueByKey("noti_showIndex", 0);
                notificationManager.notify(showIndex, notification);
                showIndex++;
                Log.e(TAG, "showIndex is : " + showIndex);
                ProcessSpDataOperator.getInstance(context).putValue("noti_showIndex", showIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void initNotification(NotiCallback callback) {
        if (notificationManager == null) {
            notificationManager = (android.app.NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        contentIntent = null;
        if (contentIntent == null) {
            if (callback != null && callback.OnClickCallBack() != null) {
                Intent appIntent = callback.OnClickCallBack();
                appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                contentIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
            } else {
                contentIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
            }
        }
    }

}
