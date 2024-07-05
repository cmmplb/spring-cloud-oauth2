package com.cmmplb.oauth2.resource.server.utils;

import java.security.MessageDigest;

/**
 * @author penglibo
 * @date 2024-07-04 10:19:59
 * @since jdk 1.8
 * MD5加密工具类
 */
public class MD5Util {

    // 加密算法
    public static final String MD5 = "MD5";

    /**
     * Md5加密
     * @param str 待加密字符串
     * @return md5加密串
     */
    public static String encode(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(MD5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        // 32位加密
        return hexValue.toString();
        // 16位的加密
        // return hexValue.toString().substring(8, 24);
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encode("123456"));
    }
}
