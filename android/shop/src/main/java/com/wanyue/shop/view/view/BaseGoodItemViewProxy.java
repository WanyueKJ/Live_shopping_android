package com.wanyue.shop.view.view;

import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.L;

public abstract class BaseGoodItemViewProxy extends RxViewProxy {

    private int mTop = -1;
    private int mBottom = -1;
    private int mOffectTabHeight;
    private int lastScroolDy;

    public boolean isScrollTop(int dy) {
        boolean isUp = lastScroolDy - dy > 0;//是否是上滑
        dy = dy + mOffectTabHeight; //加入偏移量
        if (mContentView != null) {
            mTop = mContentView.getTop(); //
            mBottom = mContentView.getBottom();
            /*因为onScrollChange不是绝对实时的,并不能精确控制到没有误差,所以加入30上下到允许值*/
            if (Math.abs((dy - mTop)) < 30 ) {   //当滑动到view顶部的时候
                return true;
            } else if (isUp && Math.abs((dy - mBottom)) < 30) {//当向上滑到view的底部的时候,也应该切换到对应的栏目
                return true;
            }
        }
        lastScroolDy = dy; //记录上一次滑动的值
        return false;
    }


    public void setOffectTabHeight(int offectTabHeight) {
        mOffectTabHeight = offectTabHeight;
    }

    public int getOffectTabHeight() {
        return mOffectTabHeight;
    }

    public int getTop() {
        L.e("mContentView.getBottom()==" + mContentView.getBottom());
        L.e("mContentView.getTop()==" + mContentView.getTop());
        return mContentView.getTop() - mOffectTabHeight;
    }
}
