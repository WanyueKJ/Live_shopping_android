package com.wanyue.live.dialog;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.PopupWindow;

import com.wanyue.live.R;

/**
 * Created by  on 2018/11/7.
 * 主播连麦PK搜索弹窗
 */

public class LiveLinkMicPkSearchDialog extends PopupWindow {

    private View mParent;
    private View mContentView;
    private ActionListener mActionListener;

    public LiveLinkMicPkSearchDialog(View parent, View contentView, ActionListener actionListener) {
        mParent = parent;
        mActionListener = actionListener;
        ViewParent viewParent = contentView.getParent();
        if (viewParent != null) {
            ((ViewGroup) viewParent).removeView(contentView);
        }
        mContentView = contentView;
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        setAnimationStyle(R.style.leftToRightAnim);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mActionListener != null) {
                    mActionListener.onSearchDialogDismiss();
                }
                ViewParent viewParent = mContentView.getParent();
                if (viewParent != null) {
                    ((ViewGroup) viewParent).removeView(mContentView);
                }
                mContentView = null;
            }
        });
    }


    public void show() {
        showAtLocation(mParent, Gravity.CENTER, 0, 0);
    }

    public interface ActionListener {
        void onSearchDialogDismiss();
    }

}
