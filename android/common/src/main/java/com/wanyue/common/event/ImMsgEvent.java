package com.wanyue.common.event;

/**
 * Created by  on 2019/4/19.
 */

public class ImMsgEvent {

    public ImMsgEvent(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    private long mTimeStamp;

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }
}
