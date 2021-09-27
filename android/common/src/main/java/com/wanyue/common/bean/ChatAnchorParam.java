package com.wanyue.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by  on 2019/4/19.
 */

public class ChatAnchorParam implements Parcelable {

    private String mSessionId;//通话的ID
    private int mChatType;//通话的类型
    private String mAudienceID;//观众的ID
    private String mAudienceAvatar;//观众的头像
    private String mAudienceName;//观众的名字
    private boolean mAnchorActive;//是否是主播主动发起的
    private String mAnchorPlayUrl;//主播的播放地址
    private String mAnchorPushUrl;//主播的推流地址
    private String mPrice;//通话价格
    private boolean mMatch;//是否是匹配的

    public ChatAnchorParam() {
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

    public String getAudienceID() {
        return mAudienceID;
    }

    public void setAudienceID(String audienceID) {
        mAudienceID = audienceID;
    }

    public String getAudienceAvatar() {
        return mAudienceAvatar;
    }

    public void setAudienceAvatar(String audienceAvatar) {
        mAudienceAvatar = audienceAvatar;
    }

    public String getAudienceName() {
        return mAudienceName;
    }

    public void setAudienceName(String audienceName) {
        mAudienceName = audienceName;
    }

    public boolean isAnchorActive() {
        return mAnchorActive;
    }

    public void setAnchorActive(boolean anchorActive) {
        mAnchorActive = anchorActive;
    }


    public String getAnchorPlayUrl() {
        return mAnchorPlayUrl;
    }

    public void setAnchorPlayUrl(String anchorPlayUrl) {
        mAnchorPlayUrl = anchorPlayUrl;
    }

    public String getAnchorPushUrl() {
        return mAnchorPushUrl;
    }

    public void setAnchorPushUrl(String anchorPushUrl) {
        mAnchorPushUrl = anchorPushUrl;
    }


    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
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
        dest.writeString(mAudienceID);
        dest.writeString(mAudienceAvatar);
        dest.writeString(mAudienceName);
        dest.writeByte((byte) (mAnchorActive ? 1 : 0));
        dest.writeString(mAnchorPlayUrl);
        dest.writeString(mAnchorPushUrl);
        dest.writeString(mPrice);
        dest.writeByte((byte) (mMatch ? 1 : 0));
    }

    public ChatAnchorParam(Parcel in) {
        mSessionId = in.readString();
        mChatType = in.readInt();
        mAudienceID = in.readString();
        mAudienceAvatar = in.readString();
        mAudienceName = in.readString();
        mAnchorActive = in.readByte() != 0;
        mAnchorPlayUrl = in.readString();
        mAnchorPushUrl = in.readString();
        mPrice = in.readString();
        mMatch = in.readByte() != 0;
    }

    public static final Creator<ChatAnchorParam> CREATOR = new Creator<ChatAnchorParam>() {
        @Override
        public ChatAnchorParam createFromParcel(Parcel in) {
            return new ChatAnchorParam(in);
        }

        @Override
        public ChatAnchorParam[] newArray(int size) {
            return new ChatAnchorParam[size];
        }
    };
}
