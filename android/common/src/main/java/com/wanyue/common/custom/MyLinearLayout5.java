package com.wanyue.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


/**
 * Created by  on 2018/7/26.
 */

public class MyLinearLayout5 extends LinearLayout {


    public MyLinearLayout5(Context context) {
        super(context);
    }

    public MyLinearLayout5(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED), heightMeasureSpec);
    }
}
