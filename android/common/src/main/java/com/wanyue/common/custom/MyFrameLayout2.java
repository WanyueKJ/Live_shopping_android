package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wanyue.common.R;

/**
 * Created by  on 2018/9/27.
 */

public class MyFrameLayout2 extends FrameLayout {

    private float mRatio;
    private float mOffestY;

    public MyFrameLayout2(@NonNull Context context) {
        this(context, null);
    }

    public MyFrameLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout2(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyFrameLayout2);
        mRatio = ta.getFloat(R.styleable.MyFrameLayout2_mfl_ratio, 1);
        mOffestY = ta.getDimension(R.styleable.MyFrameLayout2_mfl_offestY, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRatio + mOffestY), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
