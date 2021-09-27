package com.wanyue.common.pay.wx;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.wanyue.common.Constants;
import com.wanyue.common.R;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.pay.PayCallback;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

/**
 * Created by  on 2017/9/22.
 */

public class WxPayBuilder {

    private Context mContext;
    private String mAppId;
    private PayCallback mPayCallback;
    private String mOrderParams;//订单获取订单需要的参数

    public WxPayBuilder(Context context, String appId) {
        mContext = context;
        mAppId = appId;
        WxApiWrapper.getInstance().setAppID(appId);
        EventBus.getDefault().register(this);
    }

    public WxPayBuilder setOrderParams(String orderParams) {
        mOrderParams = orderParams;
        return this;
    }

    public WxPayBuilder setPayCallback(PayCallback callback) {
        mPayCallback = new WeakReference<>(callback).get();
        return this;
    }

    public void payWx(){
        CommonAPI.getWxOrder(mOrderParams, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code) && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String partnerId = obj.getString("partnerid");
                    String prepayId = obj.getString("prepayid");
                    String packageValue = obj.getString("package");
                    String nonceStr = obj.getString("noncestr");
                    String timestamp = obj.getString("timestamp");
                    String sign = obj.getString("sign");
                    if (TextUtils.isEmpty(partnerId) ||
                            TextUtils.isEmpty(prepayId) ||
                            TextUtils.isEmpty(packageValue) ||
                            TextUtils.isEmpty(nonceStr) ||
                            TextUtils.isEmpty(timestamp) ||
                            TextUtils.isEmpty(sign)) {
                        ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
                        return;
                    }
                    PayReq req = new PayReq();
                    req.appId = mAppId;
                    req.partnerId = partnerId;
                    req.prepayId = prepayId;
                    req.packageValue = packageValue;
                    req.nonceStr = nonceStr;
                    req.timeStamp = timestamp;
                    req.sign = sign;
                    IWXAPI wxApi = WxApiWrapper.getInstance().getWxApi();
                    if (wxApi == null) {
                        ToastUtil.show(R.string.coin_charge_failed);
                        return;
                    }
                    boolean result = wxApi.sendReq(req);
                    if (!result) {
                        ToastUtil.show(R.string.coin_charge_failed);
                    }
                }
            }

            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
        });
    }

    public void pay(JSONObject obj) {
        String partnerId = obj.getString("partnerid");
        String prepayId = obj.getString("prepayid");
        String packageValue = obj.getString("package");
        String nonceStr = obj.getString("noncestr");
        String timestamp = obj.getString("timestamp");
        String sign = obj.getString("sign");
        try {
            BigDecimal bd = new BigDecimal(timestamp);
            String str = bd.toPlainString();
            timestamp= str;
        }catch (Exception e){
            e.printStackTrace();
        }
            if (TextUtils.isEmpty(partnerId) ||
                    TextUtils.isEmpty(prepayId) ||
                    TextUtils.isEmpty(packageValue) ||
                    TextUtils.isEmpty(nonceStr) ||
                    TextUtils.isEmpty(timestamp) ||
                    TextUtils.isEmpty(sign)) {
                ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
                return;
            }
            PayReq req = new PayReq();
            req.appId = mAppId;
            req.partnerId = partnerId;
            req.prepayId = prepayId;
            req.packageValue = packageValue;
            req.nonceStr = nonceStr;
            req.timeStamp = timestamp;
            req.sign = sign;

            IWXAPI wxApi = WxApiWrapper.getInstance().getWxApi();
            if (wxApi == null) {
                ToastUtil.show(R.string.coin_charge_failed);
                return;
            }
            boolean result = wxApi.sendReq(req);
            if (!result) {
                ToastUtil.show(R.string.coin_charge_failed);
            }
        }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResponse(BaseResp resp) {
        L.e("resp---微信支付回调---->" + resp.errCode);
        if (mPayCallback != null) {
            if (0 == resp.errCode) {//支付成功
                mPayCallback.onSuccess();
            } else {//支付失败
                if(resp.errCode==BaseResp.ErrCode.ERR_USER_CANCEL){
                    mPayCallback.onFailed(PayCallback.CANCLE_PAY);
                }else{
                    mPayCallback.onFailed(PayCallback.FAILED);
                }
            }
        }
        mContext = null;
        mPayCallback = null;
        EventBus.getDefault().unregister(this);
    }


}
