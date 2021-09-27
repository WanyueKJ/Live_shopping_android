package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/11/21.
 */

public class RedPackResultBean {

    private String avatar;
    private String time;
    private String uid;
    private String userNiceName;
    private String winCoin;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @JSONField(name = "user_nicename")
    public String getUserNiceName() {
        return userNiceName;
    }

    @JSONField(name = "user_nicename")
    public void setUserNiceName(String userNiceName) {
        this.userNiceName = userNiceName;
    }

    @JSONField(name = "win")
    public String getWinCoin() {
        return winCoin;
    }

    @JSONField(name = "win")
    public void setWinCoin(String winCoin) {
        this.winCoin = winCoin;
    }
}
