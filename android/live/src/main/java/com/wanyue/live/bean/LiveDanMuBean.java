package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2017/8/22.
 * 发弹幕的实体类
 */

public class LiveDanMuBean {

    private String uid;
    private int level;
    private String votes;
    private String userNiceName;
    private String avatar;
    private String content;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "votestotal")
    public String getVotes() {
        return votes;
    }
    @JSONField(name = "votestotal")
    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getUserNiceName() {
        return userNiceName;
    }

    public void setUserNiceName(String userNiceName) {
        this.userNiceName = userNiceName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
