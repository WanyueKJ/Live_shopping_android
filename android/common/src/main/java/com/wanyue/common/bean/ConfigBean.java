package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by  on 2017/8/5.
 */

public class ConfigBean {
    private String version;//Android apk安装包 版本号
    private String downloadApkUrl;//Android apk安装包 下载地址
    private String updateDes;//版本更新描述
    private String liveWxShareUrl;//直播间微信分享地址
    private String liveShareTitle;//直播间分享标题
    private String liveShareDes;//直播间分享描述
    private String videoShareTitle;//短视频分享标题
    private String videoShareDes;//短视频分享描述
    private int videoAuditSwitch;//短视频审核是否开启
    private int videoCloudType;//短视频云储存类型 1七牛云 2腾讯云
    private String videoQiNiuHost;//短视频七牛云域名
    private String txCosAppId;//腾讯云存储appId
    private String txCosRegion;//腾讯云存储区域
    private String txCosBucketName;//腾讯云存储桶名字
    private String txCosVideoPath;//腾讯云存储视频文件夹
    private String txCosImagePath;//腾讯云存储图片文件夹
    private String coinName;//钻石名称
    private String votesName;//映票名称
    private String scoreName;//积分名称
    private String[] liveTimeCoin;//直播间计时收费规则
    private String[] loginType;//三方登录类型
    private String[][] liveType;//直播间开播类型
    private String[] shareType;//分享类型
    private List<LiveClassBean> liveClass;//直播分类
    private String videoClass;//短视频分类
    private int maintainSwitch;//维护开关
    private String maintainTips;//维护提示
    private String mAdInfo;//引导页 广告信息
    private int priMsgSwitch;//私信开关
    private int forceUpdate;//强制更新
    private String mWaterMarkUrl;//水印
    private String shopExplainUrl;//商品客服
    private String mShopSystemName;//商店名称


    private String beautyKey;//美颜鉴权码
    private int skin_whiting;
    private int skin_smooth;
    private int skin_tenderness;
    private int skin_saturation;
    private int eye_brow;
    private int big_eye;
    private int eye_length;
    private int eye_corner;
    private int eye_alat;
    private int face_lift;
    private int face_shave;
    private int mouse_lift;
    private int nose_lift;
    private int chin_lift;
    private int forehead_lift;
    private int lengthen_noseLift;

    @JSONField(name="shop_url")
    @SerializedName("shop_url")
    private String shopUrl;

    public MeiyanConfig parseMeiyanConfig() {
        return new MeiyanConfig(skin_whiting,
                skin_smooth,
                skin_tenderness,
                50,
                skin_saturation,
                eye_brow,
                big_eye,
                eye_length,
                eye_corner,
                eye_alat,
                face_lift,
                face_shave,
                mouse_lift,
                nose_lift,
                chin_lift,
                forehead_lift,
                lengthen_noseLift
        );
    }


    @JSONField(name = "apk_ver")
    public String getVersion() {
        return version;
    }

    @JSONField(name = "apk_ver")
    public void setVersion(String version) {
        this.version = version;
    }

    @JSONField(name = "apk_url")
    public String getDownloadApkUrl() {
        return downloadApkUrl;
    }

    @JSONField(name = "apk_url")
    public void setDownloadApkUrl(String downloadApkUrl) {
        this.downloadApkUrl = downloadApkUrl;
    }

    @JSONField(name = "apk_des")
    public String getUpdateDes() {
        return updateDes;
    }

    @JSONField(name = "apk_des")
    public void setUpdateDes(String updateDes) {
        this.updateDes = updateDes;
    }

    @JSONField(name = "wx_siteurl")
    public void setLiveWxShareUrl(String liveWxShareUrl) {
        this.liveWxShareUrl = liveWxShareUrl;
    }

    @JSONField(name = "wx_siteurl")
    public String getLiveWxShareUrl() {
        return liveWxShareUrl;
    }

    @JSONField(name = "share_title")
    public String getLiveShareTitle() {
        return liveShareTitle;
    }

    @JSONField(name = "share_title")
    public void setLiveShareTitle(String liveShareTitle) {
        this.liveShareTitle = liveShareTitle;
    }

    @JSONField(name = "share_des")
    public String getLiveShareDes() {
        return liveShareDes;
    }

    @JSONField(name = "share_des")
    public void setLiveShareDes(String liveShareDes) {
        this.liveShareDes = liveShareDes;
    }

    @JSONField(name = "coin_name")
    public String getCoinName() {
        return coinName;
    }

    @JSONField(name = "coin_name")
    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    @JSONField(name = "name_score")
    public String getScoreName() {
        return scoreName;
    }
    @JSONField(name = "name_score")
    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    @JSONField(name = "votes_name")
    public String getVotesName() {
        return votesName;
    }

