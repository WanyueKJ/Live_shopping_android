package com.yunbao.im.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.im.R;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.event.ImRemoveAllMsgEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by  on 2019/4/1.
 */

public class ChatOptionDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private TextView mBtnFollow;
    private String mToUid;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_chat_option;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mToUid = bundle.getString(Constants.TO_UID);
        if (TextUtils.isEmpty(mToUid)) {
            return;
        }
        findViewById(R.id.btn_home).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_clear_chat_record).setOnClickListener(this);
        mBtnFollow = (TextView) findViewById(R.id.btn_follow);
        mBtnFollow.setOnClickListener(this);
        boolean following = bundle.getBoolean(Constants.FOLLOW, false);
        mBtnFollow.setText(following ? R.string.following : R.string.follow);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_home) {
            forwardHome();
        } else if (i == R.id.btn_clear_chat_record) {
            clearChatRecord();
        } else if (i == R.id.btn_follow) {
            follow();
        } else if (i == R.id.btn_cancel) {
            dismiss();
        }
    }


    /**
     * 跳转到主页
     */
    private void forwardHome() {
        dismiss();
        if (mContext instanceof ChatRoomActivity && ((ChatRoomActivity) mContext).isFromUserHome()) {
            ((ChatRoomActivity) mContext).superBackPressed();
        } else {
            RouteUtil.forwardUserHome(mToUid);
        }
    }

    /**
     * 关注
     */
    private void follow() {
        dismiss();
        CommonHttpUtil.setAttention(mToUid, null);
    }

    /**
     * 清除聊天记录
     */
    private void clearChatRecord() {
        dismiss();
        EventBus.getDefault().post(new ImRemoveAllMsgEvent(mToUid));
    }

}
