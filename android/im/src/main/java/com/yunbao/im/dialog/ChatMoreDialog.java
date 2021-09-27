package com.yunbao.im.dialog;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.PopupWindow;

import com.yunbao.im.R;
import com.yunbao.common.utils.DpUtil;

/**
 * Created by  on 2018/11/7.
 * 聊天更多弹窗
 */

public class ChatMoreDialog extends PopupWindow {

    private View mParent;
    private View mContentView;
    private ActionListener mActionListener;

    public ChatMoreDialog(View parent, View contentView, boolean needAnim, ActionListener actionListener) {
        mParent = parent;
        mActionListener = actionListener;
        ViewParent viewParent = contentView.getParent();
        if (viewParent != null) {
            ((ViewGroup) viewParent).removeView(contentView);
        }
        mContentView = contentView;
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(DpUtil.dp2px(140));
        setOutsideTouchable(false);
        if (needAnim) {
            setAnimationStyle(R.style.bottomToTopAnim2);
        }
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewParent viewParent = mContentView.getParent();
                if (viewParent != null) {
                    ((ViewGroup) viewParent).removeView(mContentView);
                }
                mContentView = null;
                if (mActionListener != null) {
                    mActionListener.onMoreDialogDismiss();
                }
                mActionListener = null;
            }
        });
    }


    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
    }


    public interface ActionListener {
        void onMoreDialogDismiss();
    }

}
