package com.wanyue.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by  on 2018/6/7.
 */

public class SquareRoundedImageView extends RoundedImageView {
    public SquareRoundedImageView(Context context) {
        super(context);
    }
    public SquareRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
