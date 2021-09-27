package com.wanyue.live.business.floatwindow;

import android.view.View;

public interface IExpotFlowContainer {
    //暴露需要提供给window层的View
    public View exportFlowView();
    public void restoreFlowView(FloatFrameLayout floatFrameLayout);
}
