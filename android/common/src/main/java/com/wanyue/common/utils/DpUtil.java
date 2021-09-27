package com.wanyue.common.utils;

import android.content.Context;

import com.wanyue.common.CommonApplication;

/**
 * Created by  on 2017/8/9.
 * dp转px工具类
 */

public class DpUtil {

    private static float scale;

    static {
        scale = CommonApplication.sInstance.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(int dpVal) {
        return (int) (scale * dpVal + 0.5f);
    }


    public static int dp2pxResource( Context context,int dimenId) {
        float dimen=context.getResources().getDimension(dimenId);
        return (int) dimen;
    }



}
