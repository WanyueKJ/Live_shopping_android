package com.yunbao.im.business.state;

import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;

public class CallStateMachine {
   private static CallStateMachine callStateMachine;
   private ICallState currentState=new FreeCallState();
   public int currentRole= Constants.ROLE_ANTHOR;
   public  int callType;
   public UserBean toUserBean;
   public int roomId;
   public CallStateMachine() {
   }

    public static CallStateMachine getInstance(){
        if(callStateMachine==null){
           synchronized (CallStateMachine.class){
             callStateMachine=new CallStateMachine();
          }
        }
        return callStateMachine;
    }
    public CallStateMachine setRole(int currentRole) {
        this.currentRole = currentRole;
        return this;
    }
    public CallStateMachine setToUserBean(UserBean toUserBean) {
        this.toUserBean = toUserBean;
        return this;
}
    public CallStateMachine setCallType(int callType) {
        this.callType = callType;
        return this;
    }

    public CallStateMachine setRoomId(int roomId) {
        this.roomId = roomId;
        return this;
    }

    public void hand(int action){
       currentState.handle(action,currentRole);
    }

    public void setCurrentState(ICallState currentState) {
        this.currentState = currentState;
    }
}
