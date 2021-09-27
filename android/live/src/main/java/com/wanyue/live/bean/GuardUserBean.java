package com.wanyue.live.bean;

import com.wanyue.common.bean.UserBean;

/**
 * Created by  on 2018/11/7.
 */

public class GuardUserBean extends UserBean {

    private int type;//守护类型
    private String contribute;//贡献值

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContribute() {
        return contribute;
    }

    public void setContribute(String contribute) {
        this.contribute = contribute;
    }
}
