package com.wanyue.common.bean;

public class LiveInfo {
    private String mLiveUid;
    private String mSteam;
    private int mRoomId;
    public String getLiveUid() {
        return mLiveUid;
    }
    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }
    public String getSteam() {
        return mSteam;
    }
    public void setSteam(String steam) {
        mSteam = steam;
    }
    public int getRoomId() {
        return mRoomId;
    }
    public void setRoomId(int roomId) {
        mRoomId = roomId;
    }
}
