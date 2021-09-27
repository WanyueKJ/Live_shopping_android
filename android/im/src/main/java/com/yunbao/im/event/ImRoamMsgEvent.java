package com.yunbao.im.event;


import com.yunbao.im.bean.ImUserBean;

/**
 * Created by  on 2018/7/20.
 * IM漫游消息 事件
 */

public class ImRoamMsgEvent {

    private ImUserBean mBean;

    public ImRoamMsgEvent(ImUserBean bean){
        mBean=bean;
    }

}
