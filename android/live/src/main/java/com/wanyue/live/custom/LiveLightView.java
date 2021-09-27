package com.wanyue.live.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PathMeasure;

import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by  on 2017/8/23.
 * 直播间飘心的ImageView
 */

public class LiveLightView extends AppCompatImageView {

    public static int sOffsetY = 0;
    private static final int DURATION = 3000;
    private boolean idle;
    private ValueAnimator mValueAnimator;
    private TimeInterpolator mInterpolator;
    private PathMeasure mPathMeasure;
    private float[] mPos;
    private float mLength;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private AnimatorListenerAdapter mAnimatorListenerAdapter;
    private boolean mCanceled;

    public LiveLightView(Context context) {
        this(context, null);
    }

    public LiveLightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveLightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float v = (float) animator.getAnimatedValue();
                float scale = v / mLength;
                if (scale < 0.08f) {
                    setScaleX(scale * 12.5f);
                    setScaleY(scale * 12.5f);
                } else {
                    setScaleX(1 + 0.5f * scale);
                    setScaleY(1 + 0.5f * scale);
                }
                if (scale > 0.5) {
                    setAlpha((1 - scale) * 2);
                }
                mPathMeasure.getPosTan(v, mPos, null);
                setX(mPos[0]);
                setY(mPos[1] - sOffsetY);
            }
        };
        mAnimatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setIdle(true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                setAlpha(1f);
            }
        };
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }


    public void play(PathMeasure pathMeasure) {
        if (mCanceled) {
            return;
        }
        mPathMeasure = pathMeasure;
        mPos = new float[2];
        mLength = pathMeasure.getLength();
        mValueAnimator = ValueAnimator.ofFloat(0, mLength);
        mValueAnimator.setDuration(DURATION);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(mUpdateListener);
        mValueAnimator.addListener(mAnimatorListenerAdapter);
        mValueAnimator.start();
    }

    public void cancel() {
        mCanceled = true;
        if (mValueAnimator != null) {
            if (mValueAnimator.isStarted() || mValueAnimator.isRunning()) {
                mValueAnimator.cancel();
            }
        }
        mUpdateListener = null;
        mAnimatorListenerAdapter = null;
    }
}
