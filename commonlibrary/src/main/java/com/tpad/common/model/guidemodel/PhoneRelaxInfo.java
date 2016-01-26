package com.tpad.common.model.guidemodel;

import android.os.Build;

/**
 * Created by jiajun.jiang on 2015/4/17.
 */
public class PhoneRelaxInfo {
    // 手机品牌机型类别
    public final static String PHONE_CURR_BRAND_MODEL_XIAOMI = "小米";//小米
    public final static String PHONE_CURR_BRAND_MODEL_HUAWEI = "华为";//华为
    public final static String PHONE_CURR_BRAND_MODEL_MEIZU = "魅族";//魅族
    public final static String PHONE_CURR_BRAND_MODEL_JINLI = "JINLI";//金立
    public final static String PHONE_CURR_BRAND_MODEL_OPPO_X9007 = "OPPO_X9007";//oppo
    public final static String PHONE_CURR_BRAND_MODEL_OPPO_U750T = "OPPO_U750T";//oppo
    public final static String PHONE_CURR_BRAND_MODEL_OPPO = "OPPO";
    public final static String PHONE_CURR_BRAND_MODEL_LENOVO = "联想";
    public final static String PHONE_CURR_BRAND_MODEL_BBK = "步步高";

    public final static String MANGER_QIHOO = "com.qihoo360.mobilesafe";    //360手机卫士
    public final static String MANGER_LBE = "com.lbe.security";             //LBE安全大师
    public final static String MANGER_TENCENT = "com.tencent.qqpimsecure";      //手机管家
    public final static String MANGER_HAIZUO = "net.hidroid.himanager";        // 海卓手机管家
    public final static String MANGER_LIEBAO = "com.cleanmaster.mguard_cn";    //猎豹清理大师
    public final static String MANGER_BAIDU = "cn.opda.a.phonoalbumshoushou"; //百度手机卫士
    public final static String MANGER_JINSHAN = "com.ijinshan.mguard";          //金山手机卫士

    public final static String[] ALREADY_INSTALL_MOBILE_MANGER = {
            "com.qihoo360.mobilesafe",      //360手机卫士
            "com.lbe.security",             //LBE安全大师
            "com.tencent.qqpimsecure",      //手机管家
            "net.hidroid.himanager",        // 海卓手机管家
            "com.cleanmaster.mguard_cn",    //猎豹清理大师
            "cn.opda.a.phonoalbumshoushou", //百度手机卫士
            "com.ijinshan.mguard",          //金山手机卫士
    };

    /**
     * 获取当前手机的品牌机型
     *
     * @return
     */
    public String getCurrPhoneBrandModel() {
        String curr_brand = "";
        String curr_manufacture = "";
        String curr_model = "";
        String curr_device = "";
        String model_result = "";

        curr_brand = Build.BRAND;
        curr_manufacture = Build.MANUFACTURER;
        curr_model = Build.MODEL;
        curr_device = Build.DEVICE;
        if (curr_brand.contains("Xiaomi") || curr_manufacture.contains("Xiaomi")) {
            // 当前手机是小米
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_XIAOMI;
        } else if (curr_brand.contains("Huawei") || curr_manufacture.contains("HUAWEI")) {
            // 当前手机是华为
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_HUAWEI;
        } else if (curr_brand.contains("Meizu") || curr_manufacture.contains("Meizu")) {
            // 当前手机是魅族
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_MEIZU;
        } else if (curr_brand.contains("GiONEE") || curr_manufacture.contains("GiONEE")) {
            // 当前手机是金立
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_JINLI;
        } else if (curr_brand.equals("OPPO")) {
            //当前是oppo
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO;
        } else if (curr_brand.equals("OPPO") && curr_model.equals("U705T")) {
            // 当前手机是金立
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_U750T;
        } else if (curr_brand.equals("OPPO") && curr_model.equals("X9007")) {
            // 当前手机是金立
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_OPPO_X9007;
        } else if (curr_brand.contains("Lenovo") || curr_manufacture.contains("Lenovo")) {
            // 当前手机是华为
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_LENOVO;
        } else if (curr_brand.contains("BBK") || curr_manufacture.contains("BBK")) {
            // 当前手机是华为
            model_result = PhoneRelaxInfo.PHONE_CURR_BRAND_MODEL_BBK;
        }
        return model_result;
    }

    public static boolean isFlymeOS3x(){
        String display = Build.DISPLAY;
        display = display.replace("_", " ");
        if(display.contains("Flyme OS 3.")){
            return true;
        }
        return false;
    }

    public static boolean isFlymeOS4x(){
        String display = Build.DISPLAY;
        display = display.replace("_", " ");
        if(display.contains("Flyme OS 4.")){
            return true;
        }
        return false;
    }
}
