package com.tpad.common.application;

import com.tpad.common.model.db.ormlite.AppOrmLiteConfigInterface;
import com.tpad.common.model.processData.AppProcessConfigInterface;
import com.tpad.common.model.processData.ProcessDBDataOperator;
import com.tpad.common.model.processData.ProcessDataOperator;
import com.tpad.common.model.processData.ProcessSpDataOperator;

/**
 * 包含跨进程读写的Application
 * @author jone.sun on 2015/3/24.
 */
public abstract class CommonWithProcessDataApp extends CommonApp implements AppOrmLiteConfigInterface, AppProcessConfigInterface {
    private static CommonWithProcessDataApp instance;
    private static ProcessDataOperator processDataDBOperator, processDataSPOperator;
    public static CommonWithProcessDataApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        processDataDBOperator = ProcessDBDataOperator.getInstance(this);
        processDataSPOperator = ProcessSpDataOperator.getInstance(this);
    }

    public static ProcessDataOperator getProcessDataDBOperator() {
        return processDataDBOperator;
    }

    public static ProcessDataOperator getProcessDataSPOperator() {
        return processDataSPOperator;
    }
}
