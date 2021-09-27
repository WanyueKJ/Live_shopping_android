package com.wanyue.shop.view.pop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.business.UserModel;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.pay.PayCallback;
import com.wanyue.common.pay.PayPresenter;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.model.OrderModel;

public class PayOrderPopView extends BaseBottomPopView implements View.OnClickListener {
    private ImageView mBtnClose;
    private ViewGroup mBtnWx;
    private ViewGroup mBtnCoin;
    private TextView mTvCoinMoney;
    private String mId;
    private PayPresenter mPayPresenter;

    @Override
    protected void init() {
        super.init();
        mBtnClose = findViewById(R.id.btn_close);
        mBtnWx =  findViewById(R.id.btn_wx);
        mBtnCoin = findViewById(R.id.btn_coin);
        mTvCoinMoney =  findViewById(R.id.tv_coin_money);
        mBtnClose.setOnClickListener(this);
        mBtnWx.setOnClickListener(this);
        mBtnCoin.setOnClickListener(this);

        UserBean userBean= CommonAppConfig.getUserBean();
        if(userBean!=null){
         String price= StringUtil.getFormatPrice(userBean.getBalance());
         mTvCoinMoney.setText(price);
        }
    }

    public PayOrderPopView(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_pay_order;
    }
    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_close){
            dismiss();
        }else if(id==R.id.btn_wx){
            payforWx();
        }else if(id==R.id.btn_coin){
            payForCoin();
        }
    }

    public void setId(String id) {
        mId = id;
    }

    private void initPayPrestner() {
        if(mPayPresenter==null){
           mPayPresenter=new PayPresenter((Activity) getContext());
        }
    }

    private void payforWx() {
        if(mId==null){
            dismiss();
            return;
        }
        ShopAPI.orderPay(mId, Constants.PAY_TYPE_WX, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)){
                    tuneWx(info);

                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }
            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(getContext());
            }
        });
    }

    private void tuneWx(JSONObject info) {
        initPayPrestner();
        mPayPresenter.setPayCallback(new PayCallback() {
            @Override
            public void onSuccess() {
                buySucc();
            }
            @Override
            public void onFailed(int errorCode) {
            }
        });
        try {
            JSONObject wxConfig=info.getJSONObject("result").getJSONObject("jsConfig");
            if(wxConfig!=null){
                mPayPresenter.setWxAppID(wxConfig.getString("appid"));
                mPayPresenter.wxPay2(wxConfig);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void payForCoin() {
        if(mId==null){
            dismiss();
            return;
        }
        ShopAPI.orderPay(mId, Constants.PAY_TYPE_COIN, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)){
                    buySucc();
                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }
            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(getContext());
            }
        });
    }

    private void buySucc() {
        OrderModel.sendOrderChangeEvent(mId);
        UserModel.refreshBalance();
        dismiss();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        ShopAPI.cancle(ShopAPI.ORDER_PAY);
    }
}
