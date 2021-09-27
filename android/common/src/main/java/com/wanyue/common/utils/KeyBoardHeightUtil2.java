package com.wanyue.common.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import com.wanyue.common.interfaces.KeyBoardHeightChangeListener;

import java.lang.ref.WeakReference;

/**
 * Created by  on 2018/10/27.
 * 获取键盘高度的工具类
 */

public class KeyBoardHeightUtil2 implements ViewTreeObserver.OnGlobalLayoutListener {

    private final static String TAG = "KeyBoardHeightUtil";
    private View mParentView;
    private Rect mRect;
    private int mScreenHeight;
    private int mScreenStatusHeight;
    private int mLastHeight;
    private KeyBoardHeightChangeListener mKeyBoardChangeListener;
    private boolean mSoftInputShowed;

    public KeyBoardHeightUtil2(Context context, View parentView, KeyBoardHeightChangeListener listener) {
        mParentView = parentView;
        mRect = new Rect();
        ScreenDimenUtil util = ScreenDimenUtil.getInstance();
        mScreenHeight = util.getScreenHeight();
        mScreenStatusHeight = util.getStatusBarHeight();
        L.e(TAG, "---屏幕高度--->" + mScreenHeight);
        L.e(TAG, "---状态栏高度--->" + mScreenStatusHeight);
        mKeyBoardChangeListener = new WeakReference<>(listener).get();
    }


    @Override
    public void onGlobalLayout() {
        if (mParentView != null && mRect != null) {
            mParentView.getWindowVisibleDisplayFrame(mRect);
            int visibleHeight = mRect.height() + mScreenStatusHeight;
            if (visibleHeight > mScreenHeight) {
                mScreenHeight = visibleHeight;
            }
            if (mLastHeight != visibleHeight) {
                mLastHeight = visibleHeight;
                if (mKeyBoardChangeListener != null) {
                    int keyboardHeight = mScreenHeight - visibleHeight;
                    if (keyboardHeight < 0) {
                        keyboardHeight = 0;
                    }
                    L.e(TAG, "-------可视区高度----->" + visibleHeight);
                    L.e(TAG, "-------键盘高度----->" + keyboardHeight);
                    mSoftInputShowed = keyboardHeight > 100;
                    mKeyBoardChangeListener.onKeyBoardHeightChanged(visibleHeight, keyboardHeight);
                }
            }
        }
    }


    /**
     * 添加布局变化的监听器
     */
    public void start() {
        if (mParentView != null) {
            mParentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            L.e(TAG, "-------添加键盘监听--->");
        }
    }


    /**
     * 移除布局变化的监听器
     */
    public void release() {
        if (mParentView != null) {
            mParentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        mParentView = null;
        mRect = null;
        mKeyBoardChangeListener = null;
        L.e(TAG, "-------移除键盘监听--->");
    }


    public boolean isSoftInputShowed() {
        return mSoftInputShowed;
    }
}
