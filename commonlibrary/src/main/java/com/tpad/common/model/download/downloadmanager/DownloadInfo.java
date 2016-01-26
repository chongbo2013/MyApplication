package com.tpad.common.model.download.downloadmanager;

import java.io.Serializable;

/**
 * 下载的信息(从DownloadManager中查询得到)
 * Created by jone.sun on 2014/10/16.
 */
public class DownloadInfo implements Serializable {
    private static final long serialVersionUID = 2L;
    private long _id;
    private int bytes_so_far; //当前下载的大小
    private String description; //描述
    private long last_modified_timestamp;
    private String local_filename; //文件存储路径
    private String local_uri;
    private String mediaprovider_uri;
    private String media_type;
    private int reason; //原因
    private int status; //状态
    private String title;
    private int total_size; //总文件大小
    private String uri;
    public DownloadInfo(){}

    public DownloadInfo(long _id, int bytes_so_far, String description,
                        long last_modified_timestamp, String local_filename,
                        String local_uri, String mediaprovider_uri,
                        String media_type, int reason, int status,
                        String title, int total_size, String uri){
        this._id = _id;
        this.bytes_so_far = bytes_so_far;
        this.description = description;

        this.last_modified_timestamp = last_modified_timestamp;
        this.local_filename = local_filename;
        this.local_uri = local_uri;
        this.mediaprovider_uri = mediaprovider_uri;
        this.media_type = media_type;
        this.reason = reason;
        this.status = status;
        this.title = title;
        this.total_size = total_size;
        this.uri = uri;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getBytes_so_far() {
        return bytes_so_far;
    }

    public void setBytes_so_far(int bytes_so_far) {
        this.bytes_so_far = bytes_so_far;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLast_modified_timestamp() {
        return last_modified_timestamp;
    }

    public void setLast_modified_timestamp(long last_modified_timestamp) {
        this.last_modified_timestamp = last_modified_timestamp;
    }

    public String getLocal_filename() {
        return local_filename;
    }

    public void setLocal_filename(String local_filename) {
        this.local_filename = local_filename;
    }

    public String getLocal_uri() {
        return local_uri;
    }

    public void setLocal_uri(String local_uri) {
        this.local_uri = local_uri;
    }

    public String getMediaprovider_uri() {
        return mediaprovider_uri;
    }

    public void setMediaprovider_uri(String mediaprovider_uri) {
        this.mediaprovider_uri = mediaprovider_uri;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal_size() {
        return total_size;
    }

    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
