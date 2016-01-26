package com.tpad.common.model.guidemodel;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import java.util.List;

/**
 * Created by jiajun.jiang on 2015/4/17.
 */
public class LockMethod {

    private Context context;
    private GuideModelSave guideModelSave;
    private String pkg;
    private String cls;
    private String PKGSelf;

    public LockMethod(Context context, GuideModelSave guideModelSave, String PKGSelf){
        this.context = context;
        this.guideModelSave = guideModelSave;
        this.PKGSelf = PKGSelf;
    }

    public boolean handleLockHomeKey() {
        Intent intent = ((Activity)context).getIntent();
        String act = intent != null ? intent.getAction() == null ? "" : intent
                .getAction() : "";
        if (act.equals(Intent.ACTION_MAIN)) {
            pkg = guideModelSave.getDefaultHomePKG();
            cls = guideModelSave.getDefaultHomeClass();
            if (pkg == null || pkg.equals("")) {
                PackageManager localPackageManager = context.getPackageManager();
                Intent localIntent1 = new Intent(Intent.ACTION_MAIN);
                localIntent1.addCategory(Intent.CATEGORY_HOME);
                localIntent1.addCategory(Intent.CATEGORY_DEFAULT);
                List localList = localPackageManager.queryIntentActivities(
                        localIntent1, PackageManager.PERMISSION_GRANTED);
                for (int j = 0; j < localList.size(); j++) {
                    ResolveInfo localResolveInfo = (ResolveInfo) localList
                            .get(j);
                    if ((localResolveInfo != null)
                            && (localResolveInfo.activityInfo != null)
                            && (localResolveInfo.activityInfo.packageName != null)
                            && (!localResolveInfo.activityInfo.packageName
                            .equals(PKGSelf))) {
                        pkg = localResolveInfo.activityInfo.packageName;
                        cls = localResolveInfo.activityInfo.name;
                        guideModelSave.setDefaultHome(1);
                        guideModelSave.setDefaultHomePKG(pkg);
                        guideModelSave.setDefaultHomeClass(cls);
                        break;
                    }
                }
            }
            try {
                List localList3 = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                        .getRunningTasks(2);
                String topActivity = ((ActivityManager.RunningTaskInfo) localList3
                        .get(1)).topActivity.getClassName();
                String topActivitypkg = ((ActivityManager.RunningTaskInfo) localList3
                        .get(1)).topActivity.getPackageName();
                if (topActivitypkg.equals(pkg)
                        || topActivity.equals("com.tpadsz.lockview.UXLock")) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!pkg.equals("")) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                i.setComponent(new ComponentName(pkg, cls));
                try {
                    context.startActivity(i);
                } catch (Exception e) {
                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            } else {
                context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                // 启动应用列表显示 选择启动应用
            }
            return false;
        } else {
            return true;
        }
    }
}
