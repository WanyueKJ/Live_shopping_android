package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.wanyue.common.R;

/**
 * Created by  on 2018/9/25.
 */

public class MyLinearLayout2 extends LinearLayout {

    private float mScreenWidth;
    private int mSpanCount;

    public MyLinearLayout2(Context context) {
        this(context, null);
    }

    public MyLinearLayout2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout2);
        mSpanCount = ta.getInteger(R.styleable.MyLinearLayout2_mll_span_count, 6);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mScreenWidth / mSpanCount), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
