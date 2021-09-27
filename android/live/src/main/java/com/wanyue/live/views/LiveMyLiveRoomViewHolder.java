package com.wanyue.live.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.views.AbsCommonViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveAdminListActivity;
import com.wanyue.live.activity.LiveBlackActivity;
import com.wanyue.live.activity.LiveShutUpActivity;

/**
 * Created by  on 2019/4/23.
 * 我的直播间
 */

public class LiveMyLiveRoomViewHolder extends AbsCommonViewHolder implements View.OnClickListener {

    public LiveMyLiveRoomViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_my_live_room;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_admin).setOnClickListener(this);
        findViewById(R.id.btn_user_shut_up).setOnClickListener(this);
        findViewById(R.id.btn_user_black).setOnClickListener(this);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_admin) {
            LiveAdminListActivity.forward(mContext,
  CommonAppConfig.getUid());
        } else if (i == R.id.btn_user_shut_up) {
            LiveShutUpActivity.forward(mContext,
  CommonAppConfig.getUid());
        } else if (i == R.id.btn_user_black) {
            LiveBlackActivity.forward(mContext,
  CommonAppConfig.getUid());
        }
    }
}
