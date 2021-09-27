package com.wanyue.common.pay;

import android.text.StaticLayout;

/**
 * Created by  on 2018/10/23.
 */

public interface PayCallback {
    public static final int CANCLE_PAY=-2;
    public static final int FAILED=-1;
    void onSuccess();
    void onFailed(int errorCode);
}
