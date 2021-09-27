package com.wanyue.common.event;

/**
 * Created by  on 2019/5/13.
 */

public class BlackEvent {

    private String mToUid;
    private int mIsBlack;

    public BlackEvent(String toUid, int isBlack) {
        mToUid = toUid;
        mIsBlack = isBlack;
    }

    public String getToUid() {
        return mToUid;
    }

    public void setToUid(String toUid) {
        mToUid = toUid;
    }

    public int getIsBlack() {
        return mIsBlack;
    }

    public void setIsBlack(int isBlack) {
        mIsBlack = isBlack;
    }
}
