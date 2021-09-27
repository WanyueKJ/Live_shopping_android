package com.yunbao.im.config;

import com.tencent.trtc.TRTCCloudDef;

public class CallConfig {
    public static final int CALL=1;
    public static final int WAIT=2;
    public static final int CONNECT=3;
    public static final int DISCONNECT=4;





    final static int DEFAULT_BITRATE = 600;
    final static int DEFAULT_FPS = 15;
    final static int DEFAULT_RESOLUTION = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;


    public static  TRTCCloudDef.TRTCVideoEncParam createDefaultBigEncParam(){
        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = DEFAULT_RESOLUTION;
        encParam.videoFps = DEFAULT_FPS;
        encParam.videoBitrate =DEFAULT_BITRATE;
        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
        return encParam;
    }


    public static  TRTCCloudDef.TRTCVideoEncParam createDefaultSmallEncParam(){
        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_160_90;
        encParam.videoFps = DEFAULT_FPS;
        encParam.videoBitrate =100;
        return encParam;
    }


}
