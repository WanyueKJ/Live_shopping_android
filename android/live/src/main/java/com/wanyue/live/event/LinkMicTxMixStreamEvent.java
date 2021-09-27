package com.wanyue.live.event;

/**
 * Created by  on 2019/3/25.
 * 腾讯连麦的时候 主播混流
 */

public class LinkMicTxMixStreamEvent {

    private int mType;
    private String mToStream;

    public LinkMicTxMixStreamEvent(int type, String toStream) {
        mType = type;
        mToStream = toStream;
    }

    public int getType() {
        return mType;
    }

    public String getToStream() {
        return mToStream;
    }
}
