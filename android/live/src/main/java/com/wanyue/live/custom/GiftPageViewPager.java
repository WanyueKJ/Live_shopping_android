package com.wanyue.live.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;

/**
 * Created by  on 2018/10/13.
 */

public class GiftPageViewPager extends ViewPager {


    public GiftPageViewPager(@NonNull Context context) {
        this(context, null);
    }

    public GiftPageViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize / 2, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
