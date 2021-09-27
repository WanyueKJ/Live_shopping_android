package com.wanyue.live.socket;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.GlobalGiftBean;
import com.wanyue.live.bean.LiveBuyGuardMsgBean;
import com.wanyue.live.bean.LiveChatBean;
import com.wanyue.live.bean.LiveDanMuBean;
import com.wanyue.live.bean.LiveEnterRoomBean;
import com.wanyue.live.bean.LiveGiftPrizePoolWinBean;
import com.wanyue.live.bean.LiveLuckGiftWinBean;
import com.wanyue.live.bean.LiveReceiveGiftBean;
import com.wanyue.live.bean.LiveUserGiftBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by  on 2018/10/9.
 */

public class SocketClient {

    private static final String TAG = "socket";
    private Socket mSocket;
    private String mLiveUid;
    private String mStream;
    private String mToken;
    private SocketHandler mSocketHandler;

    public SocketClient(String url, SocketMessageListener listener) {
        if (!TextUtils.isEmpty(url)) {
            try {
                IO.Options option = new IO.Options();
                option.forceNew = true;
                option.reconnection = true;
                option.reconnectionDelay = 2000;
                mSocket = IO.socket(url, option);
                mSocket.on(Socket.EVENT_CONNECT, mConnectListener);//连接成功
                mSocket.on(Socket.EVENT_DISCONNECT, mDisConnectListener);//断开连接
                mSocket.on(Socket.EVENT_CONNECT_ERROR, mErrorListener);//连接错误
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, mTimeOutListener);//连接超时
                mSocket.on(Socket.EVENT_RECONNECT, mReConnectListener);//重连
                mSocket.on(Constants.SOCKET_CONN, onConn);//连接socket消息
                mSocket.on(Constants.SOCKET_BROADCAST, onBroadcast);//接收服务器广播的具体业务逻辑相关的消息
                mSocketHandler = new SocketHandler(listener);
            } catch (Exception e) {
                L.e(TAG, "socket url 异常--->" + e.getMessage());
            }
        }
    }


    public void connect(String liveuid, String stream, String token) {
        mLiveUid = liveuid;
        mStream = stream;
        mToken = token;
        if (mSocket != null) {
            mSocket.connect();
        }
        if (mSocketHandler != null) {
            mSocketHandler.setLiveUid(liveuid);
        }
    }

    public void disConnect() {
        if (mSocket != null) {
            mSocket.close();
            mSocket.off();
        }
        if (mSocketHandler != null) {
            mSocketHandler.release();
        }
        mSocketHandler = null;
        mLiveUid = null;
        mStream = null;
        mToken = null;
    }

    /**
     * 向服务发送连接消息
     */
    private void conn() {
        org.json.JSONObject data = new org.json.JSONObject();

        try {
            data.put("uid",
                    CommonAppConfig.getUid());
            data.put("token",
                    mToken);
            data.put("liveuid", mLiveUid);
            data.put("roomnum", mLiveUid);
            data.put("stream", mStream);
            mSocket.emit("conn", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Emitter.Listener mConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            L.e(TAG, "--onConnect-->" + args);
            conn();
        }
    };

    private Emitter.Listener mReConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            L.e(TAG, "--reConnect-->" + args);
            //conn();
        }
    };

    private Emitter.Listener mDisConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            L.e(TAG, "--onDisconnect-->" + args);
            if (mSocketHandler != null) {
                mSocketHandler.sendEmptyMessage(Constants.SOCKET_WHAT_DISCONN);
            }
        }
    };
    private Emitter.Listener mErrorListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            L.e(TAG, "--onConnectError-->" + args);
        }
    };

    private Emitter.Listener mTimeOutListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            L.e(TAG, "--onConnectTimeOut-->" + args);
        }
    };

    private Emitter.Listener onConn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketHandler != null) {
                try {
                    String s = ((JSONArray) args[0]).getString(0);
                    L.e(TAG, "--onConn-->" + s);
                    Message msg = Message.obtain();
                    msg.what = Constants.SOCKET_WHAT_CONN;
                    msg.obj = s.equals("ok");
                    mSocketHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Emitter.Listener onBroadcast = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocketHandler != null) {
                try {
                    JSONArray array = (JSONArray) args[0];
                    for (int i = 0; i < array.length(); i++) {
                        Message msg = Message.obtain();
                        msg.what = Constants.SOCKET_WHAT_BROADCAST;
                        msg.obj = array.getString(i);
                        if (mSocketHandler != null) {
                            mSocketHandler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };


    public void send(SocketSendBean bean) {
        if (mSocket != null) {
            mSocket.emit(Constants.SOCKET_SEND, bean.create());
        }
    }

    private static class SocketHandler extends Handler {

        private SocketMessageListener mListener;
        private String mLiveUid;

        public SocketHandler(SocketMessageListener listener) {
            mListener = new WeakReference<>(listener).get();
        }

        public void setLiveUid(String liveUid) {
            mLiveUid = liveUid;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListener == null) {
                return;
            }
            switch (msg.what) {
                case Constants.SOCKET_WHAT_CONN:
                    mListener.onConnect((Boolean) msg.obj);
                    break;
                case Constants.SOCKET_WHAT_BROADCAST:
                    try {
                        processBroadcast((String) msg.obj);
                    } catch (Exception e) {
                        CrashReport.postCatchedException(new Exception("原始异常的提示==" + e));
                        //ToastUtil.show("socket数据异常,请前往bugly查看");
                        CrashReport.postCatchedException(new Exception("socket数据==" + (String) msg.obj, e.getCause()));
                        e.printStackTrace();
                    }
                    break;
                case Constants.SOCKET_WHAT_DISCONN:
                    mListener.onDisConnect();
                    break;

            }
        }


        private void processBroadcast(String socketMsg) {
            L.e("收到socket--->" + socketMsg);
            if (Constants.SOCKET_STOP_PLAY.equals(socketMsg)) {
                mListener.onSuperCloseLive();//超管关闭房间
                return;
            }
            SocketReceiveBean received = JSON.parseObject(socketMsg, SocketReceiveBean.class);
            JSONObject map = received.getMsg().getJSONObject(0);
            switch (map.getString("_method_")) {
                case Constants.SOCKET_SYSTEM://系统消息
                    systemChatMessage(map.getString("ct"));
                    break;
                case Constants.SOCKET_KICK://踢人
                    systemChatMessage(map.getString("ct"));
                    mListener.onKick(map.getString("touid"));
                    break;
                case Constants.SOCKET_SHUT_UP://禁言
                    int actionData = map.getIntValue("action");
                    String content= map.getString("content");
                    systemChatMessage(content);
                    //mListener.onShutUp(map.getString("touid"), actionData, content);
                    break;
                case Constants.SOCKET_SEND_MSG://文字消息，点亮，用户进房间，这种混乱的设计是因为服务器端逻辑就是这样设计的,客户端无法自行修改
                    int action = map.getIntValue("action");
                    if (action == 1) {//发言，点亮
                        LiveChatBean chatBean = new LiveChatBean();
                        chatBean.setId(map.getString("uid"));
                        chatBean.setUserNiceName(map.getString("usernickname"));
                        chatBean.setUserType(map.getIntValue("usertype"));
                        chatBean.setContent(map.getString("content"));
                        chatBean.setAvatar(map.getString("avatar"));
                        chatBean.setIsFollow(map.getIntValue("isattent"));
                        int heart = map.getIntValue("heart");
                        chatBean.setHeart(heart);
                        if (heart > 0) {
                            chatBean.setType(LiveChatBean.LIGHT);
                            chatBean.setContent(WordUtil.getString(R.string.live_lighted));
                        }
                        mListener.onChat(chatBean);
                    }else if (action==409002){
                        ToastUtil.show(R.string.live_you_are_shut);
                    }else if (action == 0) {//用户进入房间
                        JSONObject obj = JSON.parseObject(map.getString("ct"));
                        LiveChatBean chatBean = new LiveChatBean();
                        chatBean.setType(LiveChatBean.ENTER_ROOM);
                        chatBean.setId(obj.getString("uid"));
                        chatBean.setUserNiceName(obj.getString("name"));
                        chatBean.setAvatar(obj.getString("avatar"));
                        chatBean.setUserType(obj.getIntValue("usertype"));
                        chatBean.setContent(WordUtil.getString(R.string.live_enter_room));
                        mListener.onEnterRoom(new LiveEnterRoomBean(null, chatBean));
                    }
                    break;
                case Constants.SOCKET_LIGHT://飘心
                    LiveChatBean chatBean1 = new LiveChatBean();
                    chatBean1.setId(map.getString("uid"));
                    chatBean1.setUserNiceName(map.getString("usernickname"));
                    chatBean1.setAnchor(map.getIntValue("isAnchor") == 1);
                    chatBean1.setUserType(map.getIntValue("usertype"));
                    chatBean1.setContent(WordUtil.getString(R.string.live_lighted));
                    chatBean1.setAvatar(map.getString("avatar"));
                    chatBean1.setIsFollow(map.getIntValue("isattent"));
                    chatBean1.setType(LiveChatBean.LIGHT);
                    mListener.onLight(chatBean1);

                    break;
                case Constants.SOCKET_SEND_GIFT://送礼物
                    LiveReceiveGiftBean receiveGiftBean = JSON.parseObject(map.getString("ct"), LiveReceiveGiftBean.class);
                    receiveGiftBean.setAvatar(map.getString("uhead"));
                    receiveGiftBean.setUserNiceName(map.getString("uname"));
                    LiveChatBean chatBean = new LiveChatBean();
                    chatBean.setUserNiceName(receiveGiftBean.getUserNiceName());
                    chatBean.setLevel(receiveGiftBean.getLevel());
                    chatBean.setId(map.getString("uid"));
                    /*chatBean.setLiangName(map.getString("liangname"));
                    chatBean.setVipType(map.getIntValue("vip_type"));*/
                    chatBean.setType(LiveChatBean.GIFT);
                    chatBean.setContent(WordUtil.getString(R.string.live_send_gift_1) + receiveGiftBean.getGiftCount() + WordUtil.getString(R.string.live_send_gift_2) + receiveGiftBean.getGiftName());
                    receiveGiftBean.setLiveChatBean(chatBean);
                    if (map.getIntValue("ifpk") == 1) {
                        if (!TextUtils.isEmpty(mLiveUid)) {
                            if (mLiveUid.equals(map.getString("roomnum"))) {
                                mListener.onSendGift(receiveGiftBean);
                                mListener.onSendGiftPk(map.getLongValue("pktotal1"), map.getLongValue("pktotal2"));
                            } else {
                                mListener.onSendGiftPk(map.getLongValue("pktotal2"), map.getLongValue("pktotal1"));
                            }
                        }
                    } else {
                        mListener.onSendGift(receiveGiftBean);
                    }

                    break;
                case Constants.SOCKET_SEND_BARRAGE://发弹幕
                    LiveDanMuBean liveDanMuBean = JSON.parseObject(map.getString("ct"), LiveDanMuBean.class);
                    liveDanMuBean.setAvatar(map.getString("uhead"));
                    liveDanMuBean.setUserNiceName(map.getString("uname"));
                    mListener.onSendDanMu(liveDanMuBean);
                    break;
                case Constants.SOCKET_LEAVE_ROOM://离开房间
                    UserBean u = JSON.parseObject(map.getString("ct"), UserBean.class);
                    mListener.onLeaveRoom(u);
                    break;
                case Constants.SOCKET_LIVE_END://主播关闭直播
                    action = map.getIntValue("action");
                    mListener.onLiveEnd();
                    break;
                case Constants.SOCKET_CHANGE_LIVE://主播切换计时收费类型
                    mListener.onChangeTimeCharge(map.getIntValue("type_val"));
                    break;

                case Constants.SOCKET_SET_ADMIN://设置或取消管理员
                    //systemChatMessage(map.getString("ct"));
                    mListener.onSetAdmin(map.getString("touid"), map.getIntValue("action"));
                    break;

                case Constants.SOCKET_WARN://主播警告
                    mListener.onWarn(map.getString("ct"));
                    break;

                case Constants.SOCKET_LINK_MIC://连麦
                    processLinkMic(map);
                    break;
                case Constants.SOCKET_LINK_MIC_ANCHOR://主播连麦
                    processLinkMicAnchor(map);
                    break;
                case Constants.SOCKET_LINK_MIC_PK://主播PK
                    processAnchorLinkMicPk(map);
                    break;


                case Constants.SOCKET_LUCK_WIN://幸运礼物中奖
                    mListener.onLuckGiftWin(map.toJavaObject(LiveLuckGiftWinBean.class));
                    break;

                case Constants.SOCKET_PRIZE_POOL_WIN://奖池中奖
                    mListener.onPrizePoolWin(map.toJavaObject(LiveGiftPrizePoolWinBean.class));
                    break;
                case Constants.SOCKET_PRIZE_POOL_UP://奖池升级
                    mListener.onPrizePoolUp(map.getString("uplevel"));
                    break;

                case Constants.SOCKET_GIFT_GLOBAL://全站礼物
                    mListener.onGlobalGift(map.toJavaObject(GlobalGiftBean.class));
                    break;
                default:
                    break;
            }
        }


        /**
         * 接收到系统消息，显示在聊天栏中
         */
        private void systemChatMessage(String content) {
            LiveChatBean bean = new LiveChatBean();
            bean.setContent(content);
            bean.setType(LiveChatBean.SYSTEM);
            mListener.onChat(bean);
        }

        /**
         * 处理观众与主播连麦逻辑
         */
        private void processLinkMic(JSONObject map) {
            int action = map.getIntValue("action");
            switch (action) {
                case 1://主播收到观众连麦的申请
                    UserBean u = new UserBean();
                    u.setId(map.getString("uid"));
                    u.setUserNiceName(map.getString("uname"));
                    u.setAvatar(map.getString("uhead"));
                    u.setSex(map.getIntValue("sex"));
                    u.setLevel(map.getIntValue("level"));
                    mListener.onAudienceApplyLinkMic(u);
                    break;
                case 2://观众收到主播同意连麦的消息
                    if (map.getString("touid").equals(
                            CommonAppConfig.getUid())) {
                        mListener.onAnchorAcceptLinkMic();
                    }
                    break;
                case 3://观众收到主播拒绝连麦的消息
                    if (map.getString("touid").equals(
                            CommonAppConfig.getUid())) {
                        mListener.onAnchorRefuseLinkMic();
                    }
                    break;
                case 4://所有人收到连麦观众发过来的流地址
                    String uid = map.getString("uid");
                    if (!TextUtils.isEmpty(uid) && !uid.equals(
                            CommonAppConfig.getUid())) {
                        mListener.onAudienceSendLinkMicUrl(uid, map.getString("uname"), map.getString("playurl"));
                    }
                    break;
                case 5://连麦观众自己断开连麦
                    mListener.onAudienceCloseLinkMic(map.getString("uid"), map.getString("uname"));
                    break;
                case 6://主播断开已连麦观众的连麦
                    mListener.onAnchorCloseLinkMic(map.getString("touid"), map.getString("uname"));
                    break;
                case 7://已申请连麦的观众收到主播繁忙的消息
                    if (map.getString("touid").equals(
                            CommonAppConfig.getUid())) {
                        mListener.onAnchorBusy();
                    }
                    break;
                case 8://已申请连麦的观众收到主播无响应的消息
                    if (map.getString("touid").equals(
                            CommonAppConfig.getUid())) {
                        mListener.onAnchorNotResponse();
                    }
                    break;
                case 9://所有人收到已连麦的观众退出直播间消息
                    mListener.onAudienceLinkMicExitRoom(map.getString("touid"));
                    break;
            }
        }

        /**
         * 处理主播与主播连麦逻辑
         *
         * @param map
         */
        private void processLinkMicAnchor(JSONObject map) {
            int action = map.getIntValue("action");
            switch (action) {
                case 1://收到其他主播连麦的邀请的回调
                    UserBean u = new UserBean();
                    u.setId(map.getString("uid"));
                    u.setUserNiceName(map.getString("uname"));
                    u.setAvatar(map.getString("uhead"));
                    u.setSex(map.getIntValue("sex"));
                    u.setLevel(map.getIntValue("level"));
                    u.setLevelAnchor(map.getIntValue("level_anchor"));
                    mListener.onLinkMicAnchorApply(u, map.getString("stream"));
                    break;
                case 3://对方主播拒绝连麦的回调
                    mListener.onLinkMicAnchorRefuse();
                    break;
                case 4://所有人收到对方主播的播流地址的回调
                    mListener.onLinkMicAnchorPlayUrl(map.getString("pkuid"), map.getString("pkpull"));
                    break;
                case 5://断开连麦的回调
                    mListener.onLinkMicAnchorClose();
                    break;
                case 7://对方主播正在忙的回调
                    mListener.onLinkMicAnchorBusy();
                    break;
                case 8://对方主播无响应的回调
                    mListener.onLinkMicAnchorNotResponse();
                    break;
                case 9://对方主播正在游戏
                    mListener.onlinkMicPlayGaming();
                    break;
            }
        }


        /**
         * 处理主播与主播PK逻辑
         *
         * @param map
         */
        private void processAnchorLinkMicPk(JSONObject map) {
            int action = map.getIntValue("action");
            switch (action) {
                case 1://收到对方主播PK回调
                    UserBean u = new UserBean();
                    u.setId(map.getString("uid"));
                    u.setUserNiceName(map.getString("uname"));
                    u.setAvatar(map.getString("uhead"));
                    u.setSex(map.getIntValue("sex"));
                    u.setLevel(map.getIntValue("level"));
                    u.setLevelAnchor(map.getIntValue("level_anchor"));
                    mListener.onLinkMicPkApply(u, map.getString("stream"));
                    break;
                case 3://对方主播拒绝PK的回调
                    mListener.onLinkMicPkRefuse();
                    break;
                case 4://所有人收到PK开始址的回调
                    mListener.onLinkMicPkStart(map.getString("pkuid"));
                    break;
                case 5://PK时候断开连麦的回调
                    mListener.onLinkMicPkClose();
                    break;
                case 7://对方主播正在忙的回调
                    mListener.onLinkMicPkBusy();
                    break;
                case 8://对方主播无响应的回调
                    mListener.onLinkMicPkNotResponse();
                    break;
                case 9://pk结束的回调
                    mListener.onLinkMicPkEnd(map.getString("win_uid"));
                    break;
            }
        }

        public void release() {
            mListener = null;
        }
    }
}
