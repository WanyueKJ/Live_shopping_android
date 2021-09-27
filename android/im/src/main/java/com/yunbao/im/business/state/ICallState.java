package com.yunbao.im.business.state;

public interface ICallState {
    public static final int STATE_FREE=-1;   //空闲状态
    public static final int STATE_CALL_START=0;   //发起通话
    public static final int STATE_CALL_CANCEL = 1;//取消通话
    public static final int STATE_CALL_ACCEPT = 2;//接受通话
    public static final int STATE_CALL_REFUSE = 3;//拒绝通话
    public static final int STATE_CALL_BUSY = 9;//正在忙碌
    public void handle(int action,int role);
}
