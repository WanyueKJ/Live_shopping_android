package com.wanyue.common;

/**
 * Created by  on 2018/6/7.
 */

public class Constants {
    public static final String URL = "url";
    public static final String PAYLOAD = "payload";
    public static final String DATA = "data";
    public static final String TYPE = "type";
    public static final String ICON_ID = "icon_id";
    public static final String POSITION = "position";
    public static final String COVER = "cover";

    public static final String TO_UID = "toUid";
    public static final String FROM = "from";
    public static final String TIP = "tip";
    public static final String FIRST_LOGIN = "firstLogin";
    public static final String USER_BEAN = "userBean";
    public static final String COIN_NAME = "coinName";
    public static final String LIVE_UID = "liveUid";
    public static final String FOLLOW = "follow";
    public static final String BLACK = "black";
    public static final String IM_FROM_HOME = "imFromUserHome";
    public static final String ROLE = "role";
    public static final String ROOM_ID = "room_id";
    public static final String CALL_TYPE = "call_type";

    public static final String VIDEO_COMMENT_BEAN = "videoCommnetBean";
    public static final String VIDEO_FACE_OPEN = "videoOpenFace";
    public static final String VIDEO_FACE_HEIGHT = "videoFaceHeight";

    public static final String DIAMONDS = "钻石";
    public static final String VOTES = "映票";
    public static final String PAY_ALI_NOT_ENABLE = "支付宝未接入";
    public static final String PAY_WX_NOT_ENABLE = "微信支付未接入";
    public static final String PAY_ALL_NOT_ENABLE = "未开启支付";
    public static final String PAY_TYPE_COIN = "yue";//余额支付
    public static final String PAY_TYPE_ALI = "1";//支付宝支付
    public static final String PAY_TYPE_WX = "weixin";//微信宝支付
    public static final String PAY_TYPE_GOOGLE = "4";//google支付
    public static final String PAY_TYPE_TEST = "5";//模拟支付

    public static final String AT_NAME = "atName";
    public static final String LIVE_BEAN = "liveBean";
    public static final String LIVE_TYPE = "liveType";
    public static final String LIVE_KEY = "liveKey";
    public static final String LIVE_POSITION = "livePosition";
    public static final String LIVE_TYPE_VAL = "liveTypeVal";
    public static final String LIVE_STREAM = "liveStream";
    public static final String LIVE_HOME = "liveHome";
    public static final String LIVE_FOLLOW = "liveFollow";
    public static final String LIVE_NEAR = "liveNear";
    public static final String LIVE_CLASS_PREFIX = "liveClass_";
    public static final String LIVE_ADMIN_ROOM = "liveAdminRoom";


    public static final String PAY_BUY_COIN_ALI = "Charge.GetOrder";
    public static final String PAY_BUY_COIN_WX = "Charge.getWxOrder";
    public static final String PAY_VIP_COIN_ALI = "Vip.GetAliOrder";
    public static final String PAY_VIP_COIN_WX = "Vip.GetWxOrder";

    public static final String PACKAGE_NAME_ALI = "com.eg.android.AlipayGphone";//支付宝的包名
    public static final String PACKAGE_NAME_WX = "com.tencent.mm";//微信的包名
    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";//QQ的包名
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String ADDRESS = "address";
    public static final String SCALE = "scale";
    public static final String SELECT_IMAGE_PATH = "selectedImagePath";
    public static final String COPY_PREFIX = "copy://";
    public static final String SHARE_PREFIX = "shareagent://";

    public static final String GIF_GIFT_PREFIX = "gif_gift_";

    //主播在线类型
    public static final int LINE_TYPE_OFF = 0;//离线
    public static final int LINE_TYPE_DISTURB = 1;//勿扰
    public static final int LINE_TYPE_CHAT = 2;//在聊
    public static final int LINE_TYPE_ON = 3;//在线


    //提现账号类型，1表示支付宝，2表示微信，3表示银行卡
    public static final int CASH_ACCOUNT_ALI = 1;
    public static final int CASH_ACCOUNT_WX = 2;
    public static final int CASH_ACCOUNT_BANK = 3;
    public static final String CASH_ACCOUNT_ID = "cashAccountID";
    public static final String CASH_ACCOUNT = "cashAccount";
    public static final String CASH_ACCOUNT_TYPE = "cashAccountType";

    public static final String WATERMARK_ASSETS_FORDERNAME = "watermark";
    public static final String WATERMARK_ICON_FORDERNAME = "imgicons";
    public static final String WATERMARK_RES_FORDERNAME = "imgres";

