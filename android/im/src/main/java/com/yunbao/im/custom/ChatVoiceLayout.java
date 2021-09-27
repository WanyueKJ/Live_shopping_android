package com.yunbao.im.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yunbao.im.R;
import com.yunbao.im.bean.ImMessageBean;

/**
 * Created by  on 2018/11/13.
 */

public class ChatVoiceLayout extends FrameLayout {

    private Context mContext;
    private int mDuration;
    private int mDirection;
    private float mScale;
    private int mMaxWidth;
    private int mMinWidth;
    private int mRate;
    private ImageView mImageView;
    private Drawable[] mDrawables;
    private int mIndex = 2;
    private ImMessageBean mImMessageBean;


    public ChatVoiceLayout(@NonNull Context context) {
        this(context, null);
    }

    public ChatVoiceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatVoiceLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatVoiceLayout);
        mDuration = ta.getInt(R.styleable.ChatVoiceLayout_cvl_duration, 0);
        mDirection = ta.getInt(R.styleable.ChatVoiceLayout_cvl_direction, 0);
        ta.recycle();
        mScale = context.getResources().getDisplayMetrics().density;
        mMaxWidth = dp2px(200);
        mMinWidth = dp2px(24);
        mRate = dp2px(6);
        mDrawables = new Drawable[3];
        if (mDirection == 0) {
            mDrawables[0] = ContextCompat.getDrawable(context, R.mipmap.icon_voice_left_1);
            mDrawables[1] = ContextCompat.getDrawable(context, R.mipmap.icon_voice_left_2);
            mDrawables[2] = ContextCompat.getDrawable(context, R.mipmap.icon_voice_left_3);
        } else {
            mDrawables[0] = ContextCompat.getDrawable(context, R.mipmap.icon_voice_right_1);
            mDrawables[1] = ContextCompat.getDrawable(context, R.mipmap.icon_voice_right_2);
            mDrawables[2] = ContextCompat.getDrawable(context, R.mipmap.icon_voice_right_3);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = mRate * mDuration + mMinWidth;
        if (widthSize > mMaxWidth) {
            widthSize = mMaxWidth;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImageView = new ImageView(mContext);
        int size = dp2px(24);
        LayoutParams params = new LayoutParams(size, size);
        if (mDirection == 0) {
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        } else {
            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }
        mImageView.setLayoutParams(params);
        mImageView.setImageDrawable(mDrawables[2]);
        addView(mImageView);
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }

    public void animate(int index) {
        if (index == mIndex) {
            return;
        }
        if (mImageView != null && index >= 0 && index < 3) {
            mImageView.setImageDrawable(mDrawables[index]);
        }
        mIndex = index;
    }

    public void cancelAnim() {
        if (mIndex == 2) {
            return;
        }
        if (mImageView != null) {
            mImageView.setImageDrawable(mDrawables[2]);
        }
    }

    public void setDuration(int duration) {
        if (mDuration == duration || mImageView == null) {
            return;
        }
        mDuration = duration;
        ViewGroup.LayoutParams params = getLayoutParams();
        int widthSize = mRate * mDuration + mMinWidth;
        if (widthSize > mMaxWidth) {
            widthSize = mMaxWidth;
        }
        params.width = widthSize;
        setLayoutParams(params);
    }

    public ImMessageBean getImMessageBean() {
        return mImMessageBean;
    }

    public void setImMessageBean(ImMessageBean imMessageBean) {
        mImMessageBean = imMessageBean;
    }
}
