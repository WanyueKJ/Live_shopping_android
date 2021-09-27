package com.wanyue.live.business.floatwindow;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.DpUtil;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Predicate;

public class WindowAddHelper {
    public static final int PERMISSON_OVERLAYS=10;
    private SysPermisssonFragment mSysPermisssonFragment;
    private Context mContext;

    public WindowAddHelper(FragmentActivity activity) {
        this.mContext=activity;
        this.mSysPermisssonFragment = new SysPermisssonFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.add(mSysPermisssonFragment, "ProcessFragment").commit();
    }

    public  WindowManager.LayoutParams createDefaultWindowsParams(int x, int y) {

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
               ,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //初始化位置
        params.gravity = Gravity.CENTER | Gravity.RIGHT;
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

        //params.type = WindowManager.LayoutParams.TYPE_TOAST;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//6.0+
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            params.type = WindowManager.LayoutParams.TYPE_TOAST;
//        }
//        else {
//            params.type =  WindowManager.LayoutParams.TYPE_TOAST;
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return params;
    }

    /*检查悬浮窗权限*/

    public Observable<Boolean> checkOverLay(Context context,Predicate<Boolean> predicate, boolean needRequestPermisson) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isDrawOverLays=isDrawOverLay();
            if(!isDrawOverLays&&needRequestPermisson){
                return  openMakeWindowsPermissonDialog(context).filter(predicate).flatMap(new io.reactivex.functions.Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
                        return requestOverLay();
                    }
                });
            }
            return  Observable.just(isDrawOverLays);
        }else{
            return  Observable.just(true);
        }
    }



    public boolean isDrawOverLay() {
        boolean isDrawOverLays=true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
             isDrawOverLays= Settings.canDrawOverlays(mContext);
        }
        return isDrawOverLays;
    }


    private Observable<Boolean> openMakeWindowsPermissonDialog(final Context context){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                openMakeWindowsPermissonDialog(context,e);
            }
        });
    }

    private void openMakeWindowsPermissonDialog(Context context,final ObservableEmitter<Boolean> e){
        Dialog dialog= new DialogUitl.Builder(context)
                .setTitle("")
                .setContent("你的手机没有授权浮窗权限,是否前往申请?")
                .setCancelable(true)
                .setBackgroundDimEnabled(true)
                .setCancelString("关闭直播间")
                .setConfrimString("立即开启")
                .setClickCallback(new DialogUitl.SimpleCallback2() {
                    @Override
                    public void onConfirmClick(Dialog dialog, String content) {
                        e.onNext(true);
                    }
                    @Override
                    public void onCancelClick() {
                        e.onNext(false);
                    }
                })
                .build();
        dialog.show();
    }

    public  Observable<Boolean>  requestOverLay(){
         if(mSysPermisssonFragment!=null){
           return Observable.create(new ObservableOnSubscribe<Boolean>() {
               @Override
               public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                   mSysPermisssonFragment.startAciton(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,e);
               }
           }) ;
         }
        return null;
    }

    public boolean moveToFront(Context context) {
        if(context==null){
            return false;
        }
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
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
