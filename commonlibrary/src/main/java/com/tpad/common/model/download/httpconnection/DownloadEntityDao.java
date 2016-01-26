package com.tpad.common.model.download.httpconnection;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.tpad.common.model.db.ormlite.OrmLiteBaseDao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jone.sun on 2015/3/23.
 */
public class DownloadEntityDao extends OrmLiteBaseDao<DownloadEntity> {
    private static DownloadEntityDao instance;
    public static DownloadEntityDao getInstance(Context context){
        if(instance == null){
            instance = new DownloadEntityDao(context);
        }
        return instance;
    }

    private DownloadEntityDao(Context context) {
        super(context);
    }

    /***
     * 获取每条线程已经下载的文件长度
     * @param downloadUrl
     * @return
     */
    public Map<Integer, Integer> getData(String downloadUrl) {
        Map<Integer, Integer> data = new HashMap<>();
        try {
            List<DownloadEntity> downloadEntities = getDao().queryBuilder().where().eq("downloadUrl", downloadUrl).query();
            for (DownloadEntity downloadEntity : downloadEntities) {
                data.put(downloadEntity.getThreadId(), downloadEntity.getDownLength());
            }
        } catch (SQLException e) {
            Log.e("DownloadEntityDao", "getData", e);
        }
        return data;
    }

    /***
     * 保存每条线程已经下载的文件长度
     * @param downloadUrl
     * @param map
     */
    public void save(String downloadUrl, Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            DownloadEntity downloadEntity = new DownloadEntity();
            downloadEntity.setDownloadUrl(downloadUrl);
            downloadEntity.setThreadId(entry.getKey());
            downloadEntity.setDownLength(entry.getValue());
            super.create(downloadEntity);
        }
    }

    /***
     * 实时更新每条线程已经下载的文件长度
     * @param downloadUrl
     * @param threadId
     * @param downLength
     */
    public int update(String downloadUrl, int threadId, int downLength) {
        DownloadEntity downloadEntity = new DownloadEntity();
        downloadEntity.setDownloadUrl(downloadUrl);
        downloadEntity.setThreadId(threadId);
        downloadEntity.setDownLength(downLength);
        try{
            UpdateBuilder<DownloadEntity, Integer> updateBuilder = getDao().updateBuilder();
            updateBuilder.updateColumnValue("downloadUrl", downloadUrl);
            updateBuilder.updateColumnValue("threadId", threadId);
            updateBuilder.updateColumnValue("downLength", downLength);
            updateBuilder.where().eq("downloadUrl", downloadUrl)
                    .and().eq("threadId", threadId);
            return updateBuilder.update();
        }catch (SQLException e){
            Log.e("DownloadEntityDao", "delete", e);
        }
        return 0;

    }

    /***
     * 当文件下载完成后，删除对应的下载记录
     * @param downloadUrl
     */
    public int delete(String downloadUrl) {
        try{
            DeleteBuilder<DownloadEntity, Integer> deleteBuilder = getDao().deleteBuilder();
            deleteBuilder.where().eq("downloadUrl", downloadUrl);
            return deleteBuilder.delete();
        }catch (SQLException e){
            Log.e("DownloadEntityDao", "delete", e);
        }
        return 0;
    }
}
