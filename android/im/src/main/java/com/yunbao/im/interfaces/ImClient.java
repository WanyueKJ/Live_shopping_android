package com.yunbao.im.interfaces;

import android.content.Context;

import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.im.bean.ImMessageBean;
import com.yunbao.im.bean.ImMsgLocationBean;
import com.yunbao.im.bean.ImUserBean;

import java.io.File;
import java.util.List;


/**
 * Created by  on 2019/4/1.
 */

public interface ImClient {

    /**
     * 初始化
     */
    void init();

    /**
     * 登录IM
     */
    void loginImClient(String uid);

    /**
     * 登出IM
     */
    void logoutImClient();

    /**
     * 获取会话列表用户的uid，多个uid以逗号分隔
     */
    String getConversationUids();

    /**
     * 获取会话的最后一条消息的信息
     */
    List<ImUserBean> getLastMsgInfoList(List<ImUserBean> list);

    /**
     * 获取会话的最后一条消息的信息
     */
    ImUserBean getLastMsgInfo(String uid);

    /**
     * 获取订单列表
     */
    void getOrderMsgList(CommonCallback<List<String>> callback);

    /**
     * 获取消息列表
     */
    void getChatMessageList(String toUid, CommonCallback<List<ImMessageBean>> callback);

    /**
     * 获取某个会话的未读消息数
     */
    int getUnReadMsgCount(String uid);

    /**
     * 刷新全部未读消息的总数
     */
    void refreshAllUnReadMsgCount();

    /**
     * 获取全部未读消息的总数
     */
    String getAllUnReadMsgCount();

    /**
     * 设置某个会话的消息为已读
     *
     * @param toUid 对方uid
     */
    void markAllMessagesAsRead(String toUid, boolean needRefresh);

    /**
     * 标记所有会话为已读  即忽略所有未读
     */
    void markAllConversationAsRead();


    /**
     * 创建文本消息
     *
     * @param toUid
     * @param content
     * @return
     */
    public ImMessageBean createTextMessage(String toUid, String content);

    /**
     * 创建图片消息
     *
     * @param toUid 对方的id
     * @param path  图片路径
     * @return
     */
    ImMessageBean createImageMessage(String toUid, String path);

    /**
     * 获取图片文件
     */
    void displayImageFile(Context context, ImMessageBean bean, CommonCallback<File> commonCallback);

    /**
     * 获取语音文件
     */
    void getVoiceFile(final ImMessageBean bean, final CommonCallback<File> commonCallback);

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
    ImMessageBean createLocationMessage(String toUid, double lat, double lng, int scale, String address);

    /**
     * 创建语音消息
     *
     * @param toUid
     * @param voiceFile 语音文件
     * @param duration  语音时长
     * @return
     */
    ImMessageBean createVoiceMessage(String toUid, File voiceFile, long duration);


    /**
     * 发送自定义消息
     *
     * @param toUid
     * @param data  要发送的数据
     * @return
     */
    void sendCustomMessage(String toUid, String data, boolean save);


    /**
     * 发送消息
     *
     * @param toUid 对方的uid
     */
    public void sendMessage(String toUid, ImMessageBean bean, SendMsgResultCallback callback);


    public void removeMessage(String toUid, ImMessageBean bean);

    /**
     * 删除所有会话记录
     */
    void removeAllConversation();

    /**
     * 删除会话中的所有消息，但不会删除会话本身。
     */
    void removeAllMessage(String toUid);

    /**
     * 删除会话记录
     */
    void removeConversation(String uid);

    /**
     * 刷新聊天列表的最后一条消息
     */
    void refreshLastMessage(String uid, ImMessageBean bean);

    /**
     * 语音消息设为已读
     */
    void setVoiceMsgHasRead(ImMessageBean bean, Runnable runnable);

    /**
     * 获取文本消息的文本
     */
    String getMessageText(ImMessageBean bean);

    /**
     * 获取位置消息的位置信息
     */
    ImMsgLocationBean getMessageLocation(ImMessageBean bean);

    /**
     * 是否打开聊天Activity
     */
    void setOpenChatActivity(boolean openChatActivity);

    /**
     * 是否关闭聊天提示音
     */
    void setCloseChatMusic(boolean closeChatMusic);


    void refreshMsgTypeString();
}
