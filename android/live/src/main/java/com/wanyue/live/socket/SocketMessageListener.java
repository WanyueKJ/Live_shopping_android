package com.wanyue.live.socket;

import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.bean.UserBean;
import com.wanyue.live.bean.GlobalGiftBean;
import com.wanyue.live.bean.LiveBuyGuardMsgBean;
import com.wanyue.live.bean.LiveChatBean;
import com.wanyue.live.bean.LiveDanMuBean;
import com.wanyue.live.bean.LiveEnterRoomBean;
import com.wanyue.live.bean.LiveGiftPrizePoolWinBean;
import com.wanyue.live.bean.LiveLuckGiftWinBean;
import com.wanyue.live.bean.LiveReceiveGiftBean;
import com.wanyue.live.bean.LiveUserGiftBean;

import java.util.List;

/**
 * Created by  on 2017/8/22.
 * 直播间通用的socket逻辑
 */

public interface SocketMessageListener {

    /**
     * 直播间  连接成功socket后调用
     */
    void onConnect(boolean successConn);

    /**
     * 直播间  自己的socket断开
     */
    void onDisConnect();

    /**
     * 直播间  收到聊天消息
     */
    void onChat(LiveChatBean bean);

    /**
     * 直播间  收到飘心消息
     */
    void onLight(LiveChatBean liveChatBean);

    /**
     * 直播间  收到用户进房间消息
     */
    void onEnterRoom(LiveEnterRoomBean bean);

    /**
     * 直播间  收到用户离开房间消息
     */
    void onLeaveRoom(UserBean bean);

    /**
     * 直播间  收到送礼物消息
     *
     * @param bean 礼物信息
     */
    void onSendGift(LiveReceiveGiftBean bean);

    /**
     * @param leftGift  左边的映票数
     * @param rightGift 右边的映票数
     */

    void onSendGiftPk(long leftGift, long rightGift);

    /**
     * 直播间  收到弹幕消息
     */
    void onSendDanMu(LiveDanMuBean bean);

    /**
     * 直播间  观众收到直播结束消息
     */
    void onLiveEnd();

    /**
     * 直播间  主播登录失效
     */
    void onAnchorInvalid();

    /**
     * 直播间  超管关闭直播间
     */
    void onSuperCloseLive();

    /**
     * 直播间  踢人
     */
    void onKick(String touid);

    /**
     * 直播间  禁言
     */
    void onShutUp(String touid, int action,String content);

    /**
     * 直播间  设置或取消管理员
     */
    void onSetAdmin(String toUid, int isAdmin);

    /**
     * 主播收到警告
     * @param content
     */
    void onWarn(String content);

    /**
     * 直播间  主播切换计时收费或更改计时收费价格的时候执行
     */
    void onChangeTimeCharge(int typeVal);


    /***********************以下是观众与主播连麦*********************************/

    /**
     * 连麦  主播收到观众申请连麦的回调
     */
    void onAudienceApplyLinkMic(UserBean u);

    /**
     * 连麦  主播同意连麦的回调
     */
    void onAnchorAcceptLinkMic();

    /**
     * 连麦  观众收到主播拒绝连麦的回调
     */
    void onAnchorRefuseLinkMic();

    /**
     * 连麦  所有人收到连麦观众发过来的播流地址的回调
     */
    void onAudienceSendLinkMicUrl(String uid, String uname, String playUrl);

    /**
     * 连麦  所有人收到主播关闭连麦的回调
     */
    void onAnchorCloseLinkMic(String touid, String uname);

    /**
     * 连麦  所有人收到已连麦观众关闭连麦的回调
     */
    void onAudienceCloseLinkMic(String uid, String uname);

    /**
     * 连麦  观众申请连麦时，收到主播无响应的回调
     */
    void onAnchorNotResponse();

    /**
     * 连麦  观众申请连麦时，收到主播正在忙的回调
     */
    void onAnchorBusy();

    /**
     * 连麦  已连麦用户退出直播间的回调
     */
    void onAudienceLinkMicExitRoom(String touid);

    /***********************以下是主播连麦*********************************/

    /**
     * 主播与主播连麦  主播收到其他主播发过来的连麦申请的回调
     *
     * @param u      对方主播的信息
     * @param stream 对方主播的stream
     */
    void onLinkMicAnchorApply(UserBean u, String stream);

    /**
     * 主播与主播连麦 所有人收到对方主播的播流地址的回调
     *
     * @param playUrl 对方主播的播流地址
     */
    void onLinkMicAnchorPlayUrl(String pkUid, String playUrl);

    /**
     * 主播与主播连麦  断开连麦pk的回调
     */
    void onLinkMicAnchorClose();

    /**
     * 主播与主播连麦  对方主播拒绝连麦pk的回调
     */
    void onLinkMicAnchorRefuse();

    /**
     * 主播与主播连麦  对方主播正在忙的回调
     */
    void onLinkMicAnchorBusy();

    /**
     * 主播与主播连麦  对方主播无响应的回调
     */
    void onLinkMicAnchorNotResponse();

    /**
     * 主播与主播连麦  对方主播正在玩游戏
     */
    void onlinkMicPlayGaming();

    /***********************以下是主播PK*********************************/
    /**
     * 主播与主播PK  主播收到对方主播发过来的PK申请的回调
     *
     * @param u      对方主播的信息
     * @param stream 对方主播的stream
     */
    void onLinkMicPkApply(UserBean u, String stream);

    /**
     * 主播与主播PK 所有人收到PK开始的回调
     */
    void onLinkMicPkStart(String pkUid);

    /**
     * 主播与主播PK  断开连麦pk的回调
     */
    void onLinkMicPkClose();

    /**
     * 主播与主播PK  对方主播拒绝pk的回调
     */
    void onLinkMicPkRefuse();

    /**
     * 主播与主播PK   对方主播正在忙的回调
     */
    void onLinkMicPkBusy();

    /**
     * 主播与主播PK   对方主播无响应的回调
     */
    void onLinkMicPkNotResponse();

    /**
     * 主播与主播PK   所有人收到PK结果的回调
     */
    void onLinkMicPkEnd(String winUid);


    /**
     * 幸运礼物中奖
     */
    void onLuckGiftWin(LiveLuckGiftWinBean bean);

    /**
     * 奖池中奖
     */
    void onPrizePoolWin(LiveGiftPrizePoolWinBean bean);

    /**
     * 奖池升级
     */
    void onPrizePoolUp(String level);


    /**
     * 全站礼物
     */
    void onGlobalGift(GlobalGiftBean bean);



}
