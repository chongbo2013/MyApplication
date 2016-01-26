package com.tpad.common.model.download.autodownload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author jone.sun on 2015/5/8.
 */
@DatabaseTable
public class AutoDownloadData implements Serializable{
    public static final String SAVE_TYPE_OF_APK = "saveTypeOfApk";
    public static final String SAVE_TYPE_OF_HTML = "saveTypeOfHtml";
    public static final String SAVE_TYPE_OF_IMAGE = "saveTypeOfImage";
    public static final String KEY_COLUMN_URL = "url";
    public static final String KEY_COLUMN_DOWNLOAD_TYPE = "downloadType";
    public static final String KEY_COLUMN_TIME = "time";
    public static final String KEY_COLUMN_INDEX = "index";

    @DatabaseField(id = true)
    private String url; //下载的URL

    @DatabaseField
    private String savePath; //下载到文件目录

    @DatabaseField
    private String saveName; //下载保存的文件名称

    @DatabaseField
    private Boolean isCanMobile; //是否允许在移动网络下下载

    @DatabaseField
    private String downloadType; //下载的类型

    @DatabaseField
    private int index; //记录第几个

    @DatabaseField
    private long size; //文件的大小

    @DatabaseField
    private long time; //保存数据的时间

    public AutoDownloadData(String url, String savePath, String saveName,
                            boolean isCanMobile, String downloadType, int index, long size, long time){
        this.url = url;
        this.savePath = savePath;
        this.saveName = saveName;
        this.isCanMobile = isCanMobile;
        this.downloadType = downloadType;
        this.index = index;
        this.size = size;
        this.time = time;
    }

    public AutoDownloadData(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public Boolean getIsCanMobile() {
        return isCanMobile;
    }

    public void setIsCanMobile(Boolean isCanMobile) {
        this.isCanMobile = isCanMobile;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
