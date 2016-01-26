package com.tpad.common.model.download.downloadmanager;

import android.util.Log;

import com.tpad.common.utils.Md5Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author one.sun on 2015/5/7.
 */
public class DownloadMd5Util {
    private static final String TAG = DownloadMd5Util.class.getSimpleName();
    private static String dstName = "www.uichange.com";
    private static final int dstPort = 80;

    private static DownloadMd5Util instance = null;

    public static DownloadMd5Util getInstance() {
        if (instance == null) {
            instance = new DownloadMd5Util();
        }
        return instance;
    }

    public boolean isCompleteFile(String fileUrl, String filePath) {
        boolean isComplete = false;
        if (fileUrl != null && filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                String MD5_URL = getMD5FromUrl(fileUrl);
                String MD5_Native = getMd5FromNative(file);
                if (MD5_URL == null) {
                    isComplete = true;
                } else {
                    if (MD5_Native != null && MD5_URL.equals(MD5_Native)) {
                        isComplete = true;
                    }
                }
                Log.e(TAG, "fileUrl: " + fileUrl + " ,MD5_URL: " + MD5_URL + ", MD5_Native: " + MD5_Native);
            }
        }
        return isComplete;
    }

    @SuppressWarnings("finally")
    public String getMD5FromUrl(String url) {
        Socket client = null;
        String md5 = null;
        try {
            StringBuffer sb = getHttpHeaders(url, true);
            client = new Socket(dstName, dstPort);

            // 发出HTTP请求
            OutputStream socketOut = client.getOutputStream();
            socketOut.write(sb.toString().getBytes());
            socketOut.flush();

            client.setSoTimeout(5 * 1000);

            InputStream is = client.getInputStream();
            String line = null;
            String location = null;
            do {
                line = readLine(is, 0);
                System.out.println(line);
                if (line.contains("Content-MD5")) {
                    md5 = line.substring(12).trim();
                    break;
                }
                if (line.contains("Location:")) {
                    location = line.substring(line.indexOf("http:"), line.length() - 1).trim();
                    break;
                }
            } while (!line.equals("\r\n"));

            if (location != null) {
                sb = getHttpHeaders(location, false);
                socketOut.write(sb.toString().getBytes());
                socketOut.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                line = null;
                while ((line = rd.readLine()) != null) {
                    if (line.startsWith("Content-MD5")) {
                        //System.out.println(line);
                        md5 = line.substring(line.indexOf(":") + 1).trim();
                        break;
                    }
                }
            }

            is.close();
            client.close();
        } catch (IOException e) {
            Log.e(TAG, "", e);
        } finally {
            if (client != null && !client.isClosed()) {
                try {
                    client.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
//            Log.d(TAG, "socket已关闭");
            return md5;
        }
    }

    public String getMd5FromNative(File file) {
        String md5 = null;
        try {
            md5 = Md5Utils.getFileMD5StringForApache(file);
        } catch (IOException e) {
        }
        return md5;
    }

    private StringBuffer getHttpHeaders(String url, boolean ifKeepAlive) {
        String connection = ifKeepAlive ? "Keep-Alive" : "Close";
        url = url.replace("http://", "");
        dstName = url.substring(0, url.indexOf("/"));
        String path = url.substring(url.indexOf("/"));
        StringBuffer sb = new StringBuffer("GET " + path + " HTTP/1.1\r\n");
        sb.append("Host: " + dstName + "\r\n");
        sb.append("Connection: " + connection + "\r\n");
        sb.append("Accept: */*\r\n\r\n\r\n");
        //System.out.println(sb.toString());
        return sb;

    }

    private static String readLine(InputStream is, int contentLe) throws IOException {
        ArrayList<Byte> lineByteList = new ArrayList<Byte>();
        byte readByte;
        int total = 0;
        if (contentLe != 0) {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
                total++;
            } while (total < contentLe);//消息体读还未读完
        } else {
            do {
                readByte = (byte) is.read();
                if (readByte == -1) {
                    break;
                }
                lineByteList.add(Byte.valueOf(readByte));
            } while (readByte != 10);
        }

        byte[] tmpByteArr = new byte[lineByteList.size()];
        for (int i = 0; i < lineByteList.size(); i++) {
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();
        }
        lineByteList.clear();

        return new String(tmpByteArr, "UTF-8");
    }

    public static void main(String[] args) throws IOException {
        String md5 = new DownloadMd5Util().getMD5FromUrl("http://www.uichange.com/public/ctc/app/apk/com.j.horizon-@7F0B059D-960.apk");
        System.out.println("md5: " + md5);
    }

    /**
     * 设置端口
     * @param dstName
     */
    public static void setDstName(String dstName) {
        DownloadMd5Util.dstName = dstName;
    }
}
