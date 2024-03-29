package com.monica.seckilldemo.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
    private static final Pattern mobile_pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

    public static boolean isMobile(String mobile){
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        Matcher matcher =  mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
