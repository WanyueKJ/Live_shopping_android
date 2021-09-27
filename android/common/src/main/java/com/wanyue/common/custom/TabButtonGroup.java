package com.wanyue.common.custom;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wanyue.common.business.JumpInterceptor;

/**
 * Created by  on 2018/9/22.
 */

public class TabButtonGroup extends LinearLayout implements View.OnClickListener {

    private TabButton[] mTabButtons;
    private ViewPager mViewPager;
    private int mCurPosition;


    public TabButtonGroup(Context context) {
        this(context, null);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount > 0) {
            mTabButtons = new TabButton[childCount];
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                v.setTag(i);
                v.setOnClickListener(this);
                mTabButtons[i] = (TabButton) v;
            }
        }
    }


    public void setCurPosition(int position) {
        if (position == mCurPosition) {
            return;
        }
        mTabButtons[mCurPosition].setChecked(false);
        mTabButtons[position].setChecked(true);
        mCurPosition = position;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
           final int position=(int) tag;
            if(position!=0){
                setCurPosition(position);
               /* JumpInterceptor.shouldInterceptor(getContext()).subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                            if(aBoolean){

                            }
                    }
                });*/
            }else{
                setCurPosition(position);
            }


        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void cancelAnim() {
        if (mTabButtons != null) {
            for (TabButton tbn : mTabButtons) {
                if (tbn != null) {
                    tbn.cancelAnim();
                }
            }
        }
    }
}
