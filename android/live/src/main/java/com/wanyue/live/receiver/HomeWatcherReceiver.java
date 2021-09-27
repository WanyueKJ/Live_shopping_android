package com.wanyue.live.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.wanyue.common.utils.L;

public class HomeWatcherReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "HomeReceiver";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    //action内的某些reason
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";//home键旁边的最近程序列表键
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";//按下home键
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";//锁屏键
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";//某些三星手机的程序列表键

    private ShortHomeClickLitner mShortHomeClickLitner;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        App app = (App) context.getApplicationContext();
        Log.i(LOG_TAG, "onReceive: action: " + action);
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {//Action
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            L.d(LOG_TAG, "reason: " + reason);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) { // 短按Home键
               if(mShortHomeClickLitner!=null){
                   mShortHomeClickLitner.shortClick();
               }
                //可以在这里实现关闭程序操作。。。
                L.d(LOG_TAG, "homekey");
            } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {//Home键旁边的显示最近的程序的按钮
                // 长按Home键 或者 activity切换键
                L.d(LOG_TAG, "long press home key or activity switch");
            } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {  // 锁屏，似乎是没有反应，监听Intent.ACTION_SCREEN_OFF这个Action才有用
                L.d(LOG_TAG, "lock");
            } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {   // samsung 长按Home键
                L.d(LOG_TAG, "assist");
            }
        }
    }

    public void setShortHomeClickLitner(ShortHomeClickLitner shortHomeClickLitner) {
        mShortHomeClickLitner = shortHomeClickLitner;
    }

    public static interface ShortHomeClickLitner{
        public void shortClick();
    }

}