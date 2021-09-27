package com.wanyue.live.socket;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;

/**
 * Created by  on 2018/11/16.
 * 主播PK socket
 */

public class SocketLinkMicPkUtil {

    /**
     * 发起PK申请
     *
     * @param pkUid  对方主播的uid
     * @param stream 自己直播间的stream
     */
    public static void linkMicPkApply(SocketClient client, String pkUid, String stream) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_PK)
                .param("action", 1)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("level_anchor", u.getLevelAnchor())
                .param("sex", u.getSex())
                .param("uhead", u.getAvatar())
                .param("ct", "")
                .param("stream", stream)
                .param("pkuid", pkUid));
    }


    /**
     * 主播接受对方主播的Pk请求
     *
     * @param pkUid 对方主播的uid
     */
    public static void linkMicPkAccept(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_PK)
                .param("action", 2)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("pkuid", pkUid)
                .param("ct", ""));
    }

    /**
     * 主播拒绝其他主播的连麦请求
     */
    public static void linkMicPkRefuse(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_PK)
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
                .param("_method_", Constants.SOCKET_LINK_MIC_PK)
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
    public static void linkMicPkBusy(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_PK)
                .param("action", 7)
                .param("msgtype", 10)
                .param("pkuid", pkUid));
    }

    /**
     * 当收到主播连麦的请求时候主播无响应
     */
    public static void linkMicPkNotResponse(SocketClient client, String pkUid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC_PK)
                .param("action", 8)
                .param("msgtype", 10)
                .param("pkuid", pkUid));
    }


}
