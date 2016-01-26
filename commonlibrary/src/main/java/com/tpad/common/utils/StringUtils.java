package com.tpad.common.utils;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * * Created by loang.chen on 2015/4/9.
 */
public class StringUtils {

    private final static String TAG = StringUtils.class.getSimpleName();
    private static StringUtils mStringUtils;

    private StringUtils() {

    }

    public static StringUtils getInstance() {
        if (mStringUtils == null) {
            mStringUtils = new StringUtils();
        }
        return mStringUtils;
    }

    /**
     * 从一串字符串中截取数字
     *
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 判断传入的字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 余额转换，取小数点后两位
     *
     * @param price
     * @return
     */

    public static String formatPrice(float price) {
        DecimalFormat fnum = new DecimalFormat("##0.000");
        String dd = fnum.format(price);
        return dd;
    }

    public static String formatPriceTwo(float price) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(price);
        return dd;
    }

    public static String formatPhoneNum(String phoneNum) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(phoneNum.substring(0, 3)).append("****").append(phoneNum.substring(7));
        return stringBuffer.toString();
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").trim();
    }


}
