package com.wanyue.live.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/29.
 * 直播记录播放页面
 */

public class LiveRecordPlayViewHolder extends AbsViewHolder implements ITXLivePlayListener {

    private static final String TAG = "VideoPlayViewHolder";
    private TXCloudVideoView mVideoView;
    private View mLoading;
    private TXVodPlayer mPlayer;
    private boolean mClickPaused;
    private boolean mPaused;
    private boolean mStarted;
    private boolean mEnd;
    private long mDuration;
    private long mCurTime;
    private int mProgress;
    private ActionListener mActionListener;

    public LiveRecordPlayViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_record_play;
    }

    @Override
    public void init() {
        mLoading = findViewById(R.id.loading);
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mVideoView.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mPlayer = new TXVodPlayer(mContext);
        mPlayer.setPlayerView(mVideoView);
        mPlayer.setAutoPlay(true);
        mPlayer.setPlayListener(this);
    }

    @Override
    public void onPlayEvent(int e, Bundle bundle) {
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS://进度条
                if (mActionListener != null) {
                    int duration = bundle.getInt("EVT_PLAY_DURATION_MS");
                    if (mDuration != duration) {
                        mDuration = duration;
                        mActionListener.onDuration(duration);
                    }
                    int curTime = bundle.getInt("EVT_PLAY_PROGRESS_MS");
                    if (mCurTime != curTime) {
                        mCurTime = curTime;
                        mActionListener.onCurTime(curTime);
                    }
                    int progress = curTime * 100 / duration;
                    if (mProgress != progress) {
                        mProgress = progress;
                        mActionListener.onProgress(progress);
                    }
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                if (!mEnd) {
                    L.e(TAG, "VideoPlayView------>播放开始");
                    if (mLoading != null && mLoading.getVisibility() == View.VISIBLE) {
                        mLoading.setVisibility(View.INVISIBLE);
                    }
                } else {
                    release();
                }
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
            case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
                ToastUtil.show(mContext.getString(R.string.live_play_error));
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                if (mLoading != null && mLoading.getVisibility() != View.VISIBLE) {
                    mLoading.setVisibility(View.VISIBLE);
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                L.e(TAG, "VideoPlayView------>第一帧");
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                onReplay();
                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION:
                int width = bundle.getInt("EVT_PARAM1", 0);
                int height = bundle.getInt("EVT_PARAM2", 0);
                if (mVideoView != null && width >= height) {//横屏视频
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
                    float rate = ((float) width) / height;
                    params.height = (int) (mVideoView.getWidth() / rate);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mVideoView.requestLayout();
                }
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        L.e("onNetStatus-------->");
    }

    /**
     * 循环播放
     */
    private void onReplay() {
        if (!mEnd && mStarted && mPlayer != null) {
            mPlayer.seek(0);
            mPlayer.resume();
        }
    }

    /**
     * 开始播放
     */
    public void play(String url) {
        if (!mEnd && mPlayer != null) {
            if (mStarted) {
                mPlayer.stopPlay(false);
            }
            mPlayer.startPlay(url);
            mStarted = true;
            L.e(TAG, "play------->" + url);
        }
    }

    public void release() {
        mActionListener = null;
        mEnd = true;
        if (mPlayer != null) {
            mPlayer.stopPlay(true);
        }
        mPlayer = null;
        if (mVideoView != null) {
            mVideoView.onDestroy();
        }
        mVideoView = null;
        L.e(TAG, "release------->");
    }

    public void clickPause() {
        mClickPaused = true;
        if (!mEnd && mPlayer != null) {
            mPlayer.pause();
        }
        if (mActionListener != null) {
            mActionListener.onClickPause();
        }
    }

    public void clickResume() {
        if (mClickPaused) {
            mClickPaused = false;
            if (!mEnd && mPlayer != null) {
                mPlayer.resume();
            }
            if (mActionListener != null) {
                mActionListener.onClickResume();
            }
        }
    }

    /**
     * 跳转到视频流指定时间点.
     * 可实现视频快进,快退,进度条跳转等功能.
     *
     * @param time 视频流时间点,小数点后为毫秒
     */
    public void seekTo(float time) {
        if (!mEnd && mPlayer != null) {
            mPlayer.seek(time);
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {

        void onProgress(int progress);

        void onDuration(long duration);

        void onCurTime(long curTime);

        void onClickPause();

        void onClickResume();
    }

    @Override
    public void onPause() {
        mPaused = true;
        if (!mEnd && !mClickPaused && mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        if (mPaused) {
            mPaused = false;
            if (!mEnd && !mClickPaused && mPlayer != null) {
                mPlayer.resume();
            }
        }
    }

}
