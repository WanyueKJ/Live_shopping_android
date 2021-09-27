package com.wanyue.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.wanyue.common.CommonApplication;
import java.util.List;

public class SystemUtil {
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance
                        + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /*获取屏幕宽度*/
    public static int getWindowsPixelWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 取得窗口属性
        manager.getDefaultDisplay().getMetrics(dm);
        // 窗口的宽度
        int viewWidth = dm.widthPixels;
        return viewWidth;
    }
    /*获取屏幕高度*/
    public static int getWindowsPixelHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 取得窗口属性
        manager.getDefaultDisplay().getMetrics(dm);
        // 窗口的宽度
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*关闭所有的弹框*/
    public static void disMissAllDialog(FragmentActivity fragmentActivity) {
        List<Fragment> fragmentList=fragmentActivity.getSupportFragmentManager().getFragments();
        for(Fragment fragment:fragmentList){
            if(fragment instanceof DialogFragment){
                DialogFragment dialogFragment= (DialogFragment) fragment;
                if (dialogFragment != null) {
                    dialogFragment.dismissAllowingStateLoss();

                }
            }
        }
    }

    /**
     * 设置透明状态栏
     */
    public static void setTransparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }
    /**
     * 判断某APP是否安装
     */

    public static boolean isAppExist(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            PackageManager manager = CommonApplication.sInstance.getPackageManager();
            List<PackageInfo> list = manager.getInstalledPackages(0);
            for (PackageInfo info : list) {
                if (packageName.equalsIgnoreCase(info.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断当前应用是否是debug状态
     */


    public  static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 打印当前线程
     */

    public static  void logCurrentThread(){
        L.e("当前thread 名称=="+Thread.currentThread().getName());
    }
}
