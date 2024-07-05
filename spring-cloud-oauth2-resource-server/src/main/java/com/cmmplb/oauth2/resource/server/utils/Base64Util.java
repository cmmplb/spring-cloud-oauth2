package com.cmmplb.oauth2.resource.server.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author penglibo
 * @date 2021-12-10 16:14:49
 * @since jdk 1.8
 * Base64工具
 */

public class Base64Util {

    /**
     * base64 编码
     * @param str 待编码字符串
     * @return 编码字符串
     */
    public static String encoder(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64 解码
     * @param str 待解码字符串
     * @return 解码字符串
     */
    public static String decoder(String str) {
        return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        System.out.println(encoder("web:123456"));
    }
}
