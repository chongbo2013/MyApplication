package com.tpad.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private final static String TAG = DateUtils.class.getSimpleName();

    public DateUtils() {

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
     *
     * @param date
     * @return 2014-XXXX-XXXX
     */
    public static String ConverLongToStringDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(date);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * 将已经保存的格式化日期，转换为long类型
     *
     * @param date
     * @return
     */
    public static long ConverStringDataToLong(String date) {
        long value = 0;
        //date = "2015-04-09 15:29:41";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date strtodate = formatter.parse(date);
            value = strtodate.getTime();
        } catch (Exception e) {

        }
        return value;
    }

}
