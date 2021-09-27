package com.wanyue.common.utils;

import android.os.Build;

import com.wanyue.common.mob.MobShareUtil;

public class ModelUtils {
    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
 
    public static boolean isEMUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("HUAWEI".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
 
    public static boolean isOPPO() {
        String manufacturer = Build.MANUFACTURER;
        if ("OPPO".equalsIgnoreCase(manufacturer)) {
            return true;
        }

        return false;
    }
 
    public static boolean isVIVO() {
        String manufacturer = Build.MANUFACTURER;
        if ("vivo".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
 
    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static int getSystemVersion() {
        return Integer.parseInt(Build.VERSION.RELEASE);
    }
}
