package com.tpad.common.model.guidemodel;

import android.graphics.drawable.Drawable;

/**
 * Created by jiajun.jiang on 2015/3/13.
 */
public class MobileManger {
    private String name;
    private Drawable icon;
    private String linkUrl;

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setIcon(Drawable icon){
        this.icon = icon;
    }
    public Drawable getIcon(){
        return icon;
    }
    public void setLinkUrl(String linkUrl){
        this.linkUrl = linkUrl;
    }
    public String getLinkUrl(){
        return linkUrl;
    }

    public MobileManger(){

    }

    public MobileManger(String name, Drawable icon, String linkUrl){
        this.name = name;
        this.icon = icon;
        this.linkUrl = linkUrl;
    }
}
