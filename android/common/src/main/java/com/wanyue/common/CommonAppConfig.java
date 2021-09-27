package com.wanyue.common;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.bean.LevelBean;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.bean.UserItemBean;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.SpUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.SystemUtil;
import com.wanyue.common.utils.VersionUtil;
import com.wanyue.common.utils.WordUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by  on 2017/8/4.
 */

public class CommonAppConfig {
    //域名
    public static final String HOST = getMetaDataString("SERVER_HOST");
    //外部sd卡
    public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    //内部存储 /data/data/<application package>/files目录
    public static final String INNER_PATH = CommonApplication.sInstance.getFilesDir().getAbsolutePath();
    public static final String HEADER ="" ;
    //文件夹名字
    private static final String DIR_NAME = "wanyueMail";
    //保存视频的时候，在sd卡存储短视频的路径DCIM下
    public static final String VIDEO_PATH = DCMI_PATH + "/" + DIR_NAME + "/video/";
    public static final String DOWN_LOAD_PATH = DCMI_PATH + "/" + DIR_NAME + "/download/";
    public static final String VIDEO_PATH_RECORD = DCMI_PATH + "/" + DIR_NAME + "/video/record";

    public static final String VIDEO_RECORD_TEMP_PATH = VIDEO_PATH + "recordParts";
    //下载贴纸的时候保存的路径
    public static final String VIDEO_TIE_ZHI_PATH = DCMI_PATH + "/" + DIR_NAME + "/tieZhi/";
    //下载音乐的时候保存的路径
    public static final String MUSIC_PATH = DCMI_PATH + "/" + DIR_NAME + "/music/";
    //拍照时图片保存路径
    public static final String CAMERA_IMAGE_PATH = DCMI_PATH + "/" + DIR_NAME + "/camera/";
    public static final String GIF_PATH = INNER_PATH + "/gif/";
    //QQ登录是否与PC端互通
    public static final boolean QQ_LOGIN_WITH_PC = true;
    public static final boolean APP_IS_YUNBAO_SELF = false;

    public static final String APP_VERSION = VersionUtil.getVersion();//app版本号
    public static final String SYSTEM_MODEL = android.os.Build.MODEL;//手机型号
    public static final String SYSTEM_RELEASE = android.os.Build.VERSION.RELEASE;//手机系统版本号
    public static int TX_IM_APP_Id;

    private static int windowWidth;
    private static int windowHeight;

    private static int statuBarHeight;
    private static int navigationBarHeight;

    private static String mBeautyKey;
    public static final String NOT_LOGIN_UID="0";
    private static int mAppIconRes;
    private static String mGiftDaoListJson;
    private static String mGiftListJson;
    private CommonAppConfig() {

    }

    private static String mUid;
    private static String mToken;
    private static ConfigBean mConfig;
    private static double mLng;
    private static double mLat;
    private static String mProvince;//省
    private static String mCity;//市
    private static String mDistrict;//区
    private static UserBean mUserBean;
    private static String mVersion;
    private static boolean mLoginIM;//IM是否登录了
    private static Boolean mLaunched;//App是否启动了
    private static Long mLaunchTime;//MainActivity打开的时间戳，极光IM用到
    private static String mJPushAppKey;//极光推送的AppKey
    private static List<UserItemBean> mUserItemList;//个人中心功能列表
    private static SparseArray<LevelBean> mLevelMap;
    private static SparseArray<LevelBean> mAnchorLevelMap;
    private static String mTxMapAppKey;//腾讯定位，地图的AppKey
    private static String mTxMapAppSecret;//腾讯地图的AppSecret
    private static boolean mFrontGround;
    private static String mAppName;
    private static Boolean mTiBeautyEnable;//是否使用萌颜 true使用萌颜 false 使用基础美颜


