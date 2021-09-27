package com.wanyue.main.view.proxy.adavance;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.main.R;
import com.wanyue.main.R2;
import com.wanyue.main.bean.CommissionBankBean;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class BankAdvanceViewProxy extends AdavanceViewProxy {
    @BindView(R2.id.tv_user_name)
    EditText mTvUserName;
    @BindView(R2.id.tv_card_num)
    EditText mTvCardNum;
    @BindView(R2.id.tv_bank)
    TextView mTvBank;
    @BindView(R2.id.tv_advance_money)
    EditText mTvAdvanceMoney;

    private SparseArray<String> mSparseArray;

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);

    }

    @OnTextChanged(value = R2.id.tv_user_name, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchUserName(CharSequence sequence, int start, int before, int count) {
        String string = sequence.toString();
        initBundle();
        if (mCommitBundle != null) {
            mCommitBundle.setName(string);
        }
    }

    @OnTextChanged(value = R2.id.tv_card_num, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchCardNum(CharSequence sequence, int start, int before, int count) {
        String string = sequence.toString();
        initBundle();
        if (mCommitBundle != null) {
            mCommitBundle.setCardnum(string);
        }
    }

    @OnTextChanged(value = R2.id.tv_bank, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchBank(CharSequence sequence, int start, int before, int count) {
        String string = sequence.toString();
        initBundle();
        if (mCommitBundle != null) {
            mCommitBundle.setBankname(string);
        }
    }
    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_bank_advance;
    }


    @OnClick(R2.id.tv_bank)
    public void openSelectBank(){
        if(!ClickUtil.canClick()){
            return;
        }
        SparseArray<String> array=new SparseArray<>();
        if(mSparseArray==null){
           mSparseArray=transFormBankList();
        }
        if(mSparseArray==null){
            return;
        }
        DialogUitl.showStringArrayDialog(getActivity(), mSparseArray, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                mTvBank.setText(text);
            }
        });
    }

    private SparseArray<String> transFormBankList() {
        if(mData==null|| !ListUtil.haveData(mData.getExtractBank())){
           return null;
        }
        List<String> list=mData.getExtractBank();
        SparseArray<String> array=new SparseArray<>();
        int size=list.size();
        for(int i=0;i<size;i++){
            array.put(i,list.get(i));
        }
        return array;
    }

    @Override
    public boolean localCheckCommitMessage() {
        if(mData==null){
            DebugUtil.sendException("mData==null 请检查写法");
            return false;
        }
        initBundle();
        if(TextUtils.isEmpty(mCommitBundle.getName())){
            ToastUtil.show(mTvUserName.getHint().toString());
        }else if(TextUtils.isEmpty(mCommitBundle.getCardnum())){
            ToastUtil.show(mTvCardNum.getHint().toString());

        }else if(TextUtils.isEmpty(mCommitBundle.getBankname())){
            ToastUtil.show(mTvBank.getHint().toString());
        }
        else{
            return checkMoney();
        }
        return false;
    }



    @Override
    public String getAdvanceType() {
        return "bank";
    }

}
