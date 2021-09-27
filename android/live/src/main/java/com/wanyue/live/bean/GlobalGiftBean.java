package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 全站礼物实体类
 */
public class GlobalGiftBean {

    private String mLiveUid;
    private String mLiveName;
    private String mUserName;
    private String mGiftName;

    @JSONField(name = "liveuid")
    public String getLiveUid() {
        return mLiveUid;
    }

    @JSONField(name = "liveuid")
    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }

    @JSONField(name = "livename")
    public String getLiveName() {
        return mLiveName;
    }

    @JSONField(name = "livename")
    public void setLiveName(String liveName) {
        mLiveName = liveName;
    }

    @JSONField(name = "uname")
    public String getUserName() {
        return mUserName;
    }

    @JSONField(name = "uname")
    public void setUserName(String userName) {
        mUserName = userName;
    }

    @JSONField(name = "giftname")
    public String getGiftName() {
        return mGiftName;
    }

    @JSONField(name = "giftname")
    public void setGiftName(String giftName) {
        mGiftName = giftName;
    }
}
