package com.wanyue.live.socket;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;

/**
 * Created by  on 2018/11/16.
 * 主播连麦 socket
 */

public class SocketLinkMicAnchorUtil {

    /**
     * 发起连麦申请
     *
     * @param myPlayUrl 自己的播流地址
     * @param myStream  自己直播间的stream
     * @param pkUid     对方主播的uid
     */
    public static void linkMicAnchorApply(SocketClient client, String myPlayUrl, String myStream, String pkUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 1)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("level_anchor", u.getLevelAnchor())
                .param("sex", u.getSex())
                .param("uhead", u.getAvatar())
                .param("ct", "")
                .param("stream", myStream)
                .param("pkpull", myPlayUrl)
                .param("pkuid", pkUid));
    }


    /**
     * 主播接受其他主播的连麦请求
     *
     * @param myPlayUrl 自己的播流地址
     * @param pkUid     对方主播的uid
     */
    public static void linkMicAnchorAccept(SocketClient client, String myPlayUrl, String pkUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 2)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("pkuid", pkUid)
                .param("pkpull", myPlayUrl)
                .param("ct", ""));
    }

    /**
     * 主播拒绝其他主播的连麦请求
     */
    public static void linkMicAnchorRefuse(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 3)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("pkuid", pkUid)
                .param("ct", ""));
    }


    /**
     * 主播断开连麦
     */
    public static void linkMicAnchorClose(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 5)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("ct", "")
                .param("pkuid", pkUid)
                .param("uname", u.getUserNiceName()));
    }

    /**
     * 当收到主播连麦的请求时候主播正在忙
     */
    public static void linkMicAnchorBusy(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 7)
                .param("msgtype", 0)
                .param("pkuid", pkUid));
    }

    /**
     * 当收到主播连麦的请求时候主播无响应
     */
    public static void linkMicNotResponse(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 8)
                .param("msgtype", 0)
                .param("pkuid", pkUid));
    }


    /**
     * 当收到主播连麦的请求时候对方主播正在游戏中
     */
    public static void linkMicPlayGaming(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_ANCHOR)
                .param("action", 9)
                .param("msgtype", 0)
                .param("pkuid", pkUid));
    }

}
