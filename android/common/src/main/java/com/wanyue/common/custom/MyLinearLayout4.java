package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.wanyue.common.R;


/**
 * Created by  on 2018/7/26.
 */

public class MyLinearLayout4 extends LinearLayout {

    private int mScreenWidth;
    private float mCount;

    public MyLinearLayout4(Context context) {
        this(context, null);
    }

    public MyLinearLayout4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout4);
        mCount = ta.getFloat(R.styleable.MyLinearLayout4_mll4_count, 1);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mScreenWidth / mCount), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
