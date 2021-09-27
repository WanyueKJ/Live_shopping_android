package com.wanyue.common.interfaces;

import java.io.File;

public interface VideoResultCallback {

    void onSuccess(File file);

    void onFailure();
}
