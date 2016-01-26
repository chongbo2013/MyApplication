package com.tpad.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 文件操作方法公共类
 * Created by loang on 2014/12/30.
 */
public class FileUtils {

    private final static String TAG = FileUtils.class
            .getSimpleName();


    /**
     * 读取文本文件的内容
     *
     * @param path（本地T卡）
     * @return string
     */
    public static String getStringFromTxtFiles(String path) {
        String Name = "";
        String final_Name = "";

        File file = new File(path);
        if (!file.exists()) {
            return final_Name;
        }
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312");
            BufferedReader reader = new BufferedReader(isr);
            Name = reader.readLine();
            if (Name != null) {
                String x = Name.substring(Name.length() - 1,
                        Name.length());
                String y = " ";
                if (x.equals(y))
                    final_Name = Name.substring(0, Name.length() - 1);
                else
                    final_Name = Name;
            } else
                final_Name = "";
            fis.close();

        } catch (FileNotFoundException e) {
            final_Name = "";
        } catch (IOException e) {
            final_Name = "";
        }
        return final_Name;
    }

    /**
     * getLocalAppUseMarketPkgName
     *
     * @param context
     * @param filename
     * @return
     */
    public static String getStringFromAssetsTxtFiles(Context context, String filename) {
        InputStream inputStream = null;
        AssetManager am = null;
        String line;
        am = context.getAssets();
        StringBuffer marketname = new StringBuffer("");
        try {
            inputStream = am.open(filename);
        } catch (IOException e2) {
            return marketname.toString();
        }

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            while ((line = reader.readLine()) != null) {
                marketname.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return marketname.toString();
    }

    /**
     * getStringFromTxt
     *
     * @param filePath
     * @return
     */
    public static String getStringFromTxt(String filePath) {
        return getStringFromTxt(filePath, "gbk");
    }

    /**
     * 获取文本文件的全部内容
     *
     * @param filePath
     * @param charset
     * @return
     */
    public static String getStringFromTxt(String filePath, String charset) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        InputStreamReader inputStreamReader = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(fis, charset);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void saveStrToFile(String path, String value) {
        saveStrToFile(path, value, "gbk");
    }

    /**
     * 保存字符串到指定格式的文本文件中
     * gbk 模式
     *
     * @param path
     * @param value
     * @param charset
     */
    public static void saveStrToFile(String path, String value, String charset) {

        File file = new File(path);
        BufferedWriter bw = null;
        FileOutputStream fos = null;

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "get exception is : " + e.getMessage());
        }
        try {
            fos = new FileOutputStream(path, true);
            bw = new BufferedWriter(new OutputStreamWriter(
                    fos, charset));
            bw.write(value);
            Log.i(TAG, "save success!!!");

        } catch (IOException e) {
            Log.e(TAG, "get exception is : " + e.getMessage());
        }
        try {
            bw.flush();
            bw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bw.close();
                fos.close();
            } catch (Exception e2) {
                Log.e(TAG, "get exception is : " + e.getMessage());
            }
        }
    }

    /**
     * savePkgNameToLocal
     *
     * @param path
     * @param value
     */
    public void savePkgNameToLocal(String path, String value) {
        File file = new File(path);
        FileWriter fw = null;
        BufferedWriter bw = null;

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "get exception is : " + e.getMessage());
        }

        try {
            fw = new FileWriter(path, true);
        } catch (IOException e) {
            Log.e(TAG, "get exception is : " + e.getMessage());
        }
        bw = new BufferedWriter(fw);
        try {
            bw.write(new String(value.getBytes(), "gbk"));
        } catch (IOException e) {
            Log.e(TAG, "get exception is : " + e.getMessage());
        }
        try {
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (Exception e2) {
                Log.e(TAG, "get exception is : " + e.getMessage());
            }
        }

        Log.i(TAG, "write package to pkgname.txt success !!!");

    }

    /**
     * 获取文件的大小
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                return fis.available();
            } catch (Exception e) {
//                e.printStackTrace();
                return -1;
            }
        } else {
            return -1;
        }

    }

    /**
     * 获取SD卡文件空间的大小
     *
     * @return
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        return (freeBlocks * blockSize); //单位MB
    }

    /**
     * 向目标文件中追加内容展示(方法1)
     *
     * @param filepath      目标文件路径
     * @param appendContent 追加内容
     */
    public static void WriteStringAppendToFileMethod1(String filepath, String appendContent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filepath, true), "UTF-8"));
            out.write(appendContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向目标文件中追加内容展示(方法2)
     *
     * @param filepath
     * @param appendContent
     */
    public static void WriteStringAppendToFileMethod2(String filepath, String appendContent) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(filepath, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(appendContent);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFM(Context context, String fmfilepath) {
        if (!new File(fmfilepath).exists()) {
            saveStrToFile(fmfilepath, getFmOfApp(context));
        }
    }

    /**
     * 读取FM值
     *
     * @return
     */
    public static String getFm(Context context, String fmfilepath) {
        if (new File(fmfilepath).exists()) {
            return getStringFromTxtFiles(fmfilepath);
        } else {
            return getFmOfApp(context);
        }
    }

    /**
     * 获取应用中设置的FM值
     *
     * @return
     */
    public static String getFmOfApp(Context context) {
        String fmValue = null;
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            if (bundle != null && bundle.containsKey("FM_NAME")) {
                fmValue = bundle.getString("FM_NAME").trim();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("saveFM", "", e);
        } finally {
            if (fmValue == null) {
                fmValue = "";
            }
            return fmValue;
        }
    }

    public static boolean isFileExists(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean initFilePath(String filePath) {
        if (!isFileExists(filePath)) {
            return new File(filePath).mkdirs();
        }
        return true;
    }

    public static File[] getFiles(String path) {
        if (isFileExists(path)) {
            File root = new File(path);
            File files[] = root.listFiles();
            return files;
        }
        return null;
    }


    public static void deleteFile(String path) {
        if (isFileExists(path)) {
            new File(path).delete();
        }
    }


    public static boolean deleteDir(String dirPath) {
        boolean delFlag = true;
        try {
            delDir(new File(dirPath));
        } catch (Exception e) {
            delFlag = false;
        }
        return delFlag;
    }

    public static void delDir(File dirFile) throws Exception {
        if (dirFile.exists()) {
            if (dirFile.isDirectory()) {
                File[] files = dirFile.listFiles();
                for (File file : files) {
                    delDir(file);
                }
                dirFile.delete();
            } else {
                dirFile.delete();
            }
        }
    }
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    public static void createDirFile(String path) {
        if(isSdcardExist()) {
            File dir = new File(path);
            if (!dir.exists() && dir.mkdirs()) {
                System.out.println(path + "创建成功");
            }
        }
    }

    public static File createNewFile(String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return null;
            }
        }
        return file;
    }

    public static boolean deleteFileByDaysApart(String path,int dateCount){
        if (isFileExists(path)){
            File file = new File(path);
            int daysApart = CtrDateUtils.getGapCount(new Date(file.lastModified()),new Date());
            if (daysApart > dateCount){
                return file.delete();
            }
        }
        return false;
    }
}
