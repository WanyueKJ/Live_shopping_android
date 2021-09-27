package com.wanyue.common.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.L;

public class NoScrollWebView extends WebView {
    private  float xDistance;
    private  float yDistance;
    private  float lastX;
    private  float lastY;

    private int mMaxHeight;
    private int mMinLimitHeight;

    @SuppressLint("NewApi")
    public NoScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public NoScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public NoScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollWebView(Context context) {
        super(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(mMinLimitHeight==0){
            mMinLimitHeight= DpUtil.dp2px(230);
        }
        int contentHeight=getContentHeight();
        int mExpandSpec =0;
        if(contentHeight<mMinLimitHeight&&mMaxHeight==mMinLimitHeight){
           mExpandSpec=MeasureSpec.makeMeasureSpec(mMinLimitHeight, MeasureSpec.EXACTLY);;
        }
        else{
           mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
           L.e("mExpandSpec mExpandSpec MAX_VALUE=="+mExpandSpec);
        }
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void loadUrl(String url) {
        mMaxHeight=0;
        super.loadUrl(url);
    }

    public void setMaxHeight(int maxHeight) {
        if(mMaxHeight<maxHeight){
           mMaxHeight = maxHeight;
        }
    }
}