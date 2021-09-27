package com.wanyue.main.view.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.main.R;
import com.wanyue.shop.bean.OrderStatementBean;
import com.wanyue.shop.business.ShopState;

public class SelectOrderTypePop extends PartShadowPopupView implements View.OnClickListener {
    private TextView mBtn1;
    private TextView mBtn2;
    private TextView mBtn3;
    private SelectListner mSelectListner;

    private OrderStatementBean mOrderStatementBean;

    public SelectOrderTypePop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();

        mBtn1 = (TextView) findViewById(R.id.btn1);
        mBtn2 = (TextView) findViewById(R.id.btn2);
        mBtn3 = (TextView) findViewById(R.id.btn3);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);

       setButtonTip();
    }

    private void setButtonTip() {
        if(mOrderStatementBean==null){
            return;
        }
        if(mBtn1!=null){
           mBtn1.setText(StringUtil.contact("已收货"," ",mOrderStatementBean.getEvaluatedCount()));
        }
        if(mBtn2!=null){
           mBtn2.setText(StringUtil.contact("已完成"," ",mOrderStatementBean.getCompleteCount()));
        }
        if(mBtn3!=null){
            mBtn3.setText(StringUtil.contact("退款"," ",mOrderStatementBean.getRefundCount()));
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_select_order_type;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn1){
            select(mBtn1.getText().toString(), ShopState.ORDER_STATE_WAIT_EVALUATE);
        }else if(id==R.id.btn2){
            select(mBtn2.getText().toString(), ShopState.ORDER_STATE_COMPELETE);
        }else if(id==R.id.btn3){
            select(mBtn3.getText().toString(), ShopState.ORDER_STATE_REFUND);
        }
    }

    private void select(String title, int type) {
        if(mSelectListner!=null){
           mSelectListner.select(title,type);
           dismiss();
        }
    }

    @Override
    public void destroy(){
        if(dialog!=null) {
            dialog.dismiss();
        }
        onDetachedFromWindow();
        if(popupInfo!=null){
            popupInfo.atView = null;
            popupInfo.watchView = null;
            popupInfo.xPopupCallback = null;
            popupInfo = null;
        }
    }

    public void setOrderStatementBean(OrderStatementBean orderStatementBean) {
        mOrderStatementBean = orderStatementBean;
    }

    public void setSelectListner(SelectListner selectListner) {
        mSelectListner = selectListner;
    }

    public interface SelectListner{
        public void select(String title,int type);
    }
}
