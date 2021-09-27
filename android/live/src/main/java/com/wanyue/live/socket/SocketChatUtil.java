package com.wanyue.live.socket;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordFilterUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/9.
 * 直播间发言
 */

public class SocketChatUtil {

    /**
     * 发言
     */
    public static void sendChatMessage(SocketClient client, String content, boolean isAnchor, int isattent, int userType) {
        if (client == null) {
            return;
        }
        UserBean u = CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        content = WordFilterUtil.getInstance().filter(content);
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SEND_MSG)
                .param("action", 1)
                .param("isAnchor", isAnchor ? 1 : 0)
                .param("usernickname", u.getUserNiceName())
                .param("uid", u.getId())
                .param("isattent", isattent)
                .param("usertype", userType)
                .param("content", content));
    }

    /**
     * 点亮
     */
    public static void sendLightMessage(SocketClient client, int heart) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LIGHT)
                .param("action", 0)
                .param("usernickname", u.getUserNiceName())
                .param("uid", u.getId()));
    }

    /**
     * 发送飘心消息
     */
    public static void sendFloatHeart(SocketClient client) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LIGHT)
                .param("action", 2)
                .param("msgtype", 0)
                .param("ct", ""));
    }

    /**
     * 发送弹幕消息
     */
    public static void sendDanmuMessage(SocketClient client, String danmuToken) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SEND_BARRAGE)
                .param("action", 7)
                .param("msgtype", 1)
                .param("level", u.getLevel())
                .param("uname", u.getUserNiceName())
                .param("uid", u.getId())
                .param("uhead", u.getAvatar())
                .param("ct", danmuToken));
    }


    /**
     * 发送礼物消息
     */
    public static void sendGiftMessage(SocketClient client, int giftType, String giftToken,
                                       String liveUid, String liveName) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SEND_GIFT)
                .param("action", 0)
                .param("msgtype", 1)
                .param("level", u.getLevel())
                .param("uname", u.getUserNiceName())
                .param("uid", u.getId())
                .param("uhead", u.getAvatar())
                .param("evensend", giftType)
                .param("liangname", u.getGoodName())
                .param("vip_type", u.getVip())
                .param("ct", giftToken)
                .param("roomnum", liveUid)
                .param("livename", liveName)
        );
    }


    /**
     * 主播或管理员 踢人
     */
    public static void sendKickMessage(SocketClient client, String toUid, String toName) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_KICK)
                .param("action", 2)
                .param("msgtype", 4)
                .param("level", u.getLevel())
                .param("uname", u.getUserNiceName())
                .param("uid", u.getId())
                .param("touid", toUid)
                .param("toname", toName)
                .param("ct", toName + WordUtil.getString(R.string.live_kicked)));
    }


    /**
     * 主播或管理员 禁言
     */
    public static void sendShutUpMessage(SocketClient client, String toUid, String toName, boolean isShut, String name) {
        if (client == null) {
            return;
        }
        int action = isShut ? 1 : 2;
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SHUT_UP)
                .param("action", action)
                .param("touid", toUid)
                .param("toname", toName)
                .param("content", StringUtil.contact(toName,"被",name))
        );

    }

    /**
     * 设置或取消管理员消息
     */

    public static void sendSetAdminMessage(SocketClient client, boolean isSetAdmin, String toUid, String toName) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        String s = isSetAdmin ? WordUtil.getString(R.string.live_set_admin) : WordUtil.getString(R.string.live_set_admin_cancel);
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SET_ADMIN)
                .param("action", isSetAdmin ? 1 : 0)
                .param("msgtype", 1)
                .param("uname", u.getUserNiceName())
                .param("uid", u.getId())
                .param("touid", toUid)
                .param("toname", toName)
                .param("ct", toName + " " + s));
    }

    /**
     * 超管关闭直播间
     */
    public static void superCloseRoom(SocketClient client) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_STOP_PLAY)
                .param("action", 19)
                .param("msgtype", 1)
                .param("ct", ""));
    }


    public static void sendEndLiveRoom(SocketClient client) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_LIVE_END)
                .param("action", 18));
    }

    /**
     * 发系统消息
     */
    public static void sendSystemMessage(SocketClient client, String content) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_SYSTEM)
                .param("action", 13)
                .param("msgtype", 4)
                .param("uname", u.getUserNiceName())
                .param("uid", u.getId())
                .param("ct", content));
    }


    /**
     * 更新主播映票数
     */
    public static void sendUpdateVotesMessage(SocketClient client, int votes, int first) {
        if (client == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_UPDATE_VOTES)
                .param("action", 1)
                .param("msgtype", 26)
                .param("votes", votes)
                .param("uid",
                        CommonAppConfig.getUid())
                .param("isfirst", first)
                .param("ct", ""));
    }

    /**
     * 更新主播映票数
     */
    public static void sendUpdateVotesMessage(SocketClient client, int votes) {
        sendUpdateVotesMessage(client, votes, 0);
    }

    /**
     * 发送发红包成功消息
     */
    public static void sendRedPackMessage(SocketClient client) {
        if (client == null) {
            return;
        }
        UserBean u =
                CommonAppConfig.getUserBean();
        if (u == null) {
            return;
        }
        client.send(new SocketSendBean()
                .param("_method_", Constants.SOCKET_RED_PACK)
                .param("action", 0)
                .param("msgtype", 0)
                .param("uid", u.getId())
                .param("uname", u.getUserNiceName())
                .param("ct", WordUtil.getString(R.string.red_pack_22))
        );

    }

}
