package com.wanyue.main.view.proxy.adavance;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wanyue.common.utils.ToastUtil;
import com.wanyue.main.R;
import com.wanyue.main.R2;
import butterknife.BindView;
import butterknife.OnTextChanged;

public class ZfbAdvanceViewProxy extends AdavanceViewProxy {
    @BindView(R2.id.tv_user_name)
    EditText mTvUserName;

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
    }
    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }


    @OnTextChanged(value = R2.id.tv_user_name, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchAccount(CharSequence sequence, int start, int before, int count) {
        String string = sequence.toString();
        initBundle();
        if (mCommitBundle != null) {
            mCommitBundle.setAlipay_code(string);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.view_zfb_adavance;
    }

    @Override
    public boolean localCheckCommitMessage() {
        initBundle();
        if(TextUtils.isEmpty(mCommitBundle.getAlipay_code())){
            ToastUtil.show(mTvUserName.getHint().toString());
        }else{
            return checkMoney();
        }
        return false;
    }

    @Override
    public String getAdvanceType() {
        return "alipay";
    }
}
