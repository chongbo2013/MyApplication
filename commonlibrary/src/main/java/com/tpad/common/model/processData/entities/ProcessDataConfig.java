package com.tpad.common.model.processData.entities;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by jone.sun on 2015/9/25.
 */
public class ProcessDataConfig implements Serializable {
    private String string_sp_uri, string_db_uri;
    private Uri sp_uri, db_uri;

    public ProcessDataConfig(String string_sp_uri, String string_db_uri){
        this.string_sp_uri = string_sp_uri;
        this.string_db_uri = string_db_uri;
        sp_uri = Uri.parse(string_sp_uri + "1");
        db_uri = Uri.parse(string_db_uri + "/1");
    }

    public String getString_sp_uri() {
        return string_sp_uri;
    }

    public String getString_db_uri() {
        return string_db_uri;
    }

    public Uri getSp_uri() {
        return sp_uri;
    }

    public Uri getDb_uri() {
        return db_uri;
    }
}
