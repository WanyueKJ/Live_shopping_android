package com.wanyue.common;

/**
 * Created by  on 2018/10/15.
 */

public class HtmlConfig {
    //登录即代表同意服务和隐私条款
    public static final String LOGIN_PRIVCAY = CommonAppConfig.HOST + "/appapi/page/detail?id=2";
    //提现记录
    public static final String CASH_RECORD = CommonAppConfig.HOST + "/appapi/cash/index?";
    //支付宝课程支付回调
    public static final String ALI_PAY_URL = CommonAppConfig.HOST + "/appapi/cartpay/notify_ali";
    //微信课程支付回调
    public static final String ALI_WX_URL = CommonAppConfig.HOST + "/appapi/coursepay/notify_wx";
    //视频分享地址
    public static final String SHARE_VIDEO = CommonAppConfig.HOST + "/Appapi/Video/share?id=";

    //关于我们
    public static final String ABOUT_US = CommonAppConfig.HOST + "/appapi/page/detail?id=1";
    //联系我们
    public static final String CONNECT_US = CommonAppConfig.HOST + "/appapi/page/detail?id=1";

    //谷歌支付充值回调地址
    public static final String GOOGLE_PAY_COIN_URL = CommonAppConfig.HOST + "/appapi/google/notify";
    //谷歌支付下单回调地址
    public static final String GOOGLE_PAY_ORDER_URL = CommonAppConfig.HOST + "/appapi/Orderback/notify_google";


    //个人主页分享链接
    public static final String SHARE_HOME_PAGE = CommonAppConfig.HOST + "/Appapi/home/index?touid=";
    //直播间贡献榜
    public static final String LIVE_LIST = CommonAppConfig.HOST + "/Appapi/contribute/index?uid=";

    //支付宝充值回调地址
    public static final String ALI_PAY_COIN_URL = CommonAppConfig.HOST + "/Appapi/Pay/notify_ali";

    //充值协议
    public static final String CHARGE_PRIVCAY = CommonAppConfig.HOST + "/portal/page/index?id=6";
}



