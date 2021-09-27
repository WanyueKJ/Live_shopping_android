package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.wanyue.live.R;

/**
 * Created by  on 2018/10/25.
 */

public class MyFrameLayout3 extends FrameLayout {

    private int mScreenWidth;
    private int mScreenHeight;
    private float mRatio;

    public MyFrameLayout3(@NonNull Context context) {
        this(context, null);
    }

    public MyFrameLayout3(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout3(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyFrameLayout3);
        mRatio = ta.getFloat(R.styleable.MyFrameLayout3_mfl3_ratio, 1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mScreenWidth * mRatio), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mScreenHeight * mRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
