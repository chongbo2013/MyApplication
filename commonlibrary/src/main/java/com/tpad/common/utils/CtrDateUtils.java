package com.tpad.common.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CtrDateUtils {

    private final static String TAG = CtrDateUtils.class.getSimpleName();

    public CtrDateUtils() {

    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        int result = 0;
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        result = (int) ((toCalendar.getTime().getTime() - fromCalendar
                .getTime().getTime()) / (1000 * 60 * 60 * 24));
            Log.e(TAG, "日期间隔 is： " + result + " 天");
        return result;
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    public static int getGapHourCount(Date startDate, Date endDate) {
        int result = 0;
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        result = (int) ((toCalendar.getTime().getTime() - fromCalendar
                .getTime().getTime()) / (1000 * 60 * 60));
        Log.e(TAG, "日期间隔小时数 is： " + result + " 小时");
        return result;
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    public static int getGapHaoMiaoCount(Date startDate, Date endDate) {
        int result = 0;
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        result = (int) ((toCalendar.getTime().getTime() - fromCalendar
                .getTime().getTime()));
        Log.e(TAG, "日期间隔毫秒数 is： " + result + " 毫秒");
        return result;
    }


    // 把字符串转为日期
    public static Date ConverStringToDate(String strDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 把字符串转为日期
    public static Date ConverHourStringToDate(String strDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static String ConverLongDateToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(date * 1000);
        String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    public static String ConverLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(date);
        String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    /**
     * 将获取到的当前秒数转换为年-月-日
     * @param date
     * @return 2014-XXXX-XXXX
     */
    public static String ConverLongToStringDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(date);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

}
