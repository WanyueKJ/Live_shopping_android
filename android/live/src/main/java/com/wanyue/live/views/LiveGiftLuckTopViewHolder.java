package com.wanyue.live.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveLuckGiftWinBean;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by  on 2019/4/29.
 */

public class LiveGiftLuckTopViewHolder extends AbsViewHolder {

    private int mParentWidth;
    private int mDp20;
    private View mRoot;
    private TextView mGiftTip;
    private String mTipString;
    private ObjectAnimator mContainerAnimtor1;
    private ObjectAnimator mContainerAnimtor2;
    private ObjectAnimator mTextAnimtor;
    private boolean mAimating;
    private ConcurrentLinkedQueue<LiveLuckGiftWinBean> mQueue;

    public LiveGiftLuckTopViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.view_gift_luck_anim;
    }

    @Override
    public void init() {
        mQueue = new ConcurrentLinkedQueue<>();
        mTipString = WordUtil.getString(R.string.live_gift_luck_tip);
        mRoot = findViewById(R.id.root);
        mGiftTip = (TextView) findViewById(R.id.luck_gift_tip);
        mParentWidth = mParentView.getWidth();
        mDp20 = DpUtil.dp2px(20);
        ViewGroup.LayoutParams params = mRoot.getLayoutParams();
        params.width = mParentWidth + mDp20;
        mRoot.requestLayout();
        mGiftTip.setTranslationX(mParentWidth);
        TimeInterpolator interpolator = new LinearInterpolator();
        mContainerAnimtor1 = ObjectAnimator.ofFloat(mRoot, "translationX", mParentWidth, -mDp20);
        mContainerAnimtor1.setDuration(400);
        mContainerAnimtor1.setInterpolator(interpolator);
        mContainerAnimtor1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mTextAnimtor != null) {
                    mTextAnimtor.start();
                }
            }
        });

        mContainerAnimtor2 = ObjectAnimator.ofFloat(mRoot, "translationX", -mDp20, -mDp20 - mParentWidth);
        mContainerAnimtor2.setDuration(400);
        mContainerAnimtor2.setInterpolator(interpolator);
        mContainerAnimtor2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAimating = false;
                if (mQueue != null) {
                    LiveLuckGiftWinBean bean = mQueue.poll();
                    if (bean != null) {
                        show(bean);
                    }
                }
            }
        });
        mTextAnimtor = ObjectAnimator.ofFloat(mGiftTip, "translationX", 0);
        mTextAnimtor.setDuration(3000);
        mTextAnimtor.setInterpolator(interpolator);
        mTextAnimtor.setRepeatCount(1);
        mTextAnimtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mContainerAnimtor2 != null) {
                    mContainerAnimtor2.start();
                }
            }
        });
    }

    public void show(LiveLuckGiftWinBean bean) {
        if (mAimating) {
            if (mQueue != null) {
                mQueue.offer(bean);
            }
            return;
        }
        mAimating = true;
        mGiftTip.setText(String.format(mTipString, bean.getUserNiceName(), bean.getGiftName(), bean.getLuckTime()));
        mGiftTip.measure(0, 0);
        int parentWidth = mParentView.getWidth();
        if (mParentWidth != parentWidth) {
            mParentWidth = parentWidth;
            ViewGroup.LayoutParams params = mRoot.getLayoutParams();
            params.width = mParentWidth + mDp20;
            mRoot.requestLayout();
            mGiftTip.setTranslationX(mParentWidth);
            mContainerAnimtor1.setFloatValues(mParentWidth, -mDp20);
            mContainerAnimtor2.setFloatValues(-mDp20, -mDp20 - mParentWidth);
        }
        mTextAnimtor.setFloatValues(parentWidth, -mGiftTip.getMeasuredWidth());
        mContainerAnimtor1.start();
    }


    public void clearAnim() {
        if (mContainerAnimtor1 != null) {
            mContainerAnimtor1.cancel();
        }
        if (mContainerAnimtor2 != null) {
            mContainerAnimtor2.cancel();
        }
        if (mTextAnimtor != null) {
            mTextAnimtor.cancel();
        }
        if (mQueue != null) {
            mQueue.clear();
        }
    }

    @Override
    public void release() {
        if (mContainerAnimtor1 != null) {
            mContainerAnimtor1.cancel();
            mContainerAnimtor1.removeAllListeners();
            mContainerAnimtor1.removeAllUpdateListeners();
        }
        if (mContainerAnimtor2 != null) {
            mContainerAnimtor2.cancel();
            mContainerAnimtor2.removeAllListeners();
            mContainerAnimtor2.removeAllUpdateListeners();
        }
        if (mTextAnimtor != null) {
            mTextAnimtor.cancel();
            mTextAnimtor.removeAllListeners();
            mTextAnimtor.removeAllUpdateListeners();
        }
        if (mQueue != null) {
            mQueue.clear();
        }
        mContainerAnimtor1 = null;
        mContainerAnimtor2 = null;
        mTextAnimtor = null;
        mQueue = null;
    }

}
