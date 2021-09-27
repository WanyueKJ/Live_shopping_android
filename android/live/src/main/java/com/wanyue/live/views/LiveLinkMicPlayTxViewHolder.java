package com.wanyue.live.views;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/25.
 * 连麦播放小窗口  使用腾讯sdk
 */

public class LiveLinkMicPlayTxViewHolder extends AbsLiveLinkMicPlayViewHolder implements ITXLivePlayListener {

    private static final String TAG = "LiveLinkMicPlayTxViewHolder";
    private TXCloudVideoView mVideoView;
    private TXLivePlayer mPlayer;

    public LiveLinkMicPlayTxViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_link_mic_play_tx;
    }

    @Override
    public void init() {
        super.init();
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mPlayer = new TXLivePlayer(mContext);
        mPlayer.setPlayListener(this);
        mPlayer.setPlayerView(mVideoView);
        mPlayer.enableHardwareDecode(true);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        TXLivePlayConfig playConfig = new TXLivePlayConfig();
        playConfig.enableAEC(true);
        playConfig.setAutoAdjustCacheTime(true);
        playConfig.setMaxAutoAdjustCacheTime(5.0f);
        playConfig.setMinAutoAdjustCacheTime(1.0f);
        mPlayer.setConfig(playConfig);
    }

    @Override
    public void setOnCloseListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            mBtnClose.setVisibility(View.VISIBLE);
            mBtnClose.setOnClickListener(onClickListener);
        }
    }

    /**
     * 开始播放
     *
     * @param url 流地址
     */
    @Override
    public void play(final String url) {
        if (TextUtils.isEmpty(url)) {
            if (mPlayer != null) {
                mPlayer.stopPlay(true);
            }
            mEndPlay = true;
            return;
        }
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEndPlay = false;
                    if (mPlayer != null) {
                        mPlayer.startPlay(url, TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
                    }
                    L.e(TAG, "play----url--->" + url);
                }
            }, 500);
        }
    }

    @Override
    public void release() {
        mEndPlay = true;
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
            mPlayer.setPlayListener(null);
        }
        mPlayer = null;
        if (mBtnClose != null) {
            mBtnClose.setOnClickListener(null);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        L.e(TAG, "release------->");
    }

    @Override
    public void resume() {
        if (mPaused && mVideoView != null) {
            mPlayer.resume();
        }
        mPaused = false;
    }
    @Override
    public void pause() {
        if (mVideoView != null) {
            mPlayer.pause();
        }
        mPaused = true;
    }


    @Override
    public void onPlayEvent(int e, Bundle bundle) {
        if (mEndPlay) {
            return;
        }
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN://播放开始
                if (mLoading != null && mLoading.getVisibility() == View.VISIBLE) {
                    mLoading.setVisibility(View.INVISIBLE);
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                if (mLoading != null && mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
                break;
//            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME://第一帧
//                break;
//            case TXLiveConstants.PLAY_EVT_PLAY_END://播放结束
//                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION://获取视频宽高
//                float width = bundle.getInt("EVT_PARAM1", 0);
//                float height = bundle.getInt("EVT_PARAM2", 0);
//                L.e(TAG, "流---width----->" + width);
//                L.e(TAG, "流---height----->" + height);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
//                int targetH = 0;
//                if (width / height > 0.5625f) {//横屏 9:16=0.5625
//                    targetH = (int) (mVideoView.getWidth() / width * height);
//                } else {
//                    targetH = ViewGroup.LayoutParams.MATCH_PARENT;
//                }
//                if (targetH != params.height) {
//                    params.height = targetH;
//                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
//                    mVideoView.requestLayout();
//                }

                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT://播放失败
            case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
                ToastUtil.show(WordUtil.getString(R.string.live_play_error));
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }
}
