package com.wanyue.common.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidatePhoneUtil {
    //判断手机号码的正则表达式
    private static final String MOBILE_NUM_REGEX = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(16[0-9])|(19[0-9]))\\d{8}$";


    /**
     * 验证一个号码是不是手机号，以前这个地方是用正则判断的，现在改由服务端验证
     *
     * @param mobileNumber
     */
    public static boolean validateMobileNumber(String mobileNumber) {
        if(TextUtils.isEmpty(mobileNumber)){
            return false;
        }
        Pattern p = Pattern.compile(MOBILE_NUM_REGEX);
        Matcher m = p.matcher(mobileNumber);
        return m.matches();
    }


}
