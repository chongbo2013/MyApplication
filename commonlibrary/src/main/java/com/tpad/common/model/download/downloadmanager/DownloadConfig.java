package com.tpad.common.model.download.downloadmanager;

import java.io.Serializable;
import java.util.Map;

/**
 * 下载配置
 * @author jone.sun on 2015/5/7.
 */
public class DownloadConfig implements Serializable{
    private String url;
    private String savePath;
    private String saveName;
    private Boolean allowScanningByMediaScanner; //设置可被媒体扫描器找到
    private Boolean showInNotification; //是否在通知栏显示
    private String mimeType; //下载文件的mineType。因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件，所以可以利用这个属性
    private Boolean canDownloadByNetMobile; //是否允许在移动网络下下载
    private Map<String, String> headerMap; //下载请求的头信息
    
    public DownloadConfig(String url, String savePath, String saveName){
        this.url = url;
        this.savePath = savePath;
        this.saveName = saveName;
    }

    public DownloadConfig(String url, String savePath, String saveName, boolean showInNotification){
        this.url = url;
        this.savePath = savePath;
        this.saveName = saveName;
        this.showInNotification = showInNotification;
    }

    public DownloadConfig(String url, String savePath, String saveName, boolean showInNotification, boolean canDownloadByNetMobile){
        this.url = url;
        this.savePath = savePath;
        this.saveName = saveName;
        this.showInNotification = showInNotification;
        this.canDownloadByNetMobile = canDownloadByNetMobile;
    }

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

    public Boolean getAllowScanningByMediaScanner() {
        return allowScanningByMediaScanner;
    }

    public void setAllowScanningByMediaScanner(Boolean allowScanningByMediaScanner) {
        this.allowScanningByMediaScanner = allowScanningByMediaScanner;
    }

    public Boolean getShowInNotification() {
        return showInNotification;
    }

    public void setShowInNotification(Boolean showInNotification) {
        this.showInNotification = showInNotification;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Boolean getCanDownloadByNetMobile() {
        return canDownloadByNetMobile;
    }

    public void setCanDownloadByNetMobile(Boolean canDownloadByNetMobile) {
        this.canDownloadByNetMobile = canDownloadByNetMobile;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }
}
