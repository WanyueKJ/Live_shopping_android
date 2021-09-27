package com.wanyue.live.custom;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by  on 2019/4/29.
 */

public class MyTextView2 extends AppCompatTextView {
    public MyTextView2(Context context) {
        super(context);
    }

    public MyTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED), heightMeasureSpec);
    }
}
