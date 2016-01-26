package com.tpad.common.model.db.ormlite;

import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by jone.sun on 2015/9/25.
 */
public interface AppOrmLiteConfigInterface {
    ConnectionSource getConnectionSource();
}
