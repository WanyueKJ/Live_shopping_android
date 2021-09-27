package com.wanyue.malls;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.utils.ResourceUtil;

public class MailApplication extends CommonApplication {

    
    @Override
    public void onCreate() {
        super.onCreate();
        InitHelper initHelper=new InitHelper();
        initHelper.startDelayInit(this,100);
        initHelper.startNowInit(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ResourceUtil.clearAllDrawable();
    }
}
