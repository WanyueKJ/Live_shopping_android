package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2019/8/1.
 */

public class FansUserBean extends UserBean {
    private int mAttention;

    @JSONField(name = "isattent")
    public int getAttention() {
        return mAttention;
    }

    @JSONField(name = "isattent")
    public void setAttention(int attention) {
        mAttention = attention;
    }

    public boolean isAttent() {
        return mAttention == 1;
    }
}
