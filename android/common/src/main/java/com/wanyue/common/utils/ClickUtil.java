package com.wanyue.common.utils;

/**
 * Created by  on 2018/9/29.
 */

public class ClickUtil {

    private static long sLastClickTime;
    public static boolean canClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - sLastClickTime < 500) {
            return false;
        }
        sLastClickTime = curTime;
        return true;
    }

    public static boolean canLongClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - sLastClickTime < 1000) {
            return false;
        }
        sLastClickTime = curTime;
        return true;
    }

}
