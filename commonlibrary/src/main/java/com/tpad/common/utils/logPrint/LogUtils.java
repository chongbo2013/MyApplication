package com.tpad.common.utils.logPrint;

import android.content.Context;
import android.os.Environment;

import com.tpad.common.utils.FileUtils;
import com.tpad.common.utils.TimeUtils;
import com.tpad.common.utils.phonebaseinfo.PhoneBaseInfoUtils;

import java.io.File;

public class LogUtils {

    public final static String SP_KEY_LOCAL_LOG_SWITCH = "sp_key_local_log_switch";
    private final static String TAG = LogUtils.class.getSimpleName();
    private static boolean logSwitch = true;
    private static boolean SpeclogSwitch = false;
    private static LogUtils mLogUtils;
    public final static String FILE_SPEC_LOG = "log_bl_";
    public final static String FILE_LOCAL_LOG = "log_bosslocker_";
    public final static String FILE_TAG_LOCK = "log_file_lock";// 锁屏端
    public final static String FILE_TAG_CLIENT = "log_file_client";// 客户端
    public final static String FILE_TAG_MODEL_TASK = "log_file_model_task";// 任务模块
    public final static String FILE_TAG_LOGIN = "log_file_login";// 登录模块
    private String FILE_SUFFIX = ".txt";
    private Context mContext;


    public LogUtils(Context c) {
        this.mContext = c;
    }

    public static LogUtils getInstance(Context c) {
        if (mLogUtils == null) {
            mLogUtils = new LogUtils(c);
        }
        return mLogUtils;
    }

    /**
     * 日志保存文件
     *
     * @param filename
     * @param tag
     * @param s_title
     * @param type
     * @param desc
     */
    public void printf(String filename, String tag, String s_title, LogType type, String... desc) {
        if (logSwitch) {
            try {
                String pathPrefix = Environment
                        .getExternalStorageDirectory().toString() + "/TpadLog/";
                File files = new File(pathPrefix);
                files.mkdirs();
                String path = pathPrefix + FILE_LOCAL_LOG + TimeUtils.getDateForCurrent() + FILE_SUFFIX;
                File file = new File(path);
                StringBuffer sb = new StringBuffer();
                String logType = "调试";
                for (String str : desc) {
                    sb.append(str).append("  ");
                }
                if (LogType.ERROR.equals(type)) {
                    logType = "错误";
                } else if (LogType.INFO.equals(type)) {
                    logType = "正常";
                } else if (LogType.WARING.equals(type)) {
                    logType = "警告";
                }
                String savestr = filename + "\t" + logType + "\t" +
                        PhoneBaseInfoUtils.getInstance(mContext).getPhoneAppVersion(mContext.getPackageName()) + "\t" +
                        TimeUtils.getDateForCurrent() + "\t" +
                        TimeUtils.getCurrTime() + "\t" +
                        tag + "\t" +
                        s_title + "\t" +
                        sb.toString() + "\n";
                if (file.exists()) {
                    // 如果文件存在，则追加内容到已经存在的文件
                    FileUtils.WriteStringAppendToFileMethod1(path, "\n" + new String(savestr.getBytes(), "UTF-8"));
                } else {
                    // 如果文件不存在，则创建新的文件
                    FileUtils.saveStrToFile(path, "模块名称\t类别\t版本号\t日期\t时间\t类名\t概要\t描述" + "\n" + savestr, "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //TTApplication.showToast("日志保存失败！！！！" + tag + "\n" + s_title);
            }
        }
    }

    public void printfSpec(String tag, String s_title, String... desc) {
        if (SpeclogSwitch) {
            try {
                String filename = FILE_TAG_CLIENT;
                String pathPrefix = Environment
                        .getExternalStorageDirectory().toString() + "/";
                File files = new File(pathPrefix);
                files.mkdirs();
                String path = pathPrefix + FILE_SPEC_LOG + TimeUtils.getDateForCurrent() + FILE_SUFFIX;
                File file = new File(path);
                StringBuffer sb = new StringBuffer();
                String logType = "调试";
                for (String str : desc) {
                    sb.append(str).append("  ");
                }

                String savestr = filename + "\t" + logType + "\t" +
                        PhoneBaseInfoUtils.getInstance(mContext).getPhoneAppVersion(mContext.getPackageName()) + "\t" +
                        TimeUtils.getDateForCurrent() + "\t" +
                        TimeUtils.getCurrTime() + "\t" +
                        tag + "\t" +
                        s_title + "\t" +
                        sb.toString() + "\n";
                if (file.exists()) {
                    // 如果文件存在，则追加内容到已经存在的文件
                    FileUtils.WriteStringAppendToFileMethod1(path, "\n" + new String(savestr.getBytes(), "UTF-8"));
                } else {
                    // 如果文件不存在，则创建新的文件
                    FileUtils.saveStrToFile(path, "模块名称\t类别\t版本号\t日期\t时间\t类名\t概要\t描述" + "\n" + savestr, "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //TTApplication.showToast("日志保存失败！！！！" + tag + "\n" + s_title);
            }
        }
    }

    /**
     * 删除本地日志文件信息
     */
    public void DelLogFiles() {

    }

    public static boolean isLogSwitch() {
        return logSwitch;
    }

    public static void setLogSwitch(boolean logSwitch) {
        LogUtils.logSwitch = logSwitch;
    }

}
