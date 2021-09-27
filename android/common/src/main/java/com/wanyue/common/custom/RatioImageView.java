package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wanyue.common.R;

/**
 * Created by  on 2018/9/27.
 */

public class RatioImageView extends AppCompatImageView {

    private float mRatio;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
