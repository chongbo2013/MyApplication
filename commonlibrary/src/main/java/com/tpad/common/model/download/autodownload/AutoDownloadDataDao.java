package com.tpad.common.model.download.autodownload;

import android.content.Context;
import android.util.Log;

import com.tpad.common.model.db.ormlite.OrmLiteBaseDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jone.sun on 2015/5/8.
 */
public class AutoDownloadDataDao extends OrmLiteBaseDao<AutoDownloadData> {
    private static final String TAG = AutoDownloadDataDao.class.getSimpleName();
    private static AutoDownloadDataDao instance;
    public static AutoDownloadDataDao getInstance(Context context){
        if(instance == null){
            instance = new AutoDownloadDataDao(context);
        }
        return instance;
    }

    private AutoDownloadDataDao(Context context) {
        super(context);
    }

    public AutoDownloadData getAutoDownloadDataByUrl(String url) {
        return super.queryByColumn(AutoDownloadData.KEY_COLUMN_URL, url);
    }

    public List<AutoDownloadData> getAutoDownloadDatasByDownloadType(String downloadType){
        List<AutoDownloadData> autoDownloadDataList = new ArrayList<>();
        try {
            autoDownloadDataList = getDao().queryBuilder().orderBy(AutoDownloadData.KEY_COLUMN_INDEX, true)
                    .where().eq(AutoDownloadData.KEY_COLUMN_DOWNLOAD_TYPE, downloadType).query();
        } catch (SQLException e) {
            Log.e(TAG, "getAutoDownloadDatasByDownloadType", e);
        }
        return autoDownloadDataList;
    }

    public void save(AutoDownloadData autoDownloadData) {
        super.createOrUpdate(autoDownloadData);
    }

    public void saveList(List<AutoDownloadData> autoDownloadDataList){
        if(autoDownloadDataList != null && autoDownloadDataList.size() > 0){
            for(AutoDownloadData autoDownloadData : autoDownloadDataList){
                save(autoDownloadData);
            }
        }
    }

    public int delete(AutoDownloadData autoDownloadData) {
        return super.delete(autoDownloadData);
    }

    public int deleteAll(){
        return super.deleteAll();
    }
}
