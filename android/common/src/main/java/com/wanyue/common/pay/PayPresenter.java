package com.wanyue.common.pay;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.HtmlConfig;
import com.wanyue.common.R;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.event.CoinChangeEvent;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.pay.ali.AliPayBuilder;
import com.wanyue.common.pay.google.GooglePayTask;
import com.wanyue.common.pay.wx.WxPayBuilder;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * Created by  on 2019/4/22.
 */

public class PayPresenter {

    private String mAliPartner;// 支付宝商户ID
    private String mAliSellerId; // 支付宝商户收款账号
    private String mAliPrivateKey; // 支付宝商户私钥，pkcs8格式
    private String mAliCallbackUrl= HtmlConfig.ALI_PAY_URL;//支付宝充值回调地址
    private String mWxAppID;//微信AppID
    private String mServiceNameAli;
    private String mServiceNameWx;
    private long mBalanceValue;
    private Activity mActivity;
    private PayCallback mPayCallback;
    private String mGoogleCallbackUrl;
    private GooglePayTask.GooglePayCallback mGooglePayCallback;


    public PayPresenter(Activity activity) {
        mActivity = new WeakReference<>(activity).get();
    }

    public void setPayCallback(PayCallback callback){
        mPayCallback=callback;
    }

    public long getBalanceValue() {
        return mBalanceValue;
    }

    public void setBalanceValue(long balanceValue) {
        mBalanceValue = balanceValue;
    }

    public void setAliPartner(String aliPartner) {
        mAliPartner = aliPartner;
    }

    public void setAliSellerId(String aliSellerId) {
        mAliSellerId = aliSellerId;
    }

    public void setAliPrivateKey(String aliPrivateKey) {
        mAliPrivateKey = aliPrivateKey;
    }

    public void setWxAppID(String wxAppID) {
        mWxAppID = wxAppID;
    }


    public void setServiceNameAli(String serviceNameAli) {
        mServiceNameAli = serviceNameAli;
    }

    public void setServiceNameWx(String serviceNameWx) {
        mServiceNameWx = serviceNameWx;
    }


    public void setAliCallbackUrl(String aliCallbackUrl) {
        mAliCallbackUrl = aliCallbackUrl;
    }


    public String getGoogleCallbackUrl() {
        return mGoogleCallbackUrl;
    }

    public void setGoogleCallbackUrl(String googleCallbackUrl) {
        mGoogleCallbackUrl = googleCallbackUrl;
    }


    public void setGooglePayCallback(GooglePayTask.GooglePayCallback googlePayCallback) {
        mGooglePayCallback = googlePayCallback;
    }

    public void pay(String payType, String money, String goodsName, String orderParams) {
        if (TextUtils.isEmpty(payType)) {
            ToastUtil.show(R.string.wallet_tip_5);
            return;
        }
        switch (payType) {
            case Constants.PAY_TYPE_ALI://支付宝支付
                aliPay(money, goodsName, orderParams);
                break;
            case Constants.PAY_TYPE_WX://微信支付
                wxPay(orderParams);
                break;
            case Constants.PAY_TYPE_GOOGLE://谷歌支付
                googlePay(orderParams);
                break;
        }
    }


    public void pay2(String payType, String money, String goodsName,  JSONObject jsonParm,String orderId) {
        if (TextUtils.isEmpty(payType)) {
            ToastUtil.show(R.string.wallet_tip_5);
            return;
        }
        switch (payType) {
            case Constants.PAY_TYPE_ALI://支付宝支付
                aliPay2(money, goodsName, null,orderId);
                break;
            case Constants.PAY_TYPE_WX://微信支付
                wxPay2(jsonParm);
                break;
            case Constants.PAY_TYPE_GOOGLE://谷歌支付
                googlePay(null);
                break;
        }
    }


