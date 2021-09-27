package com.yunbao.im.business;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class WindowAddHelper {
    public static final int PERMISSON_OVERLAYS=10;
    private SysPermisssonFragment sysPermisssonFragment;
    private Context context;

    public WindowAddHelper(FragmentActivity activity) {
        this.context=activity;
        this.sysPermisssonFragment = new SysPermisssonFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.add(sysPermisssonFragment, "ProcessFragment").commit();
    }

    public  WindowManager.LayoutParams createDefaultWindowsParams(int x, int y) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //初始化位置
        params.gravity = Gravity.CENTER | Gravity.LEFT;
        params.x = x;
        params.y = y;
        params.width= ActionBar.LayoutParams.WRAP_CONTENT;
        params.height= ActionBar.LayoutParams.WRAP_CONTENT;
        //设置图片格式，效果为背景透明
        params.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //接收touch事件
        params.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //排版不受限制
        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//6.0+
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        else {
            params.type =  WindowManager.LayoutParams.TYPE_TOAST;
        }
        return params;
    }

    /*检查悬浮窗权限*/
    public Observable<Boolean> checkOverLay() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isDrawOverLays= Settings.canDrawOverlays(context);
            if(!isDrawOverLays){
                requestOverLay();
            }
            return  Observable.just(isDrawOverLays);
        }else{
            return  Observable.just(true);
        }
    }


    public  Observable<Boolean>  requestOverLay(){
         if(sysPermisssonFragment!=null){
           return Observable.create(new ObservableOnSubscribe<Boolean>() {
               @Override
               public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                   sysPermisssonFragment.startAciton(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,e);
               }
           }) ;
         }
        return null;
    }


    public void moveToFront(Context context) {
        if (Build.VERSION.SDK_INT >= 11) { // honeycomb
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> recentTasks = manager.getRunningTasks(Integer.MAX_VALUE);
            for (int i = 0; i < recentTasks.size(); i++){
                Log.e("xk", "  "+recentTasks.get(i).baseActivity.toShortString() + "   ID: "+recentTasks.get(i).id+"");
                Log.e("xk","@@@@  "+recentTasks.get(i).baseActivity.toShortString());
                // bring to front
                if (recentTasks.get(i).baseActivity.toShortString().indexOf(context.getPackageName()) > -1) {
                    if(recentTasks.get(i).baseActivity.getShortClassName().equals(context.getClass().getName())){
                        manager.moveTaskToFront(recentTasks.get(i).id, ActivityManager.MOVE_TASK_WITH_HOME);
                        return;
                    }

                }
            }
        }
    }


}
