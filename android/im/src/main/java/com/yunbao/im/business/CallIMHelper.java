package com.yunbao.im.business;

import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.event.AcceptCallEvent;
import com.yunbao.common.event.CancleCallEvent;
import com.yunbao.common.event.RefuseCallEvent;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.R;
import com.yunbao.im.activity.MediaCallActivity;
import com.yunbao.im.utils.ImMessageUtil;
import org.greenrobot.eventbus.EventBus;

public class CallIMHelper {

    public static final String IM_CHAT_CALL = "call";//通话消息
    public static final String METHOD = "method";
    public static final String ACTION = "action";
    public static final String CHAT_TYPE = "type";
    private static final String CONTENT = "content";
    private static final String AVATAR = "avatar";
    private static final String USER_NAME = "user_nickname";
    private static final String UID = "id";

    public static final int ACTION_CALL_START=0;   //发起通话
    public static final int ACTION_CALL_CANCEL = 1;//取消通话
    public static final int ACTION_CALL_ACCEPT = 2;//接受通话
    public static final int ACTION_CALL_REFUSE = 3;//拒绝通话
    public static final int ACTION_CALL_BUSY = 9;//正在忙碌

    public static void filterMessage(JSONObject obj, String senderId){
        if (obj == null) {
            return;
        }
        String method=obj.getString(METHOD);
        if(method.equals(IM_CHAT_CALL)){
            receiverCall(obj);
        }
    }


    public  static String actionString(int action,boolean isSelf){
        if(action==ACTION_CALL_START){
           return isSelf ?WordUtil.getString(R.string.im_type_chat_start_1):WordUtil.getString(R.string.im_type_chat_start_2);
        }else if(action==ACTION_CALL_REFUSE){
            return isSelf?WordUtil.getString(R.string.im_type_chat_refuse_1):WordUtil.getString(R.string.im_type_chat_refuse_2);
        }else if(action==ACTION_CALL_CANCEL){
            return isSelf?WordUtil.getString(R.string.im_type_chat_cancel_1):WordUtil.getString(R.string.im_type_chat_cancel_2);
        }else if(action==ACTION_CALL_BUSY){
            return isSelf?WordUtil.getString(R.string.im_type_chat_busy_1):WordUtil.getString(R.string.im_type_chat_busy_2);
        }
        return "";
    }


    private static void receiverCall(JSONObject obj) {
        int action=obj.getInteger(ACTION);
        if(action==ACTION_CALL_START){
            if(!isBusy(obj)){
              receiverStart(obj);
            }
        }else if(action==ACTION_CALL_CANCEL){
            receiverCancle(obj);
        }else if(action==ACTION_CALL_ACCEPT){
            receiverAccept(obj);
        }else if(action==ACTION_CALL_REFUSE){
            receiverRefuse(obj);
        }
    }
    private static boolean isBusy(JSONObject jsonObject) {
        if(MediaCallActivity.isBusy){
          sendBusy(jsonObject.getIntValue(CHAT_TYPE),jsonObject.getString(UID));
        }
        return MediaCallActivity.isBusy;
    }

    private static void receiverRefuse(JSONObject obj) {
        EventBus.getDefault().post(new RefuseCallEvent());
    }
    private static void receiverAccept(JSONObject obj) {
        EventBus.getDefault().post(new AcceptCallEvent());
    }
    private static void receiverCancle(JSONObject obj) {
        EventBus.getDefault().post(new CancleCallEvent());
    }

    private static void receiverStart(JSONObject obj) {
        int type=obj.getIntValue(CHAT_TYPE);
        UserBean userBean=new UserBean();
        userBean.setAvatar(obj.getString(AVATAR));
        userBean.setUserNiceName(obj.getString(USER_NAME));
        userBean.setId(obj.getString(UID));
        RouteUtil.forwardCallActivity(Constants.ROLE_AUDIENCE,Integer.parseInt(CommonAppConfig.getInstance().getUid()),type,userBean);
    }

    /*通知发起聊天*/
    public static void sendStart(int type,String toUid){
        UserBean userBean= CommonAppConfig.getInstance().getUserBean();
        if(userBean==null)
            return;
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject();
            jsonObject.put(CHAT_TYPE,type)
                    .put(METHOD,IM_CHAT_CALL)
                    .put(AVATAR,userBean.getAvatar())
                    .put(USER_NAME,userBean.getUserNiceName())
                    .put(UID,userBean.getId())
                    .put(ACTION,ACTION_CALL_START);
            ImMessageUtil.getInstance().sendCustomMessage(toUid, jsonObject.toString(), false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*通知取消聊天*/
    public static void sendCancle(int type,String toUid){
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject();
            jsonObject.put(METHOD,IM_CHAT_CALL);
            jsonObject.put(ACTION,ACTION_CALL_CANCEL);
            jsonObject.put(METHOD,IM_CHAT_CALL);
            jsonObject.put(CHAT_TYPE,type);
            ImMessageUtil.getInstance().sendCustomMessage(toUid, jsonObject.toString(), true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*通知接受聊天*/
    public static void sendAccept(int type,String toUid){
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject();
            jsonObject.put(METHOD,IM_CHAT_CALL);
            jsonObject.put(ACTION,ACTION_CALL_ACCEPT);
            jsonObject.put(METHOD,IM_CHAT_CALL);
            jsonObject.put(CHAT_TYPE,type);
            ImMessageUtil.getInstance().sendCustomMessage(toUid, jsonObject.toString(), true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*通知拒绝聊天*/
    public static void sendRefuse(int type,String toUid){
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject();
            jsonObject.put(ACTION,ACTION_CALL_REFUSE);
            jsonObject.put(METHOD,IM_CHAT_CALL);
            jsonObject.put(CHAT_TYPE,type);
            ImMessageUtil.getInstance().sendCustomMessage(toUid, jsonObject.toString(), true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*通知繁忙*/
    public static void sendBusy(int type,String toUid){
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject();
            jsonObject.put(ACTION,ACTION_CALL_BUSY);
            jsonObject.put(METHOD,IM_CHAT_CALL);
            jsonObject.put(CHAT_TYPE,type);
            ImMessageUtil.getInstance().sendCustomMessage(toUid, jsonObject.toString(), true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
