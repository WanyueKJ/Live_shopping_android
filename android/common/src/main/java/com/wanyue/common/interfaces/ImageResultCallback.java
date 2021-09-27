package com.wanyue.common.interfaces;

import java.io.File;

/**
 * Created by  on 2018/9/29.
 */

public interface ImageResultCallback {
    //跳转相机前执行
    void beforeCamera();

    void onSuccess(File file);

    void onFailure();
}
