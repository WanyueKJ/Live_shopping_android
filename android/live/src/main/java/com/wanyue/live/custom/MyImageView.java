package com.wanyue.live.custom;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by  on 2018/11/21.
 */

public class MyImageView extends AppCompatImageView {

    private boolean mAnimating;
    private RotateAnimation mRotateAnimation;


    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRotateAnimation = new RotateAnimation(-30f, 30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.2f);
        mRotateAnimation.setDuration(300);
        mRotateAnimation.setRepeatCount(-1);
        mRotateAnimation.setRepeatMode(Animation.REVERSE);
    }

    public void startAnim() {
        if (!mAnimating) {
            mAnimating = true;
            startAnimation(mRotateAnimation);
        }
    }

    public void stopAnim() {
        if(mAnimating){
            mAnimating = false;
            clearAnimation();
        }
    }

}
