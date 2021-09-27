package com.yunbao.im.business;

import android.view.View;
import com.yunbao.im.custom.FloatFrameLayout;

public interface IExpotFlowContainer {
    //暴露需要提供给window层的View
    public View exportFlowView();
    public void restoreFlowView(FloatFrameLayout floatFrameLayout);
}
