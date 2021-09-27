package com.wanyue.live.bean;

/**
 * Created by  on 2018/10/12.
 */

public class LiveEnterRoomBean {

    private LiveUserGiftBean mUserBean;
    private LiveChatBean mLiveChatBean;

    public LiveEnterRoomBean(LiveUserGiftBean userBean, LiveChatBean liveChatBean) {
        mUserBean = userBean;
        mLiveChatBean = liveChatBean;
    }


    public LiveUserGiftBean getUserBean() {
        return mUserBean;
    }

    public void setUserBean(LiveUserGiftBean userBean) {
        mUserBean = userBean;
    }

    public LiveChatBean getLiveChatBean() {
        return mLiveChatBean;
    }

    public void setLiveChatBean(LiveChatBean liveChatBean) {
        mLiveChatBean = liveChatBean;
    }
}
