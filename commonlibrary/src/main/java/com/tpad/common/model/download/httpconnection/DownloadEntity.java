package com.tpad.common.model.download.httpconnection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class DownloadEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
	private int id;

    @DatabaseField
	private String downloadUrl;

    @DatabaseField
	private int threadId;

    @DatabaseField
	private int downLength;

    public DownloadEntity(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getDownLength() {
        return downLength;
    }

    public void setDownLength(int downLength) {
        this.downLength = downLength;
    }
}
