package com.wanyue.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by  on 2018/9/26.
 */

public class MyRelativeLayout1 extends RelativeLayout {

    public MyRelativeLayout1(Context context) {
        super(context);
    }

    public MyRelativeLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
