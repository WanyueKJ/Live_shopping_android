package com.wanyue.live;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.live.bean.LiveKsyConfigBean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by  on 2018/10/7.
 * 直播相关的参数配置
 */

public class LiveConfig {
    //推流参数配置
    public static final int PUSH_CAP_RESOLUTION = 0;//采集分辨率
    public static final int PUSH_PREVIEW_RESOLUTION = 0;//直播预览分辨率
    public static final int LINK_MIC_PUSH_PREVIEW_RESOLUTION = 0;//连麦预览分辨率
    public static final int PUSH_VIDEO_RESOLUTION = 0;//推流分辨率
    public static final int PUSH_ENCODE_TYPE = 0;//H264
    public static final int PUSH_ENCODE_METHOD = 0;//软编
    public static final int PUSH_ENCODE_SCENE = 0;//秀场模式
    public static final int PUSH_ENCODE_PROFILE = 0;//低功耗
    public static final int PUSH_FRAME_RATE = 20;//采集帧率
    public static final int PUSH_VIDEO_BITRATE = 500;//视频码率
    public static final int PUSH_VIDEO_BITRATE_MAX = 800;//视频码率
    public static final int PUSH_VIDEO_BITRATE_MIN = 300;//视频码率
    public static final int PUSH_AUDIO_BITRATE = 48;//音频码率
    public static final int PUSH_GOP = 3;//gop


    public static LiveKsyConfigBean getDefaultKsyConfig() {
        LiveKsyConfigBean bean = new LiveKsyConfigBean();
        bean.setEncodeMethod(PUSH_ENCODE_METHOD);
        bean.setTargetResolution(PUSH_VIDEO_RESOLUTION);
        bean.setTargetFps(PUSH_FRAME_RATE);
        bean.setTargetGop(PUSH_GOP);
        bean.setVideoKBitrate(PUSH_VIDEO_BITRATE);
        bean.setVideoKBitrateMax(PUSH_VIDEO_BITRATE_MAX);
        bean.setVideoKBitrateMin(PUSH_VIDEO_BITRATE_MIN);
        bean.setAudioKBitrate(PUSH_AUDIO_BITRATE);
        bean.setPreviewFps(PUSH_FRAME_RATE);
        bean.setPreviewResolution(PUSH_PREVIEW_RESOLUTION);
        return bean;
    }


    /**
     * 获取手机相关信息，开播时候用到
     */
    public static String getSystemParams() {
        String sysParams = StringUtil.contact(
                android.os.Build.BRAND,//手机厂商
                "_",
                android.os.Build.MODEL,//手机型号
                "_",
                android.os.Build.VERSION.RELEASE,//系统版本号
                "_",
                getMemory(),
                "_",
                getNetworkType()
        );
        L.e("开播", "手机信息------> " + sysParams);
        return sysParams;
    }

    /**
     * 获取手机内存大小
     */
    public static String getMemory() {
        String MemorySize = "NONE";
        BufferedReader br = null;
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            br = new BufferedReader(fileReader, 8192);
            String str = br.readLine();// 读取meminfo第一行，系统总内存大小
            if (!TextUtils.isEmpty(str)) {
                String[] array = str.split("\\s+");
                int totalRam = (int) Math.ceil(Double.valueOf(array[1]) / (1024*1024));
                MemorySize = StringUtil.contact(String.valueOf(totalRam), "GB");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return MemorySize;
    }

    /**
     * 获取当前网络类型
     **/
    public static String getNetworkType() {
        String netType = "NONE";
        ConnectivityManager manager = (ConnectivityManager) CommonApplication.sInstance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "WIFI";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) CommonApplication.sInstance.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "4G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "2G";
            } else {
                netType = "NO DISPLAY";
            }
        }
        return netType;
    }

}
