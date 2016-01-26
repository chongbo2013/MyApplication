package com.tpad.common.model.download.autodownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/***
 * 保存HTMl工具类
 * @author jone.sun
 *
 */
public class DownloadHtmlFileUtil {

    private static DownloadHtmlFileUtil instance;
    public static DownloadHtmlFileUtil getInstance(){
        if(instance == null){
            instance = new DownloadHtmlFileUtil();
        }
        return instance;
    }

    /**
     * 下载网页上图片、JS、CSS
     * @param httpUrl
     * @param urlPath
     */
    @SuppressWarnings("finally")
    private boolean saveHtmlFile(String savePath, String httpUrl, String urlPath) {
        boolean isSuccess = false;
        URL url;
        BufferedInputStream in;
        FileOutputStream file;
        try {
            String fileName = urlPath.substring(urlPath.lastIndexOf("/"));
            String filePath = savePath + urlPath.substring(0, urlPath.lastIndexOf("/"));
            //System.out.println(filePath);
            url = new URL(httpUrl + urlPath);
            File uploadFilePath = new File(filePath);
            if (!uploadFilePath.exists()) {
                uploadFilePath.mkdirs();
            }

            in = new BufferedInputStream(url.openStream());
            file = new FileOutputStream(new File(filePath + fileName));

            int t;
            while ((t = in.read()) != -1) {
                file.write(t);
            }
            file.close();
            in.close();
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        }finally{
            return isSuccess;
        }
    }

    /****
     * 下载网页代码及图片并替换图片链接地址
     * @param htmlContent
     * @param savePath
     * @param name
     * @return
     * @throws IOException
     */
    public boolean save(String htmlContent, String savePath, String name) throws IOException {
        String searchImgReg = "(?x)(href|HREF|src|SRC|background|BACKGROUND)=('|\")(http://.*?/)(.*?.(css|CSS|js|JS|jpg|JPG|png|PNG|gif|GIF))('|\")";
        Pattern pattern = Pattern.compile(searchImgReg);
        Matcher matcher = pattern.matcher(htmlContent);
        StringBuffer replaceStr = new StringBuffer();
        new File(savePath).mkdirs();
        while (matcher.find()) {
            if(saveHtmlFile(savePath, matcher.group(3), matcher.group(4))){
                matcher.appendReplacement(replaceStr, matcher.group(1) + "='"
                        + matcher.group(4) + "'");
            }
        }
        matcher.appendTail(replaceStr);//添加尾部
        return saveStrToFile(replaceStr.toString(), savePath + name);
    }

    /**
     * 获得网页html代码
     * @param httpUrl
     * @return
     * @throws IOException
     */
    public String getHtmlCode(String httpUrl) throws IOException {
        String content = "";
        URL uu = new URL(httpUrl); // 创建URL类对象
        BufferedReader ii = new BufferedReader(new InputStreamReader(uu
                .openStream())); // //使用openStream得到一输入流并由此构造一个BufferedReader对象
        String input;
        while ((input = ii.readLine()) != null) { // 建立读取循环，并判断是否有读取值
            content += input;
        }
        ii.close();
        return content;
    }

    public static boolean saveStrToFile(String name, String path) {
        boolean isSuccess = false;
        byte[] b = name.getBytes();
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 测试方法
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DownloadHtmlFileUtil gcp = new DownloadHtmlFileUtil();
        new File("c://test/").mkdirs();
        gcp.save(gcp.getHtmlCode("http://www.uichange.com/public/ctc/cpa/desc/qingtingfm/index.html"), "c://test/", "tt.html");
    }
}