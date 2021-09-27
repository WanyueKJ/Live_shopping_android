package com.wanyue.live.event;

/**
 * Created by  on 2019/3/25.
 */

public class LinkMicTxAccEvent {

    private boolean mLinkMic;

    public LinkMicTxAccEvent(boolean linkMic) {
        mLinkMic = linkMic;
    }

    public boolean isLinkMic() {
        return mLinkMic;
    }
}
