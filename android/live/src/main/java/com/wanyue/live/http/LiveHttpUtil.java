package com.wanyue.live.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.adapter.radio.CheckEntity;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.HttpClient;
import com.wanyue.common.http.JsonBean;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.server.MapBuilder;
import com.wanyue.common.server.RequestFactory;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.MD5Util;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.live.LiveConfig;
import com.wanyue.live.bean.LiveUserActionBean;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by  on 2019/3/21.
 */

public class LiveHttpUtil {

    private static final String SALT = "76576076c1f5f657b634e966c8836a06";

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }

    /**
     * 获取当前直播间的用户列表
     */
    public static void getUserList(String liveuid, String stream, int p, HttpCallback callback) {
        HttpClient.getInstance().get("getuserlist", LiveHttpConsts.GET_USER_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveuid)
                .params("stream", stream)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 当直播间是门票收费，计时收费或切换成计时收费的时候，观众请求这个接口
     *
     * @param liveUid 主播的uid
     * @param stream  主播的stream
     */
    public static void roomCharge(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.roomCharge", LiveHttpConsts.ROOM_CHARGE)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("liveuid", liveUid)
                .execute(callback);

    }

    /**
     * 当直播间是计时收费的时候，观众每隔一分钟请求这个接口进行扣费
     *
     * @param liveUid 主播的uid
     * @param stream  主播的stream
     */
    public static void timeCharge(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.timeCharge", LiveHttpConsts.TIME_CHARGE)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("liveuid", liveUid)
                .execute(callback);
    }


    /**
     * 获取用户余额
     */
    public static void getCoin(HttpCallback callback) {
        HttpClient.getInstance().get("Live.getCoin", LiveHttpConsts.GET_COIN)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .execute(callback);
    }

    /**
     * 获取用户的直播记录
     *
     * @param touid 对方的uid
     */
    public static void getLiveRecord(String touid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("User.getLiverecord", LiveHttpConsts.GET_LIVE_RECORD)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("touid", touid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 获取直播回放url
     *
     * @param recordId 视频的id
     */
    public static void getAliCdnRecord(String recordId, HttpCallback callback) {
        HttpClient.getInstance().get("User.getAliCdnRecord", LiveHttpConsts.GET_ALI_CDN_RECORD)
                .params("id", recordId)
                .execute(callback);
    }


    /**
     * 获取主播人数
     */


    public static void getUserNums(String stream, BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().get(LiveHttpConsts.GET_USER_NUMS, LiveHttpConsts.GET_USER_NUMS)
                .params("stream", stream)
                .execute(httpCallback);
    }


    /**
     * 获取直播点赞人数
     */

    public static void getLiveLikes(String stream, ParseHttpCallback<JSONObject> httpCallback) {
        HttpClient.getInstance().get(LiveHttpConsts.GET_LIVE_LIKES, LiveHttpConsts.GET_LIVE_LIKES)
                .params("stream", stream)
                .execute(httpCallback);
    }


    /**
     * 获取当前直播间的管理员列表
     */
    public static void getAdminList(String liveUid, int p, HttpCallback callback) {
        HttpClient.getInstance().post("managerlist", LiveHttpConsts.GET_ADMIN_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 主播设置或取消直播间的管理员
     */
    public static void setAdmin(String liveUid, String touid, boolean setAdmin, HttpCallback callback) {
        HttpClient.getInstance().post(setAdmin ? "setmanager" : "delmanager", LiveHttpConsts.SET_ADMIN)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 获取直播间的禁言列表
     */
    public static void getLiveShutUpList(String liveUid, int p, HttpCallback callback) {
        HttpClient.getInstance().post("usershutlist", LiveHttpConsts.GET_LIVE_SHUT_UP_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 直播间解除禁言
     */
    public static void liveCancelShutUp(String liveUid, String toUid, HttpCallback callback) {
        HttpClient.getInstance().post("liveunshut", LiveHttpConsts.LIVE_CANCEL_SHUT_UP)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("touid", toUid)
                .execute(callback);
    }

    /**
     * 获取直播间的拉黑列表
     */
    public static void getLiveBlackList(String liveUid, int p, HttpCallback callback) {
        HttpClient.getInstance().post("userkicklist ", LiveHttpConsts.GET_LIVE_BLACK_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("p", p)
                .execute(callback);
    }

    /**
     * 直播间解除拉黑
     */
    public static void liveCancelBlack(String liveUid, String toUid, HttpCallback callback) {
        HttpClient.getInstance().post("liveunkick", LiveHttpConsts.LIVE_CANCEL_BLACK)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("touid", toUid)
                .execute(callback);
    }


    /**
     * 直播结束后，获取直播收益，观看人数，时长等信息
     */
    public static void getLiveEndInfo(String stream, BaseHttpCallBack callback) {
        HttpClient.getInstance().get(LiveHttpConsts.GET_LIVE_END_INFO,
                LiveHttpConsts.GET_LIVE_END_INFO)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 获取直播间举报内容列表
     */
    public static void getLiveReportList(HttpCallback callback) {
        HttpClient.getInstance().get("Live.getReportClass", LiveHttpConsts.GET_LIVE_REPORT_LIST)
                .execute(callback);
    }

    /**
     * 举报用户
     */
    public static void setReport(String touid, String content, HttpCallback callback) {
        HttpClient.getInstance().get("Live.setReport", LiveHttpConsts.SET_REPORT)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("touid", touid)
                .params("content", content)
                .execute(callback);
    }

    /**
     * 直播间点击聊天列表和头像出现的弹窗
     */
    public static void getLiveUser(String touid, String liveUid, BaseHttpCallBack callback) {
        HttpClient.getInstance().get(LiveHttpConsts.GET_LIVE_USER, LiveHttpConsts.GET_LIVE_USER)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("touid", touid)
                .params("liveuid", liveUid)
                .execute(callback);
    }


    /**
     * 主播或管理员踢人
     */
    public static void kicking(String liveUid, String touid, HttpCallback callback) {
        HttpClient.getInstance().post(LiveHttpConsts.KICKING, LiveHttpConsts.KICKING)
                .params("liveuid", liveUid)
                .params("touid", touid)
                .execute(callback);
    }

    /**
     * 主播设置禁言或者取消禁言
     */

    public static void setShutUp(String liveUid, String touid, boolean needShut, String shutid, HttpCallback callback) {
        String path = needShut ? LiveHttpConsts.SET_SHUT_UP : LiveHttpConsts.SET_SHUT_CANCLE;
        HttpClient.getInstance().post(path, path)
                .params("liveuid", liveUid)
                .params("touid", touid)
                .params("shutid", shutid)
                .execute(callback);
    }

    /**
     * 超管关闭直播间或禁用账户
     *
     * @param type 0表示关闭当前直播 1表示禁播，2表示封禁账号
     */
    public static void superCloseRoom(String liveUid, int type, String stream, HttpCallback callback) {
        HttpClient.getInstance().post("supershut", LiveHttpConsts.SUPER_CLOSE_ROOM)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("type", type)
                .execute(callback);
    }


    /**
     * 守护商品类型列表
     */
    public static void getGuardBuyList(HttpCallback callback) {
        HttpClient.getInstance().get("Guard.getList", LiveHttpConsts.GET_GUARD_BUY_LIST)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .execute(callback);
    }

    /**
     * 购买守护接口
     */
    public static void buyGuard(String liveUid, String stream, int guardId, HttpCallback callback) {
        HttpClient.getInstance().get("Guard.BuyGuard", LiveHttpConsts.BUY_GUARD)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("guardid", guardId)
                .execute(callback);
    }


    /**
     * 查看主播的守护列表
     */
    public static void getGuardList(String liveUid, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Guard.GetGuardList", LiveHttpConsts.GET_GUARD_LIST)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 观众跟主播连麦时，获取自己的流地址
     */
    public static void getLinkMicStream(HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.RequestLVBAddrForLinkMic", LiveHttpConsts.GET_LINK_MIC_STREAM)
                .params("uid",
                        CommonAppConfig.getUid())
                .execute(callback);
    }

    /**
     * 主播连麦成功后，要把这些信息提交给服务器
     *
     * @param touid    连麦用户ID
     * @param pull_url 连麦用户播流地址
     */
    public static void linkMicShowVideo(String touid, String pull_url) {
        HttpClient.getInstance().get("Live.showVideo", LiveHttpConsts.LINK_MIC_SHOW_VIDEO)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("liveuid",
                        CommonAppConfig.getUid())
                .params("touid", touid)
                .params("pull_url", pull_url)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {

                    }
                });
    }


    /**
     * 主播设置是否允许观众发起连麦
     */
    public static void setLinkMicEnable(boolean linkMicEnable, HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.setMic", LiveHttpConsts.SET_LINK_MIC_ENABLE)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("ismic", linkMicEnable ? 1 : 0)
                .execute(callback);
    }


    /**
     * 观众检查主播是否允许连麦
     */
    public static void checkLinkMicEnable(String liveUid, HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.isMic", LiveHttpConsts.CHECK_LINK_MIC_ENABLE)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 连麦pk检查对方主播在线状态
     */
    public static void livePkCheckLive(String liveUid, String stream, String uidStream, HttpCallback callback) {
        HttpClient.getInstance().get("Livepk.checkLive", LiveHttpConsts.LIVE_PK_CHECK_LIVE)
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("uid_stream", uidStream)
                .execute(callback);
    }

    /**
     * 直播间发红包
     */
    public static void sendRedPack(String stream, String coin, String count, String title, int type, int sendType, HttpCallback callback) {
        HttpClient.getInstance().get("Red.SendRed", LiveHttpConsts.SEND_RED_PACK)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("coin", coin)
                .params("nums", count)
                .params("des", title)
                .params("type", type)
                .params("type_grant", sendType)
                .execute(callback);
    }

    /**
     * 获取直播间红包列表
     */
    public static void getRedPackList(String stream, HttpCallback callback) {
        String sign = MD5Util.getMD5("stream=" + stream + "&" + SALT);
        HttpClient.getInstance().get("Red.GetRedList", LiveHttpConsts.GET_RED_PACK_LIST)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 直播间抢红包
     */
    public static void robRedPack(String stream, int redPackId, HttpCallback callback) {
        String uid =
                CommonAppConfig.getUid();
        String sign = MD5Util.getMD5("redid=" + redPackId + "&stream=" + stream + "&uid=" + uid + "&" + SALT);
        HttpClient.getInstance().get("Red.RobRed", LiveHttpConsts.ROB_RED_PACK)
                .params("uid", uid)
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("redid", redPackId)
                .params("sign", sign)
                .execute(callback);
    }


    /**
     * 直播间红包领取详情
     */
    public static void getRedPackResult(String stream, int redPackId, HttpCallback callback) {
        String uid =
                CommonAppConfig.getUid();
        String sign = MD5Util.getMD5("redid=" + redPackId + "&stream=" + stream + "&" + SALT);
        HttpClient.getInstance().get("Red.GetRedRobList", LiveHttpConsts.GET_RED_PACK_RESULT)
                .params("uid", uid)
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("redid", redPackId)
                .params("sign", sign)
                .execute(callback);
    }

    /**
     * 发送弹幕
     */
    public static void sendDanmu(String content, String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Live.sendBarrage", LiveHttpConsts.SEND_DANMU)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("giftid", "1")
                .params("giftcount", "1")
                .params("content", content)
                .execute(callback);
    }

    /**
     * 检查直播间状态，是否收费 是否有密码等
     *
     * @param liveUid 主播的uid
     * @param stream  主播的stream
     */
    public static void checkLive(String liveUid, String stream, BaseHttpCallBack callback) {
        HttpClient.getInstance().get("checklive", LiveHttpConsts.CHECK_LIVE)
                .params("stream", stream)
                .execute(callback);
    }


    /**
     * 观众进入直播间
     */
    public static void enterRoom(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().post("live/enter", LiveHttpConsts.ENTER_ROOM)
                .params("city", CommonAppConfig.getCity())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .execute(callback);
    }


    /**
     * 获取礼物列表，同时会返回剩余的钱
     */
    public static void getGiftList(HttpCallback callback) {
        HttpClient.getInstance().get("giftlist", LiveHttpConsts.GET_GIFT_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .execute(callback);
    }

    /**
     * 观众给主播送礼物
     */
    public static void sendGift(String liveUid, String stream, int giftId, String giftCount, int ispack, int is_sticker, HttpCallback callback) {
        HttpClient.getInstance().post("sendgift", LiveHttpConsts.SEND_GIFT)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .params("giftid", giftId)
                .params("count", TextUtils.isEmpty(giftCount) ? "1" : giftCount)
                //.params("ispack", ispack)
                //.params("is_sticker", is_sticker)
                .execute(callback);
    }

    /**
     * 连麦pk搜索主播
     */

    public static void livePkSearchAnchor(String key, int p, HttpCallback callback) {
        HttpClient.getInstance().get("Livepk.Search", LiveHttpConsts.LIVE_PK_SEARCH_ANCHOR)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("key", key)
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取主播连麦pk列表
     */
    public static void getLivePkList(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Livepk.GetLiveList", LiveHttpConsts.GET_LIVE_PK_LIST)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("p", p)
                .execute(callback);
    }

    /**
     * 主播添加背景音乐时，搜索歌曲
     *
     * @param key      关键字
     * @param callback
     */
    public static void searchMusic(String key, HttpCallback callback) {
        HttpClient.getInstance().get("Livemusic.searchMusic", LiveHttpConsts.SEARCH_MUSIC)
                .params("key", key)
                .execute(callback);
    }

    /**
     * 获取歌曲的地址 和歌词的地址
     */
    public static void getMusicUrl(String musicId, HttpCallback callback) {
        HttpClient.getInstance().get("Livemusic.getDownurl", LiveHttpConsts.GET_MUSIC_URL)
                .params("audio_id", musicId)
                .execute(callback);
    }

    /**
     * 主播开播
     *
     * @param title    直播标题
     * @param callback
     */


    public static void createRoom(String title, int liveClassId, String thumb, HttpCallback callback) {
        PostRequest<JsonBean> request = HttpClient.getInstance().post(LiveHttpConsts.CREATE_ROOM, LiveHttpConsts.CREATE_ROOM)
                .params("token", CommonAppConfig.getToken())
                .params("city", CommonAppConfig.getCity())
                .params("province", CommonAppConfig.getProvince())
                .params("title", title)
                .params("classid", liveClassId)
                .params("thumb", thumb)
                .params("source", 1)
                .params("deviceinfo", LiveConfig.getSystemParams());
        request.execute(callback);
    }


    /**
     * 修改直播状态
     */


    public static void changeLive(String stream) {
        HttpClient.getInstance().post(LiveHttpConsts.CHANGE_LIVE, LiveHttpConsts.CHANGE_LIVE)
                .params("token",
                        CommonAppConfig.getToken())
                .params("stream", stream)
                .params("islive", "1")
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {

                    }
                });
    }

    /**
     * 主播结束直播
     */

    public static void stopLive(String stream, HttpCallback callback) {
        HttpClient.getInstance().post(LiveHttpConsts.STOP_LIVE, LiveHttpConsts.STOP_LIVE)
                .params("stream", stream)
                .params("token",
                        CommonAppConfig.getToken())
                .execute(callback);
    }

    /**
     * 主播开播前获取sdk类型  0金山  1腾讯
     */

    public static void getLiveSdk(HttpCallback callback) {
        HttpClient.getInstance().get("Live.getSDK", LiveHttpConsts.GET_LIVE_SDK)
                .params("uid",
                        CommonAppConfig.getUid())
                .execute(callback);
    }

    public static void getLiveClass(BaseHttpCallBack callback) {
        HttpClient.getInstance().get(LiveHttpConsts.GET_LIVE_CLASS, LiveHttpConsts.GET_LIVE_CLASS)
                .params("token",
                        CommonAppConfig.getToken())
                .execute(callback);

    }


    /**
     * 腾讯sdk 跟主播连麦时，获取主播的低延时流
     */
    public static void getTxLinkMicAccUrl(String originStreamUrl, HttpCallback callback) {
        HttpClient.getInstance().get("Linkmic.RequestPlayUrlWithSignForLinkMic", LiveHttpConsts.GET_TX_LINK_MIC_ACC_URL)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("originStreamUrl", originStreamUrl)
                .execute(callback);
    }


    /**
     * 连麦时候 主播混流
     */
    public static void linkMicTxMixStream(String mergeparams) {
        HttpClient.getInstance().get("Linkmic.MergeVideoStream", LiveHttpConsts.LINK_MIC_TX_MIX_STREAM)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("mergeparams", mergeparams)
                .execute(CommonHttpUtil.NO_CALLBACK);
    }


    /**
     * 我是哪些直播间的管理员，返回这些直播间列表
     */
    public static void getMyAdminRoomList(int p, HttpCallback callback) {
        HttpClient.getInstance().post("managelist", LiveHttpConsts.GET_MY_ADMIN_ROOM_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("p", p)
                .execute(callback);
    }


    /**
     * 获取直播间奖池等级
     */
    public static void getLiveGiftPrizePool(String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Jackpot.GetJackpot", LiveHttpConsts.GET_LIVE_GIFT_PRIZE_POOL)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("liveuid", liveUid)
                .params("stream", stream)
                .execute(callback);
    }

    /**
     * 主播checkLive
     */
    public static void anchorCheckLive(String stream, HttpCallback callback) {
        HttpClient.getInstance().get(LiveHttpConsts.ANCHOR_CHECK_LIVE, LiveHttpConsts.ANCHOR_CHECK_LIVE)
                .params("stream", stream)
                .execute(callback);
    }


    /**
     * 获取直播间信息
     */
    public static void getLiveInfo(String liveUid, HttpCallback callback) {
        HttpClient.getInstance().get("Live.getLiveInfo", LiveHttpConsts.GET_LIVE_INFO)
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 获取转盘礼物列表
     */
    public static void getTurntable(HttpCallback callback) {
        HttpClient.getInstance().get("Turntable.GetTurntable", LiveHttpConsts.GET_TURN_TABLE)
                .execute(callback);
    }

    /**
     * 转盘抽奖
     */

    public static void turn(String id, String liveUid, String stream, HttpCallback callback) {
        HttpClient.getInstance().get("Turntable.Turn", LiveHttpConsts.TURN)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("id", id)
                .params("stream", stream)
                .params("liveuid", liveUid)
                .execute(callback);
    }

    /**
     * 转盘抽奖记录
     */

    public static void getTurnRecord(int p, HttpCallback callback) {
        HttpClient.getInstance().get("Turntable.GetWin", LiveHttpConsts.GET_WIN)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .params("p", p)
                .execute(callback);
    }

    /*获取背包礼物*/
    public static void getBackpack(HttpCallback callback) {
        HttpClient.getInstance().get("Backpack.getBackpack", LiveHttpConsts.GET_BACK_PACK)
                .params("uid",
                        CommonAppConfig.getUid())
                .params("token",
                        CommonAppConfig.getToken())
                .execute(callback);
    }


    /**
     * 获取道具礼物贴纸列表
     */

    public static void getLiveStickerList(String beautyKey, final CommonCallback<String> commonCallback) {
        String packageName = CommonApplication.sInstance.getPackageName();
        String sign = MD5Util.getMD5(StringUtil.contact("package_name=", packageName, "&source=android&", beautyKey));
        OkGo.<String>get("https://data.facegl.com/appapi/gift/index")
                .params("package_name", packageName)
                .params("source", "android")
                .params("sign", sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject obj = JSON.parseObject(response.body());
                            if (commonCallback != null) {
                                commonCallback.callback(obj.getString("info"));
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }


    /**
     * 获取直播间小程序二维码
     */


    public static void getLiveQrCode(String liveUid, BaseHttpCallBack httpCallBack) {
        HttpClient.getInstance().get(LiveHttpConsts.LIVE_QR_CODE + "/" + liveUid, LiveHttpConsts.LIVE_QR_CODE)
                .execute(httpCallBack);
    }


    /**
     * 获取直播间人数
     */


    public static void getLiveNums(String stream, BaseHttpCallBack httpCallBack) {
        HttpClient.getInstance().get(LiveHttpConsts.LIVE_NUMS, LiveHttpConsts.LIVE_NUMS)
                .execute(httpCallBack);
    }

    /**
     * 获取直播间点赞数
     */

    public static void getLiveLikes(String stream, BaseHttpCallBack httpCallBack) {
        HttpClient.getInstance().get(LiveHttpConsts.LIVE_LIKES, LiveHttpConsts.LIVE_LIKES)
                .params("stream", stream)
                .execute(httpCallBack);
    }

    /**
     * 直播间点赞数
     */
    public static void setLiveLikes(String stream, BaseHttpCallBack httpCallBack) {
        HttpClient.getInstance().post(LiveHttpConsts.SET_LIVE_LIKES, LiveHttpConsts.SET_LIVE_LIKES)
                .params("stream", stream)
                .execute(httpCallBack);
    }


    /*获取举报列表*/
    public static Observable<List<CheckEntity>> getUserReportList() {
        return RequestFactory.getRequestManager().get("livereportcat", null, CheckEntity.class, false);
    }

    public static Observable<Boolean> setUserReport(String touid, String content) {
        Map<String, Object> parmMap = MapBuilder.factory()
                .put("touid", touid)
                .put("content", content)
                .build();
        return RequestFactory.getRequestManager().commit("livereport", parmMap, true);
    }

    /**
     * 禁言时长列表
     *
     * @return
     */
    public static Observable<List<LiveUserActionBean>> getShutList() {
        return RequestFactory.getRequestManager().get("shutlist", null, LiveUserActionBean.class, false);
    }

}
