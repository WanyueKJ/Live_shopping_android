package com.wanyue.live.socket;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;

/**
 * Created by  on 2018/10/25.
 */

public class SocketLinkMicUtil {

    /**
     * 观众申请连麦
     */
    public static void audienceApplyLinkMic(SocketClient client) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 1)
                .param("msgtype", 10)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("sex", u.getSex())
                .param("uhead", u.getAvatar())
                .param("ct", ""));
    }

    /**
     * 主播同意观众连麦请求
     */
    public static void anchorAcceptLinkMic(SocketClient client, String toUid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 2)
                .param("msgtype", 10)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("touid", toUid)
                .param("ct", ""));
    }


    /**
     * 主播拒绝观众连麦请求
     */
    public static void anchorRefuseLinkMic(SocketClient client, String touid) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 3)
                .param("msgtype", 10)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("touid", touid)
                .param("ct", ""));
    }

    /**
     * 主播同意连麦后，观众把自己的流地址发送给主播
     */
    public static void audienceSendLinkMicUrl(SocketClient client, String playUrl) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 4)
                .param("msgtype", 10)
                .param("uid", u.getId())
                .param("ct", "")
                .param("uname", u.getUserNiceName())
                .param("level", u.getLevel())
                .param("playurl", playUrl));
    }

    /**
     * 观众断开连麦
     */
    public static void audienceCloseLinkMic(SocketClient client) {
        if (client == null) {
            return;
        }
        UserBean u =
  CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 5)
                .param("msgtype", 10)
                .param("uid", u.getId())
                .param("ct", "")
                .param("uname", u.getUserNiceName()));
    }

    /**
     * 主播断开某人的连麦
     */
    public static void anchorCloseLinkMic(SocketClient client, String touid, String uname) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 6)
                .param("msgtype", 10)
                .param("touid", touid)
                .param("ct", "")
                .param("uname", uname));
    }


    /**
     * 主播正在忙
     */
    public static void anchorBusy(SocketClient client, String touid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 7)
                .param("msgtype", 10)
                .param("touid", touid));
    }

    /**
     * 主播未响应
     */
    public static void anchorNotResponse(SocketClient client, String touid) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LINK_MIC)
                .param("action", 8)
                .param("msgtype", 10)
                .param("touid", touid));
    }
}
