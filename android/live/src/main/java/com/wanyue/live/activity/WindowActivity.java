package com.wanyue.live.activity;

import com.wanyue.common.activity.BaseActivity;
import com.wanyue.live.R;

public class WindowActivity extends BaseActivity {
    private boolean mIsFirstStart=true;
    @Override
    public void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsFirstStart){
           mIsFirstStart=false;
        }else{
           return;
        }
        startActivity(LiveAudienceActivity.class);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_window;
    }
}
