package com.wanyue.common.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollRecyleview extends RecyclerView {
    private boolean mIsNoCanSCroll=true;

    public NoScrollRecyleview(@NonNull Context context) {
        super(context);
    }

    public NoScrollRecyleview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyleview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mIsNoCanSCroll){
           return false;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setNoCanSCroll(boolean noCanSCroll) {
        mIsNoCanSCroll = noCanSCroll;
    }
}
