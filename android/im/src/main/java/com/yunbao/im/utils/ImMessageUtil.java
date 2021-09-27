package com.yunbao.im.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.im.bean.ImMessageBean;
import com.yunbao.im.bean.ImMsgLocationBean;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.interfaces.ImClient;
import com.yunbao.im.interfaces.SendMsgResultCallback;

import java.io.File;
import java.util.List;

/**
 * Created by  on 2017/8/10.
 * 极光IM注册、登陆等功能
 */

public class ImMessageUtil {

    private static ImMessageUtil sInstance;
    private ImClient mImClient;

    private ImMessageUtil() {
        //mImClient = new JimMessageUtil();
        mImClient = new TxImMessageUtil();
    }

    public static ImMessageUtil getInstance() {
        if (sInstance == null) {
            synchronized (ImMessageUtil.class) {
                if (sInstance == null) {
                    sInstance = new ImMessageUtil();
                }
            }
        }
        return sInstance;
    }

    public void init() {
        if (mImClient != null) {
            mImClient.init();
        }
    }

    /**
     * 登录极光IM
     */
    public void loginImClient(String uid) {
        if (mImClient != null) {
            mImClient.loginImClient(uid);
        }
    }


    /**
     * 登出IM
     */
    public void logoutImClient() {
        if (mImClient != null) {
            mImClient.logoutImClient();
        }
    }

    /**
     * 获取会话列表用户的uid，多个uid以逗号分隔
     */
    public String getConversationUids() {
        if (mImClient != null) {
            return mImClient.getConversationUids();
        }
        return "";
    }

    /**
     * 获取会话的最后一条消息的信息
     */
    public List<ImUserBean> getLastMsgInfoList(List<ImUserBean> list) {
        if (mImClient != null) {
            return mImClient.getLastMsgInfoList(list);
        }
        return null;
    }

    /**
     * 获取会话的最后一条消息的信息
     */
    public ImUserBean getLastMsgInfo(String uid) {
        if (mImClient != null) {
            return mImClient.getLastMsgInfo(uid);
        }
        return null;
    }


    /**
     * 获取订单列表
     */
    public void getOrderMsgList(CommonCallback<List<String>> callback) {
        if (mImClient != null) {
            mImClient.getOrderMsgList(callback);
        }
    }


    /**
     * 获取消息列表
     */
    public void getChatMessageList(String toUid, CommonCallback<List<ImMessageBean>> callback) {
        if (mImClient != null) {
            mImClient.getChatMessageList(toUid, callback);
        }
    }

    /**
     * 获取某个会话的未读消息数
     */
    public int getUnReadMsgCount(String uid) {
        if (mImClient != null) {
            return mImClient.getUnReadMsgCount(uid);
        }
        return 0;
    }

    /**
     * 刷新全部未读消息的总数
     */
    public void refreshAllUnReadMsgCount() {
        if (mImClient != null) {
            mImClient.refreshAllUnReadMsgCount();
        }
    }

    /**
     * 获取全部未读消息的总数
     */
    public String getAllUnReadMsgCount() {
        if (mImClient != null) {
            return mImClient.getAllUnReadMsgCount();
        }
        return "0";
    }


    /**
     * 设置某个会话的消息为已读
     *
     * @param toUid 对方uid
     */
    public void markAllMessagesAsRead(String toUid, boolean needRefresh) {
        if (mImClient != null) {
            mImClient.markAllMessagesAsRead(toUid, needRefresh);
        }
    }

    /**
     * 标记所有会话为已读  即忽略所有未读
     */
    public void markAllConversationAsRead() {
        if (mImClient != null) {
            mImClient.markAllConversationAsRead();
        }
    }

    /**
     * 创建文本消息
     *
     * @param toUid
     * @param content
     * @return
     */
    public ImMessageBean createTextMessage(String toUid, String content) {
        if (mImClient != null) {
            return mImClient.createTextMessage(toUid, content);
        }
        return null;
    }

    /**
     * 创建图片消息
     *
     * @param toUid 对方的id
     * @param path  图片路径
     * @return
     */
    public ImMessageBean createImageMessage(String toUid, String path) {
        if (mImClient != null) {
            return mImClient.createImageMessage(toUid, path);
        }
        return null;
    }

