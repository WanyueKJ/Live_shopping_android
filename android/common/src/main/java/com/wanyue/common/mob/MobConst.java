package com.wanyue.common.mob;

import com.wanyue.common.Constants;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class MobConst {

    public static final Map<String, String> MAP;

    static {
        MAP = new HashMap<>();
        MAP.put(Constants.MOB_QQ, QQ.NAME);
        MAP.put(Constants.MOB_QQ_ZATION, QQ.NAME);
        MAP.put(Constants.MOB_QZONE, QZone.NAME);
        MAP.put(Constants.MOB_WX, Wechat.NAME);
        MAP.put(Constants.MOB_WX_PYQ, WechatMoments.NAME);
        MAP.put(Constants.MOB_FACEBOOK, Facebook.NAME);
        MAP.put(Constants.MOB_TWITTER, Twitter.NAME);
    }
    
}