    @JSONField(name = "votes_name")
    public void setVotesName(String votesName) {
        this.votesName = votesName;
    }

    @JSONField(name = "live_time_coin")
    public String[] getLiveTimeCoin() {
        return liveTimeCoin;
    }

    @JSONField(name = "live_time_coin")
    public void setLiveTimeCoin(String[] liveTimeCoin) {
        this.liveTimeCoin = liveTimeCoin;
    }

    @JSONField(name = "login_type")
    public String[] getLoginType() {
        return loginType;
    }

    @JSONField(name = "login_type")
    public void setLoginType(String[] loginType) {
        this.loginType = loginType;
    }

    @JSONField(name = "live_type")
    public String[][] getLiveType() {
        return liveType;
    }

    @JSONField(name = "live_type")
    public void setLiveType(String[][] liveType) {
        this.liveType = liveType;
    }

    @JSONField(name = "share_type")
    public String[] getShareType() {
        return shareType;
    }

    @JSONField(name = "share_type")
    public void setShareType(String[] shareType) {
        this.shareType = shareType;
    }

    @JSONField(name = "liveclass")
    public List<LiveClassBean> getLiveClass() {
        return liveClass;
    }

    @JSONField(name = "liveclass")
    public void setLiveClass(List<LiveClassBean> liveClass) {
        this.liveClass = liveClass;
    }


    @JSONField(name = "videoclass")
    public String getVideoClass() {
        return videoClass;
    }

    @JSONField(name = "videoclass")
    public void setVideoClass(String videoClass) {
        this.videoClass = videoClass;
    }

    @JSONField(name = "maintain_switch")
    public int getMaintainSwitch() {
        return maintainSwitch;
    }

    @JSONField(name = "maintain_switch")
    public void setMaintainSwitch(int maintainSwitch) {
        this.maintainSwitch = maintainSwitch;
    }

    @JSONField(name = "maintain_tips")
    public String getMaintainTips() {
        return maintainTips;
    }

    @JSONField(name = "maintain_tips")
    public void setMaintainTips(String maintainTips) {
        this.maintainTips = maintainTips;
    }

    @JSONField(name = "sprout_key")
    public String getBeautyKey() {
        return beautyKey;
    }

    @JSONField(name = "sprout_key")
    public void setBeautyKey(String beautyKey) {
        this.beautyKey = beautyKey;
    }


    public String[] getVideoShareTypes() {
        return shareType;
    }

    @JSONField(name = "video_share_title")
    public String getVideoShareTitle() {
        return videoShareTitle;
    }

    @JSONField(name = "video_share_title")
    public void setVideoShareTitle(String videoShareTitle) {
        this.videoShareTitle = videoShareTitle;
    }

    @JSONField(name = "video_share_des")
    public String getVideoShareDes() {
        return videoShareDes;
    }

    @JSONField(name = "video_share_des")
    public void setVideoShareDes(String videoShareDes) {
        this.videoShareDes = videoShareDes;
    }

    @JSONField(name = "video_audit_switch")
    public int getVideoAuditSwitch() {
        return videoAuditSwitch;
    }

    @JSONField(name = "video_audit_switch")
    public void setVideoAuditSwitch(int videoAuditSwitch) {
        this.videoAuditSwitch = videoAuditSwitch;
    }

    @JSONField(name = "cloudtype")
    public int getVideoCloudType() {
        return videoCloudType;
    }

    @JSONField(name = "cloudtype")
    public void setVideoCloudType(int videoCloudType) {
        this.videoCloudType = videoCloudType;
    }

    @JSONField(name = "qiniu_domain")
    public String getVideoQiNiuHost() {
        return videoQiNiuHost;
    }

    @JSONField(name = "qiniu_domain")
    public void setVideoQiNiuHost(String videoQiNiuHost) {
        this.videoQiNiuHost = videoQiNiuHost;
    }

    @JSONField(name = "txcloud_appid")
    public String getTxCosAppId() {
        return txCosAppId;
    }

    @JSONField(name = "txcloud_appid")
    public void setTxCosAppId(String txCosAppId) {
        this.txCosAppId = txCosAppId;
    }

    @JSONField(name = "txcloud_region")
    public String getTxCosRegion() {
        return txCosRegion;
    }

    @JSONField(name = "txcloud_region")
    public void setTxCosRegion(String txCosRegion) {
        this.txCosRegion = txCosRegion;
    }

    @JSONField(name = "txcloud_bucket")
    public String getTxCosBucketName() {
        return txCosBucketName;
    }

    @JSONField(name = "txcloud_bucket")
    public void setTxCosBucketName(String txCosBucketName) {
        this.txCosBucketName = txCosBucketName;
    }

