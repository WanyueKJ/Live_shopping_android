package com.wanyue.live.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/26.
 * 连麦推流小窗口  腾讯sdk
 */

public class LiveLinkMicPushTxViewHolder extends AbsLiveLinkMicPushViewHolder implements ITXLivePushListener {

    private static final String TAG = "LiveLinkMicPushTxViewHolder";
    private TXLivePusher mLivePusher;
    private TXLivePushConfig mLivePushConfig;

    public LiveLinkMicPushTxViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_link_mic_push_tx;
    }

    @Override
    public void init() {
        mLivePusher = new TXLivePusher(mContext);
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoFPS(15);//视频帧率
        mLivePushConfig.setVideoEncodeGop(1);//GOP大小
        mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_320_480);
        mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_AUTO);//硬件加速
        Bitmap bitmap = decodeResource(mContext.getResources(), R.mipmap.bg_live_tx_pause);
        mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setTouchFocus(false);//自动对焦
        //mLivePushConfig.setANS(true);//噪声抑制
        mLivePushConfig.enableAEC(true);//开启回声消除：连麦时必须开启，非连麦时不要开启
        mLivePushConfig.setAudioSampleRate(48000);
        mLivePushConfig.setAudioChannels(1);//声道数量
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.setMirror(true);
        mLivePusher.setPushListener(this);
        mLivePusher.setMicVolume(4f);
    }


    /**
     * 开始推流
     *
     * @param pushUrl 推流地址
     */
    @Override
    public void startPush(String pushUrl) {
        if (mLivePusher != null) {
            mLivePusher.startCameraPreview((TXCloudVideoView) findViewById(R.id.camera_preview));
            mLivePusher.startPusher(pushUrl);
        }
    }

    @Override
    public void release() {
        mLivePushListener = null;
        if (mLivePusher != null) {
            mLivePusher.stopPusher();
            mLivePusher.stopScreenCapture();
            mLivePusher.stopCameraPreview(false);
            mLivePusher.setPushListener(null);
        }
        mLivePusher = null;
        if (mLivePushConfig != null) {
            mLivePushConfig.setPauseImg(null);
        }
        mLivePushConfig = null;
    }

    @Override
    public void pause() {
        mPaused = true;
        if (mStartPush && mLivePusher != null) {
            mLivePusher.pausePusher();
        }
    }

    @Override
    public void resume() {
        if (mPaused && mStartPush && mLivePusher != null) {
            mLivePusher.resumePusher();
        }
        mPaused = false;
    }


    @Override
    public void onPushEvent(int e, Bundle bundle) {
        if (e == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
            ToastUtil.show(R.string.live_push_failed_1);
            if (mLivePushListener != null) {
                mLivePushListener.onPushFailed();
            }
        } else if (e == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            ToastUtil.show(R.string.live_push_failed_1);
            if (mLivePushListener != null) {
                mLivePushListener.onPushFailed();
            }
        } else if (e == TXLiveConstants.PUSH_ERR_NET_DISCONNECT || e == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS) {
            L.e(TAG, "网络断开，推流失败------>");
            if (mLivePushListener != null) {
                mLivePushListener.onPushFailed();
            }
        } else if (e == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            L.e(TAG, "不支持硬件加速------>");
            if (mLivePushConfig != null && mLivePusher != null) {
                mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
                mLivePusher.setConfig(mLivePushConfig);
            }
        } else if (e == TXLiveConstants.PUSH_EVT_FIRST_FRAME_AVAILABLE) {//预览成功
            L.e(TAG, "mStearm--->初始化完毕");
            if (mLivePushListener != null) {
                mLivePushListener.onPreviewStart();
            }
        } else if (e == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {//推流成功
            L.e(TAG, "mStearm--->推流成功");
            if (!mStartPush) {
                mStartPush = true;
                if (mLivePushListener != null) {
                    mLivePushListener.onPushStart();
                }
            }
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

}
