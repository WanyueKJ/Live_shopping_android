package com.wanyue.malls;

import android.app.Application;
import android.content.Context;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.TXLiveBase;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.SystemUtil;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class InitHelper {
    //腾讯推流SDK key
    public static final String TX_KEY="";
    //腾讯推流SDK listnse
    public static final String TX_LISTNSE="";

   private static boolean mBeautyInited;
    public void startDelayInit(final Application context,int time){
        Observable.timer(time, TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        delayInit(context);
                    }
            });
    }
    private  void delayInit(Application context){
        boolean isDebug= SystemUtil.isApkInDebug(context);
        DebugUtil.setDeBug(isDebug);
        L.setDeBug(isDebug);
        //初始化腾讯bugly
        CrashReport.initCrashReport(context);
        CrashReport.setAppVersion(context, CommonAppConfig.getVersion());
        //初始化ShareSdk
        MobSDK.init(context);
        //初始化极光推送
        //初始化极光推送
        //ImMessageUtil.getInstance().init();
        //初始化 ARouter
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
            //LeakCanary.install(context);
            if (LeakCanary.isInAnalyzerProcess(context)) {
                return;
            }
        }
        ARouter.init(context);
    }



    public void startNowInit(Context context){
        TXLiveBase.getInstance().setLicence(context,
                TX_LISTNSE,
                TX_KEY
                );
    }




    /*美狐是否初始化*/
    public static boolean isBeautyInited() {
        return mBeautyInited;
    }
}
