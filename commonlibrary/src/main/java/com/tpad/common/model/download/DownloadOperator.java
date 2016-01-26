package com.tpad.common.model.download;

import android.content.Context;

import com.tpad.common.model.download.downloadmanager.DownloadConfig;
import com.tpad.common.model.download.downloadmanager.DownloadInfo;
import com.tpad.common.model.download.downloadmanager.DownloadManagerOperator;
import com.tpad.common.model.download.httpconnection.DownloadEntityDao;
import com.tpad.common.model.download.httpconnection.HttpConnDownloadOperator;

import java.io.File;

/**
 * Created by jone.sun on 2015/10/9.
 */
public class DownloadOperator {
    private static DownloadOperator instance = null;
    public static DownloadOperator getInstance(){
        if(instance == null){
            instance = new DownloadOperator();
        }
        return instance;
    }
    private DownloadOperator(){}
    public boolean isDownloadingBySecond(Context context, DownloadConfig downloadConfig) {
        return (new File(downloadConfig.getSavePath()
                + File.separator + downloadConfig.getSaveName() + HttpConnDownloadOperator.fileTmpSuffix).exists()
                && DownloadEntityDao.getInstance(context).
                getData(downloadConfig.getUrl()).size() > 0);
    }

    public boolean renameDownloadCompleteFile(DownloadInfo downloadInfo){
        return renameDownloadCompleteFile(downloadInfo.getLocal_filename());
    }

    public boolean renameDownloadCompleteFile(String filePath){
        File file = new File(filePath);
        String path = file.getPath();
        String lastFileName = path.replace(DownloadManagerOperator.DOWNLOAD_FILE_TMP_SUFFIX, "");
        return file.renameTo(new File(lastFileName));
    }
}
