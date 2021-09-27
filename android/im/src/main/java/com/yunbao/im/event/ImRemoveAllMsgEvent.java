package com.yunbao.im.event;

/**
 * Created by  on 2019/4/1.
 */

public class ImRemoveAllMsgEvent {

    private String mToUid;

    public ImRemoveAllMsgEvent(String toUid) {
        mToUid = toUid;
    }

    public String getToUid() {
        return mToUid;
    }
}
