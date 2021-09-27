package com.yunbao.im.event;

/**
 * Created by  on 2018/7/20.
 * IM登录事件
 */

public class ImLoginEvent {

    private boolean mLogin;

    public ImLoginEvent(boolean login) {
        mLogin = login;
    }


    public boolean isLogin() {
        return mLogin;
    }
}
