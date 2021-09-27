package com.wanyue.live.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2017/8/22.
 * 收到送礼物消息的实体类
 */

public class LiveReceiveGiftBean {

    private String uid;
    private String avatar;
    private String userNiceName;
    private int level;
    private String giftId;
    private int giftCount;
    private String giftName;
    private String giftIcon;
    private String votes;
    private LiveChatBean mLiveChatBean;
    private int lianCount = 1;
    private int gif;//是否是gif礼物  1是 0不是
    private int gitType;//豪华礼物类型 0是gif  1是svga
    private String gifUrl;
    private String mKey;
    private int mMark;//礼物标记 // 0 普通  1热门  2守护 3幸运
    private int mLuck;//幸运礼物中奖了
    private String mLuckTime;//中奖倍数
    private String mStickerId;//道具礼物id
    private float mPlayTime;//播放时长单位秒
    private int mIsGlobal;//是否是全站礼物

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserNiceName() {
        return userNiceName;
    }

    public void setUserNiceName(String userNiceName) {
        if (!TextUtils.isEmpty(userNiceName) && userNiceName.length() > 7) {
            userNiceName = userNiceName.substring(0, 7) + "...";
        }
        this.userNiceName = userNiceName;
    }

    @JSONField(name = "level")
    public int getLevel() {
        return level;
    }

    @JSONField(name = "level")
    public void setLevel(int level) {
        this.level = level;
    }

    @JSONField(name = "giftid")
    public String getGiftId() {
        return giftId;
    }

    @JSONField(name = "giftid")
    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    @JSONField(name = "giftcount")
    public int getGiftCount() {
        return giftCount;
    }

    @JSONField(name = "giftcount")
    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    @JSONField(name = "giftname")
    public String getGiftName() {
        return giftName;
    }

    @JSONField(name = "giftname")
    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    @JSONField(name = "gifticon")
    public String getGiftIcon() {
        return giftIcon;
    }

    @JSONField(name = "gifticon")
    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    @JSONField(name = "votestotal")
    public String getVotes() {
        return votes;
    }

    @JSONField(name = "votestotal")
    public void setVotes(String votes) {
        this.votes = votes;
    }

    @JSONField(name = "type")
    public int getGif() {
        return gif;
    }

    @JSONField(name = "type")
    public void setGif(int gif) {
        this.gif = gif;
    }

    @JSONField(name = "swf")
    public String getGifUrl() {
        return gifUrl;
    }

    @JSONField(name = "swf")
    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    @JSONField(name = "swftype")
    public int getGitType() {
        return gitType;
    }

    @JSONField(name = "swftype")
    public void setGitType(int gitType) {
        this.gitType = gitType;
    }

    public LiveChatBean getLiveChatBean() {
        return mLiveChatBean;
    }

    public void setLiveChatBean(LiveChatBean liveChatBean) {
        mLiveChatBean = liveChatBean;
    }

    public int getLianCount() {
        return lianCount;
    }

    public void setLianCount(int lianCount) {
        this.lianCount = lianCount;
    }

    @JSONField(name = "mark")
    public int getMark() {
        return mMark;
    }

    @JSONField(name = "mark")
    public void setMark(int mark) {
        mMark = mark;
    }

    @JSONField(name = "isluck")
    public int getLuck() {
        return mLuck;
    }

    @JSONField(name = "isluck")
    public void setLuck(int luck) {
        mLuck = luck;
    }

    @JSONField(name = "lucktimes")
    public String getLuckTime() {
        return mLuckTime;
    }

    @JSONField(name = "lucktimes")
    public void setLuckTime(String luckTime) {
        mLuckTime = luckTime;
    }

    public String getKey() {
        if (TextUtils.isEmpty(mKey)) {
            mKey = this.uid + this.giftId + this.giftCount;
        }
        return mKey;
    }

    @JSONField(name = "sticker_id")
    public String getStickerId() {
        return mStickerId;
    }

    @JSONField(name = "sticker_id")
    public void setStickerId(String stickerId) {
        mStickerId = stickerId;
    }

    @JSONField(name = "swftime")
    public float getPlayTime() {
        return mPlayTime;
    }

    @JSONField(name = "swftime")
    public void setPlayTime(float playTime) {
        mPlayTime = playTime;
    }

    @JSONField(name = "isplatgift")
    public int getIsGlobal() {
        return mIsGlobal;
    }
    @JSONField(name = "isplatgift")
    public void setIsGlobal(int isGlobal) {
        mIsGlobal = isGlobal;
    }
}