    public static final int DYNAMIC_IMG_MAX_NUM = 9;
    public static final int DYNAMIC_VOICE_MIN_TIME = 3;

    public static final String MOB_QQ = "qq";
    public static final String MOB_QQ_ZATION = "QQ";
    public static final String MOB_QZONE = "qzone";
    public static final String MOB_WX = "wx";
    public static final String MOB_WX_PYQ = "wchat";
    public static final String MOB_FACEBOOK = "Facebook";
    public static final String MOB_TWITTER = "twitter";
    public static final String MOB_PHONE = "phone";
    public static final String MOB_DOWNLOAD = "download";



    public static final String AUTH_STATUS = "authStatus";//认证状态
    public static final int AUTH_IMAGE_MAX_SIZE = 6;//MAX_SIZE 认证时候上传图片最大张数

    public static final String CHAT_TYPE = "chatType";//通话类型 1视频 2语音
    public static final byte CHAT_TYPE_VIDEO = 1;//通话类型 视频
    public static final byte CHAT_TYPE_AUDIO = 2;//通话类型 语音
    public static final byte CHAT_TYPE_NONE = 0;//通话类型 全部

    public static final String CHAT_PARAM_AUD = "chatParamAud";
    public static final String CHAT_PARAM_ANC = "chatParamAnc";
    public static final String CHAT_PARAM_TYPE = "chatParamType";
    public static final String CHAT_SESSION_ID = "chatSessionId";
    public static final int CHAT_PARAM_TYPE_AUD = 1;
    public static final int CHAT_PARAM_TYPE_ANC = 2;


    public static final String MAIN_SEX = "mainHomeSex";
    public static final byte MAIN_SEX_NONE = 0;
    public static final byte MAIN_SEX_MALE = 1;
    public static final byte MAIN_SEX_FAMALE = 2;

    public static final String IM_MSG_ADMIN = "administrator";


    public static final String SKILL_BEAN = "skillBean";
    public static final String SKILL_ID = "skillId";
    public static final String NICKNAME = "nickname";
    public static final String ADDR = "addr";
    public static final String INTEREST = "interest";
    public static final String SIGN = "sign";
    public static final String JOB = "job";
    public static final String UP_WHEAT = "upWheat";
    public static final String SCHOOL = "school";
    public static final String VOICE = "voice";
    public static final String VOICE_DURATION = "voiceDuration";
    public static final String VOICE_FROM = "voice_from";
    public static final int VOICE_FROM_USER = 0;
    public static final int VOICE_FROM_SKILL = 1;
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_BEAN = "orderBean";
    public static final String ORDER_ANCHOR = "orderAnchor";

    public static final String LANG_EN = "en";
    public static final String LANG_ZH = "zh-cn";

    public static final int MAX_PHOTO_LENGTH=8;
    public static final int EMPTY_TYPE=11;

    public static final int DYNAMIC_PHOTO=1;
    public static final int DYNAMIC_VIDEO=2;
    public static final int DYNAMIC_VOICE=3;

    public static final int ROLE_ANTHOR=1;
    public static final int ROLE_AUDIENCE=2;
    public static final int ROLE_HOST=3;

    public static final String KEY_PHONE="phone";
    public static final String KEY_PWD="pwd";







    public static final int SOCKET_WHAT_CONN = 0;
    public static final int SOCKET_WHAT_DISCONN = 2;
    public static final int SOCKET_WHAT_BROADCAST = 1;

    public static final String KEY_DIALOG="dialog";
    public static final String KEY_LISTNER="listner";

    public static final String KEY_POSITON="sitid";
    public static final String KEY_TITLE="title";
    public static final String KEY_STATUS="status";
    public static final String KEY_ID="id";
    public static final String KEY_METHOD="method";
    public static final String KEY_TINT="tint";


    public static final String COPY = "copy";
    public static final String KEY_CLASS = "key_class";
    public static final int JPUSH_TYPE_MESSAGE =1 ;
    public static final String PUSH_ID ="push_id" ;

    public static final String EVENT_GRADE ="push_id" ;
    public static final String LINK = "link";
    public static final String OPEN_FLASH = "openFlash";


    public static final int GIFT_TYPE_NORMAL = 0;//正常礼物
    public static final int GIFT_TYPE_DAO = 1;//道具
    public static final int GIFT_TYPE_PACK = 2;//背包




    public static final String LIVE_SDK = "liveSdk";
    public static final String LIVE_KSY_CONFIG = "liveKsyConfig";
    public static final int LIVE_SDK_KSY = 0;//金山推流
    public static final int LIVE_SDK_TX = 1;//腾讯推流




