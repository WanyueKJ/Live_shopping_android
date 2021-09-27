package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2019/4/29.
 */

public class LiveGiftPrizePoolWinBean {

    private String mUid;
    private String mAvatar;
    private String mName;
    private String mCoin;

    @JSONField(name = "uid")
    public String getUid() {
        return mUid;
    }

    @JSONField(name = "uid")
    public void setUid(String uid) {
        mUid = uid;
    }

    @JSONField(name = "uhead")
    public String getAvatar() {
        return mAvatar;
    }

    @JSONField(name = "uhead")
    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    @JSONField(name = "uname")
    public String getName() {
        return mName;
    }

    @JSONField(name = "uname")
    public void setName(String name) {
        mName = name;
    }

    @JSONField(name = "wincoin")
    public String getCoin() {
        return mCoin;
    }

    @JSONField(name = "wincoin")
    public void setCoin(String coin) {
        mCoin = coin;
    }


}
