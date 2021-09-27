package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.wanyue.common.R;


/**
 * Created by  on 2018/7/26.
 */

public class MyLinearLayout6 extends LinearLayout {

    private float mRatio;

    public MyLinearLayout6(Context context) {
        this(context, null);
    }

    public MyLinearLayout6(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout6);
        mRatio = ta.getFloat(R.styleable.MyLinearLayout6_mll_ratio, 1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
