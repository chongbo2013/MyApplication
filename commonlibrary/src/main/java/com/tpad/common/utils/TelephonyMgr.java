package com.tpad.common.utils;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public final class TelephonyMgr {

    public static boolean isDualMode(Context context) {
        boolean isDouble = true;
        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 只要在反射getSimStateGemini 这个函数时报了错就是单卡手机（这是我自己的经验，不一定全正确）
            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[] { int.class });
            // 获取SIM卡1
            result_0 = method.invoke(tm, new Object[] { new Integer(0) });
            // 获取SIM卡1
            result_1 = method.invoke(tm, new Object[] { new Integer(1) });
        } catch (Exception e) {
            isDouble = false;
        }
        return true;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        return tm.getDeviceId();
    }

    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        return tm.getSubscriberId();
    }

    public static String getSubscriberId(int cardIndex) {
        String name = null;
        name = cardIndex == 1 ? "iphonesubinfo2" : "iphonesubinfo";
        try {
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[] { String.class });
            method.setAccessible(true);
            Object param = method.invoke(null, new Object[] { name });
            if ((param == null) && (cardIndex == 1))
                param = method.invoke(null, new Object[] { "iphonesubinfo1" });
            if (param == null)
                return null;
            method = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub").getDeclaredMethod("asInterface", new Class[] { IBinder.class });
            method.setAccessible(true);
            Object stubObj = method.invoke(null, new Object[] { param });
            return (String) stubObj.getClass().getMethod("getSubscriberId", new Class[0]).invoke(stubObj, new Object[0]);
        } catch (Exception e) {
        }
        return null;
    }

    public static int getFirstSimState() {
        return getSimState("gsm.sim.state");
    }

    public static int getSecondSimState() {
        int state = getSimState("gsm.sim.state_2");
        if (state == 0) {
            return getSimState("gsm.sim.state_1");
        }
        return state;
    }

    private static int getSimState(String simState) {
        try {// Class.forName("android.os.SystemProperties").getDeclaredMethod("get",
            // new Class[] { String.class })
            Method method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[] { int.class });
            method.setAccessible(true);
            String prop = (String) method.invoke(null, new Object[] { simState });
            if (prop != null)
                prop = prop.split(",")[0];
            if ("ABSENT".equals(prop))
                return 1;
            if ("PIN_REQUIRED".equals(prop))
                return 2;
            if ("PUK_REQUIRED".equals(prop))
                return 3;
            if ("NETWORK_LOCKED".equals(prop))
                return 4;
            if ("READY".equals(prop)) {
                return 5;
            }
            return 0;
        } catch (Exception e) {
        }
        return 0;
    }

    public static boolean isCardValid(Context context, String card) {
        boolean isDouble = true;
        Method method = null;
        Object card1 = null;
        Object card2 = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[] { int.class });
            // 获取SIM卡1
            card1 = method.invoke(tm, new Object[] { new Integer(0) });
            // 获取SIM卡1
            card2 = method.invoke(tm, new Object[] { new Integer(1) });
        } catch (Exception e) {
            isDouble = false;
        }
        if (isDouble) {
            // 卡一有效
            if (card1.toString().equals("5") && !card2.toString().equals("5") && "card1".equals(card)) {
                return true;
            }
            // 卡二有效
            if (!card1.toString().equals("5") && card2.toString().equals("5") && "card2".equals(card)) {
                return true;
            }
            // 两张卡都有效
            if (card1.toString().equals("5") && card2.toString().equals("5") && "all".equals(card)) {
                return true;
            }
            // 两张卡都失效
            if (!card1.toString().equals("5") && !card2.toString().equals("5")) {
                return false;
            }
        }
        return false;
    }

    public static boolean isFirstSimValid() {
        return getFirstSimState() == 5;
    }

    public static boolean isSecondSimValid() {
        return getSecondSimState() == 5;
    }

    public static boolean isChinaMobileCard(int cardIndex ,Context context) {
        String imsi = getSubscriberId(cardIndex);
        Toast.makeText(context, "imsi : " + imsi, Toast.LENGTH_LONG).show();
        if ((!TextUtils.isEmpty(imsi)) ) {
            return ((imsi.contains("46000")) || (imsi.contains("46002")) || (imsi.contains("46007")));
        }


        return false;
    }

    public static boolean hasSIMCard(Context context){
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
            return false;
        }catch (Exception e){
            //拿不到手机状态
            return false;
        }
    }
}
