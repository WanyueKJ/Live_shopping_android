package com.yunbao.im.event;

/**
 * Created by  on 2018/10/24.
 */

public class ImUnReadCountEvent {

    public String mUnReadCount;

    public ImUnReadCountEvent(String unReadCount) {
        mUnReadCount = unReadCount;
    }

    public String getUnReadCount() {
        return mUnReadCount;
    }

    public void setUnReadCount(String unReadCount) {
        mUnReadCount = unReadCount;
    }
}
