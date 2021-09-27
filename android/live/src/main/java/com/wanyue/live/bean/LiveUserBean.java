package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanyue.common.bean.UserBean;

public class LiveUserBean extends UserBean {
    @JSONField(name = "action")
    private int authority;
    private int isshut;

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getIsshut() {
        return isshut;
    }

    public void setIsshut(int isshut) {
        this.isshut = isshut;
    }
}
