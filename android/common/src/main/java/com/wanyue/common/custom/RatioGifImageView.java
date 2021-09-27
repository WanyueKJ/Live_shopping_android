package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.wanyue.common.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by  on 2019/5/7.
 */

public class RatioGifImageView extends GifImageView {

    private float mRatio;

    public RatioGifImageView(Context context) {
        this(context, null);
    }

    public RatioGifImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioGifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        mRatio = ta.getFloat(R.styleable.RatioImageView_ri_ratio, 1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
