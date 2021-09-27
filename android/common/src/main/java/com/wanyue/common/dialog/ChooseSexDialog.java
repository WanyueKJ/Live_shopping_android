package com.wanyue.common.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wanyue.common.Constants;
import com.wanyue.common.R;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.WordUtil;

/**
 * Created by  on 2019/7/23.
 */

public class ChooseSexDialog extends AbsDialogFragment implements View.OnClickListener {

    private int mSexVal;
    private String mSexString;
    private String mMaleString;
    private String mFemaleString;
    private ActionListener mActionListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_set_sex;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(220);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMaleString = WordUtil.getString(R.string.sex_male);
        mFemaleString = WordUtil.getString(R.string.sex_female);
        mSexVal = Constants.MAIN_SEX_MALE;
        mSexString = mMaleString;
        findViewById(R.id.btn_male).setOnClickListener(this);
        findViewById(R.id.btn_female).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            dismiss();
        } else if (i == R.id.btn_confirm) {
            if (mActionListener != null) {
                mActionListener.onConfirmClick(mSexString, mSexVal);
            }
            dismiss();
        } else if (i == R.id.btn_male) {
            mSexVal = Constants.MAIN_SEX_MALE;
            mSexString = mMaleString;
        } else if (i == R.id.btn_female) {
            mSexVal = Constants.MAIN_SEX_FAMALE;
            mSexString = mFemaleString;
        }
    }


    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onConfirmClick(String sexString, int sexVal);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mActionListener = null;
        super.onDestroy();
    }
}
