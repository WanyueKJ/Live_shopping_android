package com.wanyue.common.upload;

import java.util.List;

/**
 * Created by  on 2019/4/16.
 */

@Deprecated
public interface UploadCallback {
    void onFinish(List<UploadBean> list, boolean success);
}
