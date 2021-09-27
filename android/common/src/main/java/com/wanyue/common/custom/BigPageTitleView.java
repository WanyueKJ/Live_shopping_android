package com.wanyue.common.custom;

import android.content.Context;
import android.util.TypedValue;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class BigPageTitleView extends SimplePagerTitleView {
    private float mScaleValue=1.3F;
    private float mDefaultTextSize;

    public BigPageTitleView(Context context) {
        super(context);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index,totalCount);
        getPaint().setFakeBoldText(true);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,mScaleValue*mDefaultTextSize);
        postInvalidate();
    }

    public void setScaleValue(float scaleValue) {
        mScaleValue = scaleValue;
    }


    public void setTextSizeDp(int size) {
        mDefaultTextSize=size;
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        setTextColor(mNormalColor);
        getPaint().setFakeBoldText(false);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,mDefaultTextSize);
        postInvalidate();

    }
}
