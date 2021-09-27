package com.wanyue.live.activity;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.wanyue.common.Constants;
import com.wanyue.common.activity.AbsActivity;
import com.wanyue.live.R;
import com.wanyue.live.views.LiveContributeViewHolder;

/**
 * Created by  on 2018/10/19.
 */

public class LiveContributeActivity extends AbsActivity {

    private LiveContributeViewHolder mLiveContributeViewHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_empty;
    }

    @Override
    protected void main() {
        String toUid = getIntent().getStringExtra(Constants.TO_UID);
        if (TextUtils.isEmpty(toUid)) {
            return;
        }
        mLiveContributeViewHolder = new LiveContributeViewHolder(mContext, (ViewGroup) findViewById(R.id.container), toUid);
        mLiveContributeViewHolder.addToParent();
        mLiveContributeViewHolder.loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mLiveContributeViewHolder!=null){
            mLiveContributeViewHolder.release();
        }
    }
}
