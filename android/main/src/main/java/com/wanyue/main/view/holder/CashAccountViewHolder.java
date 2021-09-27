package com.wanyue.main.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.Constants;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;

/**
 * Created by  on 2018/10/22.
 */

public class CashAccountViewHolder extends AbsViewHolder implements View.OnClickListener {

    private TextView mBtnChooseType;
    private boolean mShowed;
    private LayoutInflater mInflater;
    private SparseIntArray mSparseIntArray;
    private int mKey;
    private View mGroup1;
    private View mGroup2;
    private View mGroup3;
    private EditText mEditAliAccount;
    private EditText mEditAliName;
    private EditText mEditWxAccount;
    private EditText mEditBankName;
    private EditText mEditBankAccount;
    private EditText mEditBankUserName;
    private HttpCallback mAddAccountCallback;

    public CashAccountViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_add_cash_account;
    }

    @Override
    public void init() {
        mSparseIntArray = new SparseIntArray();
        mSparseIntArray.put(Constants.CASH_ACCOUNT_ALI, R.string.cash_type_ali);
        mSparseIntArray.put(Constants.CASH_ACCOUNT_WX, R.string.cash_type_wx);
        mSparseIntArray.put(Constants.CASH_ACCOUNT_BANK, R.string.cash_type_bank);
        mKey = Constants.CASH_ACCOUNT_ALI;
        mInflater = LayoutInflater.from(mContext);
        findViewById(R.id.root).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        mBtnChooseType = (TextView) findViewById(R.id.btn_choose_type);
        mBtnChooseType.setOnClickListener(this);
        mGroup1 = findViewById(R.id.input_group_1);
        mGroup2 = findViewById(R.id.input_group_2);
        mGroup3 = findViewById(R.id.input_group_3);
        mEditAliAccount = (EditText) findViewById(R.id.input_ali_account);
        mEditAliName = (EditText) findViewById(R.id.input_ali_name);
        mEditWxAccount = (EditText) findViewById(R.id.input_wx_account);
        mEditBankName = (EditText) findViewById(R.id.input_bank_name);
        mEditBankAccount = (EditText) findViewById(R.id.input_bank_account);
        mEditBankUserName = (EditText) findViewById(R.id.input_bank_user_name);
        mAddAccountCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code) && info.length > 0) {

                }
                ToastUtil.show(msg);
            }
        };
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.root) {
            removeFromParent();

        } else if (i == R.id.btn_choose_type) {
            chooseType();

        } else if (i == R.id.btn_confirm) {
            addCashAccount();

        }
    }

    @Override
    public void addToParent() {
        super.addToParent();
        mShowed = true;
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();
        mShowed = false;
    }

    public boolean isShowed() {
        return mShowed;
    }

    private void addCashAccount() {
        String account = null;
        String name = null;
        String bank = null;
        if (mKey == Constants.CASH_ACCOUNT_ALI) {
            account = mEditAliAccount.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                ToastUtil.show(R.string.cash_input_ali_account);
                return;
            }
            mEditAliAccount.setText("");
            name = mEditAliName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastUtil.show(R.string.cash_input_ali_name);
                return;
            }
            mEditAliName.setText("");
        } else if (mKey == Constants.CASH_ACCOUNT_WX) {
            account = mEditWxAccount.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                ToastUtil.show(R.string.cash_input_wx_account);
                return;
            }
            mEditWxAccount.setText("");
        } else {
            account = mEditBankAccount.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                ToastUtil.show(R.string.cash_input_bank_account);
                return;
            }
            mEditBankAccount.setText("");
            name = mEditBankUserName.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                ToastUtil.show(R.string.cash_input_bank_user_name);
                return;
            }
            mEditBankUserName.setText("");
            bank = mEditBankName.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                ToastUtil.show(R.string.cash_input_bank_name);
                return;
            }
            mEditBankName.setText("");
        }
        removeFromParent();
        MainAPI.addCashAccount(account, name, bank, mKey, mAddAccountCallback);
    }

    /**
     * 选择账户类型
     */
    private void chooseType() {
        View v = mInflater.inflate(R.layout.view_cash_type_pop, null);
        final PopupWindow popupWindow = new PopupWindow(v, DpUtil.dp2px(60), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_pop_cash));
        TextView btn1 = v.findViewById(R.id.btn_1);
        TextView btn2 = v.findViewById(R.id.btn_2);
        if (mKey == Constants.CASH_ACCOUNT_ALI) {
            btn1.setTag(mSparseIntArray.keyAt(1));
            btn1.setText(mSparseIntArray.valueAt(1));
            btn2.setTag(mSparseIntArray.keyAt(2));
            btn2.setText(mSparseIntArray.valueAt(2));
        } else if (mKey == Constants.CASH_ACCOUNT_WX) {
            btn1.setTag(mSparseIntArray.keyAt(0));
            btn1.setText(mSparseIntArray.valueAt(0));
            btn2.setTag(mSparseIntArray.keyAt(2));
            btn2.setText(mSparseIntArray.valueAt(2));
        } else {
            btn1.setTag(mSparseIntArray.keyAt(0));
            btn1.setText(mSparseIntArray.valueAt(0));
            btn2.setTag(mSparseIntArray.keyAt(1));
            btn2.setText(mSparseIntArray.valueAt(1));
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Object tag = v.getTag();
                if (tag != null) {
                    int key = (int) tag;
                    mBtnChooseType.setText(mSparseIntArray.get(key));
                    mKey = key;
                    switch (key) {
                        case Constants.CASH_ACCOUNT_ALI:
                            if (mGroup1.getVisibility() != View.VISIBLE) {
                                mGroup1.setVisibility(View.VISIBLE);
                            }
                            if (mGroup2.getVisibility() == View.VISIBLE) {
                                mGroup2.setVisibility(View.GONE);
                            }
                            if (mGroup3.getVisibility() == View.VISIBLE) {
                                mGroup3.setVisibility(View.GONE);
                            }
                            break;
                        case Constants.CASH_ACCOUNT_WX:
                            if (mGroup1.getVisibility() == View.VISIBLE) {
                                mGroup1.setVisibility(View.GONE);
                            }
                            if (mGroup2.getVisibility() != View.VISIBLE) {
                                mGroup2.setVisibility(View.VISIBLE);
                            }
                            if (mGroup3.getVisibility() == View.VISIBLE) {
                                mGroup3.setVisibility(View.GONE);
                            }
                            break;
                        case Constants.CASH_ACCOUNT_BANK:
                            if (mGroup1.getVisibility() == View.VISIBLE) {
                                mGroup1.setVisibility(View.GONE);
                            }
                            if (mGroup2.getVisibility() == View.VISIBLE) {
                                mGroup2.setVisibility(View.GONE);
                            }
                            if (mGroup3.getVisibility() != View.VISIBLE) {
                                mGroup3.setVisibility(View.VISIBLE);
                            }
                            break;
                    }
                }
            }
        };
        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        popupWindow.showAsDropDown(mBtnChooseType);
    }


}