    @JSONField(name = "video_watermark")
    public String getWaterMarkUrl() {
        return mWaterMarkUrl;
    }

    @JSONField(name = "video_watermark")
    public void setWaterMarkUrl(String waterMarkUrl) {
        mWaterMarkUrl = waterMarkUrl;
    }

    @JSONField(name = "txvideofolder")
    public String getTxCosVideoPath() {
        return txCosVideoPath;
    }

    @JSONField(name = "txvideofolder")
    public void setTxCosVideoPath(String txCosVideoPath) {
        this.txCosVideoPath = txCosVideoPath;
    }

    @JSONField(name = "tximgfolder")
    public String getTxCosImagePath() {
        return txCosImagePath;
    }

    @JSONField(name = "tximgfolder")
    public void setTxCosImagePath(String txCosImagePath) {
        this.txCosImagePath = txCosImagePath;
    }

    @JSONField(name = "guide")
    public String getAdInfo() {
        return mAdInfo;
    }

    @JSONField(name = "guide")
    public void setAdInfo(String adInfo) {
        mAdInfo = adInfo;
    }

    @JSONField(name = "letter_switch")
    public int getPriMsgSwitch() {
        return priMsgSwitch;
    }

    @JSONField(name = "letter_switch")
    public void setPriMsgSwitch(int priMsgSwitch) {
        this.priMsgSwitch = priMsgSwitch;
    }

    @JSONField(name = "isup")
    public int getForceUpdate() {
        return forceUpdate;
    }

    @JSONField(name = "isup")
    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @JSONField(name = "shopexplain_url")
    public String getShopExplainUrl() {
        return shopExplainUrl;
    }

    @JSONField(name = "shopexplain_url")
    public void setShopExplainUrl(String shopExplainUrl) {
        this.shopExplainUrl = shopExplainUrl;
    }

    @JSONField(name = "shop_system_name")
    public String getShopSystemName() {
        return mShopSystemName;
    }
    @JSONField(name = "shop_system_name")
    public void setShopSystemName(String shopSystemName) {
        mShopSystemName = shopSystemName;
    }

    public int getSkin_whiting() {
        return skin_whiting;
    }

    public void setSkin_whiting(int skin_whiting) {
        this.skin_whiting = skin_whiting;
    }

    public int getSkin_smooth() {
        return skin_smooth;
    }

    public void setSkin_smooth(int skin_smooth) {
        this.skin_smooth = skin_smooth;
    }

    public int getSkin_tenderness() {
        return skin_tenderness;
    }

    public void setSkin_tenderness(int skin_tenderness) {
        this.skin_tenderness = skin_tenderness;
    }

    public int getSkin_saturation() {
        return skin_saturation;
    }

    public void setSkin_saturation(int skin_saturation) {
        this.skin_saturation = skin_saturation;
    }

    public int getEye_brow() {
        return eye_brow;
    }

    public void setEye_brow(int eye_brow) {
        this.eye_brow = eye_brow;
    }

    public int getBig_eye() {
        return big_eye;
    }

    public void setBig_eye(int big_eye) {
        this.big_eye = big_eye;
    }

    public int getEye_length() {
        return eye_length;
    }

    public void setEye_length(int eye_length) {
        this.eye_length = eye_length;
    }

    public int getEye_corner() {
        return eye_corner;
    }

    public void setEye_corner(int eye_corner) {
        this.eye_corner = eye_corner;
    }

    public int getEye_alat() {
        return eye_alat;
    }

    public void setEye_alat(int eye_alat) {
        this.eye_alat = eye_alat;
    }

    public int getFace_lift() {
        return face_lift;
    }

    public void setFace_lift(int face_lift) {
        this.face_lift = face_lift;
    }

    public int getFace_shave() {
        return face_shave;
    }

    public void setFace_shave(int face_shave) {
        this.face_shave = face_shave;
    }

    public int getMouse_lift() {
        return mouse_lift;
    }

    public void setMouse_lift(int mouse_lift) {
        this.mouse_lift = mouse_lift;
    }

    public int getNose_lift() {
        return nose_lift;
    }

    public void setNose_lift(int nose_lift) {
        this.nose_lift = nose_lift;
    }

    public int getChin_lift() {
        return chin_lift;
    }

    public void setChin_lift(int chin_lift) {
        this.chin_lift = chin_lift;
    }

    public int getForehead_lift() {
        return forehead_lift;
    }

    public void setForehead_lift(int forehead_lift) {
        this.forehead_lift = forehead_lift;
    }

    public int getLengthen_noseLift() {
        return lengthen_noseLift;
    }

    public void setLengthen_noseLift(int lengthen_noseLift) {
        this.lengthen_noseLift = lengthen_noseLift;
    }


    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }
}
