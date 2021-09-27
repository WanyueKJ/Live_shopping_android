package com.wanyue.live.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class LiveAudienceRecyclerView extends RecyclerView {

    private float mScale;
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public LiveAudienceRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public LiveAudienceRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveAudienceRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScale = dm.density;
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        mLeft = dp2px(10);
        mBottom = screenHeight - dp2px(50);
        mRight = screenWidth - dp2px(100);
        mTop = mBottom - dp2px(200);
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        float x = e.getRawX();
        float y = e.getRawY();
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }
}
