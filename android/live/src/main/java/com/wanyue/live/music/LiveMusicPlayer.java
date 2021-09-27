package com.wanyue.live.music;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;

import com.wanyue.common.CommonAppConfig;

import java.io.IOException;
import java.util.List;

/**
 * Created by  on 2018/10/22.
 */

public class LiveMusicPlayer implements MediaPlayer.OnPreparedListener, ValueAnimator.AnimatorUpdateListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;//MediaPlayer用来获取音频时长
    private boolean mPaused;
    private List<LrcBean> mLrcList;
    private ActionListener mActionListener;
    private int mLrcIndex;//当前歌词的索引
    private ValueAnimator mAnimator;
    private String mMusicPath;

    public LiveMusicPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setLooping(true);
        mPlayer.setVolume(0f, 0f);
        mAnimator = ValueAnimator.ofFloat();
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(-1);
        mAnimator.addUpdateListener(this);
    }

    public void play(String musicId) {
        if (TextUtils.isEmpty(musicId)) {
            return;
        }
        mLrcList = LrcParser.getLrcListByMusicId(musicId);
        try {
            mMusicPath = CommonAppConfig.MUSIC_PATH + musicId + ".mp3";
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            mPlayer.setDataSource(mMusicPath);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (mActionListener != null) {
            mActionListener.onPrepareSuccess(mMusicPath);
        }
        long duration = mp.getDuration();
        boolean hasLrc = mLrcList != null && mLrcList.size() > 0;
        if (mActionListener != null) {
            mActionListener.onParseLrcResult(hasLrc);
        }
        mLrcIndex = -1;
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        if (hasLrc) {
            for (int i = 0, size = mLrcList.size(); i < size; i++) {
                LrcBean bean = mLrcList.get(i);
                bean.setIndex(i);
                if (i == size - 1) {
                    bean.setEndTime(duration);
                } else {
                    bean.setEndTime(mLrcList.get(i + 1).getStartTime());
                }
            }
            if (mAnimator != null) {
                mAnimator.setFloatValues(0, duration);
                mAnimator.setDuration(duration);
                mAnimator.start();
            }
        }
    }

    /**
     * 获取歌词
     */
    private LrcBean getLrc(long time) {
        if (mLrcIndex >= 0 && mLrcList != null && mLrcIndex < mLrcList.size()) {
            LrcBean curLrcBean = mLrcList.get(mLrcIndex);
            if (time >= curLrcBean.getStartTime() && time <= curLrcBean.getEndTime()) {
                float progress = (time - curLrcBean.getStartTime()) / curLrcBean.getDuration();
                if (progress < 0.01f) {
                    progress = 0;
                }
                curLrcBean.setProgress(progress);
                return curLrcBean;
            }
        }
        for (LrcBean bean : mLrcList) {
            if (time >= bean.getStartTime() && time <= bean.getEndTime()) {
                float progress = (time - bean.getStartTime()) / bean.getDuration();
                if (progress < 0.01f) {
                    progress = 0;
                }
                bean.setProgress(progress);
                return bean;
            }
        }
        return null;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mActionListener != null) {
            float v = (float) animation.getAnimatedValue();
            LrcBean bean = getLrc((long) v);
            if (bean != null) {
                if (mLrcIndex != bean.getIndex()) {
                    mLrcIndex = bean.getIndex();
                    mActionListener.onLrcChanged(bean.getLrc());
                }
                mActionListener.onLrcProgressChanged(bean.getProgress());
            }
        }
    }


    public void pause() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
        mPaused = true;
    }

    public void resume() {
        if (mPaused) {
            if (mAnimator != null) {
                mAnimator.resume();
            }
            mPaused = false;
        }
    }

    public void release() {
        mActionListener = null;
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mActionListener != null) {
            mActionListener.onCompletion(mMusicPath);
        }
    }

    public interface ActionListener {

        void onPrepareSuccess(String path);

        void onCompletion(String path);

        void onParseLrcResult(boolean success);

        void onLrcChanged(String lrc);

        void onLrcProgressChanged(float progress);
    }

    public ActionListener getActionListener() {
        return mActionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }
}
