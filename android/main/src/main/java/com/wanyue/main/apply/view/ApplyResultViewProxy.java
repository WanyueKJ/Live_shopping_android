package com.wanyue.main.apply.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.Constants;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.main.R;
import com.wanyue.main.bean.ApplyAnthorInfo;

public class ApplyResultViewProxy extends AbsApplyStoreViewProxy implements View.OnClickListener {
    private TextView mTvTipTop;
    private TextView mTvTipBottom;
    private TextView mBtnReply;
    private ApplyAnthorInfo mAnthorInfo;
    private int mState;
    private String mBottomTipString;

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mTvTipTop = (TextView) findViewById(R.id.tv_tipTop);
        mTvTipBottom = (TextView) findViewById(R.id.tv_tipBottom);
        mBtnReply = (TextView) findViewById(R.id.btn_reply);
        setOnClickListner(R.id.btn_reply,this);
        ApplyAnthorInfo applyAnthorInfo= (ApplyAnthorInfo) getArgMap().get(Constants.DATA);
        setAnthorInfo(applyAnthorInfo);
    }

    public void setAnthorInfo(ApplyAnthorInfo anthorInfo) {
        mAnthorInfo = anthorInfo;
        if(mAnthorInfo!=null){
            mState= mAnthorInfo.getStatus();
            mBottomTipString= mAnthorInfo.getReason();
            setStateUI();
        }
    }

    @Override
    public boolean onBackPressed() {
        finish();
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_apply_result;
    }

    private void setStateUI() {
        if(mState== REVIEW_ERROR){
            mTvTipTop.setTextColor(ResourceUtil.getColor(getActivity(),R.color.red3));
            mTvTipTop.setText(R.string.information_audit_tip2);
            mBtnReply.setVisibility(View.VISIBLE);
        }else if(mState==REVIEW_ING){
            mTvTipTop.setText(R.string.information_audit_tip1);
            mTvTipTop.setTextColor(ResourceUtil.getColor(getActivity(),R.color.textColor)
                  );
            mBtnReply.setVisibility(View.INVISIBLE);
            mBottomTipString=getString(R.string.information_audit_tip3);
        }
        mTvTipBottom.setText(mBottomTipString);
    }

    @Override
    public void onClick(View v) {
        startViewProxy(null,ApplyStoreRxViewProxy.class,null);

    }
}
