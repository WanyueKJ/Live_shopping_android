package com.wanyue.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2019/5/6.
 */

public class LiveKsyConfigBean implements Parcelable{

    private int mEncodeMethod;//编码模式
    private int mTargetResolution;////推流分辨率
    private int mTargetFps;//推流采集帧率
    private int mTargetGop;//推流采集gop
    private int mVideoKBitrate;//视频码率
    private int mVideoKBitrateMax;//最大视频码率
    private int mVideoKBitrateMin;//最小视频码率
    private int mAudioKBitrate;//音频码率
    private int mPreviewFps;//预览采集帧率
    private int mPreviewResolution;//预览分辨率

    public LiveKsyConfigBean() {
    }


    @JSONField(name = "codingmode")
    public int getEncodeMethod() {
        return mEncodeMethod;
    }
    @JSONField(name = "codingmode")
    public void setEncodeMethod(int encodeMethod) {
        mEncodeMethod = encodeMethod;
    }
    @JSONField(name = "resolution")
    public int getTargetResolution() {
        return mTargetResolution;
    }
    @JSONField(name = "resolution")
    public void setTargetResolution(int targetResolution) {
        mTargetResolution = targetResolution;
    }
    @JSONField(name = "fps")
    public int getTargetFps() {
        return mTargetFps;
    }
    @JSONField(name = "fps")
    public void setTargetFps(int targetFps) {
        mTargetFps = targetFps;
    }
    @JSONField(name = "gop")
    public int getTargetGop() {
        return mTargetGop;
    }
    @JSONField(name = "gop")
    public void setTargetGop(int targetGop) {
        mTargetGop = targetGop;
    }
    @JSONField(name = "bitrate")
    public int getVideoKBitrate() {
        return mVideoKBitrate;
    }
    @JSONField(name = "bitrate")
    public void setVideoKBitrate(int videoKBitrate) {
        mVideoKBitrate = videoKBitrate;
    }
    @JSONField(name = "bitrate_max")
    public int getVideoKBitrateMax() {
        return mVideoKBitrateMax;
    }
    @JSONField(name = "bitrate_max")
    public void setVideoKBitrateMax(int videoKBitrateMax) {
        mVideoKBitrateMax = videoKBitrateMax;
    }
    @JSONField(name = "bitrate_min")
    public int getVideoKBitrateMin() {
        return mVideoKBitrateMin;
    }
    @JSONField(name = "bitrate_min")
    public void setVideoKBitrateMin(int videoKBitrateMin) {
        mVideoKBitrateMin = videoKBitrateMin;
    }
    @JSONField(name = "audiobitrate")
    public int getAudioKBitrate() {
        return mAudioKBitrate;
    }
    @JSONField(name = "audiobitrate")
    public void setAudioKBitrate(int audioKBitrate) {
        mAudioKBitrate = audioKBitrate;
    }
    @JSONField(name = "preview_fps")
    public int getPreviewFps() {
        return mPreviewFps;
    }
    @JSONField(name = "preview_fps")
    public void setPreviewFps(int previewFps) {
        mPreviewFps = previewFps;
    }
    @JSONField(name = "preview_resolution")
    public int getPreviewResolution() {
        return mPreviewResolution;
    }
    @JSONField(name = "preview_resolution")
    public void setPreviewResolution(int previewResolution) {
        mPreviewResolution = previewResolution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mEncodeMethod);
        dest.writeInt(mTargetResolution);
        dest.writeInt(mTargetFps);
        dest.writeInt(mTargetGop);
        dest.writeInt(mVideoKBitrate);
        dest.writeInt(mVideoKBitrateMax);
        dest.writeInt(mVideoKBitrateMin);
        dest.writeInt(mAudioKBitrate);
        dest.writeInt(mPreviewFps);
        dest.writeInt(mPreviewResolution);
    }

    public LiveKsyConfigBean(Parcel in) {
        mEncodeMethod = in.readInt();
        mTargetResolution = in.readInt();
        mTargetFps = in.readInt();
        mTargetGop = in.readInt();
        mVideoKBitrate = in.readInt();
        mVideoKBitrateMax = in.readInt();
        mVideoKBitrateMin = in.readInt();
        mAudioKBitrate = in.readInt();
        mPreviewFps = in.readInt();
        mPreviewResolution = in.readInt();
    }

    public static final Creator<LiveKsyConfigBean> CREATOR = new Creator<LiveKsyConfigBean>() {
        @Override
        public LiveKsyConfigBean createFromParcel(Parcel in) {
            return new LiveKsyConfigBean(in);
        }

        @Override
        public LiveKsyConfigBean[] newArray(int size) {
            return new LiveKsyConfigBean[size];
        }
    };
}