    //是否上下滑动切换直播间
    public static final boolean LIVE_ROOM_SCROLL = true;
    //直播sdk类型是否由后台控制的
    public static final boolean LIVE_SDK_CHANGED = true;
    //使用指定的直播sdk类型
    public static final int LIVE_SDK_USED = Constants.LIVE_SDK_KSY;

    public static String getUid() {
        if ((TextUtils.isEmpty(mUid)||StringUtil.equals(mUid,NOT_LOGIN_UID))&&CommonAppConfig.getUserBean()!=null) {
            mUid=CommonAppConfig.getUserBean().getId();
        }
        if(TextUtils.isEmpty(mUid)){
            mUid=NOT_LOGIN_UID;
        }
        return mUid;
    }


    public static int getIntegerUid(){
        String uid=getUid();
        if(StringUtil.isInt(uid)){
           return Integer.parseInt(uid);
        }
        return 0;
    }

    public static String getToken() {
        if(TextUtils.isEmpty(mToken)){
            mToken=SpUtil.getInstance().getStringValue(SpUtil.TOKEN);
        }
        return mToken;
    }

    public static String getCoinName() {
        ConfigBean configBean = getConfig();
        if (configBean != null) {
            return configBean.getCoinName();
        }
        return Constants.DIAMONDS;
    }

    public static String getVotesName() {
        ConfigBean configBean = getConfig();
        if (configBean != null) {
            return configBean.getVotesName();
        }
        return Constants.VOTES;
    }

    public static ConfigBean getConfig() {
        if (mConfig == null) {
            String configString = SpUtil.getInstance().getStringValue(SpUtil.CONFIG);
            if (!TextUtils.isEmpty(configString)) {
                mConfig = JSON.parseObject(configString, ConfigBean.class);
            }
        }
        return mConfig;
    }

    public static Observable<ConfigBean> getObserverConfig() {
        ConfigBean configBean = getConfig();
        if (configBean != null) {
           return Observable.just(configBean);
        } else {
          return   CommonAPI.getConfig();
        }
    }

    public static void setConfig(ConfigBean config) {
        if(config!=null){
         mConfig = config;
         SpUtil.getInstance().setStringValue(SpUtil.CONFIG,JSON.toJSONString(config));
        }

    }

