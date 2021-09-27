package com.yunbao.im.activity;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.im.R;
import com.yunbao.im.views.SystemMessageViewHolder;

/**
 * Created by  on 2018/11/24.
 */

public class SystemMessageActivity extends AbsActivity implements SystemMessageViewHolder.ActionListener {

    private SystemMessageViewHolder mSystemMessageViewHolder;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, SystemMessageActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sys_msg;
    }

    @Override
    protected void main() {
        mSystemMessageViewHolder = new SystemMessageViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
        mSystemMessageViewHolder.setActionListener(this);
        mSystemMessageViewHolder.addToParent();
        mSystemMessageViewHolder.loadData();
    }

    @Override
    protected void onDestroy() {
        if (mSystemMessageViewHolder != null) {
            mSystemMessageViewHolder.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }
}