    //socket
    public static final String SOCKET_CONN = "conn";
    public static final String SOCKET_BROADCAST = "broadcastingListen";
    public static final String SOCKET_SEND = "broadcast";
    public static final String SOCKET_STOP_PLAY = "stopplay";//超管关闭直播间
    public static final String SOCKET_STOP_LIVE = "stopLive";//超管关闭直播间
    public static final String SOCKET_SEND_MSG = "SendMsg";//发送文字消息，点亮，用户进房间  PS:这种混乱的设计是因为服务器端逻辑就是这样设计的,客户端无法自行修改
    public static final String SOCKET_LIGHT = "SendLight";//飘心

    public static final String SOCKET_SEND_GIFT = "SendGift";//送礼物
    public static final String SOCKET_SEND_BARRAGE = "SendBarrage";//发弹幕
    public static final String SOCKET_LEAVE_ROOM = "disconnect";//用户离开房间
    public static final String SOCKET_LIVE_END = "StartEndLive";//主播关闭直播
    public static final String SOCKET_SYSTEM = "SystemNot";//系统消息
    public static final String SOCKET_SHUT_UP = "Shutup";//禁言
    public static final String SOCKET_SET_ADMIN = "setAdmin";//设置或取消管理员
    public static final String SOCKET_CHANGE_LIVE = "changeLive";//切换计时收费类型
    public static final String SOCKET_UPDATE_VOTES = "updateVotes";//门票或计时收费时候更新主播的映票数
    public static final String SOCKET_FAKE_FANS = "requestFans";//僵尸粉
    public static final String SOCKET_LINK_MIC = "ConnectVideo";//连麦
    public static final String SOCKET_LINK_MIC_ANCHOR = "LiveConnect";//主播连麦
    public static final String SOCKET_LINK_MIC_PK = "LivePK";//主播PK
    public static final String SOCKET_BUY_GUARD = "BuyGuard";//购买守护
    public static final String SOCKET_RED_PACK = "SendRed";//红包
    public static final String SOCKET_LUCK_WIN = "luckWin";//幸运礼物中奖
    public static final String SOCKET_PRIZE_POOL_WIN = "jackpotWin";//奖池中奖
    public static final String SOCKET_PRIZE_POOL_UP = "jackpotUp";//奖池升级
    public static final String SOCKET_GIFT_GLOBAL = "Sendplatgift";//全站礼物
    public static final String SOCKET_KICK="Kick";//踢人
    public static final String SOCKET_WARN="jinggao";//警告

    //socket 用户类型
    public static final int SOCKET_USER_TYPE_NORMAL = 30;//普通用户
    public static final int SOCKET_USER_TYPE_ADMIN = 40;//房间管理员
    public static final int SOCKET_USER_TYPE_ANCHOR = 50;//主播
    public static final int SOCKET_USER_TYPE_SUPER = 60;//超管



    public static final String LIVE_DANMU_PRICE = "danmuPrice";
    //直播房间类型
    public static final int LIVE_TYPE_NORMAL = 0;//普通房间
    public static final int LIVE_TYPE_PWD = 1;//密码房间
    public static final int LIVE_TYPE_PAY = 2;//收费房间
    public static final int LIVE_TYPE_TIME = 3;//计时房间
    //主播直播间功能
    public static final int LIVE_FUNC_BEAUTY = 2001;//美颜
    public static final int LIVE_FUNC_CAMERA = 2002;//切换摄像头
    public static final int LIVE_FUNC_FLASH = 2003;//闪光灯
    public static final int LIVE_FUNC_MUSIC = 2004;//伴奏
    public static final int LIVE_FUNC_SHARE = 2005;//分享
    public static final int LIVE_FUNC_GAME = 2006;//游戏
    public static final int LIVE_FUNC_RED_PACK = 2007;//红包
    public static final int LIVE_FUNC_LINK_MIC = 2008;//连麦
    public static final String STREAM = "stream" ;
    public static final String DOWNLOAD_MUSIC = "downloadMusic";

    public static final int LINK_MIC_TYPE_NORMAL = 0;//观众与主播连麦
    public static final int LINK_MIC_TYPE_ANCHOR = 1;//主播与主播连麦

    public static final String CLASS_ID = "classID";
    public static final String CLASS_NAME = "className";
    public static final String MALL_IM_ADMIN = "goodsorder_admin";


    public static final String MONEY = "money";
}
