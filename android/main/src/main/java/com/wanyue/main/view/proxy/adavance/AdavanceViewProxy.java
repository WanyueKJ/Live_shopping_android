package com.wanyue.main.view.proxy.adavance;

import android.view.View;
import android.widget.EditText;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.main.R2;
import com.wanyue.main.bean.CommissionBankBean;
import com.wanyue.main.bean.CommitAdavanceBean;
import butterknife.BindView;
import butterknife.OnTextChanged;

public abstract  class AdavanceViewProxy extends RxViewProxy {
    protected CommissionBankBean mData;
    protected CommitAdavanceBean mCommitBundle;

    @BindView(R2.id.tv_advance_money)
    EditText mTvAdvanceMoney;

    public void initBundle(){
        if(mCommitBundle==null){
           mCommitBundle=new CommitAdavanceBean();
           mCommitBundle.setExtract_type(getAdvanceType());
        }
    }

    public CommitAdavanceBean getCommitBundle() {
        return mCommitBundle;
    }

    @OnTextChanged(value = R2.id.tv_advance_money, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchMoney(CharSequence sequence, int start, int before, int count) {
        String string = sequence.toString();
        initBundle();
        if (mCommitBundle != null) {
            mCommitBundle.setMoney(string);
        }
    }


    protected boolean checkMoney() {
        String priceString=mCommitBundle.getMoney();
        try {
            double price=Double.parseDouble(priceString);
            double currentCount=Double.parseDouble(mData.getCommissionCount());
            if(price>currentCount){
                ToastUtil.show("不能大于可提现金额");
                return false;
            }
            double minPrice=Double.parseDouble(mData.getMinPrice());
            if(price<minPrice){
                ToastUtil.show("不能小于最低提现金额");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.show("输入金额格式有误");
            return false;
        }
        return true;
    }
    public  void goneToParent(){
        if(mContentView!=null){
           ViewUtil.setVisibility(mContentView, View.GONE);
        }
    }

    public  void showToParent(){
        if(mContentView!=null){
          ViewUtil.setVisibility(mContentView, View.VISIBLE);
        }
    }

    public void setData(CommissionBankBean data){
        if (data == null) {
            DebugUtil.sendException("CommissionBankBean==null");
            return;
        }
        mData=data;
        if (mTvAdvanceMoney != null) {
            mTvAdvanceMoney.setHint(getString(R.string.min_advance_money, mData.getMinPrice()));
        }
    }

    public abstract boolean localCheckCommitMessage();

    public abstract String getAdvanceType();

}
