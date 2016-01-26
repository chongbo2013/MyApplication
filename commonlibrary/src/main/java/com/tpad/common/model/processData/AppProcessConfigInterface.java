package com.tpad.common.model.processData;

import com.tpad.common.model.processData.entities.ProcessDataConfig;

/**
 * 需要application中实现(配合BaseProcessDataContentProvider的实现类使用)
 * Created by jone.sun on 2015/9/25.
 */
public interface AppProcessConfigInterface {
    ProcessDataConfig getProcessDataConfig();
}
