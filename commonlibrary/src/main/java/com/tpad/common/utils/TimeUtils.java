package com.tpad.common.utils;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author jone.sun on 2015/4/7.
 */
public class TimeUtils {
    /**
     * 转换日期格式为yyyy-MM-dd
     */
    public static String getChangeDateFormat(Date date) {
        String str = null;
        if (date != null && !"".equals(date)) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            str = sd.format(date);
        }
        return str;
    }


    /**
     * 转换日期格式为yyyy-MM-dd HH:mm
     */
    public static String getChangeDateFormat(long data) {
        String str = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd");
        str = sd.format(data);
        return str;
    }

    /**
     * 转换日期格式为HH:mm
     */
    public static String getChangeTimeFormat(Date date) {
        String str = null;
        if (date != null && !"".equals(date)) {
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
            str = sd.format(date);
        }
        return str;
    }

    /**
     * 获取当前为星期几
     *
     * @param date
     * @return
     */
    public static String getChangeWeekFormat(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.DAY_OF_WEEK);
        String str = "" + hour;
        if ("1".equals(str)) {
            str = "星期日";
        } else if ("2".equals(str)) {
            str = "星期一";
        } else if ("3".equals(str)) {
            str = "星期二";
        } else if ("4".equals(str)) {
            str = "星期三";
        } else if ("5".equals(str)) {
            str = "星期四";
        } else if ("6".equals(str)) {
            str = "星期五";
        } else if ("7".equals(str)) {
            str = "星期六";
        }
        return str;
    }


    /**
     * 获取当前的小时数
     *
     * @return
     */
    public static int getCurrHour() {
        Time t = new Time();
        t.setToNow();
        int hour = t.hour; // 0-23
        // if (hour == 0)
        // hour = 24;
        return hour;
    }

    public static int getCurrYear() {
        Time t = new Time();
        t.setToNow();
        int year = t.year;
        return year;
    }

    /**
     * 获取当前的分钟数
     *
     * @return min
     */
    public static int getCurrMin() {
        Calendar rightNow = Calendar.getInstance();
        int min = rightNow.get(Calendar.MINUTE);
        return min;
    }

    /**
     * 获取当前的秒数
     *
     * @return sec
     */
    public static int getCurrSec() {
        Calendar rightNow = Calendar.getInstance();
        int sec = rightNow.get(Calendar.SECOND);
        return sec;
    }

    /**
     * 传入当前的分钟数，加上当前的秒数获取总共的秒数之和
     *
     * @return total sec
     */
    public static int getCurrTotalSec() {
        int total = 0;
        Calendar rightNow = Calendar.getInstance();
        int sec = rightNow.get(Calendar.SECOND);
        int min = rightNow.get(Calendar.MINUTE);
        total += (min * 60) + sec;
        return total;
    }

    /**
     * 判断当前时间是否大于预定时间
     *
     * @param hour
     * @param min
     * @return
     */
    public static boolean checkCurrTimeIsGreaterThanScheduledTime(int hour,
                                                                  int min) {
        boolean result = false;
        int currHour = getCurrHour();
        // 当前小时大于预定小时数的时候，直接返回true
        if (currHour >= hour) {
            return true;
        }
        int currMin = getCurrMin();
        // 当前小时等于预定小时数的时候，再次判断当前的分钟数是否满足
        if (currHour == hour && currMin >= min) {
            return true;
        }
        return result;
    }

    /**
     * 判斷当前的时间是否小于预定的时间
     *
     * @param hour
     * @param min
     * @return
     */
    public static boolean checkCurrTimeIsLessThanScheduledTime(int hour, int min) {
        boolean result = false;
        int currHour = getCurrHour();
        // 当前小时小于预定小时数的时候，直接返回true
        if (currHour < hour) {
            return true;
        }
        int currMin = getCurrMin();
        // 当前小时等于预定小时数的时候，再次判断当前的分钟数是否满足
        if (currHour == hour && currMin <= min) {
            return true;
        }
        return result;
    }

    /**
     * 判断当前的时间是否属于预定时间段之内
     *
     * @param srcHour
     * @param SrcMin
     * @param DstHour
     * @param DstMin
     * @return
     */
    public static boolean checkCurrTimeIsGreaterThanScheduledTime(int srcHour,
                                                                  int SrcMin, int DstHour, int DstMin) {
        boolean result = false;
        int currHour = getCurrHour();
        // 当前小时大于预定结束小时数的时候，直接返回true
        if (currHour > DstHour) {
            return result;
        }
        // 当大于结束时间点之后直接返回true
        if (checkCurrTimeIsGreaterThanScheduledTime(DstHour, DstMin)) {
            return result;
        }
        // 当前小时数在预定时间段内，则执行以下操作
        if (checkCurrTimeIsGreaterThanScheduledTime(srcHour, SrcMin)
                && checkCurrTimeIsLessThanScheduledTime(DstHour, DstMin)) {
            return true;
        }
        return result;
    }

    public static String getStamdardTime() {
        String result = "";
//        try {
//            URL url = new URL("http://nist.time.gov/timezone.cgi?UTC/s/0");// 取得资源对象
//            URLConnection uc = url.openConnection();// 生成连接对象
//            uc.connect(); // 发出连接
//            long ld = uc.getDate(); // 取得网站日期时间
//            result = CtrDateUtils.ConverLongToString(ld);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    /**
     * 判断当前秒数的奇偶性 奇数为1，偶数为0，与1相与，结果不变
     *
     * @param sec
     * @return true(奇数)false(偶数)
     */
    public static boolean isCurrSecParity(int sec) {
        return (sec & 1) != 0;
    }

    /**
     * 获取当前时刻的日期  2014--XXXX-XXXX
     *
     * @return Curr Date
     */
    public static String getSysCurrDate() {
        // String str="2011-11-11";
        Calendar rightNow = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        sb.append(year).append("-").append(month).append("-").append(day);
        return sb.toString();
    }

    /**
     * 获取当前时刻的日期  2014--XXXX-XXXX
     *
     * @return Curr Date
     */
    public static int getSysCurrDay() {
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取当前时刻的日期  2014--XXXX-XXXX
     *
     * @return Curr Date
     */
    public static int getSysCurrMonth() {
        // String str="2011-11-11";
        Calendar rightNow = Calendar.getInstance();
        int month = rightNow.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取当前日期
     *
     * @return sample 2014-XXXX-XXXX
     */
    public static String getDateForCurrent() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new java.sql.Date(System.currentTimeMillis()));
        return date;
    }

    public static String getCurrTime() {
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        String date1 = format1.format(new java.util.Date(System.currentTimeMillis()));
        return date1;
    }

    public static String getDateForCurrent(String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String date = format.format(new java.sql.Date(System.currentTimeMillis()));
        return date;
    }

    /**
     * 获取指定日期间隔之后的时间（后一天）
     *
     * @param i
     * @return
     */
    public static String getTheDayAfterDay(int i) {
        // String str="2011/11/11";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar rightNow = Calendar.getInstance();
        int dayo = rightNow.get(Calendar.DAY_OF_MONTH);
        rightNow.set(Calendar.DAY_OF_MONTH, dayo + i);
        return (format.format(rightNow.getTime()));
    }

    /**
     * 获取指定日期间隔之后的时间（前一天）
     *
     * @param i
     * @return
     */
    public static String getTheDayBeforeDay(int i) {
        // String str="2011/11/11";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        int dayo = rightNow.get(Calendar.DAY_OF_MONTH);
        rightNow.set(Calendar.DAY_OF_MONTH, dayo - i);
        return (format.format(rightNow.getTime()));
    }

    /**
     * 判断日期是否发生变化
     *
     * @param src_time  首次记录时间
     * @param dst_time  当前时间
     * @param splitflag 时间记录时候才用的格式化分隔符
     * @return
     */
    public static boolean judgeIfDateChange(String src_time, String dst_time,
                                            String splitflag) {
        boolean result = false;
        if (src_time != null && !src_time.equals("") && dst_time != null
                && !dst_time.equals("")) {
            if ((!src_time.split(splitflag)[0]
                    .equals(dst_time.split(splitflag)[0]))
                    || (!src_time.split(splitflag)[1].equals(dst_time
                    .split(splitflag)[1]))
                    || (!src_time.split(splitflag)[2].equals(dst_time
                    .split(splitflag)[2]))) {
                result = true;
            }
        } else {
            // TODO:NOTHING
            result = true;
        }
        return result;
    }

    /**
     * 获取指定日期为第几周
     *
     * @param year
     * @param month
     * @param Day
     * @return
     */
    public static String getFirstDayOfWeekOrder(int year, int month, int Day) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, Day);

        //获取当前时间为本月的第几周 
        int week = c.get(Calendar.WEEK_OF_MONTH);
        //获取当前时间为本周的第几天 
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
            week = week - 1;
        } else {
            day = day - 1;
        }
        String aa = "今天是" + year + "年" + ",第" + week + "周" + ",星期" + (day);
        return aa;
    }

    /**
     * 根据传入的年、月、第几周，获取本周的第几天的日期
     *
     * @param year         年份
     * @param month        月份
     * @param week         第几周
     * @param showFirstDay
     * @return
     */
    public static String getDateOfWeekOrder(int year, int month, int week, int showFirstDay) {
        Calendar calendar = Calendar.getInstance(); //获取日历
        calendar.set(Calendar.WEEK_OF_MONTH, week);// 设定为本月第几周
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //将日历翻到这周的周一
        switch (showFirstDay) {
            case 1:
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                break;
            case 2:
                calendar.setFirstDayOfWeek(Calendar.TUESDAY);
                break;
            case 3:
                calendar.setFirstDayOfWeek(Calendar.WEDNESDAY);
                break;
            case 4:
                calendar.setFirstDayOfWeek(Calendar.THURSDAY);
                break;
            case 5:
                calendar.setFirstDayOfWeek(Calendar.FRIDAY);
                break;
            case 6:
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                break;
            case 7:
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                break;
            default:
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                break;

        }
        java.util.Date aa = calendar.getTime();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd");//24小时制
        String LgTime = sdformat.format(aa);
        return LgTime;
    }


    public static String getWeekAndDay() {
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为本月的第几周 
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //获取当前时间为本周的第几天 
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
            week = week - 1;
        } else {
            day = day - 1;
        }
        String aa = "今天是本月的第" + week + "周" + ",星期" + (day);
        System.out.println(aa);
        return aa;
    }

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;
    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }
    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }


}