    /**
     * 支付宝支付
     */
    private void aliPay(String money, String goodsName, String orderParams) {
        if (mActivity == null || TextUtils.isEmpty(mServiceNameAli)|| TextUtils.isEmpty(mAliCallbackUrl)) {
            return;
        }
        if (!CommonAppConfig.isAppExist(Constants.PACKAGE_NAME_ALI)) {
            ToastUtil.show(R.string.coin_ali_not_install);
            return;
        }
        if (TextUtils.isEmpty(mAliPartner) || TextUtils.isEmpty(mAliSellerId) || TextUtils.isEmpty(mAliPrivateKey)) {
            ToastUtil.show(Constants.PAY_ALI_NOT_ENABLE);
            return;
        }
        AliPayBuilder builder = new AliPayBuilder(mActivity, mAliPartner, mAliSellerId, mAliPrivateKey);
        builder.setMoney(money);
        builder.setGoodsName(goodsName);
        builder.setCallbackUrl(mAliCallbackUrl);
        builder.setOrderParams(StringUtil.contact(mServiceNameAli, orderParams));
        builder.setPayCallback(mPayCallback);
        builder.pay();
    }


    /**
     * 支付宝支付
     */
    private void aliPay2(String money, String goodsName, JsonObject jsonObject, String orderId) {
        if (mActivity == null || TextUtils.isEmpty(mAliCallbackUrl)) {
            return;
        }
        if (!CommonAppConfig.isAppExist(Constants.PACKAGE_NAME_ALI)) {
            ToastUtil.show(R.string.coin_ali_not_install);
            return;
        }
        if (TextUtils.isEmpty(mAliPartner) || TextUtils.isEmpty(mAliSellerId) || TextUtils.isEmpty(mAliPrivateKey)) {
            ToastUtil.show(Constants.PAY_ALI_NOT_ENABLE);
            return;
        }
        AliPayBuilder builder = new AliPayBuilder(mActivity, mAliPartner, mAliSellerId, mAliPrivateKey);
        builder.setMoney(money);
        builder.setGoodsName(goodsName);
        builder.setCallbackUrl(mAliCallbackUrl);
        builder.setPayCallback(mPayCallback);
        builder.setOrderId(orderId);
        builder.pay2();
    }

    /**
     * 微信支付
     */
    public void wxPay2(JSONObject jsonObject) {
        if (mActivity == null ) {
            return;
        }
        if (!CommonAppConfig.isAppExist(Constants.PACKAGE_NAME_WX)) {
            ToastUtil.show(R.string.coin_wx_not_install);
            return;
        }

        if (TextUtils.isEmpty(mWxAppID)) {
            ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
            return;
        }
        WxPayBuilder builder = new WxPayBuilder(mActivity, mWxAppID);
        //builder.setOrderParams(StringUtil.contact(mServiceNameWx, orderParams));
        builder.setPayCallback(mPayCallback);
        builder.pay(jsonObject);
    }

    private void wxPay( String orderParams) {
        if (mActivity == null ) {
            return;
        }
        if (!CommonAppConfig.isAppExist(Constants.PACKAGE_NAME_WX)) {
            ToastUtil.show(R.string.coin_wx_not_install);
            return;
        }

        if (TextUtils.isEmpty(mWxAppID)) {
            ToastUtil.show(Constants.PAY_WX_NOT_ENABLE);
            return;
        }
        WxPayBuilder builder = new WxPayBuilder(mActivity, mWxAppID);
        builder.setOrderParams(StringUtil.contact(mServiceNameWx, orderParams));
        builder.setPayCallback(mPayCallback);
        builder.payWx();
    }

    /**
     * 谷歌支付
     */
    private void googlePay(String orderId) {
        new GooglePayTask(mActivity, mGoogleCallbackUrl, "1", mGooglePayCallback).start(orderId);
    }

    /**
     * 检查支付结果
     */
    public void checkPayResult() {
        CommonAPI.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    long balanceValue = Long.parseLong(coin);
                    if (balanceValue > mBalanceValue) {
                        mBalanceValue = balanceValue;
                        ToastUtil.show(R.string.coin_charge_success);
                        UserBean u = CommonAppConfig.getUserBean();
                        if (u != null) {
                            u.setCoin(coin);
                        }
                        EventBus.getDefault().post(new CoinChangeEvent(coin, true));
                    }
                }
            }
        });
    }


    public void release() {
        mActivity = null;
        mPayCallback = null;
    }
}