    /**
     * 经度
     */
    public static double getLng() {
        if (mLng == 0) {
            String lng = SpUtil.getInstance().getStringValue(SpUtil.LOCATION_LNG);
            if (!TextUtils.isEmpty(lng)) {
                try {
                    mLng = Double.parseDouble(lng);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mLng;
    }


    /**
     * 纬度
     */

    public static double getLat() {
        if (mLat == 0) {
            String lat = SpUtil.getInstance().getStringValue(SpUtil.LOCATION_LAT);
            if (!TextUtils.isEmpty(lat)) {
                try {
                    mLat = Double.parseDouble(lat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mLat;
    }

    /**
     * 省
     */
    public static String getProvince() {
        if (TextUtils.isEmpty(mProvince)) {
            mProvince = SpUtil.getInstance().getStringValue(SpUtil.LOCATION_PROVINCE);
        }
        return mProvince == null ? "" : mProvince;
    }

    /**
     * 市
     */
    public static String getCity() {
        if (TextUtils.isEmpty(mCity)) {
            mCity = SpUtil.getInstance().getStringValue(SpUtil.LOCATION_CITY);
        }
        return mCity == null ? "" : mCity;
    }

    /**
     * 区
     */
    public static String getDistrict() {
        if (TextUtils.isEmpty(mDistrict)) {
            mDistrict = SpUtil.getInstance().getStringValue(SpUtil.LOCATION_DISTRICT);
        }
        return mDistrict == null ? "" : mDistrict;
    }

    public static void setUserBean(UserBean bean,String json) {
        if(bean!=null){
            mUserBean = bean;
            SpUtil.getInstance().setStringValue(SpUtil.USER_INFO,json);
            String uid= mUserBean.getId();
            SpUtil.getInstance().setStringValue(SpUtil.UID,uid);
            mUid=uid;
        }
    }

    public static void setUserBean(UserBean bean) {
        if(bean!=null){
            mUserBean = bean;
            SpUtil.getInstance().setStringValue(SpUtil.USER_INFO,JSON.toJSONString(mUserBean));
            String uid= mUserBean.getId();
            SpUtil.getInstance().setStringValue(SpUtil.UID,uid);
            mUid=uid;
        }
    }

    public static UserBean getUserBean() {
        if (mUserBean == null) {
            String userBeanJson = SpUtil.getInstance().getStringValue(SpUtil.USER_INFO);
            if (!TextUtils.isEmpty(userBeanJson)) {
                mUserBean = JSON.parseObject(userBeanJson, UserBean.class);
            }



        }
        return mUserBean;
    }


    public static boolean isSelf(UserBean userBean){
        if(userBean!=null&&getUserBean()!=null){
            return  getUserBean().equals(userBean);
        }
        return false;
    }

    /**
     * 设置萌颜是否可用
     */
    public static void setTiBeautyEnable(boolean tiBeautyEnable) {
        mTiBeautyEnable = tiBeautyEnable;
        SpUtil.getInstance().setBooleanValue(SpUtil.TI_BEAUTY_ENABLE, tiBeautyEnable);
    }

    public static boolean isTiBeautyEnable() {
        if (mTiBeautyEnable == null) {
            mTiBeautyEnable = SpUtil.getInstance().getBooleanValue(SpUtil.TI_BEAUTY_ENABLE);
        }
        return mTiBeautyEnable;
    }

    /**
     * 设置登录信息
     */


    public static void setLoginInfo(String uid, String token, boolean save) {
        L.e("登录成功", "uid------>" + uid);
        L.e("登录成功", "token------>" + token);
        mUid = uid;
        mToken = token;
        if (save) {
            SpUtil.getInstance().setStringValue(SpUtil.UID,uid);
            SpUtil.getInstance().setStringValue(SpUtil.TOKEN,token);
        }
    }

    /**
     * 清除登录信息
     */
    public static void clearLoginInfo() {
        mUid = null;
        mToken = null;
        mLoginIM = false;
        SpUtil.getInstance().removeValue(
                SpUtil.UID, SpUtil.TOKEN, SpUtil.USER_INFO, SpUtil.TX_IM_USER_SIGN, SpUtil.IM_LOGIN
        );
    }


    /**
     * 设置位置信息
     *
     * @param lng      经度
     * @param lat      纬度
     * @param province 省
     * @param city     市
     */
    public static  void setLocationInfo(double lng, double lat, String province, String city, String district) {
        mLng = lng;
        mLat = lat;
        mProvince = province;
        mCity = city;
        mDistrict = district;
        Map<String, String> map = new HashMap<>();
        map.put(SpUtil.LOCATION_LNG, String.valueOf(lng));
        map.put(SpUtil.LOCATION_LAT, String.valueOf(lat));
        map.put(SpUtil.LOCATION_PROVINCE, province);
        map.put(SpUtil.LOCATION_CITY, city);
        map.put(SpUtil.LOCATION_DISTRICT, district);
        SpUtil.getInstance().setMultiStringValue(map);
    }

    /**
     * 清除定位信息
     */
    public static  void clearLocationInfo() {
        mLng = 0;
        mLat = 0;
        mProvince = null;
        mCity = null;
        mDistrict = null;
        SpUtil.getInstance().removeValue(
                SpUtil.LOCATION_LNG,
                SpUtil.LOCATION_LAT,
                SpUtil.LOCATION_PROVINCE,
                SpUtil.LOCATION_CITY,
                SpUtil.LOCATION_DISTRICT);

    }


    public static  boolean isLoginIM() {
        return mLoginIM;
    }

    public static  void setLoginIM(boolean loginIM) {
        mLoginIM = loginIM;
    }

    /**
     * 获取版本号
     */
    public static  String getVersion() {
        if (TextUtils.isEmpty(mVersion)) {
            try {
                PackageManager manager = CommonApplication.sInstance.getPackageManager();
                PackageInfo info = manager.getPackageInfo(CommonApplication.sInstance.getPackageName(), 0);
                mVersion = info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mVersion;
    }

    /**
     * 获取App名称
     */
    public static  String getAppName() {
        if (TextUtils.isEmpty(mAppName)) {
            mAppName = WordUtil.getString(R.string.app_name);
        }
        return mAppName;
    }


    /**
     * 获取MetaData中的极光AppKey
     */
    public static String getJPushAppKey() {
        if (mJPushAppKey == null) {
            mJPushAppKey = getMetaDataString("JPUSH_APPKEY");
        }
        return mJPushAppKey;
    }


    /**
     * 获取MetaData中的腾讯定位，地图的AppKey
     *
     * @return
     */
    public static  String getTxMapAppKey() {
        if (mTxMapAppKey == null) {
            mTxMapAppKey = getMetaDataString("TencentMapSDK");
        }
        return mTxMapAppKey;
    }


    /**
     * 获取MetaData中的腾讯定位，地图的AppSecret
     *
     * @return
     */
    public static String getTxMapAppSecret() {
        if (mTxMapAppSecret == null) {
            mTxMapAppSecret = getMetaDataString("TencentMapAppSecret");
        }
        return mTxMapAppSecret;
    }


    public static String getMetaDataString(String key) {
        String res = null;
        try {
            ApplicationInfo appInfo = CommonApplication.sInstance.getPackageManager().getApplicationInfo(CommonApplication.sInstance.getPackageName(), PackageManager.GET_META_DATA);
            res = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 个人中心功能列表
     */
    public static  List<UserItemBean> getUserItemList() {
        if (mUserItemList == null || mUserItemList.size() == 0) {
            String userBeanJson = SpUtil.getInstance().getStringValue(SpUtil.USER_INFO);
            if (!TextUtils.isEmpty(userBeanJson)) {
                JSONObject obj = JSON.parseObject(userBeanJson);
                if (obj != null) {
                    setUserItemList(obj.getString("list"));
                }
            }
        }
        return mUserItemList;
    }


    public static void setUserItemList(String listString) {
        if (!TextUtils.isEmpty(listString)) {
            try {
                mUserItemList = JSON.parseArray(listString, UserItemBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 保存用户等级信息
     */
    public static void setLevel(String levelJson) {
        if (TextUtils.isEmpty(levelJson)) {
            return;
        }
        List<LevelBean> list = JSON.parseArray(levelJson, LevelBean.class);
        if (list == null || list.size() == 0) {
            return;
        }
        if (mLevelMap == null) {
            mLevelMap = new SparseArray<>();
        }
        mLevelMap.clear();
        for (LevelBean bean : list) {
            mLevelMap.put(bean.getLevel(), bean);
        }
    }

    /**
     * 保存主播等级信息
     */
    public static  void setAnchorLevel(String anchorLevelJson) {
        if (TextUtils.isEmpty(anchorLevelJson)) {
            return;
        }
        List<LevelBean> list = JSON.parseArray(anchorLevelJson, LevelBean.class);
        if (list == null || list.size() == 0) {
            return;
        }
        if (mAnchorLevelMap == null) {
            mAnchorLevelMap = new SparseArray<>();
        }
        mAnchorLevelMap.clear();
        for (LevelBean bean : list) {
            mAnchorLevelMap.put(bean.getLevel(), bean);
        }
    }

    /**
     * 获取用户等级
     */
    public static LevelBean getLevel(int level) {
        if (mLevelMap == null) {
            String configString = SpUtil.getInstance().getStringValue(SpUtil.CONFIG);
            if (!TextUtils.isEmpty(configString)) {
                JSONObject obj = JSON.parseObject(configString);
                setLevel(obj.getString("level"));
            }
        }

        if (mLevelMap == null) {
            return null;
        }
        int size = mLevelMap.size();
        if (size == 0) {
            return null;
        }
        return mLevelMap.get(level);
    }







    /**
     * 判断某APP是否安装
     */
    public static boolean isAppExist(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            PackageManager manager = CommonApplication.sInstance.getPackageManager();
            List<PackageInfo> list = manager.getInstalledPackages(0);
            for (PackageInfo info : list) {
                if (packageName.equalsIgnoreCase(info.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static  boolean isLaunched() {
        if (mLaunched == null) {
            mLaunched = SpUtil.getInstance().getBooleanValue(SpUtil.APP_LAUNCHED);
        }
        return mLaunched;
    }

    public static  void setLaunched(boolean launched) {
        mLaunched = launched;
        SpUtil.getInstance().setBooleanValue(SpUtil.APP_LAUNCHED, launched);
    }


    public static  Long getLaunchTime() {
        if (mLaunchTime == null) {
            mLaunched = SpUtil.getInstance().getBooleanValue(SpUtil.APP_LAUNCHED);
        }
        return mLaunchTime;
    }

    public static  void setLaunchTime(Long launchTime) {
        SpUtil.getInstance().setLongValue(SpUtil.APP_LAUNCHED_TIME, launchTime);
        mLaunchTime = launchTime;
    }

    //app是否在前台
    public static  boolean isFrontGround() {
        return mFrontGround;
    }

    //app是否在前台
    public static void setFrontGround(boolean frontGround) {
        mFrontGround = frontGround;
    }

    public static int statuBarHeight() {
        if(statuBarHeight==0){
           statuBarHeight=SystemUtil.getStatusBarHeight(CommonApplication.sInstance);
        }
        return statuBarHeight;
    }

    public static int navigationBarHeight() {
        if(navigationBarHeight==0){
            navigationBarHeight=SystemUtil.getNavigationBarHeight(CommonApplication.sInstance);
        }
        return navigationBarHeight;
    }

    public static int getWindowWidth() {
        if(windowWidth==0){
            windowWidth= SystemUtil.getWindowsPixelWidth(CommonApplication.sInstance);
        }
        return windowWidth;
    }

    public static int getWindowHeight() {
        if(windowHeight==0){
            windowHeight= SystemUtil.getWindowsPixelHeight(CommonApplication.sInstance);
        }
        return windowHeight;
    }

    public static boolean isLogin() {
      if (TextUtils.isEmpty(getToken())) {
            return false;
       }
        return true;
    }


    public static void setBeautyKey(String beautyKey) {
        mBeautyKey = beautyKey;
        if (!TextUtils.isEmpty(beautyKey)) {
            SpUtil.getInstance().setStringValue(SpUtil.BEAUTY_KEY, beautyKey);
        }
    }

    public static String getBeautyKey() {
        if (TextUtils.isEmpty(mBeautyKey)) {
            mBeautyKey = SpUtil.getInstance().getStringValue(SpUtil.BEAUTY_KEY);
        }
        return mBeautyKey;
    }



    /**
     * 获取App图标的资源id
     */
    public static int getAppIconRes() {
        if (mAppIconRes == 0) {
            mAppIconRes = CommonApplication.sInstance.getResources().getIdentifier("ic_launcher", "mipmap", CommonApplication.sInstance.getPackageName());
        }
        return mAppIconRes;
    }


    public static String getGiftListJson() {
        return mGiftListJson;
    }

    public static void setGiftListJson(String getGiftListJson) {
        mGiftListJson = getGiftListJson;
        
    }


    public static String getGiftDaoListJson() {
        return mGiftDaoListJson;
    }

    public static void setGiftDaoListJson(String getGiftDaoListJson) {
        mGiftDaoListJson = getGiftDaoListJson;
    }

}
