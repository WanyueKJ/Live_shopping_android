package com.wanyue.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by  on 2019/4/19.
 */

public class ChatAudienceParam implements Parcelable {

    private String mSessionId;//通话的ID
    private int mChatType;//通话的类型
    private String mAnchorID;//主播的ID
    private String mAnchorAvatar;//主播的头像
    private String mAnchorName;//主播的名字
    private int mAnchorLevel;//主播的等级
    private String mAudiencePlayUrl;//观众的播放地址
    private String mAudiencePushUrl;//观众的推流地址
    private String mAnchorPrice;//主播的价格
    private boolean mAudienceActive;//是否是观众主动发起的
    private boolean mMatch;//是否是匹配的

    public ChatAudienceParam() {
    }


    public String getSessionId() {
        return mSessionId;
    }

    public void setSessionId(String sessionId) {
        mSessionId = sessionId;
    }

    public int getChatType() {
        return mChatType;
    }

    public void setChatType(int chatType) {
        mChatType = chatType;
    }

    public String getAnchorID() {
        return mAnchorID;
    }

    public void setAnchorID(String anchorID) {
        mAnchorID = anchorID;
    }

    public String getAnchorAvatar() {
        return mAnchorAvatar;
    }

    public void setAnchorAvatar(String anchorAvatar) {
        mAnchorAvatar = anchorAvatar;
    }

    public String getAnchorName() {
        return mAnchorName;
    }

    public void setAnchorName(String anchorName) {
        mAnchorName = anchorName;
    }

    public int getAnchorLevel() {
        if (mAnchorLevel <= 0) {
            return 1;
        }
        return mAnchorLevel;
    }

    public void setAnchorLevel(int anchorLevel) {
        mAnchorLevel = anchorLevel;
    }

    public String getAudiencePlayUrl() {
        return mAudiencePlayUrl;
    }

    public void setAudiencePlayUrl(String audiencePlayUrl) {
        mAudiencePlayUrl = audiencePlayUrl;
    }

    public String getAudiencePushUrl() {
        return mAudiencePushUrl;
    }

    public void setAudiencePushUrl(String audiencePushUrl) {
        mAudiencePushUrl = audiencePushUrl;
    }

    public String getAnchorPrice() {
        return mAnchorPrice;
    }

    public void setAnchorPrice(String anchorPrice) {
        mAnchorPrice = anchorPrice;
    }

    public boolean isAudienceActive() {
        return mAudienceActive;
    }

    public void setAudienceActive(boolean audienceActive) {
        mAudienceActive = audienceActive;
    }

    public boolean isMatch() {
        return mMatch;
    }

    public void setMatch(boolean match) {
        mMatch = match;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSessionId);
        dest.writeInt(mChatType);
        dest.writeString(mAnchorID);
        dest.writeString(mAnchorAvatar);
        dest.writeString(mAnchorName);
        dest.writeInt(mAnchorLevel);
        dest.writeString(mAudiencePlayUrl);
        dest.writeString(mAudiencePushUrl);
        dest.writeString(mAnchorPrice);
        dest.writeByte((byte) (mAudienceActive ? 1 : 0));
        dest.writeByte((byte) (mMatch ? 1 : 0));
    }

    public ChatAudienceParam(Parcel in) {
        mSessionId = in.readString();
        mChatType = in.readInt();
        mAnchorID = in.readString();
        mAnchorAvatar = in.readString();
        mAnchorName = in.readString();
        mAnchorLevel = in.readInt();
        mAudiencePlayUrl = in.readString();
        mAudiencePushUrl = in.readString();
        mAnchorPrice = in.readString();
        mAudienceActive = in.readByte() != 0;
        mMatch = in.readByte() != 0;
    }

    public static final Creator<ChatAudienceParam> CREATOR = new Creator<ChatAudienceParam>() {
        @Override
        public ChatAudienceParam createFromParcel(Parcel in) {
            return new ChatAudienceParam(in);
        }

        @Override
        public ChatAudienceParam[] newArray(int size) {
            return new ChatAudienceParam[size];
        }
    };
}