    /**
     * 获取图片文件
     */
    public void displayImageFile(Context context, ImMessageBean bean, CommonCallback<File> commonCallback) {
        if (mImClient != null) {
            mImClient.displayImageFile(context, bean, commonCallback);
        }
    }

    /**
     * 获取语音文件
     */
    public void getVoiceFile(ImMessageBean bean, CommonCallback<File> commonCallback) {
        if (mImClient != null) {
            mImClient.getVoiceFile(bean, commonCallback);
        }
    }

    /**
     * 创建位置消息
     *
     * @param toUid
     * @param lat     纬度
     * @param lng     经度
     * @param scale   缩放比例
     * @param address 位置详细地址
     * @return
     */
    public ImMessageBean createLocationMessage(String toUid, double lat, double lng, int scale, String address) {
        if (mImClient != null) {
            return mImClient.createLocationMessage(toUid, lat, lng, scale, address);
        }
        return null;
    }

    /**
     * 创建语音消息
     *
     * @param toUid
     * @param voiceFile 语音文件
     * @param duration  语音时长
     * @return
     */
    public ImMessageBean createVoiceMessage(String toUid, File voiceFile, long duration) {
        if (mImClient != null) {
            return mImClient.createVoiceMessage(toUid, voiceFile, duration);
        }
        return null;
    }


    /**
     * 发送消息
     */
    public void sendMessage(String toUid, ImMessageBean bean, SendMsgResultCallback callback) {
        if (mImClient != null) {
            mImClient.sendMessage(toUid, bean, callback);
        }
    }

    /**
     * 发送自定义消息
     */

    public void sendCustomMessage(String toUid, String data) {
        sendCustomMessage(toUid, data, true);
    }



    /**
     * 发送自定义消息
     */

    public void sendCustomMessage(String toUid, String data, boolean save) {
        if (mImClient != null) {
            mImClient.sendCustomMessage(toUid, data, save);
        }
    }


    public void removeMessage(String toUid, ImMessageBean bean) {
        if (mImClient != null) {
            mImClient.removeMessage(toUid, bean);
        }
    }

    /**
     * 删除所有会话记录
     */
    public void removeAllConversation() {
        if (mImClient != null) {
            mImClient.removeAllConversation();
        }
    }

    /**
     * 删除会话中的所有消息，但不会删除会话本身。
     */
    public void removeAllMessage(String toUid) {
        if (mImClient != null) {
            mImClient.removeAllMessage(toUid);
        }
    }

    /**
     * 删除会话记录
     */
    public void removeConversation(String uid) {
        if (mImClient != null) {
            mImClient.removeConversation(uid);
        }
    }


    /**
     * 刷新聊天列表的最后一条消息
     */
    public void refreshLastMessage(String uid, ImMessageBean bean) {
        if (mImClient != null) {
            mImClient.refreshLastMessage(uid, bean);
        }
    }

    /**
     * 设为已读
     */
    public void setVoiceMsgHasRead(ImMessageBean bean, Runnable runnable) {
        if (mImClient != null) {
            mImClient.setVoiceMsgHasRead(bean, runnable);
        }
    }

    /**
     * 获取文本消息中的文本
     */
    public String getMessageText(ImMessageBean bean) {
        if (mImClient != null) {
            return mImClient.getMessageText(bean);
        }
        return "";
    }

    /**
     * 获取位置消息的位置信息
     */
    public ImMsgLocationBean getMessageLocation(ImMessageBean bean) {
        if (mImClient != null) {
            return mImClient.getMessageLocation(bean);
        }
        return null;
    }

    /**
     * 是否打开聊天Activity
     */
    public void setOpenChatActivity(boolean openChatActivity) {
        if (mImClient != null) {
            mImClient.setOpenChatActivity(openChatActivity);
        }
    }

    /**
     * 是否关闭聊天提示音
     */
    public void setCloseChatMusic(boolean closeChatMusic) {
        if (mImClient != null) {
            mImClient.setCloseChatMusic(closeChatMusic);
        }
    }


    public void refreshMsgTypeString(){
        if (mImClient != null) {
            mImClient.refreshMsgTypeString();
        }
    }
}
