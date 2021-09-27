package com.wanyue.common.custom;

import android.view.ViewTreeObserver;

public class MyPreDrawFilter implements ViewTreeObserver.OnPreDrawListener {
    public MyPreDrawFilter(ZoomView zoomView) {
    }

    @Override
    public boolean onPreDraw() {
        return false;
    }
}
