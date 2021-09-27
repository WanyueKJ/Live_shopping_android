package com.wanyue.common.mob;

import android.text.TextUtils;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.R;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.WordUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by  on 2017/8/29.
 * Mob 分享
 */

public class MobShareUtil {

    private PlatformActionListener mPlatformActionListener;
    private MobCallback mMobCallback;


    public MobShareUtil() {
        mPlatformActionListener = new PlatformActionListener() {

            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (mMobCallback != null) {
                    mMobCallback.onSuccess(null);
                    mMobCallback.onFinish();
                    mMobCallback = null;
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (mMobCallback != null) {
                    mMobCallback.onError();
                    mMobCallback.onFinish();
                    mMobCallback = null;
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (mMobCallback != null) {
                    mMobCallback.onCancel();
                    mMobCallback.onFinish();
                    mMobCallback = null;
                }
            }
        };
    }

    public void execute(String platType, ShareData data, MobCallback callback) {
        if (TextUtils.isEmpty(platType) || data == null) {
            return;
        }
        String platName = MobConst.MAP.get(platType);
        if (TextUtils.isEmpty(platName)) {
            return;
        }
        mMobCallback = callback;
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();//设置一个总开关，用于在分享前若需要授权，则禁用sso功能
        oks.setPlatform(platName);
        oks.setSilent(true);//是否直接分享
        oks.setSite(WordUtil.getString(R.string.app_name));//site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供
        oks.setSiteUrl(CommonAppConfig.HOST);//siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供
        oks.setTitle(data.getTitle());
        oks.setText(data.getDes());
        oks.setText(data.getDes());
        oks.setImageUrl(data.getImgUrl());
        oks.setImagePath(data.getFilePath());

        String webUrl = data.getWebUrl();
        oks.setUrl(webUrl);
        oks.setSiteUrl(webUrl);
        oks.setTitleUrl(webUrl);
        oks.setCallback(mPlatformActionListener);
        oks.show(CommonApplication.sInstance);
        L.e("分享-----url--->" + webUrl);
    }

    public void release() {
        mMobCallback = null;
    }


    public void shareSmallProgram(String text,String thumb,MobCallback callback) {
        shareSmallProgram(text,thumb, "pages/index/index",callback);
    }

    public void shareSmallProgram(String text,String thumb,final String path,MobCallback callback){
        mMobCallback=callback;
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(text);
        oks.setText(text);
        oks.setImageUrl(thumb);
        oks.setUrl(CommonAppConfig.HOST);
        oks.setPlatform(Wechat.NAME);
        oks.setSilent(true);//是否直接分享
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                paramsToShare.setShareType(Platform.SHARE_WXMINIPROGRAM);//分享小程序类型
                paramsToShare.setWxUserName("gh_8b6a61e88701");//配置小程序原始ID，前面有截图说明
                paramsToShare.setWxPath(path);//分享小程序页面的具体路径，前面截图从微信小程序开发工具中可以直接复制
            }
        });
        oks.setCallback(mPlatformActionListener);
        oks.show(CommonApplication.sInstance);
    }
}
