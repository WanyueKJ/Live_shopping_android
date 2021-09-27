package com.wanyue.live.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;


import com.wanyue.common.utils.ScreenDimenUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/15.
 */

public abstract class AbsLiveDialogViewHolder extends AbsViewHolder implements View.OnClickListener {

    protected ObjectAnimator mEnterAnimator;
    protected ObjectAnimator mOutAnimator;
    protected boolean mLoad;
    protected boolean mShowed;
    protected boolean mAnimating;

    public AbsLiveDialogViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public AbsLiveDialogViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }


    @Override
    public void init() {
        int screenHeight = ScreenDimenUtil.getInstance().getScreenHeight();
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        mEnterAnimator = ObjectAnimator.ofFloat(mContentView, "translationY", screenHeight, 0);
        mEnterAnimator.setDuration(200);
        mEnterAnimator.setInterpolator(interpolator);
        mEnterAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                mShowed = true;
                onShow();
                loadData();
            }

        });
        mOutAnimator = ObjectAnimator.ofFloat(mContentView, "translationY", 0, screenHeight);
        mOutAnimator.setDuration(200);
        mOutAnimator.setInterpolator(interpolator);
        mOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                mShowed = false;
                onHide();
            }
        });
        View view = findViewById(R.id.btn_back);
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    public abstract void loadData();

    public void show() {
        if (!mAnimating) {
            mAnimating = true;
            mEnterAnimator.start();
        }
    }

    public void hide() {
        if (!mAnimating) {
            mAnimating = true;
            mOutAnimator.start();
        }
    }

    public void onShow() {

    }

    public void onHide() {

    }

    public boolean isShowed() {
        return mShowed;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_back||i==R.id.blank) {
            hide();
        }
    }

    public void release() {
        if (mEnterAnimator != null) {
            mEnterAnimator.cancel();
        }
        mEnterAnimator = null;
        if (mOutAnimator != null) {
            mOutAnimator.cancel();
        }
        mEnterAnimator = null;
    }

}
