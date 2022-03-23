package com.monica.seckilldemo.utils;

import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;


@Component
public class MD5Utils {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFromPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String frommPass,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2)+frommPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        //ce21b747de5af71ab5c2e20ff0a60eea
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(fromPassToDBPass("ce21b747de5af71ab5c2e20ff0a60eea", salt));
        System.out.println(inputPassToDBPass("123456", salt));
    }
}
