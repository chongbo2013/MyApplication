package com.tpad.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author jone.sun on 2015/3/24.
 */
public class Md5Utils {
    /**
     * The default password string combination, byte is used to convert
     * hexadecimal representation of the characters, the apache verify the
     * correctness of the downloaded files using this combination is the default
     */
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            nsaex.printStackTrace();
        }
    }

    /**
     * Generate md5 checksum value of the string
     *
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    /**
     * Determine whether the md5 checksum of the string match a known md5 code
     *
     *
     * @param password
     *            To check the string
     * @param md5PwdStr
     *            Known as the md5 checksum
     * @return
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * Md5 checksum value of the generated files
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    public static String getFileMD5StringForApache(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buf = new byte[4096];
        int count = -1;
        while ((count = fis.read(buf)) > -1) {
            if (count == buf.length) {
                messagedigest.update(buf);
            } else {
                messagedigest.update(Arrays.copyOfRange(buf, 0, count));
            }
        }
        fis.close();
        byte[] md = messagedigest.digest();
        return new String(Base64CoderUtils.encode(md));
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
