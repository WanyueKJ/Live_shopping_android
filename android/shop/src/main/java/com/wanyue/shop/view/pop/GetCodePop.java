package com.wanyue.shop.view.pop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.model.OrderModel;

public class GetCodePop extends BaseCenterPopView implements View.OnClickListener {

    private TextView mTvCodeTip;
    private Button mBtnCompeleted;
    private String mCodeTip;
    private String mOrderId;

    public GetCodePop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        mTvCodeTip = (TextView) findViewById(R.id.tv_code_tip);
        mBtnCompeleted = (Button) findViewById(R.id.btn_compeleted);
        mBtnCompeleted.setOnClickListener(this);
        if(mCodeTip!=null){
           mTvCodeTip.setText(WordUtil.getString(R.string.get_code_tip,mCodeTip));
        }
    }


    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        OrderModel.sendOrderChangeEvent(mOrderId);
    }

    public void setCodeTip(String codeTip) {
        mCodeTip = codeTip;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_get_code;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
