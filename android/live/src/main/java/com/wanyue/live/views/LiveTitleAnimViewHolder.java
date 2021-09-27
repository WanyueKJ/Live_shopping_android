package com.wanyue.live.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;

/**
 * Created by  on 2019/5/15.
 */

public class LiveTitleAnimViewHolder extends AbsViewHolder {

    private ObjectAnimator mShowAnimator;
    private ObjectAnimator mHideAnimator;
    private View mGroup;
    private TextView mTip;
    private int mDp10;
    private int mDp500;
    private Handler mHandler;

    public LiveTitleAnimViewHolder(Context context, ViewGroup parentView, Handler handler) {
        super(context, parentView,handler);
    }

    @Override
    protected void processArguments(Object... args) {
        mHandler = (Handler) args[0];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_title_anim;
    }

    @Override
    public void init() {
        mGroup = findViewById(R.id.group);
        mTip = (TextView) findViewById(R.id.tip);
        mDp500 = DpUtil.dp2px(500);
        mShowAnimator = ObjectAnimator.ofFloat(mGroup, "translationX", mDp500, 0);
        mShowAnimator.setDuration(1000);
        mShowAnimator.setInterpolator(new LinearInterpolator());
        mShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mHideAnimator != null && mGroup != null) {
                                mHideAnimator.setFloatValues(0, -mDp10 - mGroup.getWidth());
                                mHideAnimator.start();
                            }
                        }
                    }, 2000);
                }
            }
        });
        mDp10 = DpUtil.dp2px(10);
        mHideAnimator = ObjectAnimator.ofFloat(mGroup, "translationX", 0);
        mHideAnimator.setDuration(1000);
        mHideAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGroup.setAlpha(1 - animation.getAnimatedFraction());
            }
        });
    }

    public void show(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mTip.setText(text);
        mGroup.setAlpha(1f);
        mShowAnimator.start();
    }

    public void clearAnim() {
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
        }
    }

    public void release() {
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
            mShowAnimator.removeAllListeners();
            mShowAnimator.removeAllUpdateListeners();
        }
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
            mHideAnimator.removeAllListeners();
            mHideAnimator.removeAllUpdateListeners();
        }
        mHandler = null;
    }
}
