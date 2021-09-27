package com.wanyue.live.event;

import com.wanyue.live.bean.LiveBean;

/**
 * Created by  on 2019/6/27.
 */

public class LiveRoomChangeEvent {

    private LiveBean mLiveBean;
    private int mLiveType;//直播间的类型  普通 密码 门票 计时等
    private int mLiveTypeVal;//收费价格,计时收费每次扣费的值

    public LiveRoomChangeEvent(LiveBean bean, int liveType, int liveTypeVal) {
        mLiveBean = bean;
        mLiveType = liveType;
        mLiveTypeVal = liveTypeVal;
    }

    public LiveBean getLiveBean() {
        return mLiveBean;
    }

    public int getLiveType() {
        return mLiveType;
    }

    public int getLiveTypeVal() {
        return mLiveTypeVal;
    }

}
