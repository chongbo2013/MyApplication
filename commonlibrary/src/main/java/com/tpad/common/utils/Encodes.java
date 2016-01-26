package com.tpad.common.utils;

import android.util.Base64;


/**
 * 封装各种格式的编码解码工具类.
 * <p/>
 * 1.Commons-Codec的 hex/base64 编码 2.自行编写的，将long进行base62编码以缩短其长度
 * 3.Commons-Lang的xml/html escape 4.JDK提供的URLEncoder
 */
public class Encodes {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    private Encodes() {
    }

    /**
     * Base64编码, byte[]->String.
     */
    public static String encodeBase64(byte[] input) {

        return Base64.encodeToString(input, Base64.DEFAULT);

//		return Base64.encodeBase64String(input);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
     */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafeString(input);
//	}

    /**
     * Base64解码, String->byte[].
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decode(input, Base64.DEFAULT);
        //return Base64.decodeBase64(input);
    }

    private static String alphabetEncode(long num, int base) {
        num = Math.abs(num);
        StringBuilder sb = new StringBuilder();
        for (; num > 0; num /= base) {
            sb.append(ALPHABET.charAt((int) (num % base)));
        }

        return sb.toString();
    }
}
