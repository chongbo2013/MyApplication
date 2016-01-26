package com.tpad.common.model.guidemodel;

/**
 * Created by jiajun.jiang on 2015/4/17.
 */
public interface GuideModelSave {

    public final static String SHARED_DEFAULT_HOME = "default_home";
    public final static String SHARED_DEFAULT_HOME_PKG = "default_home_pkg";
    public final static String SHARED_DEFAULT_HOME_CLS = "default_home_cls";

    public int getDefaultHome();

    public void setDefaultHome(int defaultHome);

    public String getDefaultHomePKG();

    public void setDefaultHomePKG(String pkg);

    public String getDefaultHomeClass();

    public void setDefaultHomeClass(String homeClass);

}
