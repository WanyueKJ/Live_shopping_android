package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wanyue.common.R;

/**
 * Created by  on 2018/9/27.
 */

public class MyRelativeLayout2 extends RelativeLayout {

    private int mScreenWidth;
    private float mRatio;
    private float mScaleWidth;

    public MyRelativeLayout2(@NonNull Context context) {
        this(context, null);
    }

    public MyRelativeLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout2(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyRelativeLayout2);
        mRatio = ta.getFloat(R.styleable.MyRelativeLayout2_mrl_ratio, 1);
        mScaleWidth = ta.getFloat(R.styleable.MyRelativeLayout2_mrl_scaleX, 1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = (int) (mScreenWidth * mScaleWidth);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
