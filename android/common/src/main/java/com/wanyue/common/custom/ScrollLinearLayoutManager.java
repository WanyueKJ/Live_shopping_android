package com.wanyue.common.custom;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean mCanVerticalScroll = true;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean canScrollVertically() {
        if (!mCanVerticalScroll){
            return false;
        }else {
            return super.canScrollVertically();
        }
    }
 
    public void setmCanVerticalScroll(boolean b){
        mCanVerticalScroll = b;
    }
}
