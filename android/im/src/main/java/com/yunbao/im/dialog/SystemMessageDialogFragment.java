package com.yunbao.im.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.im.R;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.im.views.SystemMessageViewHolder;

/**
 * Created by  on 2018/11/28.
 * 系统消息
 */

public class SystemMessageDialogFragment extends AbsDialogFragment implements SystemMessageViewHolder.ActionListener {

    private SystemMessageViewHolder mSystemMessageViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_empty;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.leftToRightAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(300);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSystemMessageViewHolder = new SystemMessageViewHolder(mContext, (ViewGroup) mRootView);
        mSystemMessageViewHolder.setActionListener(this);
        mSystemMessageViewHolder.addToParent();
        mSystemMessageViewHolder.loadData();
    }


    @Override
    public void onBackClick() {
        dismiss();
    }

    @Override
    public void onDestroy() {
        if (mSystemMessageViewHolder != null) {
            mSystemMessageViewHolder.release();
        }
        super.onDestroy();
    }
}
