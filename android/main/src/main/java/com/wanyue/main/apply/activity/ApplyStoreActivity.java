package com.wanyue.main.apply.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.apply.view.AbsApplyStoreViewProxy;
import com.wanyue.main.apply.view.ApplyResultViewProxy;
import com.wanyue.main.apply.view.ApplyStoreRxViewProxy;
import com.wanyue.main.bean.ApplyAnthorInfo;
import com.wanyue.main.business.MainEvent;

/*申请店铺*/
public class ApplyStoreActivity extends BaseActivity {
    private ViewGroup mVpContainer;
    private ApplyAnthorInfo mApplyAnthorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        setTabTitle("开通店铺");
        mVpContainer =  findViewById(R.id.vp_container);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        MainAPI.getLiveApplyInfo().compose(this.<ApplyAnthorInfo>bindToLifecycle()).subscribe(new DefaultObserver<ApplyAnthorInfo>() {
            @Override
            public void onNext(ApplyAnthorInfo applyAnthorInfo) {
                mApplyAnthorInfo=applyAnthorInfo;
                changeState(mApplyAnthorInfo.getStatus());
            }
        });
    }

    private void changeState(int state) {
        switch (state){
            case AbsApplyStoreViewProxy.NOAPPLY:
                addApplyView();
                break;
            case AbsApplyStoreViewProxy.REVIEW_SUCCESS:
                applySucc();
                break;
            default:
                addApplyResultView();
                break;
        }
    }



    private void applySucc() {
        LiveEventBus.get(MainEvent.UPDATE_USER_CENTER).post(true);
        //startActivity(MyStoreActivity.class);
        finish();
    }

    private void addApplyResultView() {
        ApplyResultViewProxy applyResultViewProxy=new ApplyResultViewProxy();
        applyResultViewProxy.getArgMap().put(Constants.DATA,mApplyAnthorInfo);
        getViewProxyMannger().addViewProxy(mVpContainer,applyResultViewProxy,applyResultViewProxy.getDefaultTag());
    }

    private void addApplyView() {
        ApplyStoreRxViewProxy applyStoreRxViewProxy= new ApplyStoreRxViewProxy();
        getViewProxyMannger().addViewProxy(mVpContainer,applyStoreRxViewProxy,applyStoreRxViewProxy.getDefaultTag());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_store;
    }
}
