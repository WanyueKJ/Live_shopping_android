package com.wanyue.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.utils.L;

/**
 * Created by  on 2017/8/3.
 */

public class CommonApplication extends MultiDexApplication {

    public static CommonApplication sInstance;
    private int mCount;
    private boolean mFront;//是否前台

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //初始化Http
       CommonHttpUtil.init();
       CommonAppConfig.getWindowWidth();
       CommonAppConfig.getWindowHeight();
       registerActivityLifecycleCallbacks();
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }


    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (!mFront) {
                    mFront = true;
                    L.e("AppContext------->处于前台");
                    CommonAppConfig.setFrontGround(true);
                }
            }
            @Override
            public void onActivityResumed(Activity activity) {
            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
                    mCount--;
                if (mCount == 0) {
                    mFront = false;
                    L.e("AppContext------->处于后台");
                    CommonAppConfig.setFrontGround(false);
                }
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }
            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public int getResourceColor(int res){
       return getResources().getColor(res);
    }

}
