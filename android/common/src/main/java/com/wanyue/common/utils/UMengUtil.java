package com.wanyue.common.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by  on 2019/7/27.
 * 友盟工具类
 */

public class UMengUtil {

    public static void init(Context context) {
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onLogin(String loginType, String uid) {
        MobclickAgent.onProfileSignIn(loginType, uid);
    }

    public static void onLogout() {
        MobclickAgent.onProfileSignOff();
    }
}
