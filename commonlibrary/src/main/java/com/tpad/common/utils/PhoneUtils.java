package com.tpad.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by jiajun.jiang on 2015/3/27.
 */
public class PhoneUtils {
    private Context context;
    private final static String TAG = "PhoneUtils";
    private static PhoneUtils mPhoneUtils;
    private DisplayMetrics displayMetrics;

    private PhoneUtils(Context c) {
        this.context = c;
    }

    /**
     * Singleton function interface
     *
     * @param c
     * @return
     */
    public static PhoneUtils getInstance(Context c) {
        if (mPhoneUtils == null) {
            mPhoneUtils = new PhoneUtils(c);
        }
        return mPhoneUtils;
    }

    /**
     * Get Phone Screen Info
     *
     * @return (ScreenHeight ScreenWidth)
     */
    public DisplayMetrics getPhoneScreen() {
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
            WindowManager WM = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            WM.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

    public int getWidthPixels() {
        return getPhoneScreen().widthPixels;
    }

    public int getDensityDpi() {
        return getPhoneScreen().densityDpi;
    }

    public int get720WScale(int w) {
        return (int) (w * getWScale(720));
    }

    public int get480WScale(int w) {
        return (int) (w * getWScale(480));
    }


    public int get720WScale(float w) {
        return (int) (w * getWScale(720));
    }


    public float getWScale(int w) {
        int widthPhone;
        float mscale_width = 0;
        widthPhone = getPhoneScreen().widthPixels;
        mscale_width = widthPhone * 1.0f / w;
        return mscale_width;
    }

    public float getHScale(int h) {
        int widthPhone;
        float mscale_height = 0;
        widthPhone = getPhoneScreen().heightPixels;
        mscale_height = widthPhone * 1.0f / h;
        return mscale_height;
    }

    public int px2sp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getScaleTextSize(int size) {
        return px2sp(get720WScale(size));
    }

    /**
     * is Exists App By PkgName
     *
     * @param pkgName
     * @return true or false
     */
    public boolean isExistsAppByPkgName(String pkgName) {
        boolean result = false;
        if (pkgName == null)
            return result;
        try {
            ApplicationInfo api = context.getPackageManager()
                    .getApplicationInfo(pkgName, 0);
            result = true;
        } catch (PackageManager.NameNotFoundException e) {
            return result;
        }
        return result;
    }

    public static boolean judgeVersionless(String src, String dst) {
        if (src == null || src.equals(""))
            return false;
        String v_net[] = src.split("\\.");
        String v_local[] = dst.split("\\.");
        try {

            int version_net = 0;
            int version_local = 0;

            if (v_net.length == 1) {
                version_net = Integer.parseInt(v_net[0]) * 1000;
            } else if (v_net.length == 2) {
                version_net = Integer.parseInt(v_net[0]) * 1000
                        + Integer.parseInt(v_net[1]) * 100;
            } else if (v_net.length == 3) {
                version_net = Integer.parseInt(v_net[0]) * 1000
                        + Integer.parseInt(v_net[1]) * 100 + Integer.parseInt(v_net[2]) * 10;
            } else if (v_net.length >= 4) {
                version_net = Integer.parseInt(v_net[0]) * 1000
                        + Integer.parseInt(v_net[1]) * 100 + Integer.parseInt(v_net[2]) * 10 + Integer.parseInt(v_net[3]);
            }

            if (v_local.length == 1) {
                version_local = Integer.parseInt(v_local[0]) * 1000;
            } else if (v_local.length == 2) {
                version_local = Integer.parseInt(v_local[0]) * 1000
                        + Integer.parseInt(v_local[1]) * 100;
            } else if (v_local.length == 3) {
                version_local = Integer.parseInt(v_local[0]) * 1000
                        + Integer.parseInt(v_local[1]) * 100 + Integer.parseInt(v_local[2]) * 10;
            } else if (v_local.length >= 4) {
                version_local = Integer.parseInt(v_local[0]) * 1000
                        + Integer.parseInt(v_local[1]) * 100 + Integer.parseInt(v_local[2]) * 10 + Integer.parseInt(v_local[3]);
            }
            if (version_net > version_local)
                return true;
            else
                return false;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean judgeVersion(String src, String dst) {
        if (src == null || src.equals(""))
            return false;
        String v_src[] = src.split("\\.");
        String v_dst[] = dst.split("\\.");
        try {
            int version_src = 0;
            int version_dst = 0;
            switch (v_src.length) {
                case 1:
                    version_src = Integer.parseInt(v_src[0]) * 1000;
                    break;
                case 2:
                    version_src = Integer.parseInt(v_src[0]) * 1000
                            + Integer.parseInt(v_src[1]) * 100;
                    break;
                case 3:
                    version_src = Integer.parseInt(v_src[0]) * 1000
                            + Integer.parseInt(v_src[1]) * 100 + Integer.parseInt(v_src[2]) * 10;
                    break;
                case 4:
                default:
                    version_src = Integer.parseInt(v_src[0]) * 1000
                            + Integer.parseInt(v_src[1]) * 100 + Integer.parseInt(v_src[2]) * 10 + Integer.parseInt(v_src[3]);
                    break;

            }

            switch (v_dst.length) {
                case 1:
                    version_dst = Integer.parseInt(v_dst[0]) * 1000;
                    break;
                case 2:
                    version_dst = Integer.parseInt(v_dst[0]) * 1000
                            + Integer.parseInt(v_dst[1]) * 100;
                    break;
                case 3:
                    version_dst = Integer.parseInt(v_dst[0]) * 1000
                            + Integer.parseInt(v_dst[1]) * 100 + Integer.parseInt(v_dst[2]) * 10;
                    break;
                case 4:
                default:
                    version_dst = Integer.parseInt(v_dst[0]) * 1000
                            + Integer.parseInt(v_dst[1]) * 100 + Integer.parseInt(v_dst[2]) * 10 + Integer.parseInt(v_dst[3]);
                    break;
            }
            if (version_src >= version_dst)
                return true;
            else
                return false;
        } catch (Exception e) {
        }
        return false;
    }

    public String getUsetrAgent(String appname) {
        WebView webview = new WebView(context);
        WebSettings set = webview.getSettings();
        String aa = set.getUserAgentString() + "-" + appname;
        return aa;
    }


}
