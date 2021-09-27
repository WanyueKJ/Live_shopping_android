package com.yunbao.im.event;

/**
 * Created by  on 2019/4/20.
 */

public class ChatLiveImEvent {
    private byte mAction;
    private String mSenderId;
    private String mAnchorPlayUrl;
    private String mAudiencePlayUrl;
    private boolean mAudienceCameraOpen;

    public ChatLiveImEvent() {
    }

    public ChatLiveImEvent(byte action, String senderId) {
        mAction = action;
        mSenderId = senderId;
    }

    public byte getAction() {
        return mAction;
    }

    public void setAction(byte action) {
        mAction = action;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getAnchorPlayUrl() {
        return mAnchorPlayUrl;
    }

    public void setAnchorPlayUrl(String anchorPlayUrl) {
        mAnchorPlayUrl = anchorPlayUrl;
    }

    public String getAudiencePlayUrl() {
        return mAudiencePlayUrl;
    }

    public void setAudiencePlayUrl(String audiencePlayUrl) {
        mAudiencePlayUrl = audiencePlayUrl;
    }

    public boolean isAudienceCameraOpen() {
        return mAudienceCameraOpen;
    }

    public void setAudienceCameraOpen(boolean audienceCameraOpen) {
        mAudienceCameraOpen = audienceCameraOpen;
    }
}
