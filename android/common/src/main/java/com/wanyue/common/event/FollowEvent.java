package com.wanyue.common.event;

/**
 * Created by  on 2018/9/28.
 */

public class FollowEvent {

    private String mToUid;
    private int mAttention;

    public FollowEvent(String toUid, int attention) {
        mToUid = toUid;
        mAttention = attention;
    }

    public String getToUid() {
        return mToUid;
    }

    public void setToUid(String toUid) {
        mToUid = toUid;
    }

    public int getIsAttention() {
        return mAttention;
    }

    public void setAttention(int attention) {
        mAttention = attention;
    }
}
