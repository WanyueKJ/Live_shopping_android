package com.wanyue.live.bean;

import com.wanyue.common.bean.UserBean;

/**
 * Created by  on 2019/4/27.
 */

public class LiveShutUpBean extends UserBean {
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        this.id = uid;
    }
}
