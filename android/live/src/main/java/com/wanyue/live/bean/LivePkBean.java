package com.wanyue.live.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/11/15.
 * 连麦pk实体类
 */

public class LivePkBean {

    private String uid;
    private String userNiceName;
    private String avatar;
    private String stream;
    private String pkUid;
    private int level;
    private int levelAnchor;
    private int sex;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    @JSONField(name = "pkuid")
    public String getPkUid() {
        return pkUid;
    }

    @JSONField(name = "pkuid")
    public void setPkUid(String pkUid) {
        this.pkUid = pkUid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "level_anchor")
    public int getLevelAnchor() {
        return levelAnchor;
    }

    @JSONField(name = "level_anchor")
    public void setLevelAnchor(int levelAnchor) {
        this.levelAnchor = levelAnchor;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * 是否在连麦中
     */
    public boolean isLinkMic() {
        return !TextUtils.isEmpty(pkUid) && !"0".equals(pkUid);
    }

}
